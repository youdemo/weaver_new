package zx.hr.sysn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.hrm.company.SubCompanyComInfo;
import zx.tmc.org.HrmSubCompanyBean;
import zx.tmc.org.ReturnInfo;

public class Test {

	public static void main(String[] args) throws Exception {
		System.out.println(Integer.valueOf(""));
//		JSONObject jo = new JSONObject();
//		jo.put("aa", "123");
//		System.out.println(jo.length());
		JSONObject json = new JSONObject("{\"msg\":\"\",\"type\":{},\"table\":{\"main\":[{\"MSG_TYPE\":\"S\",\"MSG_TEXT\":\"同步完成\"}],\"Detail\":[{\"dt\":{\"PLANS\":\"00000000\",\"DDTEXT\":\"Company\",\"SEQID\":\"000000001\",\"STEXT\":\"中芯集成电路制造(绍兴)有限公司\",\"OBJID_U\":\"00000000\",\"OBJID\":\"10000000\",\"HIERA\":\"0\",\"DFLAG\":\"\"}}]}}");
		JSONArray jsonArr = json.getJSONObject("table").getJSONArray("Detail");
		for (int index = 0; index < jsonArr.length(); index++) {
			JSONObject jsonx = (JSONObject) jsonArr.get(index);
			JSONObject jo = jsonx.getJSONObject("dt");
			String OBJID = jo.getString("OBJID");// 组织编码
			String STEXT = jo.getString("STEXT");// 组织名称
			String HIERA = jo.getString("HIERA");// 组织层级
			String DDTEXT = jo.getString("DDTEXT");// 层级描述
			String OBJID_U = jo.getString("OBJID_U");// 上级组织
			String PLANS = jo.getString("PLANS");// 领导职位
			String DFLAG = jo.getString("DFLAG");// 删除标识
			

		}
		

	}

}
