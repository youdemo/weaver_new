package jsd.ape.serviceimp;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import weaver.general.BaseBean;

import jsd.ape.services.ReWriteDescStub;
import jsd.ape.services.ReWriteDescStub.DealResult;
import jsd.ape.services.RewriteStatusStub;
import jsd.ape.services.RewriteStatusStub.Rewrite;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-29 下午2:41:09
 * 流程回传
 */
public class RewriteStatusStubImp {
	/**
	 * 流程审批 结束后  回传
	 * @param jsonstr
	 * @param flag
	 * @return
	 */
	public String returnUST(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		log.writeLog("流程审批 结束后  回传----jsonstr----"+jsonstr+"-----flag---"+flag);
		String result = "";
		RewriteStatusStub rs = null;
		try {
			rs = new RewriteStatusStub ();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Rewrite rit = new Rewrite();
		rit.setJson(jsonstr);
		rit.setFlag(flag);
		//jsd.ape.services.RewriteStatus.Rewrite
		try {
			result = rs.Rewrite(rit).getRewriteResult();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 流程审批 过程中  回传
	 * @param jsonstr
	 * @param flag
	 * @return
	 */
	public String returnUStue(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		log.writeLog(" 流程审批 过程中  回传-------jsonstr----"+jsonstr+"-----flag---"+flag);
		ReWriteDescStub rs = null;
		String result = "";
		try {
			rs = new ReWriteDescStub();
		} catch (AxisFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DealResult dealResult = new DealResult();
		dealResult.setFlag(flag);
		dealResult.setJson(jsonstr);
		try {
			result = rs.DealResult(dealResult).getDealResultResult();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
		
	}
	

}
