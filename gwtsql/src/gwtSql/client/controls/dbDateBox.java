package gwtSql.client.controls;

import gwtSql.shared.DBRecord;
import gwtSql.shared.DateUtils;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DateBox.DefaultFormat;

public class dbDateBox extends Composite implements Controls {
	public DBRecord R;
	public String colName;
	public @UiField(provided = true) _DateBox dateBox;
	// static String Format = "yyyy-MM-dd";
	String Format;

	// @UiField Button btnDel;

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

		// this.setFormat(new
		// DateBox.DefaultFormat(DateTimeFormat.getFormat(Format)));
		// this.setFormat(new
		// DateBox.DefaultFormat(DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT)));
		// this.setFormat(new
		// DateBox.DefaultFormat(DateTimeFormat.getFormat("EEEE, MMMM dd, yyyy")));
		this.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(Format)));

		// change handler
		this.dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
			public void onValueChange(ValueChangeEvent<Date> event) {
				// in R - se pune tot timpul formatul SQL yyyy-MM-dd
				if (dbDateBox.this.R != null)
					dbDateBox.this.R.put(dbDateBox.this.colName, DateUtils.Date2String(dbDateBox.this.getValue(), Format));
				else
					Window.alert("Informatia nu se poate modifica sau nu este adaugata !");
			}
		});

		initWidget(uiBinder.createAndBindUi(this));

	}

	public void Refresh() {

		if (R != null) {
			Date d = null;
			/*
			 * System.out.println("DateBox"); System.out.println(this.colName);
			 * System.out.println(R.get(this.colName));
			 */

			// d = DateUtils.String2Date((String)R.get(this.colName));

			try {
				Object o = R.get(this.colName);
				if (o != null) {
					String strDate = o.toString();
					d = DateUtils.String2DateTime(strDate, Format);
					this.setValue(d);
				} else {
					// d = DateUtils.String2Date("1900-01-01");
					this.setValue(d);
				}
				// set the format again ... don'u know why ...
				this.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(Format)));

			} catch (Exception e) {
				System.out.println(e.toString());
			}

		} else
			System.out.println("R is null");

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

	// @UiHandler("btnDel")
	// public void btnDelClick(ClickEvent e) {
	// // daca sterg ... trec data pe 1900-01-01 si pe Save o sa trec in null.
	// this.dateBox.setValue(null);
	// dbDateBox.this.R.put(dbDateBox.this.colName,
	// DateUtils.String2SQLDate("1900-01-01"));
	// this.dateBox.setFocus(true);
	// }

	public void autofill() {
		this.dateBox.autofill();
		// DebugUtils.D(dbDateBox.this.getValue(), 1);
		if (dbDateBox.this.getValue() != null) {
			if (dbDateBox.this.R != null)
				dbDateBox.this.R.put(dbDateBox.this.colName, DateUtils.Date2String(dbDateBox.this.getValue(), "yyyy-MM-dd"));
			else
				Window.alert("Informatia nu se poate modifica sau nu este adaugata !");
		}
	}

}
