package gwtSql.client.controls;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

import gwtSql.shared.DBRecord;
import gwtSql.shared.DateUtils;
import gwtSql.shared.DebugUtils;

public class dbDateBox extends Composite implements Controls {
	public DBRecord R;
	public String colName;
	public @UiField(provided = true) _DateBox dateBox;
	String Format;
	// number of minutes to add after select
	int minutesAdd = 0;

	interface MyUiBinder extends UiBinder<Widget, dbDateBox> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	/*
	 * create and attach - onChange
	 */

	public dbDateBox(String strColName) {
		this(strColName, "yyyy-MM-dd");

	}

	public dbDateBox(String strColName, String strFormat) {
		colName = strColName;
		Format = strFormat;
		dateBox = new _DateBox();

		this.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(Format)));

		// change handler
		this.dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				// in R - we have all the time the YYYY-MM-DD format
				if (dbDateBox.this.R != null) {
					Date d = dbDateBox.this.getValue();
					if (minutesAdd != 0)
						d = DateUtils.addMinutes(d, minutesAdd);
					dbDateBox.this.R.put(dbDateBox.this.colName, DateUtils.Date2String(d, Format));
					if (minutesAdd != 0)
						dbDateBox.this.setValue(d);
				} else {
					DebugUtils.W("The value cannot be modified !");
				}
			}
		});

		initWidget(uiBinder.createAndBindUi(this));

	}

	public void Refresh() {

		if (R != null) {
			Date d = null;

			try {
				Object o = R.get(this.colName);
				if (o != null) {
					String strDate = o.toString();
					d = DateUtils.String2DateTime(strDate, Format);
					this.setValue(d);
				} else {
					this.setValue(d);
				}
				// set the format again ... don'u know why ...
				this.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(Format)));

			} catch (Exception e) {
				DebugUtils.W(e.toString());
			}

		} else
			DebugUtils.W("R is null");

	}

	public void SetR(DBRecord R1) {
		this.R = R1;
	}

	// setters - getters
	public void setValue(Date d) {
		this.dateBox.setValue(d);
	}

	public Date getValue() {
		return this.dateBox.getValue();
	}

	public void setFormat(DefaultFormat defaultFormat) {
		this.dateBox.setFormat(defaultFormat);
	}

	public void setEnabled(boolean b) {
		this.dateBox.setEnabled(b);
	}

	public void setMinutes(int minutes) {
		minutesAdd = minutes;
	}

	public void autofill() {
		this.dateBox.autofill();
		if (dbDateBox.this.getValue() != null) {
			if (dbDateBox.this.R != null)
				dbDateBox.this.R.put(dbDateBox.this.colName,
						DateUtils.Date2String(dbDateBox.this.getValue(), "yyyy-MM-dd"));
			else
				DebugUtils.W("The value cannot be modified !");
		}
	}

	public String getLinkedField() {
		return colName;
	}

}
