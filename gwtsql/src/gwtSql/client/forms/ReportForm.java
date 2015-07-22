package gwtSql.client.forms;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ReportForm extends ClosableDialogBox {

	interface MyUiBinder extends UiBinder<Widget, ReportForm> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	public String p_fileName;

	@UiField Frame f;
	@UiField HorizontalPanel h;

	public ReportForm(String fileName) {

		// DebugUtils.D("--------------", 1);
		// DebugUtils.D(fileName, 1);
		p_fileName = fileName;

		setWidget(uiBinder.createAndBindUi(this));

		/* daca am fisier sau html */
		if (p_fileName.length() > 250) {
			/* html */
			h.add(new HTML(p_fileName));
			// h.setHeight("800px");
			// h.setWidth("1000px");
			// f.setHeight("0px");
			// f.setWidth("0px");
			h.setVisible(true);
			f.setVisible(false);
		} else {
			/* fisier */
			String fullPath = p_fileName;
			int index = fullPath.lastIndexOf("\\");
			String f1 = fullPath.substring(index + 1);
			// DebugUtils.D("File name without path and extension .....",1);
			// DebugUtils.D(f1,1);
			f.setUrl("reports/" + f1);
			f.setHeight("800px");
			f.setWidth("1000px");
			h.setHeight("0px");
			h.setWidth("0px");
			h.setVisible(false);
			f.setVisible(true);
		}

		show();
		center();
		setPopupPosition(getAbsoluteLeft(), 50);

	}

}
