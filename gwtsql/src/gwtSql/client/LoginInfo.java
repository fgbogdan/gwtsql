package gwtSql.client;

import java.io.Serializable;

import gwtSql.shared.DBRecord;

public class LoginInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5453887119684604237L;

	public boolean loggedIn = false;
	public DBRecord User = new DBRecord();
	
	public String sqlServerName = "", sqlDatabase = "", sqlSufix = "", sqlIDFirma = "";

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

//	public boolean isAllowed() {
//		if (!TheApp.loginInfo.User.getBoolean("DREPTADMIN")) {
//			Window.alert("Nu aveti suficiente drepturi pentru acest modul !");
//			return false;
//		} else
//			return true;
//	}
//
//	public boolean isAdmin() {
//		if (!TheApp.loginInfo.User.getBoolean("DREPTADMIN")) {
//			return false;
//		} else
//			return true;
//	}
}