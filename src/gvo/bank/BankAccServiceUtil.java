package gvo.bank;

/**
 * 资金对接银行账号创建通用方法
 * @author adore
 * @version 1.0  2017-09-18
 * 
 **/
public class BankAccServiceUtil {

    public String bankAccServiceMethod(String json) throws Exception{


        BankAccServiceWebServiceStub bswss = new BankAccServiceWebServiceStub();

        //BankAccServiceWebServiceStub.BankAccJKResponse bajkr = new BankAccServiceWebServiceStub.BankAccJKResponse();

        BankAccServiceWebServiceStub.BankAccJK bajk = new BankAccServiceWebServiceStub.BankAccJK();

        bajk.setJson(json);

        String result = bswss.bankAccJK(bajk).get_return();

        return result;

    }

    public static void main(String[] args){
        BankAccServiceUtil basu = new BankAccServiceUtil();
        String json = "{\"bean\":[{\"is_online\":\"1\",\"bank_name\":\"305305226036\",\"acc_type1\":\"07\",\"op_ression\":\"工资卡\",\"corp_code\":\"1200\",\"bankacc\":\"6217001700001121456\",\"acc_category\":\"01\",\"acc_state\":\"01\",\"acc_type\":\"01\",\"register_date\":\"2017-10-13\",\"cur\":\"CNY\",\"acc_name\":\"吴春燕\",\"bank_type\":\"4000\"}]}";
         String result = "";
        try {
            result = basu.bankAccServiceMethod(json);
            System.out.println("Result="+result);
        }catch (Exception e){
            System.out.println("ERROR");
        }
    }
}
