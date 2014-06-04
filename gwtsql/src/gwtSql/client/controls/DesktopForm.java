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

public class DesktopForm extends Composite implements IForm {

	public DBRecord R, R_BUFFER;

	public VFloatForm VFF;

	public List<Controls> MyControls = new ArrayList<Controls>();

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
			Controls C = MyControls.get(i);
			C.Refresh();
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
					Window.alert(((DBException) caught).getMessage().replace("com.microsoft.sqlserver.jdbc.SQLServerException:", ""));
				else
					Window.alert("DesktopForm_saveDBRecord fail - " + details);
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
					System.out.println(e.toString());
					Window.alert("DesktopForm_GetBlankDBRecord Exception");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					Window.alert(((DBException) caught).getMessage().replace("com.microsoft.sqlserver.jdbc.SQLServerException:", ""));
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
					System.out.println(e.toString());
					Window.alert("DesktopForm_GetDBRecord Exception");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof DBException)
					Window.alert(((DBException) caught).getMessage().replace("com.microsoft.sqlserver.jdbc.SQLServerException:", ""));
				else
					Window.alert("DesktopForm_GetDBRecord fail - " + details);
			}
		});

	}

	@Override
	public VFloatForm getVFF() {

		return VFF;
	}

	@Override
	public void setVFF(VFloatForm v) {
		VFF = v;

	}

}
