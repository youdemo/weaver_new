package gvo.fna.type;

import gvo.webservice.OutHeadInfo;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GvoFnaTypeService {

	/**
	 * 预算科目的操作方法
	 * @param code 预算编码
	 * @param name 预算的名称
	 * @param subjectCode 上级编码
	 * @return
	 */
	public OutHeadInfo fnaTypeOperation(String head,String code,String name,String subjectCode){
		RecordSet rs = new RecordSet();
		OutHeadInfo ohi = new OutHeadInfo();
		ohi.setHead(head);
		Map<String, String> retMap = new HashMap<String, String>();
		// 上级编码是否存在
		String sql = "select * from FnaBudgetfeeType where codeName = '" + subjectCode + "'";
		rs.executeSql(sql);
		int subjectid = 0;
		int subjectLevel = 0;
		if(rs.next()){
			subjectid = rs.getInt("id");
			subjectLevel = Util.getIntValue(rs.getString("feelevel"), -1);
		}
		if(subjectid < 1) {
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "父类科目编码不存在!");
			ohi.setOut(getJsonStr(retMap));
			return ohi;
		}
		if(subjectLevel < 1){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "父类科目的级别存在异常数据！");
			ohi.setOut(getJsonStr(retMap));
			return ohi;
		}
		// 当前科目是否存在
		int nowLevel = subjectLevel + 1;
		sql = "select * from FnaBudgetfeeType where codeName = '" + code + "'";
		rs.executeSql(sql);
		int isExit = 0;
		if(rs.next()){
			isExit = rs.getInt("id");
		}
		if(isExit > 0){
			// 存在记录就更新记录
			sql = "update FnaBudgetfeeType set name='" + name + "',description='"
					+ name + "',feelevel=" + nowLevel + ",supsubject=" + subjectid
					+ " where codeName = '" + code + "'";
			rs.executeSql(sql);
			retMap.put("MSG_TYPE", "S");
			retMap.put("MSG_CONTENT", "更新记录成功！");
		}else{
			// 不存在就插入数据
			sql = "insert into FnaBudgetfeeType(name,description,feelevel,"
					+"supsubject,codeName,feetype,feeperiod,Archive,groupCtrl,"
					+"isEditFeeType,budgetAutoMove) values('"+name+"',"
					+ "'" + name + "'," + nowLevel + "," + subjectid + ","
					+"'"+code+"',1,0,0,1,0,1)";
			rs.executeSql(sql);
			retMap.put("MSG_TYPE", "S");
			retMap.put("MSG_CONTENT", "插入记录成功！");
		}
		ohi.setOut(getJsonStr(retMap));
		return ohi;
	}
	private String getJsonStr(Map<String, String> map) {
		JSONObject json = new JSONObject();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			try {
				json.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return json.toString();
	}
}
