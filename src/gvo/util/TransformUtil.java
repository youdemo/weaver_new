package gvo.util;

/**
 * 选择框字段转换
 * @author daisy
 * @version 2.0  2017-10-31
 * 
 **/
public class TransformUtil {
	public String getPaytype(String json) {
        String paytype = "";
            if (json.equals("0")){
            	paytype = "34";
            }else if (json.equals("1")){
            	paytype = "62";
            }else if (json.equals("7")){
            	paytype = "49";
            }else if (json.equals("6")){
            	paytype = "63";
            }else if (json.equals("5")){
            	paytype = "11";
            }else if (json.equals("2")){
            	paytype = "02";
            }else if (json.equals("3")){
            	paytype = "03";
            }else if (json.equals("4")){
            	paytype = "04";
            }
       
        return paytype;
    }
	public String getPrepaytype(String type) {
        String prepaytype = "";
            if (type.equals("0")){
            	prepaytype = "A";
            }else if (type.equals("1")){
            	prepaytype = "J";
            }else if (type.equals("2")){
            	prepaytype = "K";
            }
       
        return prepaytype;
    }
	public String getLoantype(String loan) {
        String loantype = "";
            if (loan.equals("0")){
            	loantype = "H";
            }else if (loan.equals("1")){
            	loantype = "M";
            }
       
        return loantype;
    }
	public String getCurrencytype(String currency) {
        String currencytype = "";
            if (currency.equals("0")){
            	currencytype = "CNY";
            }else if (currency.equals("1")){
            	currencytype = "USD";
            }else if (currency.equals("2")){
            	currencytype = "ERU";
            }else if (currency.equals("3")){
            	currencytype = "JPY";
            }else if (currency.equals("4")){
            	currencytype = "HK";
            }
       
        return currencytype;
    }
	public String getAccounttype(String type) {
        String accounttype = "";
            if (type.equals("0")){
            	accounttype = "01";
            }else if (type.equals("1")){
            	accounttype = "02";
            }else if (type.equals("2")){
            	accounttype = "03";
            }
       
        return accounttype;
    }
	public String getAccountcategory(String category) {
		String accountcategory = "";
            if (category.equals("0")){
            	accountcategory = "01";
            }else if (category.equals("1")){
            	accountcategory = "02";
            }else if (category.equals("2")){
            	accountcategory = "03";
            }else if (category.equals("3")){
            	accountcategory = "04";
            }else if (category.equals("4")){
            	accountcategory = "05";
            }else if (category.equals("5")){
            	accountcategory = "06";
            }else if (category.equals("6")){
            	accountcategory = "07";
            }
       
        return accountcategory;
    }
}
