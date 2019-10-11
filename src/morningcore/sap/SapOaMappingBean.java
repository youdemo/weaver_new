package morningcore.sap;

public class SapOaMappingBean{

	public int id;
	public String workflowId;
	public String OAFieldName;
	public String OATableType;
	public String SAPFunctionName;
	public String SAPParameterType;
	public String SAPFieldName;
	public String SAPParameterName;
	public String SAPTableType;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getWorkflowId() {
		return workflowId;
	}

	public void setWorkflowId(String workflowId) {
		this.workflowId = workflowId;
	}

	public String getOAFieldName() {
		return OAFieldName;
	}

	public void setOAFieldName(String oAFieldName) {
		OAFieldName = oAFieldName;
	}

	public String getSAPFunctionName() {
		return SAPFunctionName;
	}

	public void setSAPFunctionName(String sAPFunctionName) {
		SAPFunctionName = sAPFunctionName;
	}

	public String getSAPParameterType() {
		return SAPParameterType;
	}

	public void setSAPParameterType(String sAPParameterType) {
		SAPParameterType = sAPParameterType;
	}

	public String getSAPFieldName() {
		return SAPFieldName;
	}

	public void setSAPFieldName(String sAPFieldName) {
		SAPFieldName = sAPFieldName;
	}

	public String getSAPParameterName() {
		return SAPParameterName;
	}

	public void setSAPParameterName(String sAPParameterName) {
		SAPParameterName = sAPParameterName;
	}

	public String getOATableType() {
		return OATableType;
	}

	public void setOATableType(String oATableType) {
		OATableType = oATableType;
	}

	public String getSAPTableType() {
		return SAPTableType;
	}

	public void setSAPTableType(String sAPTableType) {
		SAPTableType = sAPTableType;
	}

	public String toString(){
		StringBuffer buff = new StringBuffer();
		buff.append("id=");buff.append(id);buff.append(";workflowId=");buff.append(workflowId);
		buff.append(";OAFieldName=");buff.append(OAFieldName);
		buff.append(";OATableType=");buff.append(OATableType);
		buff.append(";SAPFunctionName=");buff.append(SAPFunctionName);
		buff.append(";SAPParameterType=");buff.append(SAPParameterType);
		buff.append(";SAPFieldName=");buff.append(SAPFieldName);
		buff.append(";SAPParameterName=");buff.append(SAPParameterName);
		buff.append(";SAPTableType=");buff.append(SAPTableType);
		
		return buff.toString();
	}
	
	public static void main(String[] args) {
		SapOaMappingBean smb = new SapOaMappingBean();
		smb.setId(12);smb.setOAFieldName("OAFieldName");smb.setOATableType("OATableType");
		smb.setSAPFieldName("SAPFieldName");smb.setSAPFunctionName("SAPFunctionName");
		smb.setSAPParameterName("SAPParameterName");smb.setSAPParameterType("SAPParameterType");
		smb.setSAPTableType("SAPTableType");smb.setWorkflowId("100");
		
		System.out.println(smb.toString());
		
	}
}
