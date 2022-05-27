package com.jspcourse.cake_shop.servlet.cake;

import com.jspcourse.cake_shop.bean.Catalog;
import com.jspcourse.cake_shop.dao.CakeDao;
import com.jspcourse.cake_shop.dao.CatalogDao;
import com.jspcourse.cake_shop.dao.impl.CakeDaoImpl;
import com.jspcourse.cake_shop.dao.impl.CatalogDaoImpl;
import net.sf.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Servlet implementation class GetCatalog
 */
@WebServlet("/GetCatalog")
public class GetCatalog extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/json"); 
		JSONObject json = new JSONObject();
		CatalogDao cd=new CatalogDaoImpl();
		CakeDao bd=new CakeDaoImpl();
		List<Catalog> catalog = cd.getCatalog();
		
		//这里返回查询每个分类的数量
		for(int i=0;i<catalog.size();i++) {
			Catalog c = catalog.get(i);
			long size=bd.cakeReadCount(c.getCatalogId());
			c.setCatalogSize(size);
		}
		json.put("catalog", catalog);
		response.getWriter().append(json.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
