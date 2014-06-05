package gwtsql_sample.client;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.shared.DBException;
import gwtSql.shared.DBTable;
import gwtSql.shared.DebugUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */



/*
 * 
 *  in the INI file you have the connection parameters to an sql database
 *  
 *  also a table named table1 with a name field must exists.
 * 
 * 
 */
public class Gwtsql_sample implements EntryPoint {
	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DBServiceAsync dbService = GWT.create(DBService.class);

	/**
	 * This is the entry point method.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onModuleLoad() {

		String strIniFileName;
		strIniFileName = GWT.getHostPageBaseURL();
		// getHostPageBaseURL return something like
		// http://server:port/application_Name/
		// and I need only application name
		int endIndex = strIniFileName.lastIndexOf("/");
		strIniFileName = strIniFileName.substring(0, endIndex);
		int beginIndex = strIniFileName.lastIndexOf("/");
		strIniFileName = strIniFileName.substring(beginIndex + 1);
		strIniFileName = strIniFileName.replace(":", "_") + ".ini";
		DebugUtils.D(strIniFileName);
		dbService.SetIniFileName(strIniFileName, new AsyncCallback() {
			@Override
			public void onFailure(Throwable caught) {
				Window.alert("SetIniFileName  Fail  ");
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Object result) {
			}
		});

		final Button askButton = new Button("Ask - SQL");

		askButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				String strSQLCommand = "select * from table1 with (nolock)";
				dbService.getDBTable(strSQLCommand, new AsyncCallback<DBTable>() {
					@Override
					public void onSuccess(DBTable result) {
						// first item
						Window.alert(result.get(0).getString("NAME"));
					}

					@Override
					public void onFailure(Throwable caught) {

						String details = caught.getMessage();
						if (caught instanceof DBException) {
							Window.alert(((DBException) caught).getMessage());
						} else
							Window.alert("Gwtsql_sample.onClick fail" + details);

					}
				});

			}

		});

		RootPanel.get("askButton").add(askButton);

	}
}
