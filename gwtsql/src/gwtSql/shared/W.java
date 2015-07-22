package gwtSql.shared;

import com.google.gwt.user.client.Window;

public class W {

	public static void a(Object o) {
		if (o == null)
			Window.alert("NULL");
		else
			Window.alert(o.toString());
	}
}
