package goodbaby.material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 询价最大值和最小值更新
 * @author 张瑞坤
 *
 */
public class XjDx implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		List<String > list =new ArrayList<String>(); 
		String sql = "select * from formtable_main_222 where aid = '"+requestid+"' and 	JG is not null";
		rs.executeSql(sql);
		while(rs.next()){
			String jg =rs.getString("JG");	
			list.add(jg);
		}
		int size = list.size();
		if(size>0){
			float JG[] = new float[size];
			for(int i=0;i<size;i++){
				float aac = Float.parseFloat(list.get(i));
				JG[i]=aac;
			}
			Arrays.sort(JG);
			log.writeLog("JG[0]-----"+JG[0]);
			log.writeLog("JG[size-1]-----"+JG[size-1]);
			float min = JG[0];
			float max = JG[size-1];
			sql = "update formtable_main_201 set ZDJ='"+min+"' ,ZGJ='"+max+"' where requestid ='"+requestid+"'";
			log.writeLog("sql-----"+sql);
			rs.executeSql(sql);
		}
		
		
		
		return SUCCESS;
	}

}
