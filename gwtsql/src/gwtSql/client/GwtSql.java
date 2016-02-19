package gwtSql.client;

import com.google.gwt.core.client.EntryPoint;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtSql implements EntryPoint {

	// playground
	//private final DBServiceAsync dbService = GWT.create(DBService.class);

	/**
	 * This is the entry point method.
	 */

	//@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onModuleLoad() {

		// Window.alert(GWT.getHostPageBaseURL()+"test123");
		//
		// // playground
		//
		// String strIniFileName;
		// strIniFileName = GWT.getHostPageBaseURL();
		// // System.out.println(strIniFileName);
		// // System.out.println(GWT.getModuleBaseURL());
		// // getHostPageBaseURL return something like
		// // http://server:port/application_Name/
		// // and I need only application name
		// int endIndex = strIniFileName.lastIndexOf("/");
		// strIniFileName = strIniFileName.substring(0, endIndex);
		// int beginIndex = strIniFileName.lastIndexOf("/");
		// strIniFileName = strIniFileName.substring(beginIndex + 1);
		// strIniFileName = strIniFileName.replace(":", "_") + ".ini";
		//
		// dbService.SetIniFileName(strIniFileName, new AsyncCallback() {
		// @Override
		// public void onFailure(Throwable caught) {
		// AlertWidget.alertWidget("SetIniFileName  Fail  ").center();
		// caught.printStackTrace();
		// }
		//
		// @Override
		// public void onSuccess(Object result) {
		//
		// // get info
		// dbService.GetDBRecordForConditon("select top 10 * from nom with (nolock)",
		// new AsyncCallback<DBRecord>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// W.a("Fail");
		//
		// }
		//
		// @Override
		// public void onSuccess(DBRecord result) {
		// GWT.log(result.toString());
		//
		// }
		//
		// });
		//
		// }
		// });

	}
}
