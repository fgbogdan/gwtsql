package gwtSql.shared;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DebugUtils {
	
	/* serviciul de comunicare cu baza de date */
	private final static DBServiceAsync dbService = GWT.create(DBService.class);

	public static void D(Object o) {

		try {
			throw new Exception("Who called me?");
		} catch (Exception e) {
			System.out.println("Called by " + e.getStackTrace()[1].getClassName() + "." + e.getStackTrace()[1].getMethodName() + "()!");
		}
		System.out.println("-----");
		System.out.println(o);
	}

	public static void D(Object o, int bSimple) {

		System.out.println(o);
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
