package gwtSql.client.controls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.shared.DBRecord;
import gwtSql.shared.DebugUtils;

public class dbTextBox extends TextBox implements Controls {

	public DBRecord R;
	public String colName;

	// database connector */
	private final DBServiceAsync dbService = GWT.create(DBService.class);

	/**
	 * constructor
	 * 
	 * @param strColName
	 *            - name of the field
	 */
	public dbTextBox(String strColName) {
		this(strColName, false);
	}

	/**
	 * constructor
	 * 
	 * @param strColName
	 *            - name of the column
	 * @param bSave
	 *            - autosave
	 */
	public dbTextBox(String strColName, final Boolean bSave) {
		colName = strColName;

		this.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				//
				if (dbTextBox.this.R != null) {
					dbTextBox.this.R.put(dbTextBox.this.colName, dbTextBox.this.getText());

					// if save - save the R

					if (bSave) {
						dbTextBox.this.R.isNew = false;
						// save
						dbService.saveDBRecord(R, new AsyncCallback<String>() {
							@Override
							public void onSuccess(String result) {
								// nothing

							}

							@Override
							public void onFailure(Throwable caught) {
								DebugUtils.W("dbTextBox.onChange.saveDBRecord fail");
							}
						});
					}
				} else
					DebugUtils.W("The value cannot be modified !");
			}

		});

	}

	public void Refresh() {

		if (R != null) {
			Object o = R.get(this.colName);
			this.setText(o.toString());
		} else
			DebugUtils.W("R is null");

	}

	public void SetR(DBRecord R1) {
		this.R = R1;
	}

	public String getLinkedField() {
		return colName;
	}

}
