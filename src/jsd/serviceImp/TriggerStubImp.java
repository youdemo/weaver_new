package jsd.serviceImp;

import jsd.services.TriggerStub;
import jsd.services.TriggerStub.OAtoERP;

public class TriggerStubImp {
	public String serviceImp(String jsonStr ,String flag) throws Exception{
		TriggerStub ts = new TriggerStub();
		OAtoERP  oe = new OAtoERP();
		oe.setFlag(flag);
		oe.setStrJson(jsonStr);
		String result = ts.OAtoERP(oe).getOAtoERPResult();
		return result;
	}
}
