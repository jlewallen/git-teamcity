<%@include file="/include.jsp"%>
<%@ taglib prefix="props" tagdir="/WEB-INF/tags/props" %>
<jsp:useBean id="propertiesBean" scope="request" type="jetbrains.buildServer.controllers.BasePropertiesBean"/>
<table class="runnerFormTable">

  <l:settingsGroup title="Git Settings">
  <tr>
    <th><label for="clone_url">Clone URL: <l:star/></label></th>
    <td><props:textProperty name="clone_url" className="longField" maxlength="256" />
      <span class="error" id="error_clone_url"></span></td>
  </tr>
  <tr>
    <th><label for="branch">Branch: <l:star/></label></th>
    <td><props:textProperty name="branch" className="longField" maxlength="256" />
      <span class="error" id="error_branch"></span></td>
  </tr>
  <tr>
    <th><label for="server_project_directory">Project Directory (Server): <l:star/></label></th>
    <td><props:textProperty name="server_project_directory" className="longField" maxlength="256" />
      <span class="error" id="error_server_project_directory"></span></td>
  </tr>
  </l:settingsGroup>

</table>
