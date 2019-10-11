<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%@ page import="morningcore.sap.IV_Flag_Purchasing_GroupBapi" %>
<%@ page import="weaver.general.Util" %>
<%@ include file="/systeminfo/init_wev8.jsp" %>
<%@ taglib uri="/WEB-INF/weaver.tld" prefix="wea" %>
<%@ taglib uri="/browserTag" prefix="brow" %>
<jsp:useBean id="rs" class="weaver.conn.RecordSet" scope="page"/>
<jsp:useBean id="RecordSet" class="weaver.conn.RecordSet" scope="page"/>
<jsp:useBean id="ResourceComInfo" class="weaver.hrm.resource.ResourceComInfo" scope="page"/>
<HTML>
<HEAD>
    <LINK href="/css/Weaver_wev8.css" type=text/css rel=STYLESHEET>
    <script type="text/javascript" src="/appres/hrm/js/mfcommon_wev8.js"></script>
    <script language=javascript src="/js/ecology8/hrm/HrmSearchInit_wev8.js"></script>
</head>
<%
    String imagefilename = "/images/hdReport_wev8.gif";
    String titlename = SystemEnv.getHtmlLabelName(20536,user.getLanguage());
    String needfav = "1";
    String needhelp = "";
    boolean isShow = false;
    int userid = user.getUID();
    String multiIds = Util.null2String(request.getParameter("multiIds"));
%>
<BODY>

<div id="tabDiv">
    <span class="toggleLeft" id="toggleLeft"
          title="<%=SystemEnv.getHtmlLabelName(32814,user.getLanguage()) %>"><%=SystemEnv.getHtmlLabelName(20536,user.getLanguage()) %></span>
</div>
<div id="dialog">
    <div id='colShow'></div>
</div>
<input type="hidden" name="pageId" id="pageId" value="<%= PageIdConst.HRM_ONLINEUSER %>"/>
<%@ include file="/systeminfo/TopTitle_wev8.jsp" %>
<%@ include file="/systeminfo/RightClickMenuConent_wev8.jsp" %>
<%
    RCMenu += "{刷新,javascript:onBtnSearchClick(),_self} ";
    RCMenuHeight += RCMenuHeightStep;
%>
<%@ include file="/systeminfo/RightClickMenu_wev8.jsp" %>
<FORM id=report name=report method=post> <!--/task/point/sysnDevelopPerPoint.jsp-->
    <input type="hidden" name="multiIds" value="-1">
    <%
        if("1".equals(multiIds)){
            IV_Flag_Purchasing_GroupBapi IV_Flag_Purchasing_GroupBapi = new IV_Flag_Purchasing_GroupBapi();
            IV_Flag_Purchasing_GroupBapi.getData();
            out.println("<font size=\"5px\" color=\"red\">执行完成，获取成功!</font>");
        }
    %>

    <wea:layout type="2col">
        <wea:group context="采购组同步" attributes="test">
            <wea:item>采购组同步</wea:item>
            <wea:item>
                <input name="hi2" type="button" value="采购组同步" class="e8_btn_top_first"
                       <%--style="overflow: hidden; white-space: nowrap; text-overflow: ellipsis;  max-width: 200px;width:200px;height:30px;font-size:15px;"--%>
                       onClick="onCheckup()">
                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            </wea:item>
        </wea:group>
    </wea:layout>
</FORM>
</BODY>
</HTML>
<script type="text/javascript">
    function onCheckup() {

        if (window.confirm('是否要进行采购组同步？')) {
            document.report.multiIds.value = "1";
            document.report.submit();
        }

    }
    function onBtnSearchClick1() {
        report.submit();
    }
</script>	

