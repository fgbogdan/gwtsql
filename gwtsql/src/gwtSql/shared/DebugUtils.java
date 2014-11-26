package gwtSql.shared;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.client.controls.AlertWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DebugUtils {

	/* serviciul de comunicare cu baza de date */
	private final static DBServiceAsync dbService = GWT.create(DBService.class);

	// final static Logger log = Logger.getLogger(ClassName.class.getName());

	public static void D(Object o, int bComplex) {
		// Logger log = Logger.getLogger(ClassName.class.getName());
		// try {
		// throw new Exception("Who called me?");
		// } catch (Exception e) {
		// log.info("Called by " + e.getStackTrace()[1].getClassName() + "." +
		// e.getStackTrace()[1].getMethodName() + "()!");
		//
		// }
		// log.info("-----");
		// log.info(o.toString());
		try {
			throw new Exception("Who called me?");
		} catch (Exception e) {
			AlertWidget.alertWidget("Called by " + e.getStackTrace()[1].getClassName() + "." + e.getStackTrace()[1].getMethodName() + "()!");
		}
		AlertWidget.alertWidget(o.toString());
	}

	public static void D(Object o) {
		// Logger log = Logger.getLogger(ClassName.class.getName());
		// log.info(o.toString());
		AlertWidget.alertWidget(o.toString());
	}

	public static void DS(String s) {
		dbService.D(s, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

}
