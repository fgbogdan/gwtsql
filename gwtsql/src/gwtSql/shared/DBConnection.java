package gwtSql.shared;

import java.sql.Connection;
import java.util.Date;

public class DBConnection {

	public Connection con = null;
	public String UserID = "";
	public Date bornDate = new Date();
	public boolean isinuse = false;
	public static String sqlServerName, sqlDatabase, sqlSufix, sqlIDFirma;
	public static boolean isMySQL;

	public boolean isDead() {
		if (5 < DateUtils.minDiff(new Date(), this.bornDate)) {
			return true;
		} else
			return false;
	}

	public void UseMe() {
		bornDate = new Date();
		isinuse = true;
	}

	public void ReleaseMe() {
		bornDate = null;
		isinuse = false;
	}

}
