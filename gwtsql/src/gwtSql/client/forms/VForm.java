package gwtSql.client.forms;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.client.controls.Controls;
import gwtSql.shared.DBException;
import gwtSql.shared.DBRecord;
import gwtSql.shared.DBTable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.SingleSelectionModel;

public class VForm extends Composite implements IForm {

	public DBRecord R, R_BUFFER;

// reference to the dialog box ... if exists
	public ClosableDialogBox dialogbox_form;
	
	//public DialogBox DBox = null;

	public List<Controls> MyControls = new ArrayList<Controls>();

	protected VForm caller_form = null;
	protected String caller_varName;
	protected SingleSelectionModel<DBRecord> selectionModel;

	protected List<DBRecord> list = null;
	protected DBRecord selected;
	public String LAST_INSERT_ID;
	private List<String> MandatoryFields = null;

	/* serviciul de comunicare cu baza de date */
	private final DBServiceAsync dbService = GWT.create(DBService.class);

	protected void AddControl(Controls C) {
		this.MyControls.add(C);
	}

	protected void SetMyControls(DBRecord R) {
		// DebugUtils.D(MyControls.size());
		for (int i = 0; i < MyControls.size(); i++) {
			Controls C = MyControls.get(i);
			C.SetR(R);
		}
	}

	protected void RefreshMyControls(DBRecord R) {
		this.SetMyControls(R);
		this.RefreshMyControls();
	}

	protected void RefreshMyControls() {
		// DebugUtils.D(MyControls.size());
		for (int i = 0; i < MyControls.size(); i++) {
			try {
				Controls C = MyControls.get(i);
				C.Refresh();
			} catch (Exception e) {
				Window.alert("RefreshMyControls.Exception on control " + (i + 1) + " field " + MyControls.get(i).getLinkedField());
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
	 * Save button
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
					Window.alert("Fail !" + ((DBException) caught).getMessage());
				else
					Window.alert("DesktopForm_saveDBRecord fail - " + details);
			}
		});
	}

	/**
	 * Delete button
	 * 
	 * @param R
	 */
	public void DesktopForm_deleteDBRecord(final DBRecord R) {
		/* delete */
		DesktopForm_deleteDBRecord(R.tableName, R.KeyName, R.KeyValue);
	}

	public void DesktopForm_deleteDBRecord(String tableName, String colName, String colValue) {
		dbService.deleteDBRecord(tableName, colName, colValue, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				onReturn("DesktopForm_deleteDBRecord", null);
			}

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof DBException)
					Window.alert(((DBException) caught).getMessage());
				else
					Window.alert("DesktopForm_deleteDBRecord Exception");
			}
		});
	}

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
					Window.alert(e.toString());
					Window.alert("DesktopForm_GetBlankDBRecord Exception");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					Window.alert(((DBException) caught).getMessage());
				else
					Window.alert("DesktopForm_GetBlankDBRecord fail - " + details);
			}
		});

	}

	public void DesktopForm_GetDBRecord(String tableName, String colName, String colValue) {
		DesktopForm_GetDBRecord(tableName, colName, colValue, null, "");
	}

	public void DesktopForm_GetDBRecord(String tableName, String colName, String colValue, final DBRecord R_RETURN, final String type) {

		dbService.GetDBRecord(tableName, colName, colValue, new AsyncCallback<DBRecord>() {

			@Override
			public void onSuccess(DBRecord result) {
				try {

					// refresh - only for type empty - that means is a call for the
					// main form
					if (type.isEmpty()) {
						R = result;
						RefreshMyControls(R);
					}

					onReturn("DesktopForm_GetDBRecord" + type, result);

				} catch (Exception e) {
					Window.alert(e.toString());
					Window.alert("DesktopForm_GetDBRecord Exception");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					Window.alert(((DBException) caught).getMessage());
				else
					Window.alert("DesktopForm_GetDBRecord fail - " + details);
			}
		});

	}

	/**
	 * 
	 * getDBRecordForCondition
	 */
	public void DesktopForm_GetDBRecordForCondition(String strSQLCommand, final String type) {

		dbService.GetDBRecordForConditon(strSQLCommand, new AsyncCallback<DBRecord>() {

			@Override
			public void onSuccess(DBRecord result) {
				try {

					onReturn("DesktopForm_GetDBRecordForCondition" + type, result);

				} catch (Exception e) {
					Window.alert(e.toString());
					Window.alert("DesktopForm_GetDBRecordForCondition Exception");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					Window.alert(((DBException) caught).getMessage());
				else
					Window.alert("DesktopForm_GetDBRecord fail - " + details);
			}
		});

	}

	/**
	 * executeResultSetNoOutput
	 * 
	 * @param strSQLCommand
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
					Window.alert(((DBException) caught).getMessage());
				else
					Window.alert("DesktopForm_executeResultSetNoOutput fail - " + details);
			}
		});
	}

	/**
	 * executeNoResultSet
	 * 
	 * @param strSQLCommand
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
					Window.alert(((DBException) caught).getMessage());
				else
					Window.alert("DesktopForm_executeNoResultSet fail - " + details);
			}
		});
	}

	/**
	 * Save table without key
	 * 
	 * @param T
	 */
	public void DesktopForm_saveDBTable(final DBTable T) {
		DesktopForm_saveDBTable(T, "");
	}

	/**
	 * 
	 * @param T
	 *           - table to save
	 * @param key
	 *           - key for onReturn text
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
					Window.alert("Fail !" + ((DBException) caught).getMessage());
				else
					Window.alert("DesktopForm_saveDBTable fail - " + details);
			}

		});
	}

	/**
	 * select
	 */
	public void Select() {

		// DebugUtils.D(caller_form,1);
		// DebugUtils.D(caller_varName,1);
		selected = selectionModel.getSelectedObject();
		// DebugUtils.D("try to send ...", 1);
		// DebugUtils.D(selected, 1);
		if (selected != null) {
			// selected
			if (caller_form != null) {
				// DebugUtils.D("send ...", 1);
				// DebugUtils.D(selected, 1);
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
	 *           = TheApp.loginInfo.User.getString("RIGHTS")
	 * @param className
	 *           = this.getClass().getName() example of call :
	 *           ApplyRights(TheApp.loginInfo.User.getString("RIGHTS"),
	 *           this.getClass().getName());
	 */
	public void ApplyRights(String strRights, String className) {

		strRights = strRights.replaceAll("<div>", "\n");
		strRights = strRights.replaceAll("</div>", "\n");
		strRights = strRights.replaceAll("<br>", "\n");
		strRights = strRights.replaceAll(" ", "");
		strRights = strRights.replaceAll("\n\n", "\n");

		String[] aRights = strRights.split("\n");

		// cut at the $ sign
		int nPos = className.indexOf("$");
		if (nPos > 0)
			className = className.substring(0, nPos);
		className = className.trim() + ".";
		// Window.alert(className);
		String right;
		for (int i = 0; i < aRights.length; i++) {
			// search the class name in the string
			right = aRights[i];
			// Window.alert(right + "---" + className);
			if (right.contains(className)) {
				right = right.replaceAll(className, "");
				HideControl(right);
			}
		}
	}

	/**
	 * hide controls ...
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
	 * Will display the given message in the messages div.
	 * 
	 * @param msg
	 *           The string that we wish to display
	 * @param msgClass
	 *           The class that will be applied in the message div. "sad" for
	 *           error messages, "happy" for successful messages
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
	 * test for each field in the mandatory list and return false if emty or null
	 * is found
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
