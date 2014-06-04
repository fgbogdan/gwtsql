package gwtSql.client.controls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.HasDirection;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class VFloatForm extends VListForm {

	interface MyUiBinder extends UiBinder<Widget, VFloatForm> {
	}

	@UiField(provided = true) SimplePanel SP;
	private Anchor closeAnchor;

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	/**
	 * 
	 * @param f
	 *           - caller pointer
	 * @param varName
	 *           - field to complete
	 */
	public VFloatForm(IForm iform, String strCaption) {

		// caption header
		setText("");
		closeAnchor = new Anchor("X");

		FlexTable captionLayoutTable = new FlexTable();
		captionLayoutTable.setWidth("100%");
		captionLayoutTable.setText(0, 0, strCaption);
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
			}
		});

		// panel
		SP = new SimplePanel();
		SP.add((Widget) iform);
		// pointer VFloatForm
		iform.setVFF(this);
		setWidget(uiBinder.createAndBindUi(this));
		
		this.center();
		this.show();

	}

	public void addCloseHandler(ClickHandler handler) {

		closeAnchor.addClickHandler(handler);

	}

}
