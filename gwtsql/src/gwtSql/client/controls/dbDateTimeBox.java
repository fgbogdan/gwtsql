package gwtSql.client.controls;

import gwtSql.shared.DBRecord;
import gwtSql.shared.DateUtils;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

public class dbDateTimeBox extends Composite implements Controls {

	// control compus dintr-un dbDateTimeBox si doua butoane
	public dbDateBox dbdateBox = null;
	public PushButton btn1, btn2;

	public DBRecord R;
	public String colName;

	String Format;

	public dbDateTimeBox(String strColName) {
		this(strColName, "yyyy-MM-dd HH:mm:ss");
	}

	public dbDateTimeBox(String strColName, String strFormat) {

		colName = strColName;
		Format = strFormat;

		dbdateBox = new dbDateBox(strColName, strFormat);

		btn1 = new PushButton("-");
		btn1.setSize("10px", "20px");
		btn1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// 15 minutes more
				Date d = dbdateBox.getValue();
				d = DateUtils.addMinutes(d, -15);

				dbdateBox.setValue(d);
				if (dbDateTimeBox.this.R != null)
					dbDateTimeBox.this.R.put(dbDateTimeBox.this.colName, DateUtils.Date2String(d, dbDateTimeBox.this.Format));
				else
					Window.alert("Informatia nu se poate modifica sau nu este adaugata !");
			}
		});

		btn2 = new PushButton("+");
		btn2.setSize("10px", "20px");
		btn2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				// 15 minutes more
				Date d = dbdateBox.getValue();
				d = DateUtils.addMinutes(d, 15);

				dbdateBox.setValue(d);
				if (dbDateTimeBox.this.R != null)
					dbDateTimeBox.this.R.put(dbDateTimeBox.this.colName, DateUtils.Date2String(d, dbDateTimeBox.this.Format));
				else
					Window.alert("Informatia nu se poate modifica sau nu este adaugata !");
			}
		});

		HorizontalPanel panel = new HorizontalPanel();
		panel.add(btn1);
		panel.add(dbdateBox);
		panel.add(btn2);
		// panel.setSpacing(8);
		initWidget(panel);
	}

	public void Refresh() {
		this.dbdateBox.Refresh();
	}

	public void setFormat(DefaultFormat dfltFormat) {
		dbdateBox.setFormat(dfltFormat);
	}

	public Date getValue() {
		return dbdateBox.getValue();
	}

	public void SetR(DBRecord R1) {
		this.R = R1;
	}
}
