package gwtSql.client;

import gwtSql.shared.DBException;
import gwtSql.shared.DBRecord;
import gwtSql.shared.DBTable;
import gwtSql.shared.ListXD;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("DBService")
public interface DBService extends RemoteService {

	String deleteDBRecord(String p_TableName, String p_KeyColumnName, String p_KeyColumnValue);

	String deleteDBRecord(String p_SQLCommand);

	DBRecord GetDBRecord(String p_tableName, String p_colName, String p_colValue);
	
	DBRecord GetDBRecordForConditon(String p_sqlCond);

	DBRecord GetDBRecordForConditon(String p_tableName, String p_sqlCond);

	DBRecord GetBlankDBRecord(String p_tableName, String p_colName, String p_colValue, String p_MethodName);

	DBRecord GetBlankDBRecord(String p_tableName, String p_colName, String p_colValue, String p_colKeyName, String p_MethodName);

	String saveDBRecord(DBRecord R) throws DBException;

	// String ReadWriteConf(String key, String value);

	DBTable getDBTable(String strSQLCommand) throws DBException;

	DBTable getDBTable(String p_strTableName, String p_strKeyName, String p_strFilterCondition) throws DBException;

	DBTable getDBTable(String p_strTableName, String p_strKeyName, String p_strFilterCondition, String p_strOrderCondition) throws DBException;

	DBTable saveDBTable(DBTable oTable);

	String deleteForCondition(String p_tableName, String p_sqlCond);

	String executeNoResultSet(String p_sqlCommand);

	String executeResultSetNoOutput(String p_sqlCommand);

	void SetIniFileName(String strIniFileName);

	String GETNNEWID(String p_idname);

	public String deleteFile(String fileNamewithPathandExt);

	void D(String strText);

	DBRecord DoLogin(String p_strAlias, String p_strPassword);

	ListXD LoadListXDFromData(String strSQLCommand, String strFilterCondition);

	ListXD LoadListXDFromData(String strTableName, String strShowField, String strKeyField, String strFilterCondition, String strOrder);

	public String getReport(String fileName, HashMap<String, Object> param, String type)throws DBException;

}
