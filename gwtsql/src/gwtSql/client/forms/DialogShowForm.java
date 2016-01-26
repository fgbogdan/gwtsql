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

	@UiField(provided = true)
	Composite show_form;
	String p_strType;
	VForm p_caller = null;
	VForm vform = null;

	/**
	 * a window to show a Widget
	 * 
	 * @param f-
	 *            the widget (receive onReturn after the window is closed)
	 */
	public DialogShowForm(VForm f) {
		setGlassEnabled(true);
		show_form = f;
		// set the dialog box and the caller
		f.dialogbox_form = this;
		f.caller_form = null;

		vform = f;
		// vform.DBox = this;
		// show
		setWidget(uiBinder.createAndBindUi(this));

		show();
		setCenter();

	}

	/**
	 * call from dbPickBox
	 * 
	 * @param f
	 * @param f1
	 */
	public DialogShowForm(VForm f, VForm f1, String strType) {
		setGlassEnabled(true);
		p_strType = strType;
		show_form = f;
		vform = f;
		// set the dialog box and the caller
		f.caller_form = f1;
		f.dialogbox_form = this;
		f.caller_varName = strType;
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
