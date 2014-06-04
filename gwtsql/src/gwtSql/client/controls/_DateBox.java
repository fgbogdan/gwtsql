package gwtSql.client.controls;

import gwtSql.shared.DateUtils;

import java.util.Date;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;

public class _DateBox extends DateBox {

	/* margins for year month day */
	public int Y1, Y2, M1, M2, D1, D2;
	/* Delimiters */
	public String Delim;

	public String oldValue = "";

	public _DateBox() {
		this("yyyy-MM-dd");
	}

	public _DateBox(final String strFormat) {
		/* apply the format */
		this.setFormat(new DateBox.DefaultFormat(DateTimeFormat.getFormat(strFormat)));
		this.setFireNullValues(true);
		this.setTitle(strFormat);

		/* calculate the delimiters */
		Y1 = strFormat.indexOf("yyyy");
		Y2 = Y1 + 4;
		M1 = strFormat.indexOf("MM");
		M2 = M1 + 2;
		D1 = strFormat.indexOf("dd");
		D2 = D1 + 2;
		Delim = strFormat.replace("y", "").replace("M", "").replace("d", "");
		Delim = Delim.substring(1);

		// DebugUtils.D(Y1,1);
		// DebugUtils.D(Y2,1);
		// DebugUtils.D(M1,1);
		// DebugUtils.D(M2,1);
		// DebugUtils.D(D1,1);
		// DebugUtils.D(D2,1);

		this.getTextBox().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {

				/* format the text */
				int key = event.getNativeKeyCode();

				String value = _DateBox.this.getTextBox().getValue();

				/* cursor position */
				int nCursorPos = _DateBox.this.getTextBox().getCursorPos();

				// If user pressed DEL or BACK SPACE, clean the value
				// if (key == 46 || key == 8) {
				// _DateBox.this.setValue(null);
				// return;
				// }

				// tab nu pot sa il prind ...

				// la enter completez anul in cazul in care anul e ultimul
				if (key == 13 && Y1 > M2 && Y1 > D1) {

					String year = DateTimeFormat.getFormat("yyyy").format(new Date());

					int nLenght = value.length();
					// DebugUtils.D(year, 1);
					if (nLenght < 10 && nLenght >= 6) {
						value = value.substring(0, 6) + year;
						_DateBox.this.getTextBox().setValue(value);
					}
				}

				// DebugUtils.D(key, 1);
				// DebugUtils.D("key", 1);

				/* daca apas numere */
				if ((key >= 96 && key <= 105) || (key >= 48 && key <= 57)) {

					// DebugUtils.D("am intrat ... ", 1);
					/* sterg punctele sau alte semne */
					// value = value.replace(".", "").replace("-", "").replace("/",
					// "");
					//
					// int nLenght = value.length();
					//
					// String DD = nLenght >= 1 ? value.substring(0,
					// Math.min(nLenght, 2)) : "";
					// String MM = nLenght >= 3 ? value.substring(2,
					// Math.min(nLenght, 4)) : "";
					// String YYYY = nLenght >= 4 ? value.substring(4,
					// Math.min(nLenght, 10)) : "";

					/* new style */
					String DD, MM, YYYY;
					DD = getDay();
					MM = getMonth();
					YYYY = getYear();

					// DebugUtils.D(YYYY, 1);
					// DebugUtils.D(MM, 1);
					// DebugUtils.D(DD, 1);
					// DebugUtils.D("Delim" + Delim + "+");

					String newValue = strFormat;
					// DebugUtils.D(newValue, 1);
					newValue = newValue.replace("dd", DD);
					// DebugUtils.D(newValue, 1);
					newValue = newValue.replace("yyyy", YYYY);
					// DebugUtils.D(newValue, 1);
					newValue = newValue.replace("MM", MM);
					// DebugUtils.D(newValue, 1);

					_DateBox.this.getTextBox().setValue(newValue);
					oldValue = newValue;
					// _DateBox.this.setValue(DateUtils.String2Date(newValue,"dd.MM.yyyy"));

					// int nCursorPos = 0;
					// if (nLenght < 2) {
					// nCursorPos = nLenght;
					// } else if (nLenght < 5) {
					// nCursorPos = nLenght + 1;
					// } else
					// nCursorPos = nLenght + 1 + 1;

					/* daca cursorul e in final il mut mai departe */
					if (nCursorPos == Y2 || nCursorPos == M2 || nCursorPos == D2)
						nCursorPos++;
					_DateBox.this.getTextBox().setCursorPos(nCursorPos);

					/* cursor at the last position */

				} else {
					/* am apasat altceva decat numere */
					/* pun la loc vechea valoare daca nu e stergere ... */
					if (key != 8 && key != 46 && key != 13)
						_DateBox.this.getTextBox().setValue(oldValue);
				}

			}
		}); // addKeyUpHandler

		this.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {

				// daca data nu e valida ... dar cu an ar fi ...
				if (_DateBox.this.getValue() == null) {

					/* the format must be DD.MM.YYYY */
					String value = _DateBox.this.getTextBox().getValue();

					if (value.length() == 6) {
						String year = DateTimeFormat.getFormat("yyyy").format(new Date());
						value = value + year;
					}

					_DateBox.this.getTextBox().setValue(value);
				}
			}
		});

	}

	/* completez in cazul in care nu e de formatul care trebuie */
	public void autofill() {
		/* completez data */
		String strdValue = this.getTextBox().getValue();

		if (strdValue.length() != 10 && strdValue.length() > 5)
			strdValue = strdValue.substring(0, 6) + DateUtils.getYear();
		this.getTextBox().setValue(strdValue, true);
	}

	public String getDay() {
		String value = _DateBox.this.getTextBox().getValue(), strRetVal;
		if (value.length() < D1)
			strRetVal = "";
		else if (value.length() < D2)
			strRetVal = value.substring(D1, value.length());
		else
			strRetVal = value.substring(D1, D2);

		// DebugUtils.D("Day" + strRetVal);
		return strRetVal.replace(Delim, "");
	}

	public String getMonth() {
		String value = _DateBox.this.getTextBox().getValue(), strRetVal;
		if (value.length() < M1)
			strRetVal = "";
		else if (value.length() < M2)
			strRetVal = value.substring(M1, value.length());
		else
			strRetVal = value.substring(M1, M2);

		// DebugUtils.D("Month" + strRetVal);
		return strRetVal.replace(Delim, "");
	}

	public String getYear() {
		String value = _DateBox.this.getTextBox().getValue(), strRetVal;
		if (value.length() < Y1) {
			// DebugUtils.D(0, 1);
			strRetVal = "";
		} else if (value.length() < Y2) {
			// DebugUtils.D(1, 1);
			strRetVal = value.substring(Y1, value.length());
		} else {
			// DebugUtils.D(2, 1);
			// DebugUtils.D(Y1);
			// DebugUtils.D(Y2);
			strRetVal = value.substring(Y1, Y2);
		}
		// DebugUtils.D("Year" + strRetVal);
		return strRetVal.replace(Delim, "");
	}
}
