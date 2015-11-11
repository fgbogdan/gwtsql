package gwtSql.shared;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.client.controls.AlertWidget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DebugUtils {

	/* serviciul de comunicare cu baza de date */
	private final static DBServiceAsync dbService = GWT.create(DBService.class);

	/**
	 * Alert Widget - the object with trace (without wait)
	 * 
	 * @param o
	 * @param bComplex
	 */
	public static void D(Object o, int bComplex) {
		try {
			throw new Exception("Who called me?");
		} catch (Exception e) {
			AlertWidget.alertWidget("Called by " + e.getStackTrace()[1].getClassName() + "." + e.getStackTrace()[1].getMethodName() + "()!");
		}
		AlertWidget.alertWidget(o.toString());
	}

	/**
	 * Alert Widget - the object (without wait)
	 * 
	 * @param o
	 */
	public static void D(Object o) {
		// Logger log = Logger.getLogger(ClassName.class.getName());
		// log.info(o.toString());
		AlertWidget.alertWidget(o.toString());
	}

	/**
	 * Window alert - the object with trace
	 * 
	 * @param o
	 * @param bComplex
	 */
	public static void W(Object o, int bComplex) {
		try {
			throw new Exception("Who called me?");
		} catch (Exception e) {
			Window.alert("Called by " + e.getStackTrace()[1].getClassName() + "." + e.getStackTrace()[1].getMethodName() + "()!");
		}
		Window.alert(o.toString());
	}

	/**
	 * Window alert - the object
	 * 
	 * @param o
	 */
	public static void W(Object o) {
		Window.alert(o.toString());
	}

	/**
	 * Log on server side log file
	 * 
	 * @param s
	 */
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

	/**
	 * GWT log - the object
	 * 
	 * @param o
	 */
	public static void G(Object o) {
		if (o == null)
			GWT.log("null");
		else
			GWT.log(o.toString());
	}

	/**
	 * GWT log - the object with trace
	 * 
	 * @param o
	 * @param bComplex
	 */
	public static void G(Object o, int bComplex) {

		try {
			throw new Exception("Who called me?");
		} catch (Exception e) {
			G("Called by " + e.getStackTrace()[1].getClassName() + "." + e.getStackTrace()[1].getMethodName() + "()!");
		}
		G(o.toString());
	}

}
