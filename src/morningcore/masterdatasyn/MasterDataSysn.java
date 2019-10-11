package morningcore.masterdatasyn;

import morningcore.sap.IV_Flag_AccountBapi;
import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class MasterDataSysn extends BaseCronJob{
    public void execute(){
        BaseBean log = new BaseBean();
        log.writeLog("进入科目主数据同步---------");
        IV_Flag_AccountBapi IV_Flag_AccountBapi = new IV_Flag_AccountBapi();
        IV_Flag_AccountBapi.getData();
    }
}
