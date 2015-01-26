package gwtSql.client.controls;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.shared.DBException;
import gwtSql.shared.DBRecord;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.view.client.SingleSelectionModel;

public class VForm extends Composite implements IForm {

	public DBRecord R, R_BUFFER;

	public VFloatForm VFF;

	public List<Controls> MyControls = new ArrayList<Controls>();

	protected IForm caller_form;
	protected String caller_varName;
	protected SingleSelectionModel<DBRecord> selectionModel;

	protected List<DBRecord> list = null;
	protected DBRecord selected;

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
				Window.alert("RefreshMyControls.Exception on control " + (i + 1));
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
		/* save */
		dbService.saveDBRecord(R, new AsyncCallback<String>() {
			@Override
			public void onSuccess(String result) {
				/* ok */
				onReturn("DesktopForm_saveDBRecord", R);
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					Window.alert(((DBException) caught).getMessage());
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

			if (VFF != null)
				VFF.hide();

		}
	} // Select()

	@Override
	public VFloatForm getVFF() {

		return VFF;
	}

	@Override
	public void setVFF(VFloatForm v) {
		VFF = v;

	}

	public void hide() {
		if (VFF != null)
			VFF.hide();
	}

	@Override
	public DBRecord ReturnSelected() {
		// TODO Auto-generated method stub
		return null;
	}

}
