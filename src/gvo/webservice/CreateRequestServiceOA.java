package gvo.webservice;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.service.invoker.AbstractInvoker;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.general.BaseBean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CreateRequestServiceOA extends BaseBean {
    public OutHeadInfo CreateMaterialApproval(String head, String workcode, String dataInfo) {
        BaseBean log = new BaseBean();
        log.writeLog("CreateMaterialApproval workcode:" + workcode + " dataInfo:" + dataInfo);
        if ("".equals(workcode) || "".equals(dataInfo)) {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "人员编号无法匹配");
            retMap.put("OA_ID", "0");
            log.writeLog("CreateMaterialApproval result:" + getJsonStr(retMap));
            OutHeadInfo ohi = new OutHeadInfo();
            ohi.setOut(getJsonStr(retMap));
            ohi.setHead(head);
            return ohi;
            //return getJsonStr(retMap);
        }
        CreateRequestServiceOAImpl crso = new CreateRequestServiceOAImpl();
        String result = crso.doserviceMaterial(workcode, dataInfo);
        log.writeLog("CreateMaterialApproval result:" + result);
        OutHeadInfo ohi = new OutHeadInfo();
        ohi.setOut(result);
        ohi.setHead(head);
        return ohi;
//        return result;
    }

    public OutHeadInfo CreateSupplierUpdate(String head, String workcode, String dataInfo) {
        BaseBean log = new BaseBean();
        log.writeLog("CreateSupplierUpdate workcode:" + workcode + " dataInfo:" + dataInfo);
        if ("".equals(workcode) || "".equals(dataInfo)) {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "人员编号无法匹配");
            retMap.put("OA_ID", "0");
            log.writeLog("CreateSupplierUpdate result:" + getJsonStr(retMap));
            OutHeadInfo ohi = new OutHeadInfo();
            ohi.setHead(head);
            ohi.setOut(getJsonStr(retMap));
            return ohi;
//            return getJsonStr(retMap);
        }
        CreateRequestServiceOAImpl crso = new CreateRequestServiceOAImpl();
        String result = crso.doserviceSupplier(workcode, dataInfo);
        log.writeLog("CreateSupplierUpdate result:" + result);
        OutHeadInfo ohi = new OutHeadInfo();
        ohi.setHead(head);
        ohi.setOut(result);
        return ohi;
//        return result;
    }

    public String CreateHR015Service(String workcode, String dataInfo) {
        BaseBean log = new BaseBean();
        log.writeLog("CreateHR015Service workcode:" + workcode + " dataInfo:"
                + dataInfo);
        if ("".equals(workcode) || "".equals(dataInfo)) {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "人员编号无法匹配");
            retMap.put("OA_ID", "0");
            log.writeLog("CreateHR015Service result:" + getJsonStr(retMap));
            return getJsonStr(retMap);
        }
        CreateRequestServiceOAImpl crso = new CreateRequestServiceOAImpl();
        String result = crso.doserviceHR015(workcode, dataInfo);
        log.writeLog("CreateHR015Service result:" + result);
        return result;
    }

    public String CreateEmployApproval(String dataInfo) {
        BaseBean log = new BaseBean();
        log.writeLog("CreateEmployApproval dataInfo:" + dataInfo);
        if ("".equals(dataInfo)) {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "参数不能为空");
            retMap.put("OA_ID", "0");
            log.writeLog("CreateEmployApproval result:" + getJsonStr(retMap));
            return getJsonStr(retMap);
        }
        MessageContext ctx = AbstractInvoker.getContext();
        CreateRequestServiceOAImpl crso = new CreateRequestServiceOAImpl();
        String result = null;
        try {
            result = crso.doserviceEmploy(dataInfo, ctx);
        } catch (JSONException e) {
            e.printStackTrace();
            log.writeLog(e.getMessage());
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "json解析异常");
            retMap.put("OA_ID", "0");
            log.writeLog("CreateEmployApproval result:" + getJsonStr(retMap));
            return getJsonStr(retMap);
        }
        log.writeLog("CreateEmployApproval result:" + result);
        return result;
    }

    public String CreateDocPermiss(String dataInfo) throws JSONException {
        BaseBean log = new BaseBean();
        log.writeLog("CreateDocPermiss dataInfo:" + dataInfo);
        if ("".equals(dataInfo)) {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "参数不能为空");
            retMap.put("OA_ID", "0");
            log.writeLog("CreateDocPermiss result:" + getJsonStr(retMap));
            return getJsonStr(retMap);
        }
        CreateRequestServiceOAImpl crso = new CreateRequestServiceOAImpl();
        String result = crso.doserviceDocLimit(dataInfo);
        log.writeLog("CreateDocPermiss result:" + result);
        return result;
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
