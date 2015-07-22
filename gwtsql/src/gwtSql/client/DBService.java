package gwtSql.client;

import gwtSql.shared.DBException;
import gwtSql.shared.DBRecord;
import gwtSql.shared.DBTable;
import gwtSql.shared.ListXD;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("DBService")
public interface DBService extends RemoteService {

	String deleteDBRecord(String p_TableName, String p_KeyColumnName, String p_KeyColumnValue) throws DBException;

	String deleteDBRecord(String p_SQLCommand) throws DBException;

	DBRecord GetDBRecord(String p_tableName, String p_colName, String p_colValue);

	DBRecord GetDBRecordForConditon(String p_sqlCond);

	DBRecord GetDBRecordForConditon(String p_tableName, String p_sqlCond);

	DBRecord GetBlankDBRecord(String p_tableName, String p_colName, String p_colValue, String p_colKeyName);

	DBRecord GetBlankDBRecord(String p_tableName, String p_colName, String p_colValue, String p_colKeyName, int p_call_level);

	String saveDBRecord(DBRecord R) throws DBException;

	// String ReadWriteConf(String key, String value);

	DBTable getDBTable(String strSQLCommand) throws DBException;

	DBTable getDBTable(String p_strTableName, String p_strKeyName, String p_strFilterCondition) throws DBException;

	DBTable getDBTable(String p_strTableName, String p_strKeyName, String p_strFilterCondition, String p_strOrderCondition) throws DBException;

	List<DBTable> getDBXTable(String p_strSQLCommand) throws DBException;

	DBTable saveDBTable(DBTable oTable);

	String deleteForCondition(String p_tableName, String p_sqlCond) throws DBException;

	String executeNoResultSet(String p_sqlCommand) throws DBException;

	String executeResultSetNoOutput(String p_sqlCommand) throws DBException;

	void SetIniFileName(String strIniFileName);
	
	String GetIniFileName();

	String GETNNEWID(String p_idname, String p_tableName);

	public String deleteFile(String fileNamewithPathandExt);

	void D(String strText);

	DBRecord DoLogin(String p_strAlias, String p_strPassword);

	DBRecord DoLogin(String p_strAlias, String p_strPassword, String p_PasswordField);

	ListXD LoadListXDFromData(String strSQLCommand, String strFilterCondition);

	ListXD LoadListXDFromData(String strTableName, String strShowField, String strKeyField, String strFilterCondition, String strOrder);

	public String getReport(String fileName, HashMap<String, Object> param, String type) throws DBException;

}
