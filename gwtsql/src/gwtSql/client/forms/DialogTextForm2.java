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

public class DialogTextForm2 extends ClosableDialogBox {

	interface MyUiBinder extends UiBinder<Widget, DialogTextForm2> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	String textValue1, textValue2, p_type;
	VForm p_caller = null;
	@UiField(provided = true) TextArea ta1, ta2;
	@UiField Button btnSave, btnCancel;

	/**
	 * a window to input a Text
	 * 
	 * @param f
	 *           - caller
	 * @param type
	 *           - type of call used for onReturn parameter
	 * @param initialValue
	 *           - initial value of string
	 */
	public DialogTextForm2(VForm f, String type, String initialValue1, String initialValue2) {

		p_caller = f;
		p_type = type;
		textValue1 = initialValue1;
		textValue2 = initialValue2;
		ta1 = new TextArea();
		ta2 = new TextArea();

		// gray layer
		setGlassEnabled(true);

		// show
		setWidget(uiBinder.createAndBindUi(this));

		show();
		Refresh();

	}

	public void Refresh() {

		ta1.setText(textValue1);
		ta2.setText(textValue2);
		DialogTextForm2.this.center();

	} // Refresh

	@UiHandler("btnSave")
	void btnSave_click(ClickEvent e) {
		DBRecord R = new DBRecord();
		R.put("DIALOGTEXTFORM1", ta1.getText());
		R.put("DIALOGTEXTFORM2", ta2.getText());
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
