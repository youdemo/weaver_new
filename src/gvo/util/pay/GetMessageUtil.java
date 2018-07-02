package gvo.util.pay;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 付款类状态提示回写
 * @author adore
 * @version 1.0  2017-09-18
 * 
 **/
public class GetMessageUtil {

    public String getStatus(String json) {

        String status = "";
        try {
            JSONObject jsonDatas = new JSONObject(json);
            JSONArray jsonarray = jsonDatas.getJSONArray("datas");//接收JSON对象里的数组
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobj = jsonarray.getJSONObject(i);
                status = jsonobj.getString("status");
                System.out.println("status=" + status);
            }
        } catch (Exception e) {
            System.out.println("ERROR");
        }
        return status;
    }

    public String getMessage(String json) {
        String message = "";
        try {
            JSONObject jsonDatas = new JSONObject(json);
            JSONArray jsonarray = jsonDatas.getJSONArray("datas");//接收JSON对象里的数组
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobj = jsonarray.getJSONObject(i);
                message = jsonobj.getString("message");
            }
        } catch (Exception e) {
            System.out.println("ERROR");
        }
        return message;
    }

    public static void main(String args[]) {
    	
        String result = "{\"datas\":[{\"message\":\"总账科目不能为空!\",\"serial_no_sap\":\"V000520170501\",\"status\":\"F\"}]}";
        GetMessageUtil gmu = new GetMessageUtil();
        String status = gmu.getStatus(result);
        System.out.println("status=" + status);
        String message = gmu.getMessage(result);
        System.out.println("message=" + message);
    }
}
