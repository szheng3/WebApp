
<%--
  Created by IntelliJ IDEA.
  User: zxz
  Date: 1/31/17
  Time: 2:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Welcome</title>
</head>
<body>
    <div style="text-align: center;">Welcome <c:out value="${username}"/></div>
    <%--TODO: Specify absolute path--%>
    <jsp:forward page="../index.jsp">
        <jsp:param value="${username}" name="uid" />
        <jsp:param value="true" name="loggedIn" />
    </jsp:forward>
</body>
</html>

