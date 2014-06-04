package gwtSql.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class DBRecord extends HashMap<Object, Object> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6847908896735454646L;
	public String tableName = "";
	public String KeyName = "";
	public String KeyValue = "";
	public boolean isNew = false;
	public boolean isFirst = false;
	public boolean isDeleted = false;
	public boolean isChanged = false;

	// for serialisation compiler problems
	Double dbldummy;
	Boolean bdummy;
	Integer idummy;
	java.sql.Date ddummy;
	java.sql.Timestamp tdummy;

	// blank constructor
	public DBRecord() {

	}
	
	// overridde put
	public void put(String key, Object o){
		super.put(key, o);
		this.isChanged = true;
	}
	
	// put nochange
	public void put_nochange(String key, Object o){
		super.put(key, o);
	}

	@SuppressWarnings("rawtypes")
	public void CopyTo(DBRecord Destination) {

		// properties
		Destination.tableName = this.tableName;
		Destination.KeyName = this.KeyName;
		Destination.KeyValue = this.KeyValue;
		Destination.isNew = this.isNew;
		Destination.isDeleted = this.isDeleted;

		// Create a Set with the keys in the HashMap.
		Set set = this.keySet();
		// Iterate over the Set to see what it contains.
		Iterator iter = set.iterator();
		while (iter.hasNext()) {
			Object o = iter.next();
			String strKey = o.toString();
			Destination.put(strKey, this.get(strKey));
		}
	} // CopyTo

	// getString
	public String getString(String strKey) {
		return this.get(strKey).toString();
	}
	
	// getStringNotZero
	public String getStringNotZero(String strKey){
		if(this.getDouble(strKey)==0d)
			return "";
		else
			return this.getString(strKey);
	}

	// getDouble
	public Double getDouble(String strKey) {
		return Double.valueOf(this.get(strKey).toString());
	}

	// getInteger
	public int getInteger(String strKey) {
		return Integer.parseInt(this.get(strKey).toString());
	}

	// getBoolean
	public boolean getBoolean(String strKey) {
		String strColvalue = null;
		strColvalue = this.get(strKey).toString().trim();
		boolean lRetVal;
		if("0".equals(strColvalue) || "false".equals(strColvalue))
			lRetVal = false;
		else
			lRetVal = true;
		//return Boolean.valueOf(strColvalue);
		return lRetVal;

	}

	public Date getDate(String strKey) {
	
		Object o = this.get(strKey);
		if (o == null)
			return null;
		String strDate = o.toString();
		return DateUtils.String2Date(strDate, "yyyy-MM-dd");
	}

	public String getDateString(String strKey) {

		Object o = this.get(strKey);
		if (o == null)
			return null;
		String strDate = o.toString();
		return DateUtils.Date2String(DateUtils.String2Date(strDate, "yyyy-MM-dd"), "yyyy-MM-dd");
	}

	public String getDateString(String strKey, String strFormat) {
	
		Object o = this.get(strKey);
		if (o == null)
			return null;
		String strDate = o.toString();
		return DateUtils.Date2String(DateUtils.String2Date(strDate, "yyyy-MM-dd"), strFormat);
	}
}
