package com.jspcourse.cake_shop.servlet.cake;

import com.google.gson.JsonObject;
import com.jspcourse.cake_shop.bean.Cake;
import com.jspcourse.cake_shop.bean.Cart;
import com.jspcourse.cake_shop.bean.CartItem;
import com.jspcourse.cake_shop.dao.CakeDao;
import com.jspcourse.cake_shop.dao.impl.CakeDaoImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class CartServlet
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		case "add":
			addTOCart(request,response);
			break;
		case "changeIn":
			changeIn(request,response);//更改购物车商品数量
			break;
		case "delItem":
			delItem(request,response);
			break;
		case "delAll":
			delAll(request,response);
		}
	}


	private void delAll(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.getSession().removeAttribute("shopCart");
		response.sendRedirect("jsp/cake/cart.jsp");
		
	}

	//购物项删除
	private void delItem(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		int cakeId=Integer.parseInt(request.getParameter("id"));
		Cart shopCart = (Cart) request.getSession().getAttribute("shopCart");
		if(shopCart.getMap().containsKey(cakeId)) {
			shopCart.getMap().remove(cakeId);
		}
		response.sendRedirect("jsp/cake/cart.jsp");
	}

	//更改购物项数量
	private void changeIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		int cakeId=Integer.parseInt(request.getParameter("cakeId"));
		int quantity=Integer.parseInt(request.getParameter("quantity"));
		Cart shopCart = (Cart) request.getSession().getAttribute("shopCart");
		CartItem item = shopCart.getMap().get(cakeId);
		item.setQuantity(quantity);
		JsonObject json=new JsonObject();
		json.addProperty("subtotal", item.getSubtotal());
		json.addProperty("totPrice", shopCart.getTotPrice());
		json.addProperty("totQuan", shopCart.getTotQuan());
		json.addProperty("quantity", item.getQuantity());
		response.getWriter().print(json);
		
		
	}

	private void addTOCart(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String cakeId=request.getParameter("cakeId");
		CakeDao bd=new CakeDaoImpl();
		Cake cake = bd.findCakeById(Integer.parseInt(cakeId));
		
		Cart shopCart = (Cart) request.getSession().getAttribute("shopCart");
	
		if(shopCart==null) {
			shopCart=new Cart();
			request.getSession().setAttribute("shopCart", shopCart);
		}
		shopCart.addCake(cake);
		response.getWriter().print(shopCart.getTotQuan());//返回现在购物车实时数量
	}

}
 