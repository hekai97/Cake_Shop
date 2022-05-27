package com.jspcourse.cake_shop.servlet.cake;

import com.jspcourse.cake_shop.dao.CakeDao;
import com.jspcourse.cake_shop.dao.impl.CakeDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet implementation class cakedetailed
 */
@WebServlet("/cakedetail")
public class cakedetailed extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String DETAIL_PATH="jsp/cake/cakedetails.jsp";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int cakeId = Integer.parseInt(request.getParameter("cakeId"));
		CakeDao bd = new CakeDaoImpl();
		HttpSession session=request.getSession();
		session.setAttribute("cakeInfo", bd.findCakeById(cakeId));
		//request.setAttribute("cakeInfo", bd.findCakeById(cakeId));
		request.getRequestDispatcher(DETAIL_PATH).forward(request, response);


	}

}
