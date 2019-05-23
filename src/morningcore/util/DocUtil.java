package morningcore.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class DocUtil{
    /**
     * 获取表单建模id
     *
     * @param id
     * @return
     */
    public String getMuliDocName(String id){
        id = Util.null2String(id);
        RecordSet rs = new RecordSet();
        String imagefile = "";
        String fjid = "";
        String link = "";
        String versionid = "";
        String sql = "select * from V_docimage where docid = " + id;
        rs.execute(sql);
        while(rs.next()){
            fjid = Util.null2String(rs.getString("imagefileid"));
            imagefile = Util.null2String(rs.getString("imagefilename"));
            versionid = Util.null2String(rs.getString("versionid"));
            if(fjid.equals("")){
                return "";
            }else{
                link = link + "<a href='/docs/docs/DocDspExt.jsp?id=" + id + "&meetingid=0&votingId=0&versionId=" +
                        versionid + "&imagefileId=" + fjid + "&from=&userCategory=0&isFromAccessory=true&fromDocCheckOutStatus=' " +
                        "target" + "='_blank'>" + imagefile + "</a></br>";
            }
        }
        return link;
    }

}
