package ego.peixun;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class TransUtil {
	public String getAttachUrl(String value) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String url = "";
		String fileName = "";
		String flag = "";
		String fileId = "";
		if (!"".equals(value)) {
			sql = "select b.imagefileid,b.imagefilename from docimagefile a,imagefile b where a.imagefileid=b.imagefileid and a.docid in ("
					+ value + ")";
			rs.execute(sql);
			while (rs.next()) {
				url = url + flag;
				fileName = Util.null2String(rs.getString("imagefilename"));
				fileId = Util.null2String(rs.getString("imagefileid"));
				url = url
						+ "<a target=\"_blank\" href=\"/weaver/weaver.file.FileDownload?fileid="
						+ fileId + "&coworkid=-1&requestid=0&desrequestid=0\">"
						+ fileName + "</a>";
				flag = " <br/>";
			}
		}
		return url;
	} 
}
