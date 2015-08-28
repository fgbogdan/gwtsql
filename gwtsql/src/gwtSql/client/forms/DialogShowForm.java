package gwtSql.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DialogShowForm extends ClosableDialogBox {

	interface MyUiBinder extends UiBinder<Widget, DialogShowForm> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField(provided = true) Composite show_form;
	String p_strType;
	VForm p_caller = null;
	VForm p_form = null;

	public DialogShowForm(VForm f) {
		setGlassEnabled(true);
		show_form = f;
		// set the dialog box and the caller
		f.dialogbox_form = this;
		f.caller_form = null;
		
		p_form = f;
		p_form.DBox = this;
		// show
		setWidget(uiBinder.createAndBindUi(this));

		show();
		setCenter();

	}

	/**
	 * close method
	 * 
	 * @param e
	 */
	public void onClose() {
		if (p_caller != null)
			p_caller.onReturn("SHOW" + p_strType, null);
	}

	public void setCaller(VForm form) {
		p_caller = form;
	}

	// /**
	// * prevent dragging
	// */
	// protected void beginDragging(MouseDownEvent e) {
	// e.preventDefault();
	// }

}
