package gwtSql.server;

import gwtSql.client.DBService;
import gwtSql.shared.DBConnection;
import gwtSql.shared.DBException;
import gwtSql.shared.DBRecord;
import gwtSql.shared.DBTable;
import gwtSql.shared.DbManager;
import gwtSql.shared.ListXD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@SuppressWarnings("serial")
public class DBServiceImpl extends RemoteServiceServlet implements DBService {

	public String deleteDBRecord(String p_TableName, String p_KeyColumnName, String p_KeyColumnValue) throws DBException {
		String strErrorMessage = "";
		try {

			strErrorMessage = DbManager.getDB().deleteDBRecord(this.getUser(), p_TableName, p_KeyColumnName, p_KeyColumnValue);

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!strErrorMessage.isEmpty())
			throw new DBException(strErrorMessage);

		return strErrorMessage;
	}

	public String deleteDBRecord(String p_strSQLCommand) throws DBException {
		String strErrorMessage = "";
		try {

			strErrorMessage = DbManager.getDB().deleteDBRecord(this.getUser(), p_strSQLCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!strErrorMessage.isEmpty())
			throw new DBException(strErrorMessage);

		return strErrorMessage;
	}

	public DBRecord GetDBRecord(String p_tableName, String p_colName, String p_colValue) {

		DBRecord oRecord = new DBRecord(DBConnection.isLog);
		try {
			DbManager.getDB().GetDBRecord(this.getUser(), oRecord, p_tableName, p_colName, p_colValue);
			return oRecord;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oRecord;
	}

	public DBRecord GetDBRecordForConditon(String p_sqlCond) {
		DBRecord oRecord = new DBRecord(DBConnection.isLog);
		try {
			DbManager.getDB().GetDBRecordForConditon(this.getUser(), oRecord, p_sqlCond);
			return oRecord;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oRecord;
	}

	public DBRecord GetDBRecordForConditon(String p_tableName, String p_sqlCond) {
		DBRecord oRecord = new DBRecord(DBConnection.isLog);
		try {
			DbManager.getDB().GetDBRecordForConditon(this.getUser(), oRecord, p_tableName, p_sqlCond);
			return oRecord;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oRecord;
	}

	public DBRecord GetBlankDBRecord(String p_tableName, String p_colName, String p_colValue, String p_colKeyName) {
		return GetBlankDBRecord(p_tableName, p_colName, p_colValue, p_colKeyName, 0);
	}

	/* supraincarcare pentru a putea executa o metoda dupa AppendBlank */
	public DBRecord GetBlankDBRecord(String p_tableName, String p_colName, String p_colValue, String p_colKeyName, int p_call_level) {

		DBRecord oRecord = new DBRecord(DBConnection.isLog);
		try {
			DbManager.getDB().GetBlankDBRecord(this.getUser(), oRecord, p_tableName, p_colName, p_colValue, p_colKeyName, p_call_level);
			return oRecord;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oRecord;
	}

	public String saveDBRecord(DBRecord R) throws DBException {
		String strErrorMessage = "";
		try {
			strErrorMessage = DbManager.getDB().saveDBRecord(this.getUser(), R);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// if (!strErrorMessage.isEmpty())
		if (strErrorMessage.length() > 10)
			throw new DBException(strErrorMessage);
		return strErrorMessage;
	}

	public DBTable getDBTable(String p_strTableName, String p_strKeyName, String p_strFilterCondition, String p_strOrderCondition) throws DBException {

		String strErrorMessage = "";
		DBTable oTable = new DBTable();
		try {

			strErrorMessage = DbManager.getDB().getDBTable(this.getUser(), oTable, p_strTableName, p_strKeyName, p_strFilterCondition,
					p_strOrderCondition);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!strErrorMessage.isEmpty())
			throw new DBException(strErrorMessage);

		return oTable;
	}

	public DBTable getDBTable(String p_strTableName, String p_strKeyName, String p_strFilterCondition) throws DBException {

		String strErrorMessage = "";
		DBTable oTable = new DBTable();
		try {

			strErrorMessage = DbManager.getDB().getDBTable(this.getUser(), oTable, p_strTableName, p_strKeyName, p_strFilterCondition);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!strErrorMessage.isEmpty())
			throw new DBException(strErrorMessage);

		return oTable;
	}

	public DBTable getDBTable(String p_strSQLCommand) throws DBException {

		String strErrorMessage = "";
		DBTable oTable = new DBTable();
		try {

			strErrorMessage = DbManager.getDB().getDBTable(this.getUser(), oTable, p_strSQLCommand);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!strErrorMessage.isEmpty())
			throw new DBException(strErrorMessage);

		return oTable;
	}

	public List<DBTable> getDBXTable(String p_strSQLCommand) throws DBException {

		String strErrorMessage = "";
		List<DBTable> oListDB = new ArrayList<DBTable>();

		try {

			strErrorMessage = DbManager.getDB().getDBXTable(this.getUser(), oListDB, p_strSQLCommand);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (!strErrorMessage.isEmpty())
			throw new DBException(strErrorMessage);

		return oListDB;
	}

	public DBTable saveDBTable(DBTable oTable) {

		DBTable T = oTable;
		try {
			T = DbManager.getDB().saveDBTable(this.getUser(), oTable);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return T;
	}

	public void SetIniFileName(String strIniFileName) {
		DbManager.iniFileName = strIniFileName;
	}

	public String GetIniFileName() {
		return DbManager.iniFileName;
	}

	public String deleteForCondition(String p_tableName, String p_sqlCond) throws DBException {
		String strErrorMessage = "";
		try {
			strErrorMessage = DbManager.getDB().deleteForCondition(this.getUser(), p_tableName, p_sqlCond);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!strErrorMessage.isEmpty())
			throw new DBException(strErrorMessage);
		return strErrorMessage;

	}

	public String GETNNEWID(String p_idname, String p_tableName) {
		String strErrorMessage = "";
		try {
			strErrorMessage = DbManager.getDB().GETNNEWID(this.getUser(), p_idname, p_tableName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strErrorMessage;
	}

	public String executeNoResultSet(String p_sqlCommand) throws DBException {
		String strErrorMessage = "";
		try {
			strErrorMessage = DbManager.getDB().executeNoResultSet(this.getUser(), p_sqlCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!strErrorMessage.isEmpty())
			throw new DBException(strErrorMessage);
		return strErrorMessage;
	}

	public String executeResultSetNoOutput(String p_sqlCommand) throws DBException {
		String strErrorMessage = "";
		try {
			strErrorMessage = DbManager.getDB().executeResultSetNoOutput(this.getUser(), p_sqlCommand);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!strErrorMessage.isEmpty())
			throw new DBException(strErrorMessage);
		return strErrorMessage;
	}

	/**
	 * server side Println
	 */
	public void D(String strText) {
		System.out.println(strText);
	}

	// 20151112 - Use Only the complete call
	// public DBRecord DoLogin(String p_strAlias, String p_strPassword) {
	// return DoLogin(p_strAlias, p_strPassword, "PAROLA");
	// }
	//
	// public DBRecord DoLogin(String p_strAlias, String p_strPassword, String
	// p_PasswordField) {
	// return DoLogin(p_strAlias, p_strPassword, p_PasswordField, "ALIAS");
	// }

	public DBRecord DoLogin(String p_strAlias, String p_strPassword, String p_AliasField, String p_PasswordField) {

		DBRecord oRecord = new DBRecord(DBConnection.isLog);
		try {
			DbManager.getDB().DoLogin(oRecord, p_strAlias, p_strPassword, p_PasswordField, p_AliasField);

			/* store the user in session */
			HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
			HttpSession session = httpServletRequest.getSession(true);
			session.setAttribute("User", oRecord);

			return oRecord;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oRecord;
	}

	public String DoLogout() {

		try {

			/* store the user in session */
			HttpServletRequest httpServletRequest = this.getThreadLocalRequest();
			HttpSession session = httpServletRequest.getSession(true);
			session.setAttribute("User", null);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	public ListXD LoadListXDFromData(String strTableName, String strShowField, String strKeyField, String strFilterCondition, String strOrder) {

		ListXD oList = new ListXD();
		try {
			DbManager.getDB().GetList2D(this.getUser(), oList, strTableName, strShowField, strKeyField, strFilterCondition, strOrder);
			return oList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public ListXD LoadListXDFromData(String strSQLCommand, String strFilterCondition) {

		ListXD oList = new ListXD();
		try {
			DbManager.getDB().GetListXD(this.getUser(), oList, strSQLCommand, strFilterCondition);
			return oList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oList;
	}

	public String deleteFile(String fileNamewithPathandExt) {
		try {
			// System.out.println("deleteeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
			// String fileNamewithPath =
			// getServletConfig().getServletContext().getRealPath("reports\\" +
			// fileNamewithPathandExt);
			// System.out.println(fileNamewithPathandExt);
			// System.out.println(fileNamewithPath);

			File file = new File(fileNamewithPathandExt);
			// System.out.println(file.getAbsoluteFile());
			if (file.delete()) {
				// System.out.println(file.getName() + " is deleted!");
			} else {
				// System.out.println("Delete operation is failed.");
				throw new IOException("Failed to delete the file because: " + getReasonForFileDeletionFailureInPlainEnglish(file));
			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			return ex.getMessage();
		}
		return "";
	}

	public String getReasonForFileDeletionFailureInPlainEnglish(File file) {
		try {
			if (!file.exists())
				return "It doesn't exist in the first place.";
			else if (file.isDirectory() && file.list().length > 0)
				return "It's a directory and it's not empty.";
			else
				return "Somebody else has it open, we don't have write permissions, or somebody stole my disk.";
		} catch (SecurityException e) {
			return "We're sandboxed and don't have filesystem access.";
		}
	}

	// public String getReport(String fileName, HashMap<String, Object> param,
	// String type) throws DBException {
	// try {
	// // Class.forName("com.mysql.jdbc.Driver");
	// // Connection con =
	// // DriverManager.getConnection("jdbc:mysql://localhost:3306/sales",
	// // "sa", "bcmanager");
	// Connection con = DbManager.getDB().getConn(this.getUser()).con;
	// String fileNamewithPath =
	// getServletConfig().getServletContext().getRealPath("reports\\" +
	// fileName);
	// System.out.println("++++++++ report file +++++++");
	// System.out.println(fileNamewithPath);
	//
	// // define the report
	// JasperPrint print = JasperFillManager.fillReport(fileNamewithPath +
	// ".jasper", param, con);
	//
	// // generate a new name for file
	//
	// // String newFileName = filePath + "." + type;
	// // String unique_ext = String.format("%s.%s",
	// // RandomStringUtils.randomAlphanumeric(8), type);
	// SecureRandom random = new SecureRandom();
	//
	// String unique_ext = "_" + new BigInteger(30, random).toString(32) + "." +
	// type;
	//
	// String resultFileNameWithPath = fileNamewithPath + unique_ext;
	//
	// // System.out.println("++++++++ result file +++++++");
	// // System.out.println(resultFileNameWithPath);
	//
	// switch (type) {
	// case "pdf":
	// JasperExportManager.exportReportToPdfFile(print, resultFileNameWithPath);
	// break;
	// case "html":
	// JasperExportManager.exportReportToHtmlFile(print, resultFileNameWithPath);
	// break;
	// default:
	// type = "html";
	// JasperExportManager.exportReportToHtmlFile(print, resultFileNameWithPath);
	// }
	//
	// return resultFileNameWithPath;
	//
	// } catch (JRException ex) {
	// System.out.println(ex.getMessage());
	// /* nu am raport jasper ... deci e posibil sa fie un raport grafic */
	// /* trebuie sa creez parametrii din ceea ce exista in param */
	// throw new DBException("NO FILE");
	// }
	//
	// }

	public String getReport(String fileName, HashMap<String, Object> param, String type) {
		try {
			// Class.forName("com.mysql.jdbc.Driver");
			// Connection con =
			// DriverManager.getConnection("jdbc:mysql://localhost:3306/sales",
			// "sa", "bcmanager");
			Connection con = DbManager.getDB().getConn(this.getUser()).con;
			String fileNamewithPath = getServletConfig().getServletContext().getRealPath("reports");
			// System.out.println("++++++++ report file +++++++");
			// System.out.println(fileNamewithPath);
			// System.out.println(fileNamewithPath + "\\" + fileName + ".jasper");
			fileNamewithPath = fileNamewithPath + "\\" + fileName;

			// define the report
			JasperPrint jasperPrint = JasperFillManager.fillReport(fileNamewithPath + ".jasper", param, con);

			// generate a new name for file

			// String newFileName = filePath + "." + type;
			// String unique_ext = String.format("%s.%s",
			// RandomStringUtils.randomAlphanumeric(8), type);
			SecureRandom random = new SecureRandom();

			String unique_ext = "_" + new BigInteger(30, random).toString(32) + "." + type;

			String resultFileNameWithPath = fileNamewithPath + unique_ext;

			// System.out.println("++++++++ result file +++++++");
			// System.out.println(resultFileNameWithPath);

			switch (type) {
			
			case "pdf":
				JasperExportManager.exportReportToPdfFile(jasperPrint, resultFileNameWithPath);
				break;
				
			case "html":
				JasperExportManager.exportReportToHtmlFile(jasperPrint, resultFileNameWithPath);
				break;
				
			case "xls":
				JRXlsExporter exporterXLS = new JRXlsExporter();
				exporterXLS.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
				exporterXLS.setParameter(JRExporterParameter.INPUT_FILE_NAME, fileNamewithPath + ".jasper");
				exporterXLS.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, resultFileNameWithPath);

				exporterXLS.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS, Boolean.TRUE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN, Boolean.TRUE);
				exporterXLS.setParameter(JRXlsExporterParameter.IS_IGNORE_GRAPHICS, Boolean.FALSE);

				exporterXLS.exportReport();

				break;

			case "view":
				// output the html string
				type = "html";
				JasperExportManager.exportReportToHtmlFile(jasperPrint, resultFileNameWithPath);

				Scanner scanner;
				try {
					scanner = new Scanner(new File(resultFileNameWithPath));
					String html = scanner.useDelimiter("\\A").next();
					scanner.close();
					File f = new File(resultFileNameWithPath);
					f.delete();
					resultFileNameWithPath = html;

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}

				break;

			default:
				type = "html";
				JasperExportManager.exportReportToHtmlFile(jasperPrint, resultFileNameWithPath);
			}

			return resultFileNameWithPath;

		} catch (JRException ex) {
			System.out.println(ex.getMessage());
			/* nu am raport jasper ... deci e posibil sa fie un raport grafic */
			/* trebuie sa creez parametrii din ceea ce exista in param */
			// throw new DBException("NO FILE");
			return "No File";
		}

	}

	// public String put(String key, String value) {
	//
	// HttpServletRequest request = this.getThreadLocalRequest();
	// HttpSession session = request.getSession();
	//
	// if (session.getAttribute(key) == null) {
	// session.setAttribute(key, new HashMap<String, String>());
	// }
	// @SuppressWarnings("unchecked")
	// HashMap<String, String> mapOfResults = (HashMap<String, String>)
	// session.getAttribute(key);
	// mapOfResults.put(key, value);
	// return "ok";
	//
	// }
	//
	// public String get(String key) {
	//
	// HttpServletRequest request = this.getThreadLocalRequest();
	// HttpSession session = request.getSession();
	//
	// if (session.getAttribute(key) == null) {
	// session.setAttribute(key, new HashMap<String, String>());
	// }
	// @SuppressWarnings("unchecked")
	// HashMap<String, String> mapOfResults = (HashMap<String, String>)
	// session.getAttribute(key);
	// return mapOfResults.get(key);
	// }

	public DBRecord getUser() {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();

		return (DBRecord) session.getAttribute("User");

	}

}
