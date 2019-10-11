package jsd.erp.procedure;

import jsd.erp.methodbody.BomApply;
import jsd.erp.methodbody.ENBBomApply;
import jsd.erp.methodbody.ENVBomApply;
import jsd.erp.methodbody.EcnApply;
import jsd.erp.methodbody.Express;
import jsd.erp.methodbody.GeneralConsumption;
import jsd.erp.methodbody.PostGrade;
import jsd.erp.methodbody.WorkpieceDistribute;
import weaver.general.BaseBean;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-6 下午5:05:20
 * 二期  erp  自创接口触发
 */
public class ErpTrigger {
	BaseBean log = new BaseBean();
	/**
	 * 
	 * @param jsonstr 表单数据
	 * @param flag 流程标识
	 * @return
	 */
	public String triggerService(String jsonstr,String flag){
		String retstr = "";
		if("JST-ENB-001".equals(flag)){//加工件BOM申请
			BomApply tp = new  BomApply ();
			retstr = tp.triggerProcess(jsonstr, flag);
		}else if("JST-ENV-001".equals(flag)){//加工件验证需求
			ENVBomApply po = new ENVBomApply();
			retstr = po.triggerProcess(jsonstr, flag);
		}
		else if("JST-ENB-002".equals(flag)){//加工件需求-BOM需求
			ENBBomApply ea = new ENBBomApply();
			retstr = ea.triggerProcess(jsonstr, flag);
		}else if("JST-GEA-001".equals(flag)){//庶务类领料
			GeneralConsumption gc = new GeneralConsumption();
			retstr =gc.triggerProcess(jsonstr, flag);
		}else if("JST-WLI-001".equals(flag)){//上岗证等级
			PostGrade pg= new PostGrade();
			retstr =pg.triggerProcess(jsonstr, flag);
		}else if("JST-ECN-001".equals(flag)){//加工件ECN申请
			EcnApply ea = new EcnApply();
			retstr =ea.triggerProcess(jsonstr, flag);
		}else if("JST-ENP-001".equals(flag)){//加工件分发
			WorkpieceDistribute wd = new WorkpieceDistribute();
			retstr =wd.triggerProcess(jsonstr, flag);
			
		}else if("".equals(flag)){//快递
			Express ep = new Express();
			retstr =ep.triggerProcess(jsonstr, flag);
		}
		return retstr;
		
	}
	
	

}
