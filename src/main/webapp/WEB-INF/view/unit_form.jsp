<%--
  Created by IntelliJ IDEA.
  User: denis
  Date: 29.09.18
  Time: 17:34
  To change this template use File | Settings | File Templates.
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<html>
<head>
    <title>Title</title>
</head>
<body>
<form:form action="${pageContext.request.contextPath}/process_unit" method="post" modelAttribute="unitForm">
    <table cellspacing="0" cellpsaveing="0" class="table" id="table-bootstrap">
        <col width="80">
        <tr>
            <td>
                <h5 class="pmd-card-title-text typo-fill-secondary">Input id of unit for delete/edit</h5>
            </td>
            <td>
                <form:textarea path="unit.name" name="name" id="name" rows="1" cols="50"/>
            </td>
            <td>
                <form:input path="oldName" name="oldName" id="oldName" rows="1" cols="50" type="hidden" visibility="hidden"/>
            </td>
        </tr>
    </table>
    <form:input type="submit" class="button" name="save" value="save" path=""/>
</form:form>

</body>
</html>
