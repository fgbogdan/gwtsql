package gwtSql.client.controls;

import gwtSql.client.controls.Controls;
import gwtSql.shared.DBRecord;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RadioButton;

public class dbRadioButton extends RadioButton implements Controls {

	public DBRecord R;
	public String strLinkedField = "";
	public double value;
	public boolean isLoaded = false;

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
					Window.alert("Informatia nu se poate modifica sau nu este adaugata !");

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

}
