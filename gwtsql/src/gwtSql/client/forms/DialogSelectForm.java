package gwtSql.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class DialogSelectForm extends ClosableDialogBox {

	interface MyUiBinder extends UiBinder<Widget, DialogSelectForm> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField(provided = true) Composite show_form;
	VForm vform;
	String p_strType = "<DialogSelectForm>";

	/**
	 * a window with a widget and a button (select)
	 * 
	 * @param f - the widget showed
	 */
	public DialogSelectForm(VForm f) {
		setGlassEnabled(true);
		show_form = f;
		// set the dialog box and the caller
		f.dialogbox_form = this;
		f.caller_form = null;
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
	public DialogSelectForm(VForm f, VForm f1, String strType) {
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
	 * select method
	 * 
	 * @param e
	 */
	@UiHandler("select")
	void selectClick(ClickEvent e) {
		vform.caller_form.onReturn(p_strType, ((IForm) show_form).ReturnSelected());
		hide();
	}

}
