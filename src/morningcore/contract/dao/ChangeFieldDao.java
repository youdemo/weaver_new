package morningcore.contract.dao;


import morningcore.contract.bean.ChangeFieldBean;
import weaver.conn.RecordSet;
import weaver.general.Util;

/**
 * 获取项目字段列表
 * 
 * @author jianyong.tang 2018-01-16
 * 
 */
public class ChangeFieldDao {
	
	
	
	public ChangeFieldBean getChangeFieldBean(String id){
		RecordSet rs = new RecordSet();
		ChangeFieldBean pfb = new ChangeFieldBean();
		if("".equals(id)){
			return pfb;
		}
		String sql = "select * from uf_change_field_mt where id="+id;
		rs.executeSql(sql);
		if (rs.next()) { 
			
			pfb.setId(Util.null2String(rs.getString("id")));
			pfb.setTbname(Util.null2String(rs.getString("tbname")));
			pfb.setFieldname(Util.null2String(rs.getString("fieldname")));
			pfb.setShowname(Util.null2String(rs.getString("showname")));
			pfb.setFieldtype(Util.null2String(rs.getString("fieldtype")));
			pfb.setTexttype(Util.null2String(rs.getString("texttype")));
			pfb.setButtontype(Util.null2String(rs.getString("buttontype")));
			pfb.setTextlength(Util.null2String(rs.getString("textlength")));
			pfb.setFloatdigit(Util.null2String(rs.getString("floatdigit")));
			pfb.setZdyanbs(Util.null2String(rs.getString("zdyanbs")));
			pfb.setZdyanbm(Util.null2String(rs.getString("zdyanbm")));
			pfb.setZdyanzj(Util.null2String(rs.getString("zdyanzj")));
			pfb.setZdyanxszd(Util.null2String(rs.getString("zdyanxszd")));
		}
		return pfb;
	}
	
	
}
