package gvo.doc;

import org.json.JSONObject;

public class Test {

	public static void main(String[] args) throws Exception {
		JSONObject jo = new JSONObject();
		jo.put("apply_name", "HR负责人工号");
		jo.put("apply_date", "系统时间");
		jo.put("name", "应聘者姓名");
		jo.put("work_place", "工作城市");
		jo.put("grade", "职等");
		jo.put("postion", "职位代码");
		jo.put("superior", "直接上级工号");
		jo.put("zdr", "指导人工号");
		jo.put("msgpj", "面试官评价link");
		System.out.println(jo.toString());
	}

}
