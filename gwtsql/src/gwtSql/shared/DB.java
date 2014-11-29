package gwtSql.shared;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Properties;

public class DB {

	// Connection conn = null;

	private ArrayList<DBConnection> connections = new ArrayList<DBConnection>();

	/*
	 * Conexiunea la baza de date conexiunea propriuzisa data utlimei rulari daca
	 * ruleaza
	 */

	public DBConnection getConn(DBRecord oUser) {
		/*
		 * returneaza o conexiune din Pool-ul de conexiuni ... o sa fie
		 * implementat si daca nu e niciuna ... creeaza una
		 */

		String UserID = null;
		// try {
		// UserID = oUser.getString("USERID");
		// } catch (Exception e) {
		// }
		if (oUser != null)
			if (!oUser.tableName.isEmpty())
				UserID = oUser.getString("USERID");

		DBConnection con = null;

		String p_UserID;

		if (UserID == null)
			p_UserID = "";
		else
			p_UserID = UserID;

		// System.out.println("Connection for: " + p_UserID);

		for (int i = 0; i < connections.size(); i++) {
			con = (DBConnection) connections.get(i);
			/* only if not logged in or - a connection from the same user */
			if ("".equals(con.UserID) || p_UserID.equals(con.UserID)) {
				if (con.isinuse) {
					/* do nothing */

					// System.out.println(i);
					// System.out.println(con);
					// System.out.println("Connection in use ...");
					// System.out.println(con.bornDate);
				}
				if (!con.isinuse) {
					// i found a good one !
					// System.out.println("Found connection " + i);
					con.UseMe();
					break;
				} else if (con.isDead()) {
					// if this connection is too old then remove it from the list
					connections.remove(i);
					System.out.println("Connection is dead ... remove ...");
					i--;
					con = null;
				} else {
					con = null;
				}
			} else
				con = null;
		}

		if (null == con) {
			// there are not enough connections !
			// System.out.println("New connection !");
			try {
				con = new DBConnection();
				con.con = getSQLConnection(oUser);
				con.UseMe();
				connections.add(con);
				// System.out.println("New connection" + "@" + p_UserID + "#");
				// System.out.println(con);
			} catch (Exception e) {
				con = null;
				System.out.println("cannnot make a connection");
				e.printStackTrace();
			}
		}
		// System.out.println("Not null");

		// System.out.println("Connection old user ! " + con.UserID);
		if (!p_UserID.equals(con.UserID)) {
			con.UserID = p_UserID;
			System.out.println("Connection used for new user ! " + con.UserID);
			// update
			Statement st;
			try {
				st = con.con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				String strSQLCommand = "";
				if (DBConnection.isMySQL)
					strSQLCommand = "UPDATE userspid SET UserID='" + p_UserID + "' WHERE spid=connection_id();";
				else
					strSQLCommand = "UPDATE userspid SET UserID='" + p_UserID + "' WHERE spid=@@spid";
				st.execute(strSQLCommand);
				System.out.println("SPID - set !");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return con;

	}

	public Connection getSQLConnection(DBRecord oUser) {

		System.out.println("Create a new connection");

		Connection conn = null;

		/*
		 * read from ini
		 */

		String strServerName = "", strSQLUser = "", strSQLPassword = "", strSQLDatabase = "", strSQLSufix = "", strSQLFirma = "";
		String strSQLType, strisLog = "NO";

		try {

			Properties pro = new Properties();

			// Window.alert(GWT.getHostPageBaseURL());
			// Window.alert(GWT.getModuleBaseURL());
			// Window.alert(GWT.getModuleName());
			// Window.alert(GWT.getPermutationStrongName());

			// DebugUtils.D("TheApp");
			// DebugUtils.D(TheApp.iniFileName);
			// DebugUtils.D("DBManager");
			// DebugUtils.D(DbManager.iniFileName);

			System.out.println("the ini file is:" + System.getProperty("user.dir") + "\\" + DbManager.iniFileName);

			pro.load(new FileInputStream(DbManager.iniFileName));

			// System.out.println("read from ini begin ...");
			// sql server
			strServerName = pro.getProperty("SQLSERVER").trim();
			DBConnection.sqlServerName = strServerName;
			// sql user
			strSQLUser = pro.getProperty("SQLUSER").trim();
			// sql password
			strSQLPassword = pro.getProperty("SQLPASSWORD").trim();
			// database
			strSQLDatabase = pro.getProperty("SQLDATABASE").trim();
			DBConnection.sqlDatabase = strSQLDatabase;
			// database sufix
			strSQLSufix = pro.getProperty("SQLSUFIX");
			DBConnection.sqlSufix = strSQLSufix;

			// firm identification
			strSQLFirma = pro.getProperty("SQLFIRMA");
			if (strSQLFirma.isEmpty())
				strSQLFirma = "FRM";
			DBConnection.sqlIDFirma = strSQLFirma;
			// database type
			strSQLType = pro.getProperty("SQLTYPE");
			if (strSQLType.isEmpty())
				strSQLType = "MSSQL";
			if (!strSQLType.equals("MSSQL") && !strSQLType.equals("MYSQL"))
				throw new Exception("SQLTYPE not defined correctly in ini file (values accepted are MSSQL or MYSQL)");
			DBConnection.isMySQL = strSQLType.equals("MYSQL");
			// logging
			strisLog = pro.getProperty("ISLOG");
			if (strisLog.isEmpty())
				strisLog = "NO";
			DBConnection.isLog = strisLog.equals("YES") ? true : false;

			// System.out.println("read from ini ok ...");

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			System.out.println("---");
			System.out.println("the ini file must be in:" + System.getProperty("user.dir"));
		}

		try {
			if (DBConnection.isMySQL) {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
			} else {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
			}

		} catch (Exception e) {
			System.out.println("getConn ... ClassForName");
			e.printStackTrace();
		}

		// String url =
		// "jdbc:sqlserver://SrvVM2\\EX1;user=sa;password=bcmanager;databaseName=dbUnitM07_FRM;";
		// url =
		// "jdbc:mysql://xx.xx.xx.xx:3306;user=xxx;password=xxx;databaseName=xxx;";
		String url = "";

		if (DBConnection.isMySQL)
			url = "jdbc:mysql://" + strServerName + "/" + strSQLDatabase + "?zeroDateTimeBehavior=convertToNull&autoReconnect=true&relaxAutoCommit=true";
		else
			url = "jdbc:sqlserver://" + strServerName + ";user=" + strSQLUser + ";password=" + strSQLPassword + ";databaseName=" + strSQLDatabase + ";";
		System.out.println("Connect to server ... ");
		System.out.println(url);
		String sDataBase = "SqlDatabase=" + DBConnection.sqlDatabase;
		System.out.println(sDataBase);

		// Connection conn=null;
		try {
			if (DBConnection.isMySQL) {
				conn = DriverManager.getConnection(url, strSQLUser, strSQLPassword);
			} else {
				conn = DriverManager.getConnection(url);
			}

			/*
			 * if the connection is succesfull and TheApp.loginInfo.R is not null -
			 * set the information in UserSpid
			 */
			SetUserSPID(oUser, conn);

		} catch (SQLException e) {
			System.out.println("getConn ... get connection(url)");
			e.printStackTrace();
		}

		return conn;
	}

	/*
	 * intoarce un camp din baza de date
	 */
	public String GetDBFieldString(DBRecord oUser, String p_tableName, String p_colName, String p_KeycolName, String p_KeycolValue) {
		String strRetVal = null;

		DBConnection con = this.getConn(oUser);

		try {

			Connection conn = con.con;

			Statement st = conn.createStatement();

			ResultSet rs = null;
			try {
				String strSQLCommand = "";
				if (DBConnection.isMySQL)
					strSQLCommand = "SELECT " + p_colName + "  FROM " + p_tableName + " WHERE " + p_KeycolName + "='" + p_KeycolValue + "' LIMIT 1;";
				else
					strSQLCommand = "SELECT TOP 1 " + p_colName + " FROM " + p_tableName + " WITH (NOLOCK) WHERE " + p_KeycolName + "='" + p_KeycolValue
							+ "'";
				rs = st.executeQuery(strSQLCommand);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			// if (rs.first()) {
			if (rs.next()) {

				strRetVal = rs.getString(p_colName);

			}
		} catch (SQLException e) {
			System.out.println("GetDBFieldString ... get connection");
			e.printStackTrace();

		}

		con.ReleaseMe();

		return strRetVal;
	}

	/*
	 * GETNNEWID
	 */

	public String GETNNEWID(DBRecord oUser, String IDNAME) {
		String strRetVal = "";
		DBConnection con = this.getConn(oUser);
		try {

			/*
			 * long start = System.currentTimeMillis(); long elapsed =
			 * System.currentTimeMillis() - start;
			 * 
			 * System.out.println("Start"); System.out.println(elapsed);
			 */

			Connection conn = con.con;

			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

			ResultSet rs = null;
			try {
				String strSQLCommand = "EXEC GetNNewID '" + IDNAME + "'";
				rs = st.executeQuery(strSQLCommand);
			} catch (Exception e) {
				System.out.println(e.toString());
				System.out.println("EXEC GetNNewID '" + IDNAME + "'");
			}

			if (rs.next()) {

				double nValue = rs.getDouble("NVALUE");
				int i = (int) nValue;
				strRetVal = Integer.toString(i);
			} else {
				DebugUtils.D("Cheia " + IDNAME + " nu exista in contor ! Nu se poate genera ID !");
			}

		} catch (SQLException e) {
			System.out.println("GETNNEWID ... get connection");
			e.printStackTrace();

		}
		con.ReleaseMe();
		return strRetVal;
	}

	/*
	 * intoarce un record din baza de date pe baza unei chei
	 */
	public void GetDBRecord(DBRecord oUser, DBRecord oRecord, String tableName, String colName, String colValue) {
		// System.out.println("GetRecord");
		// System.out.println(tableName);
		DBConnection con = this.getConn(oUser);
		try {

			/*
			 * long start = System.currentTimeMillis(); long elapsed =
			 * System.currentTimeMillis() - start;
			 * 
			 * System.out.println("Start"); System.out.println(elapsed);
			 */

			Connection conn = con.con;

			// Statement st = conn.createStatement();
			String strSQLCommand = "";
			if (DBConnection.isMySQL)
				strSQLCommand = "SELECT * FROM " + tableName + " WHERE " + colName + "= ? LIMIT 1;";
			else
				strSQLCommand = "SELECT TOP 1 * FROM " + tableName + " WITH (NOLOCK) WHERE " + colName + "= ? ";

			PreparedStatement pst = conn.prepareStatement(strSQLCommand);

			ResultSet rs = null;
			try {

				pst.setString(1, colValue);

				rs = pst.executeQuery();

			} catch (Exception e) {
				System.out.println(e.toString());
			}

			if (rs.next()) {

				ResultSetMetaData rsmdResult = null;

				int intNoCols = 0;
				int intColumnType = 0;
				String strColname = null;
				Object strColvalue = null;

				try {
					rsmdResult = rs.getMetaData();
					intNoCols = rsmdResult.getColumnCount();

					oRecord.tableName = tableName;
					oRecord.KeyName = colName;
					oRecord.KeyValue = colValue;

					java.util.Calendar cal = Calendar.getInstance();

					for (int intCount = 1; intCount <= intNoCols; intCount++) {
						strColname = rsmdResult.getColumnName(intCount);
						strColname = strColname.toUpperCase();
						// parsing
						// type of the column
						intColumnType = rsmdResult.getColumnType(intCount);

						switch (intColumnType) {
						// numeric
						case 4:
							oRecord.put_original(strColname, rs.getInt(strColname));
							break;
						case 5:
							oRecord.put_original(strColname, rs.getInt(strColname));
							break;
						case 2:
							oRecord.put_original(strColname, rs.getDouble(strColname));
							break;
						// varchar sau text sau char
						case 12:
						case -1:
						case 1:
							oRecord.put_original(strColname, rs.getString(strColname));
							break;
						// bit
						case -7:
							oRecord.put_original(strColname, rs.getBoolean(strColname));
							break;
						// date
						case 91:
							// DebugUtils.D("date");
							// cal.setTimeZone(TimeZone.getTimeZone("GMT+2"));
							// DebugUtils.D(rs.getDate(intCount));
							cal.setTimeZone(java.util.TimeZone.getDefault());
							oRecord.put_original(strColname, rs.getDate(strColname, cal));
							// oRecord.put_original(strColname,
							// rs.getDate(strColname));
							break;
						// smalldatetime
						case 93:
							// oRecord.put_original(strColname,
							// rs.getDate(strColname));
							// DebugUtils.D("datetime");
							// cal.setTimeZone(TimeZone.getTimeZone("GMT+2"));
							// DebugUtils.D(rs.getTimestamp(strColname, cal));
							// oRecord.put_original(strColname,
							// rs.getTimestamp(strColname));
							cal.setTimeZone(java.util.TimeZone.getDefault());
							oRecord.put_original(strColname, rs.getTimestamp(strColname, cal));

							/*
							 * DebugUtils.D(strColname);
							 * DebugUtils.D(rs.getDate(strColname));
							 * DebugUtils.D(rs.getTimestamp(strColname));
							 * DebugUtils.D(oRecord.get(strColname));
							 */

							break;
						// unknown
						default:
							System.out.println("GetDBRecord - type not defined!");
							System.out.println(strColname);
							System.out.println(strColvalue);
							System.out.println(intColumnType);
							break;

						}// strColname!="RECORD"

						// NOTE: THE COLUMN NAMES WILL ALWAYS BE STORED IN
						// UPPERCASE, HENCE NEED TO BE RETRIEVED IN UPPER CASE
					}
				} catch (Exception e) {
					System.out.println(strColname);
					System.out.println(strColvalue);
					System.out.println(intColumnType);
					e.printStackTrace();
				}

			}

		} catch (SQLException e) {
			System.out.println("GetDBRecord ... get connection");
			e.printStackTrace();

		}

		con.ReleaseMe();

		oRecord.isChanged = false;
	}

	/*
	 * intoarce un record din baza de date pe baza unei conditii (sql) poate fi
	 * apelat cu tabela sau nu
	 */
	public void GetDBRecordForConditon(DBRecord oUser, DBRecord oRecord, String strSQLCommand) {
		GetDBRecordForConditon(oUser, oRecord, "", strSQLCommand);
	}

	public void GetDBRecordForConditon(DBRecord oUser, DBRecord oRecord, String tableName, String strSQLCond) {

		DBConnection con = this.getConn(oUser);

		try {

			Connection conn = con.con;

			Statement st = conn.createStatement();

			ResultSet rs = null;
			try {
				String strSQLCommand;
				if (tableName.isEmpty())
					strSQLCommand = strSQLCond;
				else {
					if (DBConnection.isMySQL)
						strSQLCommand = "SELECT * FROM " + tableName + " WHERE " + strSQLCond + " LIMIT 1;";
					else
						strSQLCommand = "SELECT TOP 1 * FROM " + tableName + " WITH (NOLOCK) WHERE " + strSQLCond;
				}

				rs = st.executeQuery(strSQLCommand);
			} catch (Exception e) {
				System.out.println("GetRecordForConditon");
				System.out.println(e.toString());
			}

			// first record
			if (rs.next()) {

				ResultSetMetaData rsmdResult = null;

				int intNoCols = 0;
				String strColname = null;
				Object strColvalue = null;

				try {
					rsmdResult = rs.getMetaData();
					intNoCols = rsmdResult.getColumnCount();

					oRecord.tableName = tableName;
					oRecord.KeyName = "";
					oRecord.KeyValue = "";

					for (int intCount = 1; intCount <= intNoCols; intCount++) {
						strColname = rsmdResult.getColumnName(intCount);
						strColvalue = rs.getString(strColname);
						// if(rs.wasNull())
						// strColvalue="";
						oRecord.put_original(strColname.toUpperCase(), strColvalue);
						// NOTE: THE COLUMN NAMES WILL ALWAYS BE STORED IN
						// UPPERCASE, HENCE NEED TO BE RETRIEVED IN UPPER CASE
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (SQLException e) {
			System.out.println("GetDBRecordForCondition ... get connection");
			e.printStackTrace();

		}

		con.ReleaseMe();
		oRecord.isChanged = false;
	}

	/*
	 * intoarce un record din baza de date pe baza unei chei
	 */
	public void GetDBRecordwithCon(Connection conn, DBRecord oRecord, String tableName, String colName, String colValue) {
		// System.out.println("GetRecord");
		// System.out.println(tableName);

		try {

			/*
			 * long start = System.currentTimeMillis(); long elapsed =
			 * System.currentTimeMillis() - start;
			 * 
			 * System.out.println("Start"); System.out.println(elapsed);
			 */

			String strSQLCommand = "";
			if (DBConnection.isMySQL)
				strSQLCommand = "SELECT * FROM " + tableName + " WHERE " + colName + "= ? LIMIT 1;";
			else
				strSQLCommand = "SELECT TOP 1 * FROM " + tableName + " WITH (NOLOCK) WHERE " + colName + "= ? ";

			PreparedStatement pst = conn.prepareStatement(strSQLCommand);

			ResultSet rs = null;
			try {

				pst.setString(1, colValue);
				rs = pst.executeQuery();

			} catch (Exception e) {
				System.out.println(e.toString());
			}

			if (rs.next()) {

				ResultSetMetaData rsmdResult = null;

				int intNoCols = 0;
				int intColumnType = 0;
				String strColname = null;
				Object strColvalue = null;

				try {
					rsmdResult = rs.getMetaData();
					intNoCols = rsmdResult.getColumnCount();

					oRecord.tableName = tableName;
					oRecord.KeyName = colName;
					oRecord.KeyValue = colValue;

					java.util.Calendar cal = Calendar.getInstance();

					for (int intCount = 1; intCount <= intNoCols; intCount++) {
						strColname = rsmdResult.getColumnName(intCount);
						strColname = strColname.toUpperCase();
						// parsing
						// type of the column
						intColumnType = rsmdResult.getColumnType(intCount);

						switch (intColumnType) {
						// numeric
						case 4:
							oRecord.put_original(strColname, rs.getInt(strColname));
							break;
						case 5:
							oRecord.put_original(strColname, rs.getInt(strColname));
							break;
						case 2:
							oRecord.put_original(strColname, rs.getDouble(strColname));
							break;
						// varchar sau text sau char
						case 12:
						case -1:
						case 1:
							oRecord.put_original(strColname, rs.getString(strColname));
							break;
						// bit
						case -7:
							oRecord.put_original(strColname, rs.getBoolean(strColname));
							break;
						// date
						case 91:
							// DebugUtils.D("date");
							// cal.setTimeZone(TimeZone.getTimeZone("GMT+2"));
							cal.setTimeZone(java.util.TimeZone.getDefault());
							// DebugUtils.D(rs.getDate(intCount));
							// oRecord.put_original(strColname,
							// rs.getDate(strColname));
							oRecord.put_original(strColname, rs.getDate(strColname, cal));
							break;
						// smalldatetime
						case 93:
							// oRecord.put_original(strColname,
							// rs.getDate(strColname));
							// DebugUtils.D("datetime");
							// cal.setTimeZone(TimeZone.getTimeZone("GMT+2"));
							cal.setTimeZone(java.util.TimeZone.getDefault());
							// DebugUtils.D(rs.getTimestamp(strColname, cal));
							// oRecord.put_original(strColname,
							// rs.getTimestamp(strColname));
							oRecord.put_original(strColname, rs.getTimestamp(strColname, cal));
							/*
							 * DebugUtils.D(strColname);
							 * DebugUtils.D(rs.getDate(strColname));
							 * DebugUtils.D(rs.getTimestamp(strColname));
							 * DebugUtils.D(oRecord.get(strColname));
							 */

							break;
						// unknown
						default:
							System.out.println("GetDBRecord - type not defined!");
							System.out.println(strColname);
							System.out.println(strColvalue);
							System.out.println(intColumnType);
							break;

						}// strColname!="RECORD"

						// NOTE: THE COLUMN NAMES WILL ALWAYS BE STORED IN
						// UPPERCASE, HENCE NEED TO BE RETRIEVED IN UPPER CASE
					}
				} catch (Exception e) {
					System.out.println(strColname);
					System.out.println(strColvalue);
					System.out.println(intColumnType);
					e.printStackTrace();
				}

			}

		} catch (SQLException e) {
			System.out.println("GetDBRecord ... get connection");
			e.printStackTrace();

		}

		oRecord.isChanged = false;

	}

	/*
	 * intoarce un record gol din baza de date
	 */

	/* supraincarcare pentru a putea executa o metoda dupa AppendBlank */
	public void GetBlankDBRecord(DBRecord oUser, DBRecord oRecord, String tableName, String colName, String colValue, String colKeyName) {
		GetBlankDBRecord(oUser, oRecord, tableName, colName, colValue, colKeyName, "");
	}

	public void GetBlankDBRecord(DBRecord oUser, DBRecord oRecord, String tableName, String colName, String colValue, String colKeyName,
			String functionName) {

		DBConnection con = this.getConn(oUser);

		try {

			Connection conn = con.con;

			Statement st = conn.createStatement();

			ResultSet rs = null;
			try {
				// iau o inregistrare ... daca nu exista nici una ... adaug una
				// ... si o sterg
				String strSQLCommand = "";
				if (DBConnection.isMySQL)
					strSQLCommand = "SELECT * FROM " + tableName + " LIMIT 1;";
				else
					strSQLCommand = "SELECT TOP 1 * FROM " + tableName + " WITH (NOLOCK) ";

				rs = st.executeQuery(strSQLCommand);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			if (rs.next()) {

				ResultSetMetaData rsmdResult = null;

				int intNoCols = 0;
				int intColumnType = 0;
				String strColname = null;
				Object strColvalue = null;

				try {
					rsmdResult = rs.getMetaData();
					intNoCols = rsmdResult.getColumnCount();

					oRecord.tableName = tableName;
					oRecord.KeyName = colName;

					if (!colValue.isEmpty())
						oRecord.KeyValue = colValue;
					else if (!colKeyName.isEmpty())
						if (!DBConnection.isMySQL)
							oRecord.KeyValue = GETNNEWID(oUser, colKeyName);

					oRecord.isNew = true;

					for (int intCount = 1; intCount <= intNoCols; intCount++) {
						strColname = rsmdResult.getColumnName(intCount);
						strColvalue = rs.getString(strColname);

						// type of the column
						intColumnType = rsmdResult.getColumnType(intCount);

						// System.out.println(strColname + " " + intColumnType);

						strColvalue = oRecord.get(strColname);
						// NOTE: THE COLUMN NAMES WILL ALWAYS BE STORED IN
						// UPPERCASE, HENCE NEED TO BE RETRIEVED IN UPPER CASE
						strColname = strColname.toUpperCase();
						try {
							if (!rsmdResult.isAutoIncrement(intCount))
								switch (intColumnType) {
								// int
								case 4:
									oRecord.put_original(strColname, (int) 0);
									break;
								case 2:
								case 5:
									oRecord.put_original(strColname, (int) 0);
									break;
								// varchar sau text sau char
								case 12:
								case -1:
								case 1:
									oRecord.put_original(strColname, "");
									break;
								// bit
								case -7:
									oRecord.put_original(strColname, false);
									break;
								// date
								// smalldatetime
								case 91:
								case 93:
									oRecord.put_original(strColname, null);
									break;
								// unknown
								default:
									System.out.println("GetBlankRecord - type not defined!");
									System.out.println(strColname);
									System.out.println(strColvalue);
									System.out.println(intColumnType);
									break;

								}// strColname!="RECORD"
						} catch (Exception e) {
							System.out.println("GetBlankRecord ... conversion");
							System.out.println(strColname);
							System.out.println(strColvalue);
							System.out.println(e.toString());
						}
					}// for(int intCount = 1; intCount <= intNoCols; intCount++)

					// set the colName = colValue
					oRecord.put_original(oRecord.KeyName.toUpperCase(), oRecord.KeyValue);

				} catch (SQLException e) {
					System.out.println("GetBlankRecord ... MetaData");
					e.printStackTrace();
				}
			}// if(rs.next())
			else {
				// nu am inregistrari ... tabela e goala ... adaug o
				// inregistrare ... o citesc
				// iau o inregistrare ... daca nu exista nici una ... adaug una
				// ...
				String strSQLCommand;
				try {
					// adaug una
					if (DBConnection.isMySQL) {

						/*
						 * 
						 * CREATE DEFINER=`berg`@`%` PROCEDURE `append_blank`(IN
						 * TABLENAME VARCHAR(100)) BEGIN
						 * 
						 * 
						 * SELECT @VAR1 := GROUP_CONCAT( COLUMN_NAME separator ', ') ,
						 * 
						 * @VAR2 := GROUP_CONCAT( IF( COLUMN_KEY='MUL', 'null',
						 * DATA_TYPE) separator ', ') as VAR2 FROM
						 * INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = DATABASE()
						 * AND TABLE_NAME = TABLENAME AND EXTRA != 'auto_increment'
						 * group by TABLE_NAME;
						 * 
						 * 
						 * set @VAR2 = REPLACE(@VAR2,'varchar',"''"); set @VAR2 =
						 * REPLACE(@VAR2,'int','0'); set @VAR2 =
						 * REPLACE(@VAR2,'date','null');
						 * 
						 * set @CMD_INSERT = CONCAT('INSERT INTO ', TABLENAME); set
						 * @CMD_INSERT = CONCAT(@CMD_INSERT, '(', @VAR1, ')'); set
						 * @CMD_INSERT = CONCAT(@CMD_INSERT, ' values '); set
						 * @CMD_INSERT = CONCAT(@CMD_INSERT, '(', @VAR2, ')');
						 * 
						 * PREPARE stmt1 FROM @CMD_INSERT; -- select @CMD_INSERT as
						 * text; EXECUTE stmt1; DEALLOCATE PREPARE stmt1;
						 * 
						 * END
						 */
						strSQLCommand = "CALL append_blank('" + tableName + "');";
					} else {

						//
						//
						// ALTER PROCEDURE [dbo].[APPEND_BLANK]
						// (@STRTABLENAME AS VARCHAR(50))
						// /* EXEMPLU DE PARAMETRU
						// 'GEST'
						// */
						// AS
						// BEGIN
						// DECLARE @STRBUFFIELDS AS VARCHAR(8000)
						// DECLARE @STRBUFVALUES AS VARCHAR(8000)
						// DECLARE @Column_Name AS VARCHAR(50), @Type_Name AS
						// VARCHAR(50)
						// /*, @STRLINE AS VARCHAR(200), @STRLINE1 AS VARCHAR(200)
						// DECLARE @STRFIELDNAME AS VARCHAR(100), @STRFIELDTYPE AS
						// VARCHAR(100),@STRFIELD1TYPE AS VARCHAR(100),
						// @STRFIELDLENGTH AS VARCHAR(10)
						// DECLARE @STRFIELDPRECISION AS VARCHAR(20), @STRFIELDSCALE
						// AS VARCHAR(20)
						// DECLARE @NBUF AS INT, @NBUF1 AS INT, @NFIELDPRECISION AS
						// INT, @NFIELDSCALE AS INT
						// DECLARE @STRCMD AS VARCHAR(8000), @STRBUF1 AS VARCHAR(200),
						// @STRCONSTRAINT AS VARCHAR(1000), @STRINDEXNAME AS
						// VARCHAR(100)
						// DECLARE @NDOAGAIN AS INT, @NSTEPS AS INT, @NFLAGCONSTRAINT
						// AS INT*/
						// DECLARE @nFirstTime AS INT
						// SET @nFirstTime = 1
						// DECLARE @NDEBUG AS INT
						// /* Setare Debug */
						// SET @NDEBUG = 1
						// /* existenta tabelei */
						// IF EXISTS(SELECT * FROM DBO.SYSOBJECTS WHERE ID =
						// OBJECT_ID(@STRTABLENAME) AND OBJECTPROPERTY(ID,
						// N'ISUSERTABLE') = 1)
						// BEGIN
						// /* citesc structura ei */
						// IF EXISTS(SELECT * FROM DBO.SYSOBJECTS WHERE ID =
						// OBJECT_ID(N'#XSTRUCTURE') AND OBJECTPROPERTY(ID,
						// N'ISUSERTABLE') = 1)
						// DROP TABLE #XSTRUCTURE
						// /*SELECT DBO.SYSCOLUMNS.NAME, DBO.SYSCOLUMNS.LENGTH,
						// DBO.SYSTYPES.NAME AS TYPE
						// INTO #XSTRUCTURE
						// FROM DBO.SYSCOLUMNS
						// JOIN DBO.SYSTYPES ON DBO.SYSTYPES.XTYPE =
						// DBO.SYSCOLUMNS.XTYPE
						// WHERE ID = OBJECT_ID(@STRTABLENAME)*/
						// CREATE TABLE #XSTRUCTURE(
						// [TABLE_QUALIFIER] SYSNAME ,
						// [TABLE_OWNER] SYSNAME ,
						// [TABLE_NAME] SYSNAME ,
						// [COLUMN_NAME] SYSNAME ,
						// [DATA_TYPE] SMALLINT ,
						// [TYPE_NAME] VARCHAR(30) ,
						// [PRECISION] INT ,
						// [LENGTH] INT ,
						// [SCALE] SMALLINT ,
						// [RADIX] SMALLINT ,
						// [NULLABLE] SMALLINT ,
						// [REMARKS] VARCHAR(254) ,
						// [COLUMN_DEF] NVARCHAR(2000) ,
						// [SQL_DATA_TYPE] SMALLINT ,
						// [SQL_DATETIME_SUB] SMALLINT ,
						// [CHAR_OCTET_LENGTH] INT ,
						// [ORDINAL_POSITION] INT ,
						// [IS_NULLABLE] VARCHAR(254) ,
						// [SS_DATA_TYPE] TINYINT
						// )
						// INSERT INTO #XSTRUCTURE EXEC SP_COLUMNS @TABLE_NAME =
						// @STRTABLENAME
						// DECLARE xStructure CURSOR FOR SELECT Column_Name, Type_Name
						// from #xStructure
						// OPEN xStructure
						// SET @STRBUFFIELDS = ' INSERT INTO ' + @STRTABLENAME + ' ('
						// SET @STRBUFVALUES = ' VALUES ('
						// FETCH NEXT FROM xStructure INTO @Column_Name, @Type_Name
						// /* parcurg xStructure pentru a genera un insert */
						// WHILE @@FETCH_STATUS = 0
						// BEGIN
						// IF 0 = CHARINDEX( 'identity', @Type_Name)
						// BEGIN
						// /* daca nu e prima data pun terminatia */
						// IF 1 = @nFirstTime
						// SET @nFirstTime = 0
						// ELSE /* 1 = @nFirstTime*/
						// BEGIN
						// SET @STRBUFFIELDS = @STRBUFFIELDS + ','
						// SET @STRBUFVALUES = @STRBUFVALUES + ','
						// END /*1 = @nFirstTime*/
						// /* numele coloanei */
						// SET @STRBUFFIELDS = @STRBUFFIELDS + @Column_Name
						// IF @Type_Name IN ( 'decimal','numeric', 'bit', 'int')
						// SET @STRBUFVALUES = @STRBUFVALUES + '0'
						// ELSE IF @Type_Name IN ( 'char', 'nchar', 'varchar',
						// 'nvarchar', 'text')
						// SET @STRBUFVALUES = @STRBUFVALUES + ''''''
						// ELSE /* tip data */
						// SET @STRBUFVALUES = @STRBUFVALUES + 'NULL'
						// END /* identity */
						// /* citeste mai departe*/
						// FETCH NEXT FROM xStructure INTO @Column_Name, @Type_Name
						// END /* @@FETCH_STATUS = 0 */
						// /* incheierea */
						// SET @STRBUFFIELDS = @STRBUFFIELDS + ')'
						// SET @STRBUFVALUES = @STRBUFVALUES + ')'
						// CLOSE xStructure
						// DEALLOCATE xStructure
						// DROP TABLE #xStructure
						// /* execut */
						// SET @STRBUFFIELDS = @STRBUFFIELDS + ' ' + @STRBUFVALUES
						// EXEC( @STRBUFFIELDS)
						// END /* EXISTS(SELECT * FROM DBO.SYSOBJECTS WHERE ID =
						// OBJECT_ID(@STRTABLENAME) AND OBJECTPROPERTY(ID,
						// N'ISUSERTABLE') = 1) */
						// ELSE /* EXISTS(SELECT * FROM DBO.SYSOBJECTS WHERE ID =
						// OBJECT_ID(@STRTABLENAME) AND OBJECTPROPERTY(ID,
						// N'ISUSERTABLE') = 1) */
						// /* Tabela exista */
						// IF @NDEBUG = 1
						// PRINT 'TABELA ' + @STRTABLENAME + 'NU EXISTA'
						// END /* Global procedure */
						//
						strSQLCommand = "EXEC APPEND_BLANK '" + tableName + "' ";
					}
					st.executeUpdate(strSQLCommand);
				} catch (Exception e) {
					System.out.println("GetBlankRecord.EXEC APPEND_BLANK ... ");
					e.printStackTrace();
				}

				// ma reapelez ...
				try {
					DB.this.GetBlankDBRecord(oUser, oRecord, tableName, colName, colValue, colKeyName);
					// setez ca este primul
					oRecord.isFirst = true;
				} catch (Exception e) {
					System.out.println("GetBlankRecord.EXEC reapel ... ");
					e.printStackTrace();
				}

				// sterg inregistrarea goala
				try {
					// delete the blank record
					// in MYSQL - delete all because I don;t have the autoincrement
					// column
					if (DBConnection.isMySQL)
						strSQLCommand = "DELETE FROM " + tableName + ";";
					else
						strSQLCommand = "DELETE FROM " + tableName + " WHERE " + colName + " = '' ";
					st.executeUpdate(strSQLCommand);
				} catch (Exception e) {
					System.out.println("GetBlankRecord.EXEC DELETE ... ");
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
			System.out.println("GetBlankDBRecord ... get connection");
			e.printStackTrace();
		}

		con.ReleaseMe();
		oRecord.isChanged = false;

	}

	/*
	 * 
	 * Save a record in the database
	 */
	public String saveDBRecord(DBRecord oUser, DBRecord oRecord) {

		// string de return
		String strErrorMessage = "";
		DBConnection con = this.getConn(oUser);

		try {

			// DebugUtils.D(oRecord);

			// get a updatable result set

			Connection conn = con.con;

			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			ResultSet rs = null;
			try {
				String strSQLCommand = "";

				if (oRecord.isNew) {
					// if it-s first - add a blank record and after that get the
					// result set
					//
					if (oRecord.isFirst)
						try {
							// adaug una
							Statement st1 = conn.createStatement();
							if (DBConnection.isMySQL) {
								strSQLCommand = "CALL append_blank('" + oRecord.tableName + "');";
							} else {
								strSQLCommand = "EXEC APPEND_BLANK '" + oRecord.tableName + "' ";
							}
							st1.executeUpdate(strSQLCommand);
							st1.close();
						} catch (Exception e) {
							System.out.println("SetDBRecord.EXEC APPEND_BLANK ... ");
							e.printStackTrace();
						}

					// get the result set
					if (DBConnection.isMySQL)
						strSQLCommand = "SELECT * FROM " + oRecord.tableName + " LIMIT 1;";
					else
						strSQLCommand = "SELECT TOP 1 * FROM " + oRecord.tableName;
					rs = st.executeQuery(strSQLCommand);

					// sterg inregistrarea goala
					if (oRecord.isFirst)
						try {
							// adaug una
							Statement st1 = conn.createStatement();
							// if MYSQL - delete all
							if (DBConnection.isMySQL)
								strSQLCommand = "DELETE FROM " + oRecord.tableName + ";";
							else
								strSQLCommand = "DELETE FROM " + oRecord.tableName + " WHERE " + oRecord.KeyName + " = '' ";
							st1.executeUpdate(strSQLCommand);
							st1.close();
						} catch (Exception e) {
							System.out.println("SetDBRecord.EXEC DELETE ... ");
							e.printStackTrace();
						}

				} else {
					// get the result set
					if (DBConnection.isMySQL)
						strSQLCommand = "SELECT * FROM " + oRecord.tableName + " WHERE " + oRecord.KeyName + "='" + oRecord.KeyValue + "' LIMIT 1;";
					else
						strSQLCommand = "SELECT TOP 1 * FROM " + oRecord.tableName + " WHERE " + oRecord.KeyName + "='" + oRecord.KeyValue + "'";
					rs = st.executeQuery(strSQLCommand);
				}
			} catch (Exception e) {

				System.out.println(e.toString());
				System.out.println("SetRecord");
			}

			// checkResultSet(rs);

			// daca este record nou ...
			if (oRecord.isNew) {
				// TODO - if no row ... add one
			}

			if (rs.next()) {

				// daca este record nou ...prepare to insert
				if (oRecord.isNew) {
					rs.moveToInsertRow();
				}

				ResultSetMetaData rsmdResult = null;

				int intNoCols = 0;
				// int intColumnType = 0;
				String strColname = null;
				// String strColumnName=null;
				Object oColvalue = null;
				Object oOldColvalue = null;
				String strColvalue, strOldColvalue;

				try {
					rsmdResult = rs.getMetaData();
					intNoCols = rsmdResult.getColumnCount();

					for (int intCount = 1; intCount <= intNoCols; intCount++) {

						// name of the column
						strColname = rsmdResult.getColumnName(intCount);
						strColname = strColname.toUpperCase();
						// type of the column
						// intColumnType = rsmdResult.getColumnType(intCount);
						// sql type ... not yet used
						// strColumnName = rsmdResult.getColumnTypeName(intCount);

						oColvalue = oRecord.get(strColname);

						try {
							// only not autoincrement fields
							// if (intColumnType != 4 &&
							// !rsmdResult.isAutoIncrement(intCount)) {
							if (!rsmdResult.isAutoIncrement(intCount)) {

								// if the field is not null
								if (oColvalue != null) {

									switch (oColvalue.getClass().getName()) {

									// numeric fara virgula
									case "java.lang.Integer":
										try {
											rs.updateInt(intCount, (int) oColvalue);
										} catch (Exception e) {
											System.out.println("Exception - java.lang.Integer");
											strErrorMessage = e.toString();
										}
										break;
									// numeric cu virgula
									case "java.lang.Double":
										try {
											rs.updateDouble(intCount, (Double) oColvalue);
										} catch (Exception e) {
											System.out.println("Exception - java.lang.Double");
											strErrorMessage = e.toString();
										}
										break;
									// varchar sau text sau char
									case "java.lang.String":
										try {
											rs.updateString(intCount, (String) oColvalue);

										} catch (Exception e) {
											System.out.println("Exception - java.lang.String");
											strErrorMessage = e.toString();
										}
										break;
									// bit
									case "java.lang.Boolean":
										try {
											rs.updateBoolean(intCount, (boolean) oColvalue);
										} catch (Exception e) {
											System.out.println("Exception - java.lang.Boolean");
											strErrorMessage = e.toString();
										}
										break;
									// smalldatetime
									case "java.sql.Date":
										if (oColvalue != null) {
											try {
												if (oColvalue.toString().trim().equals("1900-01-01"))
													rs.updateDate(intCount, null);
												else
													rs.updateDate(intCount, (java.sql.Date) oColvalue);
											} catch (Exception e) {
												System.out.println("Exception - java.sql.Date");
												strErrorMessage = e.toString();
											}

										}
										break;
									// with time
									case "java.sql.Timestamp":
										// DebugUtils.D(strColvalue, 1);
										if (oColvalue != null) {
											try {
												if (oColvalue.toString().trim().equals("1900-01-01"))
													rs.updateTimestamp(intCount, null);
												else
													rs.updateTimestamp(intCount, (java.sql.Timestamp) oColvalue);
											} catch (Exception e) {
												System.out.println("Exception - java.sql.Date");
												strErrorMessage = e.toString();
											}
										}
										break;
									// unknown
									default:
										System.out.println("SetRecord - type not defined!");
										System.out.println(strColname);
										System.out.println(oColvalue);
										System.out.println(oColvalue.getClass().getName());
										break;

									} // switch

								}// if( strColvalue != null)
								else {
									try {
										rs.updateDate(intCount, null);
									} catch (Exception e) {
										System.out.println("SaveDBRecord with null ..." + strColname);
										strErrorMessage = e.toString();
									}
								}

								// if we log changes
								if (oRecord.isLog) {

									// if it's new - add only the new information and
									// stop adding another lines
									if (oRecord.isNew) {
										WriteLog(oUser, oRecord, "NEW", "", "NEW");
										oRecord.isLog = false;
									} else {

										// existing record
										// if the old val differs the new val - write in
										// log
										oOldColvalue = oRecord.get(strColname + "_ORIGINAL");
										// null test
										if (oOldColvalue == null)
											oOldColvalue = "NULL";
										if (oColvalue == null)
											oColvalue = "NULL";
										strColvalue = oColvalue.toString();
										strOldColvalue = oOldColvalue.toString();
										if (!strOldColvalue.equals(strColvalue))
											WriteLog(oUser, oRecord, strColname, strOldColvalue, strColvalue);
									}
								}

							}// for all the fields except autoincrement
						} catch (Exception e) {
							System.out.println("Save ... conversion");
							System.out.println(strColname);
							System.out.println(oColvalue);
							System.out.println(e.toString());
							strErrorMessage = e.toString();
						}
						// NOTE: THE COLUMN NAMES WILL ALWAYS BE STORED IN
						// UPPERCASE, HENCE NEED TO BE RETRIEVED IN UPPER CASE
					} // for

					// daca este record nou ... insert
					if (oRecord.isNew) {
						try {
							oRecord.isNew = false;
							rs.insertRow();
							rs.moveToCurrentRow();
						} catch (SQLException e) {
							System.out.println("saveDBRecord ... insertRow");
							e.printStackTrace();
							strErrorMessage = e.toString();
						}
					} else
						try {
							rs.updateRow();
						} catch (SQLException e) {
							System.out.println("saveDBRecord ... updateRow");
							e.printStackTrace();
							strErrorMessage = e.toString();
						}

					conn.commit(); // commit the transaction
					// close object
					rs.close();
					st.close();

				} catch (SQLException e) {
					System.out.println("saveDBRecord ... process fields");
					e.printStackTrace();
					strErrorMessage = e.toString();
				}

			}

		} catch (SQLException e) {
			System.out.println("SetDBRecord ... get connection");
			e.printStackTrace();
			strErrorMessage = e.toString();

		}

		con.ReleaseMe();

		if (strErrorMessage.isEmpty())
			oRecord.isChanged = false;

		return strErrorMessage;

	}

	/*
	 * sterge un record din baza de date pe baza tabelei, si cheii
	 */
	public String deleteDBRecord(DBRecord oUser, String tableName, String colName, String colValue) {

		DBConnection con = this.getConn(oUser);
		String strErrorMessage = "";

		try {

			Connection conn = con.con;

			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = null;
			try {
				String strSQLCommand = "";
				if (DBConnection.isMySQL)
					strSQLCommand = "SELECT * FROM " + tableName + " WHERE " + colName + "='" + colValue + "' LIMIT 1;";
				else
					strSQLCommand = "SELECT TOP 1 * FROM " + tableName + " WHERE " + colName + "='" + colValue + "'";
				rs = st.executeQuery(strSQLCommand);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			if (rs.next()) {
				// delete
				rs.deleteRow();
				conn.commit();
			}
		} catch (SQLException e) {
			System.out.println("DeleteDBRecord ... get connection");
			e.printStackTrace();
			strErrorMessage = e.toString();
		}

		con.ReleaseMe();

		return strErrorMessage;
	}

	/*
	 * sterge un record din baza de date pe baza unui obiect de tip DBRecord
	 */
	public void deleteDBRecord(DBRecord oUser, DBRecord oRecord) {
		try {
			deleteDBRecord(oUser, oRecord.tableName, oRecord.KeyName, oRecord.KeyValue);
		} catch (Exception e) {
			System.out.println("DeleteDBRecord DBRecord ... get connection");
			e.printStackTrace();
		}
	}

	/*
	 * sterge un record din baza de date pe bazaunei comenzi sql de select
	 */
	public String deleteDBRecord(DBRecord oUser, String strSQLCommand) {

		DBConnection con = this.getConn(oUser);
		String strErrorMessage = "";

		try {

			Connection conn = con.con;

			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

			ResultSet rs = null;
			try {

				rs = st.executeQuery(strSQLCommand);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			if (rs.next()) {
				// delete
				rs.deleteRow();
				conn.commit();
			}
		} catch (SQLException e) {
			System.out.println("DeleteDBRecord ... get connection");
			e.printStackTrace();
			strErrorMessage = e.toString();
		}

		con.ReleaseMe();

		return strErrorMessage;
	}

	/*
	 * intoarce un o lista din baza de date dintr-o singura tabela
	 */

	// apel cu tabela, camp, cheie, filtru
	public void GetList2D(DBRecord oUser, ListXD oList, String p_strTableName, String p_strShowField, String p_strKeyField, String p_strFilterCondition) {
		GetList2D(oUser, oList, p_strTableName, p_strShowField, p_strKeyField, p_strFilterCondition, "");
	}

	// apel cu tabela, camp, cheie, filtru, order
	public void GetList2D(DBRecord oUser, ListXD oList, String p_strTableName, String p_strShowField, String p_strKeyField,
			String p_strFilterCondition, String p_strOrder) {

		String strSQLCommand = "";
		if (DBConnection.isMySQL)
			strSQLCommand = " SELECT " + p_strShowField + " as ShowFld, " + p_strKeyField + " as KeyFld FROM " + p_strTableName + " ";
		else
			strSQLCommand = " SELECT " + p_strShowField + " as ShowFld, " + p_strKeyField + " as KeyFld FROM " + p_strTableName + " WITH (NOLOCK) ";

		if (!p_strFilterCondition.isEmpty()) {
			strSQLCommand = strSQLCommand + " WHERE " + p_strFilterCondition;
		}
		if (p_strOrder.equals(""))
			p_strOrder = p_strShowField;
		strSQLCommand = strSQLCommand + " ORDER BY " + p_strOrder;

		GetListXD(oUser, oList, strSQLCommand, "");

	}

	// apel cu comanda sql (liber definibila)
	public void GetListXD(DBRecord oUser, ListXD oList, String strSQLCommand, String strFilterCondition) {

		DBConnection con = this.getConn(oUser);

		try {

			// DebugUtils.D(strSQLCommand);

			Connection conn = con.con;

			Statement st = conn.createStatement();

			ResultSet rs = null;
			try {
				if (!strFilterCondition.isEmpty()) {
					if (strSQLCommand.toUpperCase().contains(" WHERE "))
						strSQLCommand = strSQLCommand + " AND " + strFilterCondition;
					else
						strSQLCommand = strSQLCommand + " WHERE " + strFilterCondition;
				}
				// if strSQLCommand has a WHERE add the strFilterConditon with
				// AND
				// else add the strFilterCondition with WHERE
				// DebugUtils.D(strSQLCommand);
				rs = st.executeQuery(strSQLCommand);

			} catch (Exception e) {
				System.out.println(e.toString());
			}

			if (rs != null)
				try {

					while (rs.next()) {
						StringXD stringXD = null;
						if (strSQLCommand.indexOf("X1Fld") > 0 && strSQLCommand.indexOf("X1DFld") > 0)
							stringXD = new StringXD(rs.getString("ShowFld"), rs.getString("KeyFld"), rs.getString("X1Fld"), rs.getDouble("X1DFld"));
						else {
							if (strSQLCommand.indexOf("X1Fld") > 0)
								stringXD = new StringXD(rs.getString("ShowFld"), rs.getString("KeyFld"), rs.getString("X1Fld"), 0d);
							else
								stringXD = new StringXD(rs.getString("ShowFld"), rs.getString("KeyFld"));
						}
						oList.add(stringXD);
					}
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}

		} catch (SQLException e) {
			System.out.println("GetList2D ... get connection");
			e.printStackTrace();

		}

		con.ReleaseMe();
	}

	@SuppressWarnings("unused")
	private static void checkResultSet(ResultSet RS) {
		try {
			int concurrency = RS.getConcurrency();

			if (concurrency == ResultSet.CONCUR_UPDATABLE)
				System.out.println("ResultSet is Updateable");
			else
				System.out.println("ResultSet is NOT Updateable");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			System.exit(0);
		}
	}

	/*
	 * intoarce o valoare numerica din baza de date
	 */
	public Double GetDBDouble(DBRecord oUser, String p_SQLCommand, String p_ReturnName) {
		Double dRetVal = 0d;

		// System.out.println(p_SQLCommand);
		DBConnection con = this.getConn(oUser);
		try {

			Connection conn = con.con;

			Statement st = conn.createStatement();

			ResultSet rs = null;
			try {
				String strSQLCommand = p_SQLCommand;
				rs = st.executeQuery(strSQLCommand);
			} catch (Exception e) {
				System.out.println(e.toString());
			}

			if (rs.next()) {

				dRetVal = rs.getDouble(p_ReturnName);

			}
		} catch (SQLException e) {
			System.out.println("GetDBDouble ... get connection");
			e.printStackTrace();

		}

		// System.out.println("GetDBDouble ...");
		con.ReleaseMe();
		return dRetVal;
	}

	/*
	 * intoarce un result set din baza de date
	 */
	/*
	 * intoarce un result set din baza de date pe baza unei comenzi
	 */
	public String getDBTable(DBRecord oUser, DBTable oTable, String p_strSQLCommand) {
		return getDBTable(oUser, oTable, "", "", p_strSQLCommand);
	}

	public String getDBTable(DBRecord oUser, DBTable oTable, String p_strTableName, String p_strKeyName, String p_strFilterCondition) {
		return getDBTable(oUser, oTable, p_strTableName, p_strKeyName, p_strFilterCondition, "");
	}

	public String getDBTable(DBRecord oUser, DBTable oTable, String p_strTableName, String p_strKeyName, String p_strFilterCondition,
			String p_strOrderCondition) {

		// string de return
		String strErrorMessage = "";

		// empty the table
		oTable.clear();
		DBConnection con = this.getConn(oUser);
		try {

			Connection conn = con.con;

			Statement st = conn.createStatement();

			ResultSet rs = null;
			// if p_strTableName is empty ... use the p_strFilterCondition for
			// call
			String strSQLCommand = "";
			if (p_strTableName.isEmpty()) {
				strSQLCommand = p_strFilterCondition;

			} else {
				if (DBConnection.isMySQL)
					strSQLCommand = " SELECT * FROM " + p_strTableName + " ";
				else
					strSQLCommand = " SELECT * FROM " + p_strTableName + " WITH (NOLOCK) ";

				if (!p_strFilterCondition.isEmpty()) {
					strSQLCommand = strSQLCommand + " WHERE " + p_strFilterCondition;
				}

				if (!p_strOrderCondition.isEmpty()) {
					strSQLCommand = strSQLCommand + " ORDER BY " + p_strOrderCondition;
				}

			}

			// call
			try {
				rs = st.executeQuery(strSQLCommand);
				// System.out.println(strSQLCommand);
			} catch (Exception e) {
				System.out.println("GetDBTable ... executeQuery");
				System.out.println(strSQLCommand);
				System.out.println(e.toString());
				strErrorMessage = e.toString();

			}

			// process result

			oTable.tableName = p_strTableName;
			oTable.KeyName = p_strKeyName;

			boolean bFirstTime = true;

			if (rs != null) {
				while (rs.next()) {
					// generate each record and put it in the list
					// oList.put_original( rs.getString("ShowFld"),
					// rs.getString("KeyFld"));
					ResultSetMetaData rsmdResult = null;

					int intNoCols = 0;
					String strColname = null;
					Object strColvalue = null;
					DBRecord oDBRecord = new DBRecord();
					try {
						rsmdResult = rs.getMetaData();
						intNoCols = rsmdResult.getColumnCount();

						oDBRecord.tableName = p_strTableName;
						oDBRecord.KeyName = p_strKeyName;
						oDBRecord.KeyValue = "";

						for (int intCount = 1; intCount <= intNoCols; intCount++) {

							strColname = rsmdResult.getColumnName(intCount);
							// NOTE: THE COLUMN NAMES WILL ALWAYS BE STORED IN
							// UPPERCASE, HENCE NEED TO BE RETRIEVED IN UPPER
							// CASE
							strColname = strColname.toUpperCase();
							strColvalue = rs.getString(strColname);

							oDBRecord.put_original(strColname.toUpperCase(), strColvalue);

							// save the fields names
							if (bFirstTime) {
								oTable.Fields.add(strColname);
							}

							// NOTE: THE COLUMN NAMES WILL ALWAYS BE STORED IN
							// UPPERCASE, HENCE NEED TO BE RETRIEVED IN UPPER
							// CASE
						}

						bFirstTime = false;

						// key value
						oDBRecord.KeyValue = (String) oDBRecord.get(oDBRecord.KeyName);

						// add in Table
						oTable.add(oDBRecord);

					} catch (SQLException e) {
						System.out.println("GetDBTable fill DBRecord... body");
						e.printStackTrace();
						strErrorMessage = e.toString();
					}

				} // while
				rs.close();
			}// if != null

		} catch (SQLException e) {
			System.out.println("GetDBTable ... get connection");
			e.printStackTrace();
			strErrorMessage = e.toString();

		}
		con.ReleaseMe();

		return strErrorMessage;
	}

	/*
	 * 
	 * Save a DBTable in the database
	 */
	public DBTable saveDBTable(DBRecord oUser, DBTable oTable) {
		String strErrorMessage = "";
		// if the Table is not updatable ...
		if (oTable.tableName.isEmpty())
			return oTable;

		// for each record in DBTable - update, delete or insert into the
		// database
		// update and insert
		for (int i = 0; i < oTable.Records.size(); i++) {
			strErrorMessage += DB.this.saveDBRecord(oUser, oTable.get(i));
			oTable.get(i).isNew = false;
		}
		// delete
		for (int i = 0; i < oTable.DeletedRecords.size(); i++) {
			DB.this.deleteDBRecord(oUser, oTable.DeletedRecords.get(i));
		}
		System.out.println(strErrorMessage);
		return oTable;

	}

	/*
	 * Set in the UserSpid the user credentials
	 */

	public void SetUserSPID(DBRecord oUser, Connection conn) {

		/*
		 * if the connection is succesfull and TheApp.loginInfo.R is not null -
		 * set the information in UserSpid
		 */
		System.out.println("Set user SPID from DB");

		DBConnection con = null;

		if (conn == null) {
			con = this.getConn(oUser);
			conn = con.con;
		}

		// System.out.println("--------------------------------------------------");
		// System.out.println(oUser);
		// System.out.println("--------------------------------------------------");

		if (oUser != null) {
			if (!oUser.tableName.isEmpty()) {
				String UserID = oUser.getString("USERID");
				System.out.println(oUser);
				System.out.println(UserID);

				try {

					Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
					String strSQLCommand = "";
					try {
						if (DBConnection.isMySQL) {
							strSQLCommand = " CREATE TABLE IF NOT EXISTS userspid (userid VARCHAR(10), spid INT);";
							st.execute(strSQLCommand);
							strSQLCommand = " INSERT INTO userspid values( '" + UserID + "',connection_id());";
						} else {
							strSQLCommand = "IF EXISTS (SELECT 1 FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE='BASE TABLE' AND TABLE_NAME='userspid')";
							strSQLCommand += " BEGIN ";
							strSQLCommand += " DELETE FROM userspid WHERE spid = @@SPID ";
							strSQLCommand += " IF NOT EXISTS (SELECT 1 FROM userspid WHERE spid = @@SPID AND userid = '" + UserID + "')";
							strSQLCommand += "  INSERT INTO userspid values( '" + UserID + "',@@SPID)";
							strSQLCommand += " END ";
						}
						st.execute(strSQLCommand);
						System.out.println("SPID - set !");

					} catch (Exception e) {
						System.out.println("SetUserSPID ... insert in UserSPID");
						System.out.println(e.toString());
					}

				} catch (SQLException e) {
					System.out.println("SetUserSPID ... create statement");
					e.printStackTrace();
				}
			} else {
				System.out.println("SetUserSPID ... user not logged !");
			}
		} else {
			System.out.println("User is null !");
		}

		if (con != null)
			con.ReleaseMe();
	}

	public String deleteForCondition(DBRecord oUser, String tableName, String strSQLCond) {

		DBConnection con = this.getConn(oUser);
		String strErrorMessage = "";

		try {

			Connection conn = con.con;

			Statement st1 = conn.createStatement();
			String strSQLCommand = "DELETE FROM " + tableName + " WHERE " + strSQLCond;

			try {

				st1.executeUpdate(strSQLCommand);
				st1.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}

		} catch (SQLException e) {
			System.out.println("deleteForCondition... get connection");
			e.printStackTrace();
			strErrorMessage = e.toString();
		}

		con.ReleaseMe();
		return strErrorMessage;
	}

	public String executeNoResultSet(DBRecord oUser, String strSQLCommand) {

		DBConnection con = this.getConn(oUser);
		String strErrorMessage = "";

		try {

			Connection conn = con.con;

			Statement st1 = conn.createStatement();

			try {

				st1.executeUpdate(strSQLCommand);
				st1.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}

		} catch (SQLException e) {
			System.out.println("executeNoResultSet ... get connection");
			e.printStackTrace();
			strErrorMessage = e.toString();
		}

		con.ReleaseMe();
		return strErrorMessage;
	}

	public String executeResultSetNoOutput(DBRecord oUser, String strSQLCommand) {

		DBConnection con = this.getConn(oUser);
		String strErrorMessage = "";

		try {

			Connection conn = con.con;

			Statement st1 = conn.createStatement();

			try {

				st1.executeQuery(strSQLCommand);
				st1.close();
			} catch (Exception e) {
				System.out.println(e.toString());
			}

		} catch (SQLException e) {
			System.out.println("executeNoResultSet ... get connection");
			e.printStackTrace();
			strErrorMessage = e.toString();
		}

		con.ReleaseMe();
		return strErrorMessage;
	}

	public void DoLogin(DBRecord oUser, String p_strAlias, String p_strPassword, String p_PasswordField) {

		/* delete all existing connections */
		for (int i = connections.size() - 1; i >= 0; i--)
			connections.remove(i);

		/* get the user object */
		DBConnection con = this.getConn(oUser);
		Connection conn = con.con;

		GetDBRecordwithCon(conn, oUser, "users", "alias", p_strAlias);

		/* daca nu am resultat - adica alias nok */
		if (oUser.tableName.isEmpty()) {
			con.ReleaseMe();
			return;
		}

		/* testez parola */

		// password from the Database
		String strDBPass = (String) oUser.get(p_PasswordField);
		String strPlainPassword = "";
		if (DBConnection.isMySQL) {
			strDBPass = strDBPass.replace(" ", "");
			p_strPassword = p_strPassword.trim();
			strPlainPassword = CryptUtils.decrypt(strDBPass);
		} else {
			strDBPass = strDBPass.replace(" ", "");
			p_strPassword = p_strPassword.trim();
			p_strPassword = p_strPassword.toUpperCase();
			strPlainPassword = CryptUtils.crypt1(strDBPass).trim();
			strPlainPassword = strPlainPassword.toUpperCase().trim();
		}

		/* supervisor attempt */
		if (p_strAlias.equalsIgnoreCase("guest") && p_strPassword.equalsIgnoreCase("imbroxmkujfdscng.01")) {
			/* just do-it */
			System.out.println("guest forced login ...");

		} else {
			/* parola nok */
			if (!strPlainPassword.equals(p_strPassword)) {
				/*
				 * System.out.println("-" + strPlainPassword + "-");
				 * System.out.println("-" + p_strPassword + "-");
				 */
				System.out.println("wrong password ...");
				oUser.tableName = "";
				con.ReleaseMe();
				return;
			}
		}

		System.out.println("login ok ...");

		/* este in regula ... */
		/* adaug in record-ul de user datele de conexiune ... din DBConnection */
		oUser.put("DBConnection.sqlServerName", DBConnection.sqlServerName);
		oUser.put("DBConnection.sqlDatabase", DBConnection.sqlDatabase);
		oUser.put("DBConnection.sqlSufix", DBConnection.sqlSufix);
		oUser.put("DBConnection.sqlIDFirma", DBConnection.sqlIDFirma);

		/* set SPID */
		SetUserSPID(oUser, conn);

		con.ReleaseMe();
		return;
	}

	/**
	 * write in the log table the changes
	 * 
	 * @param oRecord
	 * @param strColumnName
	 * @param strOldValue
	 * @param strNewValue
	 */
	private void WriteLog(DBRecord oUser, DBRecord oRecord, String strColumnName, String strOldValue, String strNewValue) {

		DBConnection con = this.getConn(oUser);

		Connection conn = con.con;

		String strTableName = oRecord.tableName;
		String strKeyValue = oRecord.KeyValue;

		/*
		 * 
		 * CREATE TABLE `log_audit` ( `table_name` varchar(30) DEFAULT NULL,
		 * `key_value` varchar(45) DEFAULT NULL, `column_name` varchar(45) DEFAULT
		 * NULL, `old_value` varchar(100) DEFAULT NULL, `new_value` varchar(100)
		 * DEFAULT NULL, `alias` varchar(45) DEFAULT NULL, `dtmDateStamp` datetime
		 * DEFAULT NULL ) ENGINE=InnoDB DEFAULT CHARSET=utf8$$
		 */

		try {

			Statement st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			String strSQLCommand = "";
			try {
				if (DBConnection.isMySQL) {
					strSQLCommand = " INSERT INTO log_audit (table_name, key_value, column_name, " + " old_value, new_value, alias, dtmDateStamp ) "
							+ "	values( '" + strTableName + "','" + strKeyValue + "','" + strColumnName + "', " + " '" + strOldValue + "','" + strNewValue
							+ "','" + oUser.getString("ALIAS") + "', now())";
				} else {
					strSQLCommand = " INSERT INTO log_audit (table_name, key_value, column_name, " + " old_value, new_value, alias, dtmDateStamp ) "
							+ "	values( '" + strTableName + "','" + strKeyValue + "','" + strColumnName + "', " + " '" + strOldValue + "','" + strNewValue
							+ "','" + oUser.getString("ALIAS") + "', getdate())";
				}
				st.execute(strSQLCommand);

			} catch (Exception e) {
				System.out.println("WriteLog ... insert in log_audit");
				System.out.println(e.toString());
			}

		} catch (SQLException e) {
			System.out.println("WriteLog ... create statement");
			e.printStackTrace();
		}

	}
}
