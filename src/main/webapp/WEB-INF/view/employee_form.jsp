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
<form:form action="${pageContext.request.contextPath}/process_employee" method="post" modelAttribute="employeeForm">
                Табельный номер сотрудника
                <form:textarea path="employee.id" name="id" id="id" rows="1" cols="50"/>
                <br/>

            ФИО
                <form:textarea path="employee.fio" name="fio" id="fio" rows="1" cols="50"/>
                <br/>

            Должность
                <form:textarea path="employee.position" name="position" id="position" rows="1" cols="50"/>
                <br/>

            Адрес электронной почты
                <form:textarea path="employee.email" name="email" id="email" rows="1" cols="50"/>
                <br/>

            Номер телефона
                <form:textarea path="employee.tel" name="tel" id="tel" rows="1" cols="50"/>
                <br/>


            Доп. номер телефона
                <form:textarea path="employee.tel2" name="tel2" id="tel2" rows="1" cols="50"/>
                <br/>


            Номер кабинета
                <form:textarea path="employee.room" name="room" id="room" rows="1" cols="50"/>
                <br/>



                <form:input path="oldId" name="" id="oldId" rows="1" cols="50" type="hidden" visibility="hidden"/>


                <form:input path="unitName" name="" id="unitName" rows="1" cols="50" type="hidden" visibility="hidden"/>

    <form:input type="submit" class="button" name="Save" value="Save" path=""/>
</form:form>

</body>
</html>
