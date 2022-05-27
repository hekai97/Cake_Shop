package com.jspcourse.cake_shop.dao.impl;

import com.jspcourse.cake_shop.bean.Catalog;
import com.jspcourse.cake_shop.bean.PageBean;
import com.jspcourse.cake_shop.dao.CatalogDao;
import com.jspcourse.cake_shop.util.DbUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogDaoImpl implements CatalogDao {

	/**
	 * 获取商品分类信息
	 */
	@Override
	public List<Catalog> catalogList(PageBean pb) {
		List<Catalog> list=new ArrayList<Catalog>();
		String sql = "select * from s_catalog limit ?,?";
		// 查询的分页结果集
		List<Map<String, Object>> lm = DbUtil.executeQuery(sql, (pb.getCurPage() - 1) * pb.getMaxSize(),
				pb.getMaxSize());
		if(lm.size()>0){
			for(Map<String,Object> map:lm){
			Catalog catalog=new Catalog(map);
			list.add(catalog);
			}
		}	
		return list; 
	}

	@Override
	public long catalogReadCount() {
		long count=0;
		String sql="select count(*) as count from s_catalog";
		List<Map<String, Object>> lm=DbUtil.executeQuery(sql);
		if(lm.size()>0){
			count=(long) lm.get(0).get("count");
		}
		return count;
	}

	@Override
	public List<Catalog> getCatalog() {
		List<Catalog> list=new ArrayList<Catalog>();
		String sql="select * from s_catalog";
		
		List<Map<String,Object>> lmso=DbUtil.executeQuery(sql);
		if(lmso.size()>0){
			for(Map<String,Object> map:lmso){
			Catalog catalog=new Catalog(map);
			list.add(catalog);
			}
		}	
		return list; 
	}

	@Override
	public boolean catalogDel(int catalogId) {
		String sql = "delete from s_catalog where catalogId=?";
		int i = DbUtil.excuteUpdate(sql, catalogId);
		return i > 0 ? true : false;
	}

	@Override
	public boolean catalogBatDelById(String ids) {
		String sql="delete from s_catalog where catalogId in("+ids+")";
		int i=DbUtil.excuteUpdate(sql);
		return i>0?true:false;
	}

	@Override
	public boolean findCatalogByCatalogName(String catalogName) {
		String sql = "select * from s_catalog where catalogName=?";
		List<Map<String, Object>> list = DbUtil.executeQuery(sql, catalogName);
		return list.size() > 0 ? true : false;
	}

	@Override
	public boolean catalogAdd(String catalogName) {
		String sql="insert into s_catalog(catalogName) values(?)";
		int i = DbUtil.excuteUpdate(sql, catalogName);
		return i > 0 ? true : false;
	}


}
