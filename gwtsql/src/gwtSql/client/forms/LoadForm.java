package gwtSql.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoadForm extends BaseDialogBox {

	interface MyUiBinder extends UiBinder<Widget, LoadForm> {
	}

	// @UiField (provided=true) Label lbcounter;
	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField(provided = true) VerticalPanel vPanel;

	public LoadForm() {

		vPanel = new VerticalPanel();
		// setText("Loading ... ");
		addStyleName("round-dialog");
		// lbcounter = new Label();
		setWidget(uiBinder.createAndBindUi(this));

	}

	public void Counter(int counter) {
		// lbcounter.setText(Integer.toString(counter));
	}

	public void AddText(String text) {
		if (text.isEmpty())
			vPanel.clear();
		else
			vPanel.add(new Label(text));
	}

}