package goodbaby.gb.stock.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class StockManager {

	public boolean putStock(Stock sk){
		if(sk == null) return false;
		// 入库记录
		if(sk.getType() == 0){
			return intoStock(sk);
		}
		double nowNum = sk.getcNum();
		RecordSet rs = new RecordSet();
		
		// 防止死循环   标记
		int flag = 0;
		while(nowNum > 0 && flag < 100){
			flag++;
			// 以下处理为出库
			String sql = "select * from uf_Stock where rkly = 0 and rksl > isnull(ylysl,0) and wlmc = " + sk.getMaterielID() 
						+ " and CKID = " + sk.getCkid()
						+ " order by rkrq,rksj,id ";
			rs.executeSql(sql);
			if(rs.next()){
				String tmpID = Util.null2String(rs.getString("id"));
				sk.setfID(tmpID);
				// 获取目前的库存数
				double allNum = rs.getDouble("rksl");
				double useNum = rs.getDouble("ylysl");
				double unitPrice = rs.getDouble("rkjg");
				String gys = Util.null2String(rs.getString("GYS"));
				
				sk.setGys(gys);
				if(useNum < 0) useNum = 0;
				sk.setUnitPrice(unitPrice);
				// 剩余数量
				double otherNum = oper(allNum, useNum, 1);
				
				if(otherNum >= nowNum){
					sk.setcNum(nowNum);
					// 更新并且插入新的记录
					intoStock(sk);
					// 已出库数量为
					double nowStock = oper(useNum, nowNum, 0);
					
					sql = "update uf_Stock set ylysl = " + nowStock + " where id = " + tmpID;
					rs.executeSql(sql);
					nowNum = 0;
				}else{
					// 更新本记录库存,并且处理新的剩余数量
					nowNum = oper(nowNum, otherNum, 1);
					
					sk.setcNum(otherNum);
					intoStock(sk);
					sql = "update uf_Stock set ylysl = rksl where id = " + tmpID;
					rs.executeSql(sql);
				}
			}
		}
		if(flag > 99) return false;
		return true;
	}
	
	private double oper(double a,double b,int type){
		int res = 0;
		if(type == 0){
			res = ((int)(a*100) + (int)(b*100));
		}else{
			res = ((int)(a*100) - (int)(b*100));
		}
		return res/100.0;
	}
	
	
	// 入库为insert 插入操作
	private boolean intoStock(Stock sk){
		String sql = "insert into uf_Stock(lch,dtid,wlmc,poid,rkrq,rksj,CKID,GYS,rkjg,rksl,ylysl,rkly,glrk) values('"
			+ sk.getRequestID() + "','" + sk.getDtID() + "','" + sk.getMaterielID() + "','" + sk.getPoid()
			+ "','" + sk.getcDate() + "','" + sk.getcTime() + "','" + sk.getCkid() + "','" + sk.getGys()
			+ "'," + sk.getUnitPrice() + "," + sk.getcNum() + "," + sk.getfNum() + "," + sk.getType();
		if(sk.getfID() == null || sk.getfID().length() == 0){
			sql = sql + ",null)";
		}else{
			sql = sql + ",'" + sk.getfID() + "')";
		}
		RecordSet rs = new RecordSet();
		
		return rs.executeSql(sql);
	}
}
