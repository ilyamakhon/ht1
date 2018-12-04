<%@ page import="com.epam.ht1.app.Person" %>
<%@ page import="java.util.HashMap" %><%--
  Created by IntelliJ IDEA.
  User: ilyamakhon
  Date: 03.12.2018
  Time: 17:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Добавление номера</title>
</head>
<body>
    <%
        HashMap<String, String> jsp_parameters = new HashMap<>();
        Person person = new Person();
        String error_message = "";

        if (request.getAttribute("jsp_parameters") != null) {
            jsp_parameters = (HashMap<String, String>) request.getAttribute("jsp_parameters");
        }

        if (request.getAttribute("person") != null) {
            person = (Person) request.getAttribute("person");
        }

        error_message = jsp_parameters.get("error_message");
    %>
    <form action="<%=request.getContextPath()%>/" method="post">
        <table align="center" border="1">
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
                <td colspan="2" align="center">
                    Информация о телефоне владельца: <%=person.getName()%> <%=person.getSurname()%> <%=person.getPatronymic()%>
                </td>
            </tr>
            <tr>
                <td>Номер:</td>
                <td>
                    <input type="tel" name="phone"/>
                    <input type="hidden" name="id" value="<%=person.getId()%>">
                </td>
            </tr>
            <tr>
                <td colspan="2" align="center">
                    <input type="submit" name="<%=jsp_parameters.get("next_action")%>"
                           value="<%=jsp_parameters.get("next_action_label")%>"/>
                    <br>
                    <a href="<%=request.getContextPath()%>/?action=edit&id=<%=person.getId()%>">Вернуться к данным о человеке</a>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
