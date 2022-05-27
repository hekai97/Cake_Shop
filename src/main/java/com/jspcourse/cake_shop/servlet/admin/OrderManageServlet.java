package com.jspcourse.cake_shop.servlet.admin;

import com.jspcourse.cake_shop.bean.Order;
import com.jspcourse.cake_shop.bean.OrderItem;
import com.jspcourse.cake_shop.bean.PageBean;
import com.jspcourse.cake_shop.dao.CakeDao;
import com.jspcourse.cake_shop.dao.OrderDao;
import com.jspcourse.cake_shop.dao.OrderItemDao;
import com.jspcourse.cake_shop.dao.UserDao;
import com.jspcourse.cake_shop.dao.impl.CakeDaoImpl;
import com.jspcourse.cake_shop.dao.impl.OrderDaoImpl;
import com.jspcourse.cake_shop.dao.impl.OrderItemDaoImpl;
import com.jspcourse.cake_shop.dao.impl.UserDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class OrderManageServlet
 */
@WebServlet("/jsp/admin/OrderManageServlet")
public class OrderManageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ORDERLIST_PATH="orderManage/orderlist.jsp";
	private static final String ORDERDETAIL_PATH="orderManage/orderDetail.jsp";
	private static final String ORDEROP_PATH="orderManage/orderOp.jsp";
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action=request.getParameter("action");
		switch(action) {
		case "list":
			orderList(request,response);
			break;
		case "detail":
			orderDetail(request,response);
			break;
		case "processing":
			orderProcessing(request,response);
		case "ship":
			orderShip(request,response);
		case "seach":
			seachOrder(request,response);
		case "seach1":
			seachOrder1(request,response);	
		case "delete":
			deleteOrder(request,response);	
		}
		
	}
	
	private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException  {
		String orderid=request.getParameter("id");
		OrderDao orderDao = new OrderDaoImpl();
		if(orderid != null && !orderid.equals("")) {
			orderDao.deleteOrderItem(Integer.parseInt(orderid));
			orderDao.deleteOrder(Integer.parseInt(orderid));
		}
		orderList(request, response);
	}

	private void seachOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		int maxSize = Integer.parseInt(request.getServletContext().getInitParameter("maxPageSize"));
		String ordername = request.getParameter("ordername");
		OrderDao orderDao = new OrderDaoImpl();
		PageBean pb = null;
		if(ordername != null && ordername != "") {
			pb = new PageBean(curPage, maxSize, orderDao.orderReadCount(ordername));
			request.setAttribute("orderList", orderDao.orderList(pb,ordername));
		}else {
			pb = new PageBean(curPage, maxSize, orderDao.orderReadCount());
			request.setAttribute("orderList", orderDao.orderList(pb));
		}
		
		request.setAttribute("pageBean", pb);
		request.getRequestDispatcher(ORDERLIST_PATH).forward(request, response);
	}
	
	private void seachOrder1(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		int maxSize = Integer.parseInt(request.getServletContext().getInitParameter("maxPageSize"));
		String ordername = request.getParameter("ordername");
		OrderDao orderDao = new OrderDaoImpl();
		PageBean pb = null;
		if(ordername != null && ordername != "") {
			pb = new PageBean(curPage, maxSize, orderDao.orderReadCountByStatus(1,ordername));
			request.setAttribute("orderList", orderDao.orderListByStatus(pb,1,ordername));
		}else {
			pb = new PageBean(curPage, maxSize, orderDao.orderReadCountByStatus(1));
			request.setAttribute("orderList", orderDao.orderListByStatus(pb,1));
		}
		
		request.setAttribute("pageBean", pb);
		request.getRequestDispatcher(ORDEROP_PATH).forward(request, response);
	}

	private void orderShip(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String orderId=request.getParameter("id");
		OrderDao orderDao=new OrderDaoImpl();
		
		if(orderDao.orderStatus(Integer.parseInt(orderId),2)) {
			request.setAttribute("orderMessage", "一个订单操作成功");
		}else {
			request.setAttribute("orderMessage", "一个订单操作失败");
		}
		orderProcessing(request,response);
		
		
	}

	private void orderProcessing(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		int maxSize = Integer.parseInt(request.getServletContext().getInitParameter("maxPageSize"));
		OrderDao orderDao = new OrderDaoImpl();
		PageBean pb = new PageBean(curPage, maxSize, orderDao.orderReadCountByStatus(1));
		
		request.setAttribute("pageBean", pb);
		request.setAttribute("orderList", orderDao.orderListByStatus(pb,1));
		request.getRequestDispatcher(ORDEROP_PATH).forward(request, response);
		
		
		
	}

	private void orderDetail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int orderId=Integer.parseInt(request.getParameter("id"));
		OrderDao orderDao=new OrderDaoImpl();
		OrderItemDao oItemDao=new OrderItemDaoImpl();
		UserDao userDao=new UserDaoImpl();
		CakeDao cakeDao=new CakeDaoImpl();
		
		Order order = orderDao.findOrderByOrderId(orderId);
		order.setUser(userDao.findUser(order.getUserId()));
		order.setoItem(oItemDao.findItemByOrderId(order.getOrderId()));
		for ( OrderItem oItem : order.getoItem()) {
			//通过图书id获取图书对象
			oItem.setCake(cakeDao.findCakeById(oItem.getCakeId()));
		}
		request.setAttribute("order", order);
		request.getRequestDispatcher(ORDERDETAIL_PATH).forward(request, response);
		
		
	}

	private void orderList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int curPage = 1;
		String page = request.getParameter("page");
		if (page != null) {
			curPage = Integer.parseInt(page);
		}
		int maxSize = Integer.parseInt(request.getServletContext().getInitParameter("maxPageSize"));
		OrderDao orderDao = new OrderDaoImpl();
		PageBean pb = new PageBean(curPage, maxSize, orderDao.orderReadCount());
		
		request.setAttribute("pageBean", pb);
		request.setAttribute("orderList", orderDao.orderList(pb));
		request.getRequestDispatcher(ORDERLIST_PATH).forward(request, response);
		
	}

}
