package gwtSql.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import gwtSql.shared.DBRecord;

public class DialogTextForm extends ClosableDialogBox {

	interface MyUiBinder extends UiBinder<Widget, DialogTextForm> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	String textValue, p_type;
	VForm p_caller = null;
	@UiField(provided = true) TextArea ta;
	@UiField Button btnSave, btnCancel;

	/**
	 * 
	 * @param f
	 *           - caller
	 * @param type
	 *           - type of call
	 * @param initialValue
	 *           - initial value of string
	 */
	public DialogTextForm(VForm f, String type, String initialValue) {

		p_caller = f;
		p_type = type;
		textValue = initialValue;
		ta = new TextArea();

		// gray layer
		setGlassEnabled(true);

		// show
		setWidget(uiBinder.createAndBindUi(this));

		show();
		Refresh();

	}

	public void Refresh() {

		ta.setText(textValue);
		DialogTextForm.this.center();

	} // Refresh

	@UiHandler("btnSave")
	void btnSave_click(ClickEvent e) {
		DBRecord R = new DBRecord();
		R.put("DIALOGTEXTFORM", ta.getText());
		p_caller.onReturn(p_type, R);
		hide();

	} // Save

	@UiHandler("btnCancel")
	void btnCancel_click(ClickEvent e) {
		hide();
	} // Cancel

	// /**
	// * prevent dragging
	// */
	// protected void beginDragging(MouseDownEvent e) {
	// e.preventDefault();
	// }

}
