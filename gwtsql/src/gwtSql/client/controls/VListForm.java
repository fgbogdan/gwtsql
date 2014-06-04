package gwtSql.client.controls;

import gwtSql.client.controls.BaseDialogBox;
import gwtSql.client.controls.IForm;
import gwtSql.shared.DBRecord;

import java.util.List;

import com.google.gwt.view.client.SingleSelectionModel;

public class VListForm extends BaseDialogBox {

	protected IForm caller_form;
	protected String caller_varName;
	protected SingleSelectionModel<DBRecord> selectionModel;

	protected List<DBRecord> list = null;
	protected DBRecord selected;

	/**
	 * select
	 */
	public void Select() {

		selected = selectionModel.getSelectedObject();
		// DebugUtils.D("try to send ...", 1);
		// DebugUtils.D(selected, 1);
		if (selected != null) {
			// selected
			if (caller_form != null) {
				// DebugUtils.D("send ...", 1);
				// DebugUtils.D(selected, 1);
				caller_form.onReturn(caller_varName, selected);
			}

			hide();
		}
	} // Select()

	/**
	 * call GWT remote
	 * 
	 */

}
