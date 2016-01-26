package gwtSql.client.forms;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.SingleSelectionModel;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.client.controls.Controls;
import gwtSql.shared.DBException;
import gwtSql.shared.DBRecord;
import gwtSql.shared.DBTable;
import gwtSql.shared.DebugUtils;

public class VForm extends Composite implements IForm {

	public DBRecord R, R_BUFFER;

	// reference to the dialog box ...
	public ClosableDialogBox dialogbox_form;

	/**
	 * list of all controls in the form - added with AddControl and used for
	 * Refresh
	 */
	public List<Controls> MyControls = new ArrayList<Controls>();

	/**
	 * pointer to the caller form - will handle the onReturn event
	 */
	protected VForm caller_form = null;
	protected String caller_varName;

	/**
	 * controls used for the CellTable used in the VForm
	 */
	protected SingleSelectionModel<DBRecord> selectionModel;
	protected List<DBRecord> list = null;
	protected DBRecord selected;

	/**
	 * last insert ID - for autoincrement fields
	 */
	public String LAST_INSERT_ID;

	/**
	 * list fost mandatory fields, added with AddMandatoryField verified with
	 * VerifyMandatoryField
	 */
	private List<String> MandatoryFields = null;

	// database connector
	private final DBServiceAsync dbService = GWT.create(DBService.class);

	/**
	 * add a control to the list of controls (will be used in RefreshControls)
	 * 
	 * @param C
	 */
	protected void AddControl(Controls C) {
		this.MyControls.add(C);
	}

	/**
	 * set the R for all the controls previously added with AddControl
	 * 
	 * @param R
	 */
	protected void SetMyControls(DBRecord R) {
		// DebugUtils.D(MyControls.size());
		for (int i = 0; i < MyControls.size(); i++) {
			Controls C = MyControls.get(i);
			C.SetR(R);
		}
	}

	/**
	 * set the R for all db Controls and refresh them after
	 * 
	 * @param R
	 */
	protected void RefreshMyControls(DBRecord R) {
		this.SetMyControls(R);
		this.RefreshMyControls();
	}

	/**
	 * refresh all controls (previously added with AddControl)
	 */
	protected void RefreshMyControls() {
		// DebugUtils.D(MyControls.size());
		for (int i = 0; i < MyControls.size(); i++) {
			try {
				Controls C = MyControls.get(i);
				C.Refresh();
			} catch (Exception e) {
				DebugUtils.W("RefreshMyControls.Exception on control " + (i + 1) + " field "
						+ MyControls.get(i).getLinkedField() + " \n maybe is null ?");
			}
		}
	}

	/**
	 * function called for return
	 */
	@Override
	public void onReturn(String type, DBRecord R) {
	}

	/**
	 * save to database
	 * 
	 * @param R
	 */
	public void DesktopForm_saveDBRecord(final DBRecord R) {
		/* save without key */
		DesktopForm_saveDBRecord(R, "");
	}

	/**
	 * Save with key
	 * 
	 * @param R
	 * @param key
	 * 
	 *            return via onReturn with DesktopForm_saveDBRecord + key
	 */
	public void DesktopForm_saveDBRecord(final DBRecord R, final String key) {
		/* save */
		dbService.saveDBRecord(R, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				/* ok */
				LAST_INSERT_ID = result;
				onReturn("DesktopForm_saveDBRecord" + key, R);
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					DebugUtils.W("Fail !" + ((DBException) caught).getMessage());
				else
					DebugUtils.W("DesktopForm_saveDBRecord fail - " + details);
			}
		});
	}

	/**
	 * Delete record from database
	 * 
	 * @param R
	 * 
	 *            return via onReturn with DesktopForm_deleteDBRecord
	 */
	public void DesktopForm_deleteDBRecord(final DBRecord R) {
		/* delete */
		DesktopForm_deleteDBRecord(R.tableName, R.KeyName, R.KeyValue);
	}

	/**
	 * delete from database by column=value
	 * 
	 * @param tableName
	 * @param colName
	 * @param colValue
	 * 
	 *            return via onReturn with DesktopForm_deleteDBRecord
	 */
	public void DesktopForm_deleteDBRecord(String tableName, String colName, String colValue) {
		dbService.deleteDBRecord(tableName, colName, colValue, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				onReturn("DesktopForm_deleteDBRecord", null);
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof DBException)
					DebugUtils.W(((DBException) caught).getMessage());
				else
					DebugUtils.W("DesktopForm_deleteDBRecord Exception");
			}
		});
	}

	/**
	 * create a empty R
	 * 
	 * @param tableName
	 *            - table
	 * @param colName
	 *            - (seek column - optional)
	 * @param colValue
	 *            - (seek value - optional)
	 * @param colKeyName
	 *            - ID KeyName
	 * 
	 *            return via onReturn with DesktopForm_GetBlankDBRecord
	 */
	public void DesktopForm_GetBlankDBRecord(String tableName, String colName, String colValue, String colKeyName) {

		dbService.GetBlankDBRecord(tableName, colName, colValue, colKeyName, new AsyncCallback<DBRecord>() {

			@Override
			public void onSuccess(DBRecord result) {
				try {
					R = result;
					// refresh
					RefreshMyControls(R);

					onReturn("DesktopForm_GetBlankDBRecord", R);

				} catch (Exception e) {
					DebugUtils.W(e.toString());
					DebugUtils.W("DesktopForm_GetBlankDBRecord Exception");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					DebugUtils.W(((DBException) caught).getMessage());
				else
					DebugUtils.W("DesktopForm_GetBlankDBRecord fail - " + details);
			}
		});

	}

	/**
	 * get one record from the database by column=value
	 * 
	 * @param tableName
	 * @param colName
	 * @param colValue
	 * 
	 *            return via onReturn with DesktopForm_GetDBRecord
	 */
	public void DesktopForm_GetDBRecord(String tableName, String colName, String colValue) {
		DesktopForm_GetDBRecord(tableName, colName, colValue, null, "");
	}

	/**
	 * get one record from the database by column=value and fill the DBRecord
	 * parameter with the result
	 * 
	 * @param tableName
	 * @param colName
	 * @param colValue
	 * 
	 *            return via onReturn with DesktopForm_GetDBRecord + type
	 */
	public void DesktopForm_GetDBRecord(String tableName, String colName, String colValue, final DBRecord R_RETURN,
			final String type) {

		dbService.GetDBRecord(tableName, colName, colValue, new AsyncCallback<DBRecord>() {

			@Override
			public void onSuccess(DBRecord result) {
				try {

					// refresh - only for type empty - that means is a call for
					// the
					// main form
					if (type.isEmpty()) {
						R = result;
						RefreshMyControls(R);
					}

					onReturn("DesktopForm_GetDBRecord" + type, result);

				} catch (Exception e) {
					DebugUtils.W(e.toString());
					DebugUtils.W("DesktopForm_GetDBRecord Exception");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					DebugUtils.W(((DBException) caught).getMessage());
				else
					DebugUtils.W("DesktopForm_GetDBRecord fail - " + details);
			}
		});

	}

	/**
	 * get a record from the database using a specified SQL command
	 * 
	 * return via onReturn with DesktopForm_GetDBRecordForCondition + type
	 * 
	 */
	public void DesktopForm_GetDBRecordForCondition(String strSQLCommand, final String type) {

		dbService.GetDBRecordForConditon(strSQLCommand, new AsyncCallback<DBRecord>() {

			@Override
			public void onSuccess(DBRecord result) {
				try {

					onReturn("DesktopForm_GetDBRecordForCondition" + type, result);

				} catch (Exception e) {
					DebugUtils.W(e.toString());
					DebugUtils.W("DesktopForm_GetDBRecordForCondition Exception");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					DebugUtils.W(((DBException) caught).getMessage());
				else
					DebugUtils.W("DesktopForm_GetDBRecord fail - " + details);
			}
		});

	}

	/**
	 * execute an sql script without a specified return result (using
	 * executeUpdate) if the sql return a result, an exception will be fired
	 * 
	 * @param strSQLCommand
	 * 
	 *            return via onReturn with DesktopForm_executeResultSetNoOutput
	 *            + key
	 */
	public void DesktopForm_executeResultSetNoOutput(String strSQLCommand, final String key) {
		/* save */
		dbService.executeResultSetNoOutput(strSQLCommand, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				/* ok */
				onReturn("DesktopForm_executeResultSetNoOutput" + key, R);
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					DebugUtils.W(((DBException) caught).getMessage());
				else
					DebugUtils.W("DesktopForm_executeResultSetNoOutput fail - " + details);
			}
		});
	}

	/**
	 * execute an sql script without a specified return result (using
	 * executeQuery) if the sql return a result, an exception will be fired
	 * 
	 * @param strSQLCommand
	 * 
	 *            return via onReturn with DesktopForm_executeNoResultSet + key
	 */

	public void DesktopForm_executeNoResultSet(String strSQLCommand, final String key) {
		/* save */
		dbService.executeNoResultSet(strSQLCommand, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				/* ok */
				onReturn("DesktopForm_executeNoResultSet" + key, R);
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					DebugUtils.W(((DBException) caught).getMessage());
				else
					DebugUtils.W("DesktopForm_executeNoResultSet fail - " + details);
			}
		});
	}

	/**
	 * Save a table
	 * 
	 * @param T
	 * 
	 *            return via onReturn with DesktopForm_saveDBTable
	 */
	public void DesktopForm_saveDBTable(final DBTable T) {
		DesktopForm_saveDBTable(T, "");
	}

	/**
	 * Save a table with parametrised onReturn
	 * 
	 * @param T
	 *            - table to save
	 * @param key
	 *            - key for onReturn text
	 * 
	 *            return via onReturn with DesktopForm_saveDBTable + key
	 */
	public void DesktopForm_saveDBTable(final DBTable T, final String key) {
		/* save */
		dbService.saveDBTable(T, new AsyncCallback<DBTable>() {
			public void onSuccess(DBTable result) {
				/* ok */
				onReturn("DesktopForm_saveDBTable" + key, null);
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					DebugUtils.W("Fail !" + ((DBException) caught).getMessage());
				else
					DebugUtils.W("DesktopForm_saveDBTable fail - " + details);
			}

		});
	}

	/**
	 * action for the Select button ... (DialogSelectForm)
	 */
	public void Select() {

		selected = selectionModel.getSelectedObject();

		if (selected != null) {
			// selected
			if (caller_form != null) {

				caller_form.onReturn(caller_varName, selected);
			}

			if (dialogbox_form != null)
				dialogbox_form.hide();

		}
	} // Select()

	public void hide() {
		if (dialogbox_form != null)
			dialogbox_form.hide();
	}

	@Override
	public DBRecord ReturnSelected() {
		return selected;
	}

	public void setCaller_form(VForm f) {
		caller_form = f;
	}

	/**
	 * apply rights for the current class
	 * 
	 * @param strRights
	 *            = TheApp.loginInfo.User.getString("RIGHTS")
	 * @param className
	 *            = this.getClass().getName() example of call :
	 *            ApplyRights(TheApp.loginInfo.User.getString("RIGHTS"),
	 *            this.getClass().getName());
	 */
	public void ApplyRights(String strRights, String className) {

		strRights = strRights.replaceAll("<div>", "\n");
		strRights = strRights.replaceAll("</div>", "\n");
		strRights = strRights.replaceAll("<br>", "\n");
		strRights = strRights.replaceAll(" ", "");
		strRights = strRights.replaceAll("\n\n", "\n");

		String[] aRights = strRights.split("\n");
		boolean lShow = false;

		// cut at the $ sign
		int nPos = className.indexOf("$");
		if (nPos > 0)
			className = className.substring(0, nPos);
		className = className.trim() + ".";
		//
		String right;
		for (int i = 0; i < aRights.length; i++) {
			// search the class name in the string
			right = aRights[i];
			// if + is show if - is hide
			if (right.startsWith("-")) {
				lShow = false;
				right = right.replaceAll("-", "");
			} else {
				lShow = true;
			}

			//
			if (right.contains(className)) {
				right = right.replaceAll(className, "");
				if (lShow)
					ShowControl(right);
				else
					HideControl(right);
			}
		}
	}

	/**
	 * hide controls ... with the specified id ...and more 5 ... with the id_1,
	 * id_2, ... id_5
	 * 
	 * @param id
	 */
	public native void HideControl(String id)
	/*-{
		if ($doc.getElementById(id) != null)
			$doc.getElementById(id).style.display = "none";

		for (i = 1; i < 5; i++) {
			id1 = id + "_" + i;

			if ($doc.getElementById(id1) != null)
				$doc.getElementById(id1).style.display = "none";
		}
	}-*/;

	/**
	 * show controls ...with the specified id ...and more 5 ... with the id_1,
	 * id_2, ... id_5
	 * 
	 * @param id
	 */
	public native void ShowControl(String id)
	/*-{
		if ($doc.getElementById(id) != null)
			$doc.getElementById(id).style.display = "block";

		for (i = 1; i < 5; i++) {
			id1 = id + "_" + i;

			if ($doc.getElementById(id1) != null)
				$doc.getElementById(id1).style.display = "block";
		}
	}-*/;

	/**
	 * Will display the given message in the messages div.
	 * 
	 * @param msg
	 *            The string that we wish to display
	 * @param msgClass
	 *            The class that will be applied in the message div. "sad" for
	 *            error messages, "happy" for successful messages
	 */
	public native void showMessage(String msg, String msgClass)
	/*-{
		msgDiv = $doc.getElementById("msgText");
		if (msgDiv != null) {
			msgDiv.innerHTML = "<li class=\"" + msgClass + "\">" + msg
					+ "</li>";
			setTimeout(function() {
				$doc.getElementById("msgText").innerHTML = "";
			}, 5000);
		}
	}-*/;

	/**
	 * Will hide the message displayed in the message div.
	 */
	public native void hideMessage()
	/*-{
		if ($doc.getElementById("msgText") != null) {
			$doc.getElementById("msgText").innerHTML = "";
		}
	}-*/;

	/**
	 * add the mandatory fields of the module
	 * 
	 * @param fieldName
	 */
	public void AddMandatory(String fieldName) {
		if (MandatoryFields == null)
			MandatoryFields = new ArrayList<String>();
		this.MandatoryFields.add(fieldName);
	}

	/**
	 * test for each field in the mandatory list and return false if emty or
	 * null is found
	 * 
	 * @return (true or false)
	 */
	public boolean verifMandatory() {
		if (MandatoryFields == null)
			return true;
		for (int i = 0; i < MandatoryFields.size(); i++)
			if (R.getString(MandatoryFields.get(i)).isEmpty())
				return false;
		return true;
	}

}
