package gwtSql.client.forms;

import gwtSql.client.controls.BaseDialogBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

public class LoadForm extends BaseDialogBox {

	interface MyUiBinder extends UiBinder<Widget, LoadForm> {
	}

	// @UiField (provided=true) Label lbcounter;
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	public LoadForm() {

		setText("Loading ... ");
		// lbcounter = new Label();
		setWidget(uiBinder.createAndBindUi(this));

	}

	public void Counter(int counter) {
		// lbcounter.setText(Integer.toString(counter));
	}

}