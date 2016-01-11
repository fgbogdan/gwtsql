package gwtSql.client.forms;

import gwtSql.client.DBService;
import gwtSql.client.DBServiceAsync;
import gwtSql.shared.DBTable;
import gwtSql.shared.Print;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

@SuppressWarnings("deprecation")
public class SQLToExcel extends VForm {

	interface MyUiBinder extends UiBinder<Widget, SQLToExcel> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	private final DBServiceAsync dbService = GWT.create(DBService.class);

	@UiField(provided = true) SimplePanel pnlXLS;

	@UiField(provided = true) FormPanel pshbtnExportXLS;
	@UiField(provided = true) Label lbTitle;

	public SQLToExcel(String strSQL, String strTitle) {

		lbTitle = new Label(strTitle);

		pnlXLS = new SimplePanel();

		final FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, new Label("Loading informations from database ... "));

		dbService.getDBTable(strSQL, new AsyncCallback<DBTable>() {
			public void onFailure(Throwable caught) {
				Window.alert("SQLToExcel getDBTable Fail  ");
				caught.printStackTrace();
			} // fail

			public void onSuccess(DBTable result) {
				// try to construct the flex table
				flexTable.removeAllRows();

				String strTemp = "";
				// header row
				for (int j = 0; j < result.Fields.size(); j++) {
					flexTable.setWidget(0, j, new Label(result.Fields.get(j)));
				}
				for (int i = 0; i < result.reccount(); i++) {
					try {
						// DebugUtils.D(result.get(i));
						for (int j = 0; j < result.Fields.size(); j++) {
							try {
								strTemp = result.get(i).get(result.Fields.get(j)).toString();
							} catch (Exception e) {
								strTemp = "";
							}

							flexTable.setText(i + 1, j, strTemp);
						}
					} catch (Exception e) {
						Window.alert(e.toString());
						Window.alert(strTemp);
					}
				}

				// format...header
				for (int j = 0; j < flexTable.getCellCount(0); j++) {
					flexTable.getFlexCellFormatter().setStyleName(0, j, "tab-header");
				}

				// // format...rest
				// for (int i = 1; i < flexTable.getRowCount(); i++) {
				// for (int j = 0; j < flexTable.getCellCount(i); j++) {
				// if ((j % 2) == 0)
				// flexTable.getFlexCellFormatter().setStyleName(i, j,
				// "FlexTable-Cell");
				// else
				// flexTable.getFlexCellFormatter().setStyleName(i, j,
				// "FlexTable-Cell");
				//
				// } // for j
				// } // for i

			} // success
		});

		/* FlexTable */

		pnlXLS.add(flexTable);

		/* button to export XLS */
		TableToExcelClient tableToExcelClient = new TableToExcelClient(flexTable, new Button("Export XLS"));
		pshbtnExportXLS = tableToExcelClient.build();

		initWidget(uiBinder.createAndBindUi(this));

	}
	
	
	@UiHandler("lnkPrint")
	void onprintBtnClick(ClickEvent event) {
		Element element = DOM.getElementById("printZone_1");
		Print.it("<link rel=\"StyleSheet\" type=text/css media=\"print\" href=\"" + GWT.getModuleBaseURL()+"../css/tables.css\" />", element);

	}
	

}
