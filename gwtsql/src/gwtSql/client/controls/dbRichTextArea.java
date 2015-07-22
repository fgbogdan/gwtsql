package gwtSql.client.controls;

import gwtSql.shared.DBRecord;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

public class dbRichTextArea extends Composite implements Controls {

	public DBRecord R;
	public String colName;
	@UiField(provided = true) RichTextArea rta;
	@UiField Button btnSave, btnCancel;

	interface MyUiBinder extends UiBinder<Widget, dbRichTextArea> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	/*
	 * create and attach - onChange
	 */
	/* apel cu un parametru */
	public dbRichTextArea(String strColName) {
		colName = strColName;
		rta = new RichTextArea();

		initWidget(uiBinder.createAndBindUi(this));
	}

	public void Refresh() {

		if (R != null) {
			this.rta.setHTML(R.getString(this.colName));
		} else
			System.out.println("R is null");

	} // Refresh

	public void SetR(DBRecord R1) {
		this.R = R1;
	} // SetR

	@UiHandler("btnSave")
	void btnSave_click(ClickEvent e) {

		if (dbRichTextArea.this.R != null) {
			dbRichTextArea.this.R.put(dbRichTextArea.this.colName, dbRichTextArea.this.rta.getHTML());
		}
	} // Save

	@UiHandler("btnCancel")
	void btnCancel_click(ClickEvent e) {

		Refresh();
	} // Cancel

	public String getLinkedField() {
		return colName;
	}
}
