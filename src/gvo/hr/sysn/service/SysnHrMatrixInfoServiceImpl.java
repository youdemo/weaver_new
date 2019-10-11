package gvo.hr.sysn.service;

import org.json.JSONArray;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class SysnHrMatrixInfoServiceImpl {

	/**
	 * 同步矩阵信息
	 * 
	 * @param jsonString
	 * @throws Exception
	 */

	public String SysMatrixInfo(String jsonString, JSONArray resultArr) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		JSONArray ja = json.getJSONArray("apprInfo");
		RecordSet rs = new RecordSet();
		String deptId = "";
		String deptAppr1 = "";//部门经理
		String emplRcd1 = "";
		String deptAppr2 = "";
		String emplRcd2 = "";
		String centAppr1 = "";//中心总经理/总监
		String emplRcd3 = "";
		String centAppr2 = "";
		String emplRcd4 = "";
		String centLeader1 = "";//副总裁
		String emplRcd5 = "";
		String centLeader2 = "";
		String emplRcd6 = "";
		String comAppr1 = "";//总裁
		String emplRcd7 = "";
		String comAppr2 = "";
		String emplRcd8 = "";
		String grpAppr = "";//集团审批人
		String emplRcd9 = "";
		String hrbp1 = "";//HRBP
		String emplRcd10 = "";
		String hrbp2 = "";
		String emplRcd11 = "";
		String hrbp3 = "";
		String emplRcd12 = "";
		String hrbp4 = "";//HRBP负责人
		String emplRcd13 = "";
		String hrbp5 = "";
		String emplRcd14 = "";
		String sign = "S";
		String sql = "";
		String depid = "";
		String subid = "";
		for (int i = 0; i < ja.length(); i++) {
			depid = "";
			subid = "";
			JSONObject jo = ja.getJSONObject(i);
			JSONObject resutlJo = new JSONObject();
			deptId = Util.null2String(jo.getString("deptId"));
			deptAppr1 = Util.null2String(jo.getString("deptAppr1"));
			emplRcd1 = Util.null2String(jo.getString("emplRcd1"));
			deptAppr2 = Util.null2String(jo.getString("deptAppr2"));
			emplRcd2 = Util.null2String(jo.getString("emplRcd2"));
			centAppr1 = Util.null2String(jo.getString("centAppr1"));
			emplRcd3 = Util.null2String(jo.getString("emplRcd3"));
			centAppr2 = Util.null2String(jo.getString("centAppr2"));
			emplRcd4 = Util.null2String(jo.getString("emplRcd4"));
			centLeader1 = Util.null2String(jo.getString("centLeader1"));
			emplRcd5 = Util.null2String(jo.getString("emplRcd5"));
			centLeader2 = Util.null2String(jo.getString("centLeader2"));
			emplRcd6 = Util.null2String(jo.getString("emplRcd6"));
			comAppr1 = Util.null2String(jo.getString("comAppr1"));
			emplRcd7 = Util.null2String(jo.getString("emplRcd7"));
			comAppr2 = Util.null2String(jo.getString("comAppr2"));
			emplRcd8 = Util.null2String(jo.getString("emplRcd8"));
			grpAppr = Util.null2String(jo.getString("grpAppr"));
			emplRcd9 = Util.null2String(jo.getString("emplRcd9"));
			hrbp1 = Util.null2String(jo.getString("hrbp1"));
			emplRcd10 = Util.null2String(jo.getString("emplRcd10"));
			hrbp2 = Util.null2String(jo.getString("hrbp2"));
			emplRcd11 = Util.null2String(jo.getString("emplRcd11"));
			hrbp3 = Util.null2String(jo.getString("hrbp3"));
			emplRcd12 = Util.null2String(jo.getString("emplRcd12"));
			hrbp4 = Util.null2String(jo.getString("hrbp4"));
			emplRcd13 = Util.null2String(jo.getString("emplRcd13"));
			hrbp5 = Util.null2String(jo.getString("hrbp5"));
			emplRcd14 = Util.null2String(jo.getString("emplRcd14"));
			
			sql = "select id,subcompanyid1 from hrmdepartment where departmentcode='" + deptId + "'";
			rs.execute(sql);
			if (rs.next()) {
				depid = Util.null2String(rs.getString("id"));
				subid = Util.null2String(rs.getString("subcompanyid1"));
			}
			if ("".equals(depid)) {
				sign = "E";
				resutlJo = getResultJo(deptId, "", "", "E", "部门不存在");
				resultArr.put(resutlJo);
				continue;
			}
			if ("".equals(subid)) {
				sign = "E";
				resutlJo = getResultJo(deptId, "", "", "E", "部门所属公司不存在");
				resultArr.put(resutlJo);
				continue;
			}
			String bmjl = "";
			if(!"".equals(deptAppr1)) {
				String ryid = getRyid(deptAppr1, emplRcd1);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "deptAppr1:"+deptAppr1+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(bmjl)) {
						bmjl = ryid;
					}else {
						bmjl = bmjl + "," + ryid;
					}
				}
			}
			if(!"".equals(deptAppr2)) {
				String ryid = getRyid(deptAppr2, emplRcd2);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "deptAppr2:"+deptAppr2+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(bmjl)) {
						bmjl = ryid;
					}else {
						bmjl = bmjl + "," + ryid;
					}
				}
			}
			String zxzjl = "";
			if(!"".equals(centAppr1)) {
				String ryid = getRyid(centAppr1, emplRcd3);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "centAppr1:"+centAppr1+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(zxzjl)) {
						zxzjl = ryid;
					}else {
						zxzjl = zxzjl + "," + ryid;
					}
				}
			}
			if(!"".equals(centAppr2)) {
				String ryid = getRyid(centAppr2, emplRcd4);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "centAppr2:"+centAppr2+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(zxzjl)) {
						zxzjl = ryid;
					}else {
						zxzjl = zxzjl + "," + ryid;
					}
				}
			}
			String fzc = "";
			if(!"".equals(centLeader1)) {
				String ryid = getRyid(centLeader1, emplRcd5);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "centLeader1:"+centLeader1+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(fzc)) {
						fzc = ryid;
					}else {
						fzc = fzc + "," + ryid;
					}
				}
			}
			if(!"".equals(centLeader2)) {
				String ryid = getRyid(centLeader2, emplRcd6);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "centLeader2:"+centLeader2+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(fzc)) {
						fzc = ryid;
					}else {
						fzc = fzc + "," + ryid;
					}
				}
			}
			String zc = "";
			if(!"".equals(comAppr1)) {
				String ryid = getRyid(comAppr1, emplRcd7);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "comAppr1:"+comAppr1+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(zc)) {
						zc = ryid;
					}else {
						zc = zc + "," + ryid;
					}
				}
			}
			if(!"".equals(comAppr2)) {
				String ryid = getRyid(comAppr2, emplRcd8);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "comAppr2:"+comAppr2+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(zc)) {
						zc = ryid;
					}else {
						zc = zc + "," + ryid;
					}
				}
			}
			String jtspr = "";
			if(!"".equals(grpAppr)) {
				String ryid = getRyid(grpAppr, emplRcd9);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "grpAppr:"+grpAppr+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(jtspr)) {
						jtspr = ryid;
					}else {
						jtspr = jtspr + "," + ryid;
					}
				}
			}
			String hrbp = "";		
			if(!"".equals(hrbp1)) {
				String ryid = getRyid(hrbp1, emplRcd10);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "hrbp1:"+hrbp1+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(hrbp)) {
						hrbp = ryid;
					}else {
						hrbp = hrbp + "," + ryid;
					}
				}
			}
			if(!"".equals(hrbp2)) {
				String ryid = getRyid(hrbp2, emplRcd11);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "hrbp2:"+hrbp2+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(hrbp)) {
						hrbp = ryid;
					}else {
						hrbp = hrbp + "," + ryid;
					}
				}
			}
			if(!"".equals(hrbp3)) {
				String ryid = getRyid(hrbp3, emplRcd12);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "hrbp3:"+hrbp3+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(hrbp)) {
						hrbp = ryid;
					}else {
						hrbp = hrbp + "," + ryid;
					}
				}
			}
			String hrbpfzr = "";
			if(!"".equals(hrbp4)) {
				String ryid = getRyid(hrbp4, emplRcd13);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "hrbp4:"+hrbp4+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(hrbpfzr)) {
						hrbpfzr = ryid;
					}else {
						hrbpfzr = hrbpfzr + "," + ryid;
					}
				}
			}
			if(!"".equals(hrbp5)) {
				String ryid = getRyid(hrbp5, emplRcd14);
				if("".equals(ryid)) {
					sign = "E";
					resutlJo = getResultJo(deptId, "", "", "E", "hrbp5:"+hrbp5+"人员不存在");
					resultArr.put(resutlJo);
					continue;
				}else {
					if("".equals(hrbpfzr)) {
						hrbpfzr = ryid;
					}else {
						hrbpfzr = hrbpfzr + "," + ryid;
					}
				}
			}
			String result = insertOrUpdateMatrix(subid,depid,bmjl,zxzjl,fzc,zc,jtspr,hrbp,hrbpfzr);
			if ("-1".equals(result)) {
				sign = "E";
				resutlJo = getResultJo(deptId, "", "", "E", "统一矩阵表不存在");
				resultArr.put(resutlJo);
				continue;
			} else if ("-2".equals(result)) {
				sign = "E";
				resutlJo = getResultJo(deptId, "", "", "E", "更新sql失败");
				resultArr.put(resutlJo);
				continue;
			}else {
				resutlJo = getResultJo(deptId, "", "", "S", "成功");
				resultArr.put(resutlJo);
			}
			
		}
		return sign;
	}

	public String insertOrUpdateMatrix(String subid, String depid, String bmjl, String zxzjl, String fzc, String zc,
			String jtspr, String hrbp, String hrbpjl) {
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		String sql = "";
		String matrixTable = "";
		String Dataorder = "0";
		String mtid = "";// 矩阵数据id
		sql = "select jzbm from uf_compMatrix_map where gs='" + subid + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			matrixTable = Util.null2String(rs.getString("jzbm"));
		}
		if("".equals(matrixTable)) {
			sql = "select jzbm from uf_compMatrix_map where isall='0'";
			rs.executeSql(sql);
			if (rs.next()) {
				matrixTable = Util.null2String(rs.getString("jzbm"));
			}
		}
		
		if ("".equals(matrixTable)) {
			return "-1";
		}
		sql = "select nvl(max(dataorder),0)+1 as Dataorder from " + matrixTable;
		rs.executeSql(sql);
		if (rs.next()) {
			Dataorder = Util.null2String(rs.getString("Dataorder"));
		}
		sql = "select uuid from " + matrixTable + " where fb='" + subid + "' and bm='" + depid + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			mtid = Util.null2String(rs.getString("uuid"));
		}
		if ("".equals(mtid)) {
			sql = "insert into " + matrixTable + "(uuid,Dataorder,fb,bm,bmjl,zxzjl,fzc,zc,jtspr,hrbp,hrbpjl)" + " values(sys_guid(),"
					+ Dataorder + ",'" + subid + "','" + depid + "','" + bmjl + "','" + zxzjl + "'," + "'" + fzc + "','"
					+ zc + "','" + jtspr + "','" + hrbp + "','" + hrbpjl + "')";
			boolean result = rs.executeSql(sql);
			if (!result) {
				log.writeLog("insertOrUpdateMatrix sql:" + sql);
				return "-2";
			}
		} else {
			sql = "update " + matrixTable + " set bmjl='" + bmjl + "',zxzjl='" + zxzjl + "',fzc='" + fzc
					+ "',zc='" + zc + "',jtspr='" + jtspr + "',hrbp='" + hrbp + "',hrbpjl='" + hrbpjl + "' where uuid='" + mtid + "'";
			boolean result = rs.executeSql(sql);
			if (!result) {
				log.writeLog("insertOrUpdateMatrix sql:" + sql);
				return "-2";
			}
		}
		return "1";
	}

	public JSONObject getResultJo(String key1, String key2, String lastupdttm, String flag, String comments) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("key1", key1);
			jo.put("key2", key2);
			jo.put("lastupdttm", lastupdttm);
			jo.put("flag", flag);
			jo.put("Comments", comments);
		} catch (Exception e) {
			jo = null;
		}
		return jo;
	}

	/**
	 * 获取人力id
	 * @param workcodes
	 * @return
	 */
	public String getRyid(String repordsTold,String repordsToRcd) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String ryid = "";
		if("".equals(repordsTold)) {
			return "";
		}
		if("".equals(repordsToRcd)||"0".equals(repordsToRcd)) {
			sql = "select id from hrmresource where workcode = '" + repordsTold + "'  and nvl(belongto,0)<=0  order by id desc";
			rs.executeSql(sql);
			if (rs.next()) {
				ryid = Util.null2String(rs.getString("id"));
			}
		}else {
			sql = "select id from hrmresource where workcode = '" + repordsTold + repordsToRcd + "'   order by id desc";
			rs.executeSql(sql);
			if (rs.next()) {
				ryid = Util.null2String(rs.getString("id"));
			}
		}
		return ryid;
	}
}
