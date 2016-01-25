package gwtSql.client.controls;

import gwtSql.shared.DBRecord;

/**
 * interface for all visual controls
 * @author bursuc
 *
 */
public interface Controls {
	
	void Refresh();
	
	void SetR(DBRecord R);
	
	String getLinkedField();
	
}
