package gwtSql.client.controls;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

import gwtSql.client.controls.HourMinutePicker.PickerFormat;
import gwtSql.shared.DBRecord;
import gwtSql.shared.DateUtils;
import gwtSql.shared.DebugUtils;

@SuppressWarnings("deprecation")
public class dbDateTimeBox extends Composite implements Controls {

	// a dbDateTimeBox and 2 buttons
	public _DateBox dateBox = null;
	public HourMinutePicker hmp;
	public PushButton btn1, btn2;

	public DBRecord R;
	public String colName;

	String Format;

	public dbDateTimeBox(String strColName) {
		this(strColName, "yyyy-MM-dd");
	}

	public dbDateTimeBox(String strColName, String strFormat) {

		colName = strColName;
		Format = strFormat;

		dateBox = new _DateBox(Format);
		hmp = new HourMinutePicker(this, PickerFormat._24_HOUR);
		HorizontalPanel panel = new HorizontalPanel();
		panel.add(dateBox);
		panel.add(hmp);
		panel.setSpacing(8);
		initWidget(panel);

		// change for date
		this.dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				ValueChange();
			}
		});

	}

	public void Refresh() {

		// refresh the control
		if (R != null) {
			Date d = null;

			try {
				Object o = R.get(this.colName);
				if (o != null) {
					String strDate = o.toString();
					d = DateUtils.String2DateTime(strDate, Format);
					// refresh the date control
					dateBox.setValue(d);
					// refresh the time control
					d = DateUtils.String2DateTime(strDate);
					hmp.refreshWidget("", d.getHours(), d.getMinutes() / 15);
				} else {
					dateBox.setValue(d);
				}
				// set the format again ... don'u know why ...
				this.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(Format)));

			} catch (Exception e) {
				DebugUtils.W(e.toString());
			}

		} else
			DebugUtils.W("R is null");

	}

	public void setFormat(DefaultFormat dfltFormat) {
		dateBox.setFormat(dfltFormat);
	}

	public Date getValue() {
		Date d = dateBox.getValue();
		d.setMinutes(hmp.getMinutes());
		return d;
	}

	public void SetR(DBRecord R1) {
		this.R = R1;
	}

	public void ValueChange() {
		// in R - we have the formay YYYY-MM-DD HH:mm
		if (R != null) {
			Date d = dateBox.getValue();
			// add minutes
			d = DateUtils.addMinutes(d, hmp.getMinutes());
			// update R
			R.put(colName, DateUtils.Date2String(d, Format + " HH:mm"));
		} else {
			DebugUtils.W("The value cannot be modified !");
		}
	}

	public String getLinkedField() {
		return colName;
	}
}