package gwtSql.client.forms;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.shared.DBRecord;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;

public class DialogRichTextForm extends DialogBox {

	interface MyUiBinder extends UiBinder<Widget, DialogRichTextForm> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private final DBServiceAsync dbService = GWT.create(DBService.class);

	public DBRecord R;
	public String p_tableName, p_keyColumn, p_keyValue, p_rtaColumn;
	@UiField(provided = true) RichTextArea rta;
	@UiField Button btnSave, btnCancel;

	/**
	 * 
	 * @param f
	 *           - caller
	 * @param strType
	 *           - type of call
	 */
	public DialogRichTextForm(String tableName, String keyColumn, String keyValue, String rtaColumn) {
		p_tableName = tableName;
		p_keyColumn = keyColumn;
		p_keyValue = keyValue;
		p_rtaColumn = rtaColumn;
		rta = new RichTextArea();

		// gray layer
		setGlassEnabled(true);

		// show
		setWidget(uiBinder.createAndBindUi(this));

		show();
		Refresh();

	}

	public void Refresh() {
		/* search in the record set */
		dbService.GetDBRecord(p_tableName, p_keyColumn, p_keyValue, new AsyncCallback<DBRecord>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("DialogRichTextForm.GetDBRecord.Fail!");
			}

			@Override
			public void onSuccess(DBRecord result) {
				R = result;
				// Window.alert(R.toString());
				if (R.tableName.isEmpty())
					rta.setHTML("");
				else
					rta.setHTML(R.getString(p_rtaColumn));
				
				DialogRichTextForm.this.center();
			}
		});
	} // Refresh

	@UiHandler("btnSave")
	void btnSave_click(ClickEvent e) {
		if (!R.tableName.isEmpty()) {
			R.put(p_rtaColumn, rta.getHTML());
			dbService.saveDBRecord(R, new AsyncCallback<String>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("DialogRichTextForm.saveDBRecord.Fail!");
				}

				@Override
				public void onSuccess(String result) {
					hide();
				}
			});
		}
	} // Save

	@UiHandler("btnCancel")
	void btnCancel_click(ClickEvent e) {
		hide();
	} // Cancel

	// /**
	// * prevent dragging
	// */
	// protected void beginDragging(MouseDownEvent e) {
	// e.preventDefault();
	// }

}
