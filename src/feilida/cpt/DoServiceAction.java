package feilida.cpt;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import feilida.cpt.ECologyServiceStub.ArrayOfBudgetVO;
import feilida.cpt.ECologyServiceStub.BudgetVO;
import feilida.cpt.ECologyServiceStub.QueryBudget;
import feilida.cpt.ECologyServiceStub.QueryBudgetResponse;


public class DoServiceAction {
	
	public String doService() throws Exception{
		ECologyServiceStub ess= new ECologyServiceStub();
		ArrayOfBudgetVO arr = new ECologyServiceStub.ArrayOfBudgetVO();
		BudgetVO bvo = new ECologyServiceStub.BudgetVO();
		String str="2010-5-27";
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date date =sdf.parse(str);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		bvo.setOADATE(calendar);//申请日期
		bvo.setKOSTL("");//成本中心
		bvo.setKSTAR("");//会计科目
		bvo.setGSBER("");//业务范围
		bvo.setCURRENCY("");//币种
		bvo.setAMOUNT(new BigDecimal("11"));//金额
		bvo.setEXECTYPE("");//报销类型
		arr.addBudgetVO(bvo);
		QueryBudget qb= new ECologyServiceStub.QueryBudget();
		qb.setBuget(arr);
		QueryBudgetResponse qbr = ess.QueryBudget(qb);				
		return qbr.getQueryBudgetResult().toString();
	}

}
