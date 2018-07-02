package gvo.emreim;

import gvo.emreim.SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.Response;

public class EMReimWebService {
    public Response getResultMethod(String json) throws Exception{
        SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub sem = new SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub();
        SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.SAPHR_FI_0_EmCostReimbursementService sems = new SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.SAPHR_FI_0_EmCostReimbursementService();
        sems.setData(json);
        Response result = sem.SAPHR_FI_0_EmCostReimbursementService(sems);
        return result;
    }
}

