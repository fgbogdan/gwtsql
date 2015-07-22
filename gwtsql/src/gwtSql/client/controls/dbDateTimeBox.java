package gwtSql.client.controls;

import gwtSql.client.controls.HourMinutePicker.PickerFormat;
import gwtSql.shared.DBRecord;
import gwtSql.shared.DateUtils;

import java.util.Date;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

@SuppressWarnings("deprecation")
public class dbDateTimeBox extends Composite implements Controls {

	// control compus dintr-un dbDateTimeBox si doua butoane
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

		// change for minutes
		// this.hmp.addAttachHandler(handler)

	}

	public void Refresh() {

		// refresh the control
		if (R != null) {
			Date d = null;

			try {
				Object o = R.get(this.colName);
				// Window.alert(o.toString());
				if (o != null) {
					String strDate = o.toString();
					d = DateUtils.String2DateTime(strDate, Format);
					// refresh the date control
					dateBox.setValue(d);
					// refresh the time control
					d = DateUtils.String2DateTime(strDate);
					// Window.alert(d.getHours()+"");
					// Window.alert(d.getMinutes()+"");
					hmp.refreshWidget("", d.getHours(), d.getMinutes() / 15);
				} else {
					dateBox.setValue(d);
				}
				// set the format again ... don'u know why ...
				this.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(Format)));

			} catch (Exception e) {
				System.out.println(e.toString());
			}

		} else
			System.out.println("R is null");

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
		// in R - se pune tot timpul formatul SQL yyyy-MM-dd HH:mm
		if (R != null) {
			Date d = dateBox.getValue();
			// adaug minutele din hmp
			d = DateUtils.addMinutes(d, hmp.getMinutes());
			// update R
			R.put(colName, DateUtils.Date2String(d, Format + " HH:mm"));
		} else {
			Window.alert("Informatia nu se poate modifica sau nu este adaugata !");
		}
	}

	public String getLinkedField() {
		return colName;
	}
}