package gwtSql.client;

import gwtSql.shared.DBRecord;
import gwtSql.shared.DBTable;
import gwtSql.shared.ListXD;

import java.util.HashMap;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DBServiceAsync {

	void deleteDBRecord(String p_TableName, String p_KeyColumnName, String p_KeyColumnValue, AsyncCallback<String> asyncCallback);

	void deleteDBRecord(String p_SQLCommand, AsyncCallback<String> asyncCallback);

	void GetDBRecord(String p_tableName, String p_colName, String p_colValue, AsyncCallback<DBRecord> asyncCallback);

	void GetDBRecordForConditon(String p_sqlCond, AsyncCallback<DBRecord> asyncCallback);

	void GetDBRecordForConditon(String p_tableName, String p_sqlCond, AsyncCallback<DBRecord> asyncCallback);

	void saveDBRecord(DBRecord R, AsyncCallback<String> callback);

	void GetBlankDBRecord(String p_tableName, String p_colName, String p_colValue, String p_colKeyName, AsyncCallback<DBRecord> callback);

	/* supraincarcare pentru a putea executa o metoda dupa AppendBlank */
	void GetBlankDBRecord(String p_tableName, String p_colName, String p_colValue, String p_colKeyName, int p_call_level,
			AsyncCallback<DBRecord> callback);

	// void ReadWriteConf(String key, String value, AsyncCallback<String>
	// asyncCallback);

	void getDBTable(String strSQLCommand, AsyncCallback<DBTable> asyncCallback);

	void getDBTable(String p_strTableName, String p_strKeyName, String p_strFilterCondition, AsyncCallback<DBTable> asyncCallback);

	void getDBTable(String p_strTableName, String p_strKeyName, String p_strFilterCondition, String p_strOrderCondition,
			AsyncCallback<DBTable> asyncCallback);

	void getDBXTable(String strSQLCommand, AsyncCallback<List<DBTable>> asyncCallback);

	void saveDBTable(DBTable oTable, AsyncCallback<DBTable> callback);

	void SetIniFileName(String strIniFileName, AsyncCallback<Void> callback);

	void GetIniFileName(AsyncCallback<String> callback);

	void deleteForCondition(String p_tableName, String p_sqlCond, AsyncCallback<String> asyncCallback);

	void GETNNEWID(String p_idname, String p_tableName, AsyncCallback<String> asyncCallback);

	void executeNoResultSet(String p_sqlCommand, AsyncCallback<String> asyncCallback);

	void executeResultSetNoOutput(String p_sqlCommand, AsyncCallback<String> callback);

	void D(String strText, AsyncCallback<Void> callback);

	void deleteFile(String fileNamewithPathandExt, AsyncCallback<String> callback);

	void DoLogin(String p_strAlias, String p_strPassword, String p_AliasField, String p_PasswordField, AsyncCallback<DBRecord> callback);

	void LoadListXDFromData(String strSQLCommand, String strFilterCondition, AsyncCallback<ListXD> callback);

	void LoadListXDFromData(String strTableName, String strShowField, String strKeyField, String strFilterCondition, String strOrder,
			AsyncCallback<ListXD> callback);

	void getReport(String fileName, HashMap<String, Object> param, String type, AsyncCallback<String> callback);
}
