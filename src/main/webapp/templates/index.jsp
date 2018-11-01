<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <jsp:include page="core/resource.jsp"/>
</head>
<body>
<jsp:include page="core/header.jsp"/>

<div class="bs-docs-content container">
    <div class="row m10">
        <div class="jumbotron">
            <h1>Dapeng Mock Server</h1>
            <p>This is a mock server for Dapeng-soa, 在这里可以更看到更详细的技术文档、接口文档，同时还可以进行在线测试接口报文等。</p>
            <p><a role="button" href="${basePath}/api/index" class="btn btn-primary btn-lg">Learn more</a></p>
        </div>
    </div>
</div>

<jsp:include page="core/footer.jsp"/>
</body>
</html>
