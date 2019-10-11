package jsd.ape.procedure;

import jsd.ape.fo.FoOrder;
import jsd.ape.gys.GysFile;
import jsd.ape.os.OsOrder;
import jsd.ape.po.PoOrder;
import jsd.ape.pr.PrOrder;
import jsd.ape.qu.QuOrder;
import jsd.ape.rc.Rcorder;
import jsd.ape.re.ReOrder;
import jsd.ape.so.SoOrder;
import weaver.general.BaseBean;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-6 下午5:05:20
 * 类说明
 */
public class UTrigger {
	BaseBean log = new BaseBean();
	/**
	 * 
	 * @param jsonstr 表单数据
	 * @param flag 流程标识
	 * @return
	 */
	public String TriggerService(String jsonstr,String flag){
		String retstr = "";
		if("APE-PR-001".equals(flag)){//请购单1
			PrOrder pr = new PrOrder();
			retstr = pr.triggerProcess5(jsonstr, flag);
		}else if("APE-PO-001".equals(flag)){//采购订单1
			PoOrder po = new PoOrder();
			retstr = po.triggerProcess4(jsonstr, flag);
		}else if("APE-GYS-001".equals(flag)){//供应商档案1
			GysFile gf = new GysFile();
			retstr = gf.triggerProcess2(jsonstr, flag);
		}else if("APE-QU-001".equals(flag)){//报价单
			QuOrder qo = new QuOrder();
			retstr = qo.triggerProcess6(jsonstr, flag);
		}else if("APE-SO-001".equals(flag)){//销售订单
			SoOrder so = new SoOrder();
			retstr = so.triggerProcess9(jsonstr, flag);
		}else if("APE-RE-001".equals(flag)){//退货处理单
			ReOrder ro = new ReOrder();
			retstr = ro.triggerProcess8(jsonstr, flag);
		}else if("APE-FO-001".equals(flag)){//预测订单
			FoOrder fo = new FoOrder();
			retstr = fo.triggerProcess1(jsonstr, flag);
		}else if("APE-OS-001".equals(flag)){//外协采购订单1
			OsOrder os = new OsOrder();
			retstr = os.triggerProcess3(jsonstr, flag);
		}else if("APE-RC-001".equals(flag)){//请款单1
			Rcorder rc = new Rcorder();
			retstr = rc.triggerProcess7(jsonstr, flag);
		}
		
		return retstr;
		
	}
	
	

}
