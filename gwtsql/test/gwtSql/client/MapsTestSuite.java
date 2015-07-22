package gwtSql.client;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class MapsTestSuite extends GWTTestSuite {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for a Maps Application");
		suite.addTestSuite(DateUtilsTest.class);
		suite.addTestSuite(FormatUtilsTest.class);
		suite.addTestSuite(DBServiceTest.class);
		return suite;
	}
}
