package gwtSql.client.controls;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.shared.DBRecord;
import gwtSql.shared.ListXD;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

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

	public dbListBox(String strSQLCommand, String p_strTableName) {
		this.strTableName = p_strTableName;
		this.strShowField = "ShowFld";
		this.strKeyField = "KeyFld";
		this.strSQLCommand = strSQLCommand;
		this.LoadData();
	}

	public dbListBox(String p_strTableName, String p_strShowField, String p_strKeyField) {
		this.strTableName = p_strTableName;
		this.strShowField = p_strShowField;
		this.strKeyField = p_strKeyField;
		this.LoadData();
	}

	public dbListBox(String p_strTableName, String p_strShowField, String p_strKeyField, String p_strFilterCondition) {
		this.strTableName = p_strTableName;
		this.strShowField = p_strShowField;
		this.strKeyField = p_strKeyField;
		this.strFilterCondition = p_strFilterCondition;
		this.LoadData();
	}

	public dbListBox(String p_strTableName, String p_strShowField, String p_strKeyField, String p_strFilterCondition, String p_strOrder) {
		this.strTableName = p_strTableName;
		this.strShowField = p_strShowField;
		this.strKeyField = p_strKeyField;
		this.strFilterCondition = p_strFilterCondition;
		this.strOrder = p_strOrder;
		this.LoadData();
	}

	public dbListBox(String p_strTableName, String p_strShowField, String p_strKeyField, String p_strFilterCondition, String p_strOrder,
			String p_strLinkedField) {
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
					Window.alert("Informatia nu se poate modifica sau nu este adaugata !");

			}
		});

		this.LoadData();
	}

	// incarcare cu date
	public void LoadData() {
		this.clear();
		this.addItem("Loading ...");
		if (this.strSQLCommand.equals(""))
			dbService.LoadListXDFromData(this.strTableName, this.strShowField, this.strKeyField, this.strFilterCondition, this.strOrder,
					new AsyncCallback<ListXD>() {
						public void onFailure(Throwable caught) {
							Window.alert("Error - RPC - dbListBox");
						}

						public void onSuccess(ListXD result) {
							listXD = result;
							Fill();
						}
					});
		else
			dbService.LoadListXDFromData(this.strSQLCommand, this.strFilterCondition, new AsyncCallback<ListXD>() {
				public void onFailure(Throwable caught) {
					Window.alert("Error - RPC - dbListBox");
				}

				public void onSuccess(ListXD result) {
					listXD = result;
					Fill();
				}
			});

	}

	public void Fill() {
		// clear the list
		this.clear();
		for (int i = 0; i < listXD.size(); i++) {
			this.addItem(listXD.get(i).ShowValue);
		}
		// setez ca este incarcata
		isLoaded = true;

		// daca am item-uri care incep cu \ le fac disabled

		Element element = this.getElement().getFirstChildElement();
		for (int i = 0; i < this.getItemCount(); i++) {
			if (this.getItemText(i).startsWith("(inactiv)")) {
				element.setAttribute("disabled", "true");
				// this.setItemText(i, this.getItemText(i).replaceFirst("@", ""));
			}
			element = element.getNextSiblingElement();
		} // for

	}

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
					Window.alert("Informatia nu se poate modifica sau nu este adaugata !");
			}
		});
	}

	public void SetFilter(String strFilterText) {
		this.strFilterCondition = strFilterText;
	}

	public void SetSQLCommand(String strSQLCommand) {
		this.strSQLCommand = strSQLCommand;
		// DebugUtils.D(strSQLCommand);
	}

	public void Refresh() {
		// show in the list the value for R.colName;
		// Get the Value

		if (this.strLinkedField.equals(""))
			return;
		String KeyValue = (String) R.get(this.strLinkedField);
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
				Window.alert("dbListBox.Refresh() Exception");
			}
		}
		// not found ... set the control to the first value
		// 20131207 - B - not good ... to change R in refresh()
		if (!found && 0 < listXD.size()) {

			// R.put(this.strLinkedField, listXD.get(0).KeyValue.trim());
		}
	}

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
				Window.alert("dbListBox.Refresh() Exception");
			}
		}
	}

	public void LoadDataAndRefresh() {
		if (this.strSQLCommand.equals(""))
			dbService.LoadListXDFromData(this.strTableName, this.strShowField, this.strKeyField, this.strFilterCondition, this.strOrder,
					new AsyncCallback<ListXD>() {
						public void onFailure(Throwable caught) {
							Window.alert("Error - RPC - dbListBox");
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
					Window.alert("Error - RPC - dbListBox");
				}

				public void onSuccess(ListXD result) {
					listXD = result;
					Fill();
					Refresh();
				}
			});
	}

	public String getKeyField() {
		int i = this.getSelectedIndex();
		if (i != -1) {
			String selectedString = this.listXD.get(i).KeyValue;
			selectedString = selectedString.trim();
			return selectedString;
		} else
			return "";
	}

	public String getShowField() {
		int i = this.getSelectedIndex();
		if (i != -1) {
			String selectedString = this.listXD.get(i).ShowValue;
			selectedString = selectedString.trim();
			return selectedString;
		} else
			return "<nedefinit>";
	}

	public String getX1Field() {
		int i = this.getSelectedIndex();
		if (i != -1) {
			String selectedString = this.listXD.get(i).X1Value;
			selectedString = selectedString.trim();
			return selectedString;
		} else
			return "";

	}

	public Double getX1DField() {
		int i = this.getSelectedIndex();
		if (i != -1) {
			Double dReturnValue = this.listXD.get(i).X1DValue;
			return dReturnValue;
		} else
			return 0d;
	}

	public void SetR(DBRecord R1) {
		this.R = R1;
	}
}
