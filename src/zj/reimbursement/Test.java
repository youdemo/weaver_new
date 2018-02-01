package zj.reimbursement;

public class Test {
	public static void main(String[] args){
	
		int money=0;
		int days=3;
		String ccsj="12:00";
		String jssj="07:00";
		if(days == 0){
			money=money+getDayMoney(ccsj,jssj);
		}else if(days == 1){
			money=money+getDayMoney(ccsj,"24:00");
			money=money+getDayMoney("00:00",jssj);
		}else if(days > 1){
			money=money+getDayMoney(ccsj,"24:00");
			money=money+(days-1)*65;
			money=money+getDayMoney("00:00",jssj);
		}
		System.out.println(money);
	}
	
	public static int getDayMoney(String ccsj,String jssj){
		int money=0;
		if(ccsj.compareTo("08:00")<=0){
			if(jssj.compareTo("08:00")>=0){
				money=money+15;
				if(jssj.compareTo("13:00")>=0){
					money=money+25;
					if(jssj.compareTo("18:00")>=0){
						money=money+25;
					}
				}
			}
		}else if(ccsj.compareTo("13:00")<=0){
			if(jssj.compareTo("13:00")>=0){
				money=money+25;
				if(jssj.compareTo("18:00")>=0){
					money=money+25;
				}
			}
		}else if(ccsj.compareTo("18:00")<=0){
			if(jssj.compareTo("18:00")>=0){
				money=money+25;
			}
		}
		return money;
	}
}
