package feilida.finance;

import feilida.util.WebApi;

public class FinanceUtil {

	// http://172.20.70.216:6099/zh-CN/api/QueryBudget  
	public String queryBudget(String param){
		WebApi wb = new WebApi();		
		String code = "QueryBudgetCode";
		return wb.getPostConn(code, param);
	}
	//http://172.20.70.216:6099/zh-CN/api/UpdateBudget
	public String updateBudget(String param){
		WebApi wa = new WebApi();
		String code = "BudgetVOCode";
		return wa.getPostConn(code, param);
	}
}
