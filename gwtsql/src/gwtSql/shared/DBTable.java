package gwtSql.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DBTable implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2264335600307875360L;

	public List<DBRecord> Records;
	public List<String> Fields;
	public List<DBRecord> DeletedRecords;
	// public ArrayList<DBRecord> Records;
	public String tableName = "";
	public String KeyName = "";
	public int nLastLocatePos = -1;

	// blank constructor
	public DBTable() {
		DBTable.this.Records = new ArrayList<DBRecord>();
		DBTable.this.DeletedRecords = new ArrayList<DBRecord>();
		DBTable.this.Fields = new ArrayList<String>();
	}

	// adaugare inregistrare
	public void add(DBRecord oDBRecord) {
		// add one record in the table
		DBTable.this.Records.add(oDBRecord);
	}

	// adaugare inregistrare la o anumita pozitie
	public void add(int index, DBRecord oDBRecord) {
		// add one record in the table
		DBTable.this.Records.add(index, oDBRecord);
	}

	// return inregitrare
	public DBRecord get(int i) {
		// get one record
		return DBTable.this.Records.get(i);
	}

	// inlocuire inregistrare
	public void set(DBRecord R, int i) {
		// set a value for one record
		DBTable.this.Records.set(i, R);
	}

	// steregere pozitie
	public void delete(int i) {
		// delete one record
		// move him in the DeletedRecords if it-s not new
		DBRecord R = Records.get(i);
		if (!R.isNew)
			DBTable.this.DeletedRecords.add(R);
		DBTable.this.Records.remove(i);
	}

	// stergere
	public void clear() {
		DBTable.this.Records.clear();
	}

	// numar de inregistrari
	public int reccount() {
		return DBTable.this.Records.size();
	}

	// numar de inregistrari care corespund unei conditii
	public int count(String strFieldName, String strValue) {
		int nCount = 0;
		for (int i = 0; i < this.reccount(); i++)
			if (strValue.trim().equals(this.get(i).get(strFieldName.toUpperCase()).toString().trim())) {
				nCount++;
			}

		return nCount;
	}

	// cautare dupa un camp
	public DBRecord Locate(String strFieldName, String strValue) {
		for (int i = 0; i < this.reccount(); i++)
			if (strValue.trim().equals(this.get(i).get(strFieldName.toUpperCase()).toString().trim())) {
				nLastLocatePos = i;
				return this.get(i);
			}
		nLastLocatePos = -1;
		return null;
	}

	public DBRecord Continue(String strFieldName, String strValue) {
		for (int i = this.nLastLocatePos == -1 ? 0 : this.nLastLocatePos; i < this.reccount(); i++)
			if (strValue.trim().equals(this.get(i).get(strFieldName.toUpperCase()).toString().trim())) {
				nLastLocatePos = i;
				return this.get(i);
			}
		nLastLocatePos = -1;
		return null;
	}

	// cautare dupa doua campuri
	public DBRecord Locate(String strFieldName, String strValue, String strFieldName1, String strValue1) {
		for (int i = 0; i < this.reccount(); i++)
			if (strValue.trim().equals(this.get(i).get(strFieldName).toString().trim())
					&& strValue1.trim().equals(this.get(i).get(strFieldName1.toUpperCase()).toString().trim())) {
				nLastLocatePos = i;
				return this.get(i);
			}
		return null;
	}

	public DBRecord Continue(String strFieldName, String strValue, String strFieldName1, String strValue1) {
		for (int i = this.nLastLocatePos == -1 ? 0 : this.nLastLocatePos; i < this.reccount(); i++)
			if (strValue.trim().equals(this.get(i).get(strFieldName.toUpperCase()).toString().trim())
					&& strValue1.trim().equals(this.get(i).get(strFieldName1.toUpperCase()).toString().trim())) {
				nLastLocatePos = i;
				return this.get(i);
			}
		nLastLocatePos = -1;
		return null;
	}

	// cautare dupa doua campuri de la pozitia
	public DBRecord LocateFromPos(String strFieldName, String strValue, String strFieldName1, String strValue1) {
		for (int i = nLastLocatePos; i < this.reccount(); i++)
			if (strValue.trim().equals(this.get(i).get(strFieldName.toUpperCase()).toString().trim())
					&& strValue1.trim().equals(this.get(i).get(strFieldName1.toUpperCase()).toString().trim())) {
				nLastLocatePos = i + 1;
				return this.get(i);
			}
		nLastLocatePos = this.reccount();
		return null;
	}

	// cautare dupa un camp
	public DBRecord LocateFromPos(String strFieldName, String strValue) {
		for (int i = nLastLocatePos; i < this.reccount(); i++)
			if (strValue.trim().equals(this.get(i).get(strFieldName.toUpperCase()).toString().trim())) {
				nLastLocatePos = i + 1;
				return this.get(i);
			}
		nLastLocatePos = this.reccount();
		return null;
	}

	// selectie recorduri care corespund unei cerinte
	public DBTable Select(String strFieldName, String strValue) {

		DBTable Destination = new DBTable();
		this.nLastLocatePos = 0;
		for (int i = 0; i < this.reccount(); i++) {
			DBRecord R = null;
			R = this.LocateFromPos(strFieldName, strValue);
			if (R == null) {
				// nothing
			} else
				Destination.add(R);
		}
		return Destination;
	}

	// selectie recorduri care corespund unei cerinte
	public DBTable Select(String strFieldName, String strValue, String strFieldName1, String strValue1) {

		DBTable Destination = new DBTable();
		this.nLastLocatePos = 0;
		for (int i = 0; i < this.reccount(); i++) {
			DBRecord R = null;
			R = this.LocateFromPos(strFieldName, strValue, strFieldName1, strValue1);
			if (R == null) {
				// nothing
			} else
				Destination.add(R);
		}
		return Destination;
	}

	// sortare dupa un camp - de un anumit tip
	public void Sort(String strFieldName, String type, int sens) {

		// bubble sort
		for (int i = 0; i < this.reccount() - 1; i++)
			for (int j = i + 1; j < this.reccount(); j++) {
				if ("String".equals(type)) {
					if (this.get(i).getString(strFieldName.toUpperCase()).compareToIgnoreCase(this.get(j).getString(strFieldName.toUpperCase())) * sens > 0) {
						// switch between records
						DBRecord R;
						R = this.get(i);
						this.set(this.get(j), i);
						this.set(R, j);
					}
				} // String
				if ("Integer".equals(type)) {
					if ((this.get(i).getInteger(strFieldName.toUpperCase()) - this.get(j).getInteger(strFieldName.toUpperCase())) * sens > 0) {
						// switch between records
						DBRecord R;
						R = this.get(i);
						this.set(this.get(j), i);
						this.set(R, j);
					}
				} // Integer

			} // for for

	}

	/**
	 * Suma pe coloana camp
	 */
	public double sum(String strFieldName) {
		double nSum = 0d;
		for (int i = 0; i < this.reccount(); i++)
			nSum += this.get(i).getDouble(strFieldName.toUpperCase());

		return nSum;
	}

	/**
	 * Suma de produs pe doua coloane
	 */
	public double sum(String strFieldName1, String strFieldName2) {
		double nSum = 0d;
		for (int i = 0; i < this.reccount(); i++)
			nSum += (this.get(i).getDouble(strFieldName1.toUpperCase()) * this.get(i).getDouble(strFieldName2.toUpperCase()));

		return nSum;
	}

}
