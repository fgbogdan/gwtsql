package gwtSql.shared;


import java.io.FileInputStream;
import java.util.Properties;

import gwtSql.shared.DB;

public class DbManager {
	
	//static DB db = new DB();
	static DB db = null;
	
	public static String iniFileName="nedefinit";

	public static DB getDB () {
		
		if (db == null) {
			db = new DB();
		}
		return db;
	}
	
	//variabile implicite
	static String COLABID = null;
	

	public static String getCOLABID(){
		if(COLABID == null){
			 try {
				 	
		            Properties pro = new Properties();
		            pro.load(new FileInputStream(DbManager.iniFileName));
		            COLABID = pro.getProperty("COLABID");
		            
		        }
		        catch(Exception ex) {
		            System.out.println(ex.getMessage());
		            System.out.println("---");
		            System.out.println("the ini file must be in:" + System.getProperty("user.dir"));
		        }
		}
		return COLABID;
	}
	
	
	
}
