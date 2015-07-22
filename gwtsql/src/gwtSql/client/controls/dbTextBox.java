package gwtSql.client.controls;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.shared.DBRecord;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

public class dbTextBox extends TextBox implements Controls {

	public DBRecord R;
	public String colName;

	/* serviciul de comunicare cu baza de date */
	private final DBServiceAsync dbService = GWT.create(DBService.class);

	/*
	 * create and attach - onChange
	 */
	/* apel cu un parametru */
	public dbTextBox(String strColName) {
		this(strColName, false);
	}

	/* apel cu doi parametrii */
	public dbTextBox(String strColName, final Boolean bSave) {
		colName = strColName;

		this.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				//
				if (dbTextBox.this.R != null) {
					dbTextBox.this.R.put(dbTextBox.this.colName, dbTextBox.this.getText());

					// if save - salvez un R existent in baza de date

					if (bSave) {
						dbTextBox.this.R.isNew = false;
						// salvare inregistrare
						dbService.saveDBRecord(R, new AsyncCallback<String>() {
							@Override
							public void onSuccess(String result) {
								// nothing
								// Window.alert("dbTextBox.onChange.saveDBRecord succes");
							}

							@Override
							public void onFailure(Throwable caught) {
								Window.alert("dbTextBox.onChange.saveDBRecord fail");
							}
						});
					}
				} else
					Window.alert("Informatia nu se poate modifica sau nu este adaugata !");
			}

		});

	}

	public void Refresh() {

		if (R != null) {
			Object o = R.get(this.colName);
			this.setText(o.toString());
		} else
			System.out.println("R is null");

	}

	public void SetR(DBRecord R1) {
		this.R = R1;
	}

	public String getLinkedField() {
		return colName;
	}

}
