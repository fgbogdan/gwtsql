package gwtSql.client.controls;

import java.util.Date;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.datepicker.client.DateBox;

import gwtSql.shared.DateUtils;

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

		this.getTextBox().addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {

				/* format the text */
				int key = event.getNativeKeyCode();

				String value = _DateBox.this.getTextBox().getValue();

				/* cursor position */
				int nCursorPos = _DateBox.this.getTextBox().getCursorPos();

				// Enter - write the current year
				if (key == 13 && Y1 > M2 && Y1 > D1) {

					String year = DateTimeFormat.getFormat("yyyy").format(new Date());

					int nLenght = value.length();
					if (nLenght < 10 && nLenght >= 6) {
						value = value.substring(0, 6) + year;
						_DateBox.this.getTextBox().setValue(value);
					}
				}

				/* number keys */
				if ((key >= 96 && key <= 105) || (key >= 48 && key <= 57)) {

					/* new style */
					String DD, MM, YYYY;
					DD = getDay();
					MM = getMonth();
					YYYY = getYear();

					String newValue = strFormat;
					newValue = newValue.replace("dd", DD);
					newValue = newValue.replace("yyyy", YYYY);
					newValue = newValue.replace("MM", MM);

					_DateBox.this.getTextBox().setValue(newValue);
					oldValue = newValue;

					/* if the cursor is on the end - move forwards */
					if (nCursorPos == Y2 || nCursorPos == M2 || nCursorPos == D2)
						nCursorPos++;
					_DateBox.this.getTextBox().setCursorPos(nCursorPos);

					/* cursor at the last position */

				} else {
					/* other keys that numbers */
					/* restore old values, except for delete */
					if (key != 8 && key != 46 && key != 13)
						_DateBox.this.getTextBox().setValue(oldValue);
				}

			}
		}); // addKeyUpHandler

		this.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {

				// if the date is not complete, add the year
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

	/* complete if the format is not correct */
	public void autofill() {

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

		return strRetVal.replace(Delim, "");
	}

	public String getYear() {
		String value = _DateBox.this.getTextBox().getValue(), strRetVal;
		if (value.length() < Y1) {
			strRetVal = "";
		} else if (value.length() < Y2) {
			strRetVal = value.substring(Y1, value.length());
		} else {
			strRetVal = value.substring(Y1, Y2);
		}
		return strRetVal.replace(Delim, "");
	}
}
