package gwtSql.client;

import gwtSql.shared.DateUtils;

import java.util.Date;

import com.google.gwt.junit.client.GWTTestCase;

public class DateUtilsTest extends GWTTestCase {

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
		assertEquals("2015-07-01", DateUtils.Date2String(DateUtils.String2Date("2015-07-01")));
		assertEquals(new Date(2015,07,01),DateUtils.truncateToDay(new Date(2015,07,01)));
	}

}
