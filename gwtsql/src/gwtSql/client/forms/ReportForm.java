package gwtSql.client.forms;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.client.controls.BaseDialogBox;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class ReportForm extends BaseDialogBox {

	interface MyUiBinder extends UiBinder<Widget, ReportForm> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);
	private Anchor closeAnchor;
	public String p_fileName;

	/* serviciul de comunicare cu baza de date */
	private final DBServiceAsync dbService = GWT.create(DBService.class);

	@UiField Frame f;
	@UiField HorizontalPanel h;

	public ReportForm(String fileName) {

		// DebugUtils.D("--------------", 1);
		// DebugUtils.D(fileName, 1);
		p_fileName = fileName;
		setText("");
		closeAnchor = new Anchor("X");

		FlexTable captionLayoutTable = new FlexTable();
		captionLayoutTable.setWidth("100%");
		//captionLayoutTable.setText(0, 0, "Report form");
		captionLayoutTable.setText(0, 0, "Preview ...");
		captionLayoutTable.setWidget(0, 1, closeAnchor);
		captionLayoutTable.getCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.HorizontalAlignmentConstant.endOf(HasDirection.Direction.LTR));

		HTML caption = (HTML) getCaption();
		caption.getElement().appendChild(captionLayoutTable.getElement());

		caption.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				EventTarget target = event.getNativeEvent().getEventTarget();
				Element targetElement = (Element) target.cast();

				if (targetElement == closeAnchor.getElement()) {
					closeAnchor.fireEvent(event);
				}
			}

		});

		addCloseHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
				// bService.deleteFile("reports/" + p_url, new
				// AsyncCallback<String>() {
				dbService.deleteFile(p_fileName, new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						/* nothing */
					}

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("ReportForm.deleteFile fail");
						Window.alert(caught.getMessage());
					}
				});
			}
		});

		setWidget(uiBinder.createAndBindUi(this));

		/* daca am fisier sau html */
		if (p_fileName.length() > 250) {
			/* html */
			h.add( new HTML(p_fileName));
			h.setHeight("800px");
			h.setWidth("1000px");
			f.setHeight("0px");
			f.setWidth("0px");
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
		}

	}

	public void addCloseHandler(ClickHandler handler) {

		closeAnchor.addClickHandler(handler);

	}

}
