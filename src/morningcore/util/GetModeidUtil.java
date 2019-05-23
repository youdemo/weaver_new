package morningcore.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class GetModeidUtil {
    /**
     * 获取表单建模id
     *
     * @param tableName
     * @return
     */
    public static String getModeId(String tableName) {
        RecordSet rs = new RecordSet();
        String formid = "";
        String modeid = "";
        String sql = "select id from workflow_bill where tablename='" + tableName + "'";
        rs.executeSql(sql);
        if (rs.next()) {
            formid = Util.null2String(rs.getString("id"));
        }
        sql = "select id from modeinfo where  formid=" + formid;
        rs.executeSql(sql);
        if (rs.next()) {
            modeid = Util.null2String(rs.getString("id"));
        }
        return modeid;
    }
    /**
     * 获取表单建模表名
     *
     * @param type
     * @return
     */
    public String getBillTableName(String type) {
        RecordSet rs = new RecordSet();
        String billTableName = "";
        String sql = "select billtablename from task_table_map where type='" + type + "'";
        rs.executeSql(sql);
        if (rs.next()) {
            billTableName = Util.null2String(rs.getString("billtablename"));
        }
        return billTableName;
    }

    /**
     * 获取流程id
     *
     * @param type
     * @return
     */
    public String getWfid(String type) {
        RecordSet rs = new RecordSet();
        String wfid = "";
        String sql = "select WORKFLOWID from task_table_map where type='" + type + "'";
        rs.executeSql(sql);
        if (rs.next()) {
            wfid = Util.null2String(rs.getString("WORKFLOWID"));
        }
        return wfid;
    }

    /**
     * 获取流程表名
     *
     * @param type
     * @return
     */
    public String getWfTableName(String type) {
        RecordSet rs = new RecordSet();
        String tablename = "";
        String wfid = "";
        String sql = "select WORKFLOWID from task_table_map where type='" + type + "'";
        rs.executeSql(sql);
        if (rs.next()) {
            wfid = Util.null2String(rs.getString("WORKFLOWID"));
        }
        sql = " Select tablename From Workflow_bill Where id in (" + " Select formid From workflow_base Where id= " + wfid + ")";
        rs.execute(sql);
        if (rs.next()) {
            tablename = Util.null2String(rs.getString("tablename"));
        }
        return tablename;
    }

}
