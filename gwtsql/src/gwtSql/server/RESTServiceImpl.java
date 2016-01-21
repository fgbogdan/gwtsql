package gwtSql.server;

import gwtSql.shared.DBTable;
import gwtSql.shared.DbManager;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RESTServiceImpl extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * Process the HTTP doGet request. and return a informations from the
	 * DataBase
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String strIniFileName = "";
		try {
			strIniFileName = request.getParameter("INI");
			DbManager.iniFileName = strIniFileName;

			response.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			out.println(request);
			out.close();

			return;
		} catch (Exception e) {
			e.printStackTrace();
		}

		String sqlCommand = "";
		try {
			sqlCommand = request.getParameter("SQL");

			DBTable T = new DBTable();
			DbManager.getDB().getDBTable(null, T, sqlCommand);

			response.setContentType(CONTENT_TYPE);
			PrintWriter out = response.getWriter();
			out.println(T.get(0));
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// response.setContentType(CONTENT_TYPE);
		// PrintWriter out = response.getWriter();
		// out.println("<html>");
		// out.println("<head><title>demolet</title></head>");
		// out.println("<body>");
		// out.println("<p>The servlet has received a GET. This is the
		// reply.</p>");
		// out.println(sqlCommand);
		// out.println("</body></html>");
		// out.close();

	}

	/**
	 * Process the HTTP doPost request.
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// String var0show = "";
		try {
			request.getParameter("showthis");
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>demolet</title></head>");
		out.println("<body>");
		out.println("<p>The servlet has received a POST. This is the reply.</p>");
		out.println("</body></html>");
		out.close();
	}

	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>demolet</title></head>");
		out.println("<body>");
		out.println("<p>The servlet has received a PUT. This is the reply.</p>");
		out.println("</body></html>");
		out.close();

	}

	public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<head><title>demolet</title></head>");
		out.println("<body>");
		out.println("<p>The servlet has received a DELETE. This is the reply.</p>");
		out.println("</body></html>");
		out.close();

	}

}
