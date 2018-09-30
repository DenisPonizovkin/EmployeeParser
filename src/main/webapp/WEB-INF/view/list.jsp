<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>List of users</title>
</head>
<body>
<c:if test="${error != ''}">
    <div style="background-color: red">
        <c:out value="${error}"/>
    </div>
</c:if>

<form:form action="${pageContext.request.contextPath}/edit" method="post" modelAttribute="processInfo">
		<table cellspacing="0" cellpadding="0" class="table" id="table-bootstrap">
			<col width="80">
			<tr>
				<td>
					<h5 class="pmd-card-title-text typo-fill-secondary">Input id of unit for delete/edit</h5>
				</td>
				<td>
					<form:textarea path="uid" name="uid" id="uid" rows="1" cols="50"/>
				</td>
			</tr>
			<tr>
				<td>
					<h5 class="pmd-card-title-text typo-fill-secondary">Input id of employee for delete/edit</h5>
				</td>
				<td>
					<form:textarea path="eid" name="uid" id="uid" rows="1" cols="50"/>
				</td>
			</tr>
		</table>
    <form:input type="submit" class="button" name="add" value="add" path="action"/>
    <form:input type="submit" class="button" name="delete" value="delete" path="action"/>
    <form:input type="submit" class="button" name="Edit" value="edit" path="action"/>
</form:form>
<br/>
<c:out value="${list}" escapeXml="false"/>
</body>
</html>
