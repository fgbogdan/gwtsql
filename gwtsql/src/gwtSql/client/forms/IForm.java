package gwtSql.client.forms;

import gwtSql.shared.DBRecord;

public interface IForm {

	void onReturn(String type, DBRecord R);
	DBRecord ReturnSelected();

}
