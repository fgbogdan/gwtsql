package gwtSql.client.controls;

import gwtSql.shared.DBRecord;

public interface IForm {
	
	void onReturn(String type, DBRecord R);
	VFloatForm getVFF();
	void setVFF(VFloatForm v);
	DBRecord ReturnSelected();

}
