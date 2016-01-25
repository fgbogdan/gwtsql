package gwtSql.client.controls;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.CheckBox;

import gwtSql.shared.DBRecord;
import gwtSql.shared.DebugUtils;

public class dbCheckBox extends CheckBox implements Controls {
	public DBRecord R;
	public String colName;

	/*
	 * create and attach - onChange
	 */
	public dbCheckBox(String strColName, String strCaption) {
		colName = strColName;
		this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (dbCheckBox.this.R != null)
					dbCheckBox.this.R.put(dbCheckBox.this.colName, dbCheckBox.this.getValue());
				else
					DebugUtils.W("The value cannot be modified !");
			}
		});
		this.setText(strCaption);
	}

	public void Refresh() {

		if (R != null) {
			Object o = R.get(this.colName);
			this.setValue(o.toString() == "true" || o.toString() == "1" ? true : false);
		} else
			DebugUtils.G("R is null");
	}

	public void SetR(DBRecord R1) {
		this.R = R1;
	}

	public String getLinkedField() {
		return colName;
	}
}
