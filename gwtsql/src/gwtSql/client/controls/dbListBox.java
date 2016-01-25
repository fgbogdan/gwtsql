package gwtSql.client.controls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.shared.DBRecord;
import gwtSql.shared.DebugUtils;
import gwtSql.shared.ListXD;

public class dbListBox extends ListBox implements Controls {

	private final DBServiceAsync dbService = GWT.create(DBService.class);

	String strTableName;
	String strShowField;
	String strKeyField;
	String strFilterCondition = "";
	String strOrder = "";
	String strSQLCommand = "";
	public ListXD listXD;
	public DBRecord R;
	public String strLinkedField = "";
	public boolean isLoaded = false;

	/**
	 * constructor - 1 parameter
	 * 
	 * @param strSQLCommand
	 *            - command for the content, the result must have 2 fields -
	 *            ShowFld and KeyFld example; SELECT Name as ShowFld, ID as
	 *            KeyFld FROM CUSTOMERS;
	 * 
	 *            the items that begin with (inactiv) will be disabled
	 */
	public dbListBox(String strSQLCommand) {
		this.strTableName = "";
		this.strShowField = "ShowFld";
		this.strKeyField = "KeyFld";
		this.strSQLCommand = strSQLCommand;
		this.LoadData();
	}

	/**
	 * constructor - 2 parameters
	 * 
	 * @param strSQLCommand
	 *            - command for the content, the result must have 2 fields -
	 *            ShowFld and KeyFld example; SELECT Name as ShowFld, ID as
	 *            KeyFld FROM CUSTOMERS;
	 * @param p_strTableName
	 *            - optional or empty
	 */
	public dbListBox(String strSQLCommand, String p_strTableName) {
		this.strTableName = p_strTableName;
		this.strShowField = "ShowFld";
		this.strKeyField = "KeyFld";
		this.strSQLCommand = strSQLCommand;
		this.LoadData();
	}

	/**
	 * constructor 3 - parameters
	 * 
	 * @param p_strTableName
	 *            - name of the table from the list is builded
	 * @param p_strShowField
	 *            - name for the visible field
	 * @param p_strKeyField
	 *            - name for the id field
	 */
	public dbListBox(String p_strTableName, String p_strShowField, String p_strKeyField) {
		this.strTableName = p_strTableName;
		this.strShowField = p_strShowField;
		this.strKeyField = p_strKeyField;
		this.LoadData();
	}

	/**
	 * constructor 4 - parameters
	 * 
	 * @param p_strTableName
	 *            - name of the table from the list is builded
	 * @param p_strShowField
	 *            - name for the visible field
	 * @param p_strKeyField
	 *            - name for the id field
	 * @param p_strFilterCondition
	 *            - condition for the SQL command
	 */
	public dbListBox(String p_strTableName, String p_strShowField, String p_strKeyField, String p_strFilterCondition) {
		this.strTableName = p_strTableName;
		this.strShowField = p_strShowField;
		this.strKeyField = p_strKeyField;
		this.strFilterCondition = p_strFilterCondition;
		this.LoadData();
	}

	/**
	 * constructor 5 - parameters
	 * 
	 * @param p_strTableName
	 *            - name of the table from the list is builded
	 * @param p_strShowField
	 *            - name for the visible field
	 * @param p_strKeyField
	 *            - name for the id field
	 * @param p_strFilterCondition
	 *            - condition for the SQL command
	 * @param p_strOrder
	 *            - order fields
	 */
	public dbListBox(String p_strTableName, String p_strShowField, String p_strKeyField, String p_strFilterCondition,
			String p_strOrder) {
		this.strTableName = p_strTableName;
		this.strShowField = p_strShowField;
		this.strKeyField = p_strKeyField;
		this.strFilterCondition = p_strFilterCondition;
		this.strOrder = p_strOrder;
		this.LoadData();
	}

	/**
	 * constructor 6 - parameters
	 * 
	 * @param p_strTableName
	 *            - name of the table from the list is builded
	 * @param p_strShowField
	 *            - name for the visible field
	 * @param p_strKeyField
	 *            - name for the id field
	 * @param p_strFilterCondition
	 *            - condition for the SQL command
	 * @param p_strOrder
	 *            - order fields
	 * @param p_strLinkedField
	 *            - linked field (the R field - foreign key), The values will be
	 *            update after each change, and the initial value of the list
	 *            will correspond to him.
	 */
	public dbListBox(String p_strTableName, String p_strShowField, String p_strKeyField, String p_strFilterCondition,
			String p_strOrder, String p_strLinkedField) {
		this.strTableName = p_strTableName;
		this.strShowField = p_strShowField;
		this.strKeyField = p_strKeyField;
		this.strFilterCondition = p_strFilterCondition;
		this.strOrder = p_strOrder;
		this.strLinkedField = p_strLinkedField;
		// change handler
		this.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIntdex = dbListBox.this.getSelectedIndex();
				String selectedString = dbListBox.this.getValue(selectedIntdex);
				selectedString = listXD.get(selectedIntdex).KeyValue;
				if (dbListBox.this.R != null)
					dbListBox.this.R.put(dbListBox.this.strLinkedField, selectedString);
				else
					DebugUtils.W("Informatia nu se poate modifica sau nu este adaugata !");

			}
		});

		this.LoadData();
	}

	/**
	 * Load information from database
	 */
	public void LoadData() {
		this.clear();
		this.addItem("Loading ...");
		if (this.strSQLCommand.equals(""))
			dbService.LoadListXDFromData(this.strTableName, this.strShowField, this.strKeyField,
					this.strFilterCondition, this.strOrder, new AsyncCallback<ListXD>() {
						public void onFailure(Throwable caught) {
							DebugUtils.W("Error - RPC - dbListBox");
						}

						public void onSuccess(ListXD result) {
							listXD = result;
							Fill();
						}
					});
		else
			dbService.LoadListXDFromData(this.strSQLCommand, this.strFilterCondition, new AsyncCallback<ListXD>() {
				public void onFailure(Throwable caught) {
					DebugUtils.W("Error - RPC - dbListBox");
				}

				public void onSuccess(ListXD result) {
					listXD = result;
					Fill();
				}
			});

	}

	/**
	 * Fill the list with the loaded information
	 */
	public void Fill() {
		// clear the list
		this.clear();
		for (int i = 0; i < listXD.size(); i++) {
			this.addItem(listXD.get(i).ShowValue);
		}
		// set the list is loaded - used for a timer event to check is the list
		// is loaded.
		isLoaded = true;

		// disable the items that begin with (inactiv)

		Element element = this.getElement().getFirstChildElement();
		for (int i = 0; i < this.getItemCount(); i++) {
			if (this.getItemText(i).startsWith("(inactiv)")) {
				element.setAttribute("disabled", "true");
			}
			element = element.getNextSiblingElement();
		} // for

	}

	/**
	 * set the link field from R
	 * 
	 * @param p_strLinkedField
	 */
	public void SetLinkedField(String p_strLinkedField) {
		this.strLinkedField = p_strLinkedField;
		// change handler
		this.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int selectedIntdex = dbListBox.this.getSelectedIndex();
				String selectedString = dbListBox.this.getValue(selectedIntdex);
				selectedString = listXD.get(selectedIntdex).KeyValue;
				if (dbListBox.this.R != null)
					dbListBox.this.R.put(dbListBox.this.strLinkedField, selectedString);
				else
					DebugUtils.W("Informatia nu se poate modifica sau nu este adaugata !");
			}
		});
	}

	/**
	 * setter for filter
	 * 
	 * @param strFilterText
	 */
	public void SetFilter(String strFilterText) {
		this.strFilterCondition = strFilterText;
	}

	/**
	 * setter for SQL command
	 * 
	 * @param strSQLCommand
	 */
	public void SetSQLCommand(String strSQLCommand) {
		this.strSQLCommand = strSQLCommand;
		// DebugUtils.D(strSQLCommand);
	}

	/**
	 * Refresh - show in the list the value for R.colName
	 */
	public void Refresh() {

		// Get the Value

		if (this.strLinkedField.equals(""))
			return;
		String KeyValue = R.getString(this.strLinkedField);
		if (KeyValue == null)
			return;
		KeyValue = KeyValue.trim();
		// count in list2D
		String value;
		Boolean found = false;

		for (int i = 0; i < listXD.size(); i++) {
			try {
				value = listXD.get(i).KeyValue;
				if (value != null) {
					value = value.trim();
					if (KeyValue.equals(value)) {
						this.setSelectedIndex(i);
						found = true;
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				DebugUtils.W("dbListBox.Refresh() Exception");
			}
		}
		// not found ... set the control to the first value
		// 20131207 - B - not good ... to change R in refresh()
		if (!found && 0 < listXD.size()) {

			// R.put(this.strLinkedField, listXD.get(0).KeyValue.trim());
		}
	}

	/**
	 * move to a specified item in list (by KeyValue)
	 * 
	 * @param KeyValue
	 */
	public void GoTo(String KeyValue) {
		// show in the list the value for R.colName;
		// Get the Value

		KeyValue = KeyValue.trim();
		// count in list2D
		String value;

		for (int i = 0; i < listXD.size(); i++) {
			try {
				value = listXD.get(i).KeyValue;
				if (value != null) {
					value = value.trim();
					if (KeyValue.equals(value)) {
						this.setSelectedIndex(i);
					}
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				DebugUtils.W("dbListBox.Refresh() Exception");
			}
		}
	}

	/**
	 * Load and Refresh - used for lists that depends of another informations
	 * from GUI and need to be reloaded with different set of values
	 */
	public void LoadDataAndRefresh() {
		if (this.strSQLCommand.equals(""))
			dbService.LoadListXDFromData(this.strTableName, this.strShowField, this.strKeyField,
					this.strFilterCondition, this.strOrder, new AsyncCallback<ListXD>() {
						public void onFailure(Throwable caught) {
							DebugUtils.W("Error - RPC - dbListBox");
						}

						public void onSuccess(ListXD result) {
							listXD = result;
							Fill();
							Refresh();
						}
					});
		else
			dbService.LoadListXDFromData(this.strSQLCommand, this.strFilterCondition, new AsyncCallback<ListXD>() {
				public void onFailure(Throwable caught) {
					DebugUtils.W("Error - RPC - dbListBox");
				}

				public void onSuccess(ListXD result) {
					listXD = result;
					Fill();
					Refresh();
				}
			});
	}

	/**
	 * return the value of the KeyField for the current selection
	 * 
	 * @return
	 */
	public String getKeyField() {
		int i = this.getSelectedIndex();
		if (i != -1) {
			String selectedString = this.listXD.get(i).KeyValue;
			selectedString = selectedString.trim();
			return selectedString;
		} else
			return "";
	}

	/**
	 * return the value of the ShowField for the current selection
	 * 
	 * @return
	 */
	public String getShowField() {
		int i = this.getSelectedIndex();
		if (i != -1) {
			String selectedString = this.listXD.get(i).ShowValue;
			selectedString = selectedString.trim();
			return selectedString;
		} else
			return "<nedefinit>";
	}

	/**
	 * return the String value of the Supplementary field for the current
	 * selection
	 * 
	 * @return
	 * @deprecated
	 */
	public String getX1Field() {
		int i = this.getSelectedIndex();
		if (i != -1) {
			String selectedString = this.listXD.get(i).X1Value;
			selectedString = selectedString.trim();
			return selectedString;
		} else
			return "";

	}

	/**
	 * return the Double value of the Supplementary field for the current
	 * selection
	 * 
	 * @return
	 * @deprecated
	 */
	public Double getX1DField() {
		int i = this.getSelectedIndex();
		if (i != -1) {
			Double dReturnValue = this.listXD.get(i).X1DValue;
			return dReturnValue;
		} else
			return 0d;
	}

	/**
	 * setter for R
	 */
	public void SetR(DBRecord R1) {
		this.R = R1;
	}

	/**
	 * getter for LinkedField
	 */
	public String getLinkedField() {
		return strLinkedField;
	}

}
