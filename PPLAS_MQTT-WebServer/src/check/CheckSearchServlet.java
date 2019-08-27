package check;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/checkSearchServlet")
public class CheckSearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html);charset=UTF-8");
		String acID = request.getParameter("acID");
		System.out.println("데이터 가져오는중");
		if(acID == null || acID.equals("")) {
			response.getWriter().write("0");
		} else {
			String pulse = new CheckDAO().getMaxCheck(acID).getPulse();
			String temp = new CheckDAO().getMaxCheck(acID).getTemp();
			response.getWriter().write(String.format("{\"pulse\": %s, \"temp\": %s}", pulse, temp));
		}
	}

}
