package gwtSql.client.forms;

import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.LayoutPanel;

import gwtSql.shared.DBRecord;

public class BaseDialogBox extends DialogBox implements IForm {
	public LayoutPanel baseLayoutPanel;
	public ClosableDialogBox VFF;

	/**
	 * constructor
	 */
	public BaseDialogBox() {

		super(false, false);

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
