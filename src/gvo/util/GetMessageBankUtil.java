package gvo.util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 银行账号创建状态提示回写
 * @author adore
 * @version 2.0  2017-10-17
 * 
 **/
public class GetMessageBankUtil {
    public String getStatus(String json) {
        String status = "";
        try {
            JSONArray jsonarray = new JSONArray(json);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobj = jsonarray.getJSONObject(i);
                status = jsonobj.getString("status");
            }
        } catch (Exception e) {
            System.out.println("ERROR");
        }
        return status;
    }

    public String getMessage(String json) {
        String message = "";
        try {
            JSONArray jsonarray = new JSONArray(json);
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
        String result = "[{\"corp_code\":\"2000\",\"bankacc\":\"1244\",\"status\":\"F\",\"message\":\"联行号在系统中未维护！\"}]";
        GetMessageBankUtil gmu = new GetMessageBankUtil();
        String status = gmu.getStatus(result);
        System.out.println("status=" + status);
        String message = gmu.getMessage(result);
        System.out.println("message=" + message);
    }
}
