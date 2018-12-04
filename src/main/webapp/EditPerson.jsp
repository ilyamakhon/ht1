<?xml version="1.0" encoding="UTF-8" ?>
<%@ page import="com.epam.ht1.app.Person" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.Map" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>Управление данными о человеке</title>
</head>
<body>

<%
    HashMap<String, String> jsp_parameters = new HashMap<>();
    Person person = new Person();
    String error_message = "";
    String user_message = "";

    if (request.getAttribute("jsp_parameters") != null) {
        jsp_parameters = (HashMap<String, String>) request.getAttribute("jsp_parameters");
    }

    if (request.getAttribute("person") != null) {
        person = (Person) request.getAttribute("person");
    }
    error_message = jsp_parameters.get("error_message");

    user_message = jsp_parameters.get("current_action_result_label");
%>

<form action="<%=request.getContextPath()%>/" method="post">
    <input type="hidden" name="id" value="<%=person.getId()%>"/>
    <table align="center" border="1" width="70%">
        <%
            if ((user_message != null) && (!user_message.equals(""))) {
        %>
        <tr>
            <td colspan="6" align="center">
                <%=user_message%>
            </td>
        </tr>
        <%
            }
        %>
        <%
            if ((error_message != null) && (!error_message.equals(""))) {
        %>
        <tr>
            <td colspan="2" align="center"><span style="color:#ff0000"><%=error_message%></span></td>
        </tr>
        <%
            }
        %>
        <tr>
            <td colspan="2" align="center">Информация о человеке</td>
        </tr>
        <tr>
            <td>Фамилия:</td>
            <td><input type="text" name="surname" value="<%=person.getSurname()%>"/></td>
        </tr>
        <tr>
            <td>Имя:</td>
            <td><input type="text" name="name" value="<%=person.getName()%>"/></td>
        </tr>
        <tr>
            <td>Отчество:</td>
            <td><input type="text" name="middleName" value="<%=person.getMiddleName()%>"/></td>
        </tr>
        <tr>
            <tr>
                <td>Телефоны:</td>
                <td>
                    <table>
                    <%
                        for (Map.Entry<String, String> phone : person.getPhones().entrySet()) {
                    %>
                        <tr>
                            <td>
                                <p style="display: inline-block; margin: 0 30px 0 0"><%=phone.getValue()%></p>
                            </td>
                            <td>
                                <a style="margin-right: 30px" href="<%=request.getContextPath()%>/?action=editPhone&id=<%=person.getId()%>&phoneId=<%=phone.getKey()%>">Редактировать</a>
                            </td>
                            <td>
                                <a href="<%=request.getContextPath()%>/?action=deletePhone&id=<%=person.getId()%>&phoneId=<%=phone.getKey()%>">Удалить</a>
                            </td>
                        </tr>
                    <%
                        }
                    %>
                    </table>
                    <br/>
                    <a href="<%=request.getContextPath()%>/?action=addPhone&id=<%=person.getId()%>">Добавить</a>
                </td>
            </tr>
        <tr>
            <td colspan="2" align="center">
                <input type="submit" name="<%=jsp_parameters.get("next_action")%>"
                       value="<%=jsp_parameters.get("next_action_label")%>"/>
                <br/>
                <a href="http://localhost:8080/manageperson">Вернуться к списку</a>
            </td>
        </tr>
    </table>
</form>
</body>
</html>