package gvo.gvoks.kslc;
import gvo.gvoks.util.GetUtil;
import gvo.gvoks.util.InsertUtil;

import java.util.HashMap;
import java.util.Map;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-4-8 上午9:32:33
 * SVD-030-A-顾客抱怨处理流程-客诉流程-VXG  到达节点
 */
public class ArriveNodeTime implements Action{
	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		InsertUtil iu = new InsertUtil();
		String tablename = "uf_ksclsj";//客诉流程处理时间记录表
		GetUtil gu = new GetUtil();
		String st = gu.getNowdate();//到达时间
		Map<String ,String > map = new HashMap<String, String>();
		map.put("rid", requestid);
		map.put("lcddsj", st);
		map.put("yjcs", "0");
		map.put("lcjsbs", "0");//0 未结束  1 已结束
		iu.insert(map, tablename);
		return SUCCESS;
	}
}
