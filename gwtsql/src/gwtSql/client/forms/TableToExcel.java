package gwtSql.client.forms;

import gwtSql.shared.DBTable;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class TableToExcel extends VForm {

	interface MyUiBinder extends UiBinder<Widget, TableToExcel> {
	}

	private static MyUiBinder uiBinder = GWT.create(MyUiBinder.class);

	@UiField(provided = true) SimplePanel pnlXLS;

	@UiField(provided = true) FormPanel pshbtnExportXLS;
	@UiField(provided = true) Label lbTitle;

	public TableToExcel(DBTable p_T, String strTitle) {

		lbTitle = new Label(strTitle);

		pnlXLS = new SimplePanel();

		final FlexTable flexTable = new FlexTable();
		flexTable.setWidget(0, 0, new Label("Loading informations from database ... "));

		// try to construct the flex table
		flexTable.removeAllRows();

		String strTemp = "";
		// header row
		for (int j = 0; j < p_T.Fields.size(); j++) {
			flexTable.setWidget(0, j, new Label(p_T.Fields.get(j)));
		}
		for (int i = 0; i < p_T.reccount(); i++) {
			try {
				// DebugUtils.D(result.get(i));
				for (int j = 0; j < p_T.Fields.size(); j++) {
					try {
						strTemp = p_T.get(i).get(p_T.Fields.get(j)).toString();
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

		/* FlexTable */

		pnlXLS.add(flexTable);

		/* button to export XLS */
		TableToExcelClient tableToExcelClient = new TableToExcelClient(flexTable, new Button("Export XLS"));
		pshbtnExportXLS = tableToExcelClient.build();

		initWidget(uiBinder.createAndBindUi(this));

	}

}
