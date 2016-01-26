package gwtSql.client.forms;

import gwtSql.shared.DBRecord;

/**
 * interface for all visual forms
 * 
 * @author bursuc
 *
 */
public interface IForm {

	void onReturn(String type, DBRecord R);

	DBRecord ReturnSelected();

}
