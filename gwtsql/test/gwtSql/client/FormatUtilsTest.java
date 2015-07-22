package gwtSql.client;

import gwtSql.shared.FormatUtils;

import com.google.gwt.junit.client.GWTTestCase;

public class FormatUtilsTest extends GWTTestCase {

	public String getModuleName() {
		return "gwtSql.GwtSql";
	}

	/**
	 * simple test
	 */
	public void testSimple() {
		assertTrue(true);
	}
	
	public void test1(){
		assertFalse(FormatUtils.isNumeric(null));
		assertTrue(FormatUtils.isNumeric("1234.56"));
		assertFalse(FormatUtils.isNumeric("Abc4587"));
	}

}
