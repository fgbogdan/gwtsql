package gwtSql.client.controls;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.client.forms.DialogSelectForm;
import gwtSql.client.forms.VForm;
import gwtSql.shared.DBRecord;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class dbPickBox extends VForm implements Controls {

	public DBRecord R;
	public String colName;
	String strTableName;
	String strShowField;
	String strKeyField;
	String strFilterCondition = "";
	String strOrder = "";
	String strSQLCommand = "";
	public String strLinkedField = "";

	VForm form;

	public @UiField TextBox textBox;
	public @UiField Button btnGet;

	interface MyUiBinder extends UiBinder<Widget, dbPickBox> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private final DBServiceAsync dbService = GWT.create(DBService.class);

	public dbPickBox(VForm f, String p_strTableName, String p_strShowField, String p_strKeyField, String p_strFilterCondition, String p_strOrder,
			String p_strLinkedField) {
		form = f;
		this.strTableName = p_strTableName;
		this.strShowField = p_strShowField;
		this.strKeyField = p_strKeyField;
		this.strFilterCondition = p_strFilterCondition;
		this.strOrder = p_strOrder;
		this.strLinkedField = p_strLinkedField;
		initWidget(uiBinder.createAndBindUi(this));
		textBox.setReadOnly(true);
	} // dbPickBox

	/**
	 * from Controls
	 */
	public void SetR(DBRecord R1) {
		this.R = R1;
	}

	/**
	 * 
	 * GetButton
	 */
	@UiHandler("btnGet")
	void btnGet_onClick(ClickEvent e) {
		new DialogSelectForm(form, this, "");

	} // @UiHandler("btnGet")

	/**
	 * set the filter
	 * 
	 * @param strFilterText
	 */
	public void SetFilter(String strFilterText) {
		this.strFilterCondition = strFilterText;
	}

	/**
	 * refresh the content
	 */
	public void Refresh() {

		if (this.strLinkedField.equals(""))
			return;
		String KeyValue = R.getString(this.strLinkedField.toUpperCase());
		if (KeyValue == null)
			return;
		KeyValue = KeyValue.trim();
		try {
			/* search in the record set */
			dbService.GetDBRecord(strTableName, strKeyField, KeyValue, new AsyncCallback<DBRecord>() {

				@Override
				public void onFailure(Throwable caught) {
					Window.alert("dbPickBox.GetDBRecord.Fail!");
				}

				@Override
				public void onSuccess(DBRecord result) {
					if (result.tableName.isEmpty())
						dbPickBox.this.textBox.setText("");
					else
						dbPickBox.this.textBox.setText(result.getString(strShowField.toUpperCase()));
				}
			});
		} catch (Exception e) {
			Window.alert(e.toString());
		}

	} // Refresh()

	/* called after btnGet click */
	public void onReturn(String type, DBRecord R) {
		// Window.alert("dbPickBox");
		if (dbPickBox.this.R != null) {
			dbPickBox.this.R.put(dbPickBox.this.strLinkedField.toUpperCase(), R.getString(strKeyField.toUpperCase()));
			dbPickBox.this.textBox.setText(R.getString(strShowField.toUpperCase()));
		} else
			Window.alert("Informatia nu se poate modifica sau nu este adaugata !");

	}

	@Override
	public DBRecord ReturnSelected() {
		return null;
	}

	public String getLinkedField() {
		return colName;
	}

}