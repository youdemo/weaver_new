package morningcore.util;

public class ChangeSelectValue{
    public String getRwzt(String para) {
        String str = "";
        if("0".equals(para)){
            str = "开始";
        }
        if("1".equals(para)){
            str = "进展中";
        }
        if("2".equals(para)){
            str = "延时";
        }
        if("3".equals(para)){
            str = "终止";
        }
        if("4".equals(para)){
            str = "完成";
        }
        return str;
    }
    public String getRwlx(String para) {
        String str = "";
        if("0".equals(para)){
            str = "长期任务";
        }
        if("1".equals(para)){
            str = "限时任务";
        }

        return str;
    }
    public String getSpzt(String para) {
        String str = "";
        if("0".equals(para)){
            str = "审批中";
        }
        if("1".equals(para)){
            str = "审批未通过";
        }
        if("2".equals(para)){
            str = "审批通过";
        }
        return str;
    }
}
