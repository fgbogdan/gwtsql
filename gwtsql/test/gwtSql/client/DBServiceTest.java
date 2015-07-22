package gwtSql.client;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class DBServiceTest extends GWTTestCase {

	public String getModuleName() {
		return "gwtSql.GwtSqlJUnit";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void test1() {
		/* create the service that we will test. */
		final DBServiceAsync dbService = GWT.create(DBService.class);
		ServiceDefTarget target = (ServiceDefTarget) dbService;
		target.setServiceEntryPoint(GWT.getModuleBaseURL() + "gwtsql/DBService");

		/*
		 * since RPC calls are asynchronous, we will need to wait for a response
		 * after this test method returns. This line tells the test runner to wait
		 * up to 10 seconds before timing out.
		 */
		delayTestFinish(10000);

		/* send a request to the server. */
		final String strIniFileName = "test_ini_file.ini";

		dbService.SetIniFileName(strIniFileName, new AsyncCallback() {
			public void onFailure(Throwable caught) {
				/* The request resulted in an unexpected error. */
				fail("SetIniFileName failure: " + caught.getMessage());
			}

			public void onSuccess(Object result) {
				/* verify if the information is written to the server */
				dbService.GetIniFileName(new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						fail("GetIniFileName failure: " + caught.getMessage());
					}

					@Override
					public void onSuccess(String result) {
						assertTrue(result.endsWith(strIniFileName));
					}
				}); // GetIniFileName

				/*
				 * now that we have received a response, we need to tell the test
				 * runner that the test is complete. You must call finishTest()
				 * after an asynchronous test finishes successfully, or the test
				 * will time out.
				 */
				finishTest();
			}

		}); // SetIniFileName
	}

}
