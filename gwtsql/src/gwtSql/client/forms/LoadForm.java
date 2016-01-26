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

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField(provided = true)
	VerticalPanel vPanel;

	/**
	 * a window showed in the Load Process (handled by TheApp.StartLoading ... )
	 */
	public LoadForm() {

		vPanel = new VerticalPanel();

		addStyleName("round-dialog");

		setWidget(uiBinder.createAndBindUi(this));

	}

	public void AddText(String text) {
		if (text.isEmpty())
			vPanel.clear();
		else
			vPanel.add(new Label(text));
	}

}