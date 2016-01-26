package gwtSql.client.controls;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.RadioButton;

import gwtSql.shared.DBRecord;
import gwtSql.shared.DebugUtils;

public class dbRadioButton extends RadioButton implements Controls {

	public DBRecord R;
	public String strLinkedField = "";
	public double value;
	public boolean isLoaded = false;

	/**
	 * constructor
	 * 
	 * @param p_name
	 *            - name (all the buttons from a group must have the same name)
	 * @param p_strLinkedField
	 *            - linked field (will store the value)
	 * @param p_value
	 *            - value to store
	 */
	public dbRadioButton(String p_name, String p_strLinkedField, double p_value) {
		super(p_name);
		strLinkedField = p_strLinkedField;
		value = p_value;

		this.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {

				if (dbRadioButton.this.R != null)
					dbRadioButton.this.R.put(dbRadioButton.this.strLinkedField, value);
				else
					DebugUtils.W("The value cannot be modified !");

			}
		});

	}

	public void Refresh() {
		if (R == null)
			return;
		if (R.getDouble(this.strLinkedField) == value)
			this.setValue(true);
		else
			this.setValue(false);
	}

	public void SetR(DBRecord R1) {
		this.R = R1;
	}

	public String getLinkedField() {
		return strLinkedField;
	}

}
