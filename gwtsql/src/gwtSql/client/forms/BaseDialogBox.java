package gwtSql.client.forms;

import gwtSql.shared.DBRecord;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.LayoutPanel;

public class BaseDialogBox extends DialogBox implements IForm {
	public LayoutPanel baseLayoutPanel;
	public ClosableDialogBox VFF;

	/**
	 * constructor
	 */
	public BaseDialogBox() {

		super(false, false);

		//setText("BaseDialogBox");

		//setSize("182px", "94px");

		// Enable animation.
		setAnimationEnabled(true);

		// Enable glass background.
		setGlassEnabled(true);

		baseLayoutPanel = new LayoutPanel();
		baseLayoutPanel.setStyleName("BasePanel");
		setWidget(baseLayoutPanel);
		baseLayoutPanel.setSize("154px", "48px");

	}

	@Override
	public void onReturn(String type, DBRecord R) {

	}

	
	@Override
	public DBRecord ReturnSelected() {
		return null;
	}

}