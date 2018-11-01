<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <jsp:include page="../core/resource.jsp"/>
</head>
<body>
<jsp:include page="../core/scroll-top.jsp"/>
<jsp:include page="../core/header.jsp"/>

<%
    String[] colors = new String[]{"#2ae0c8", "#6E8B3D", "#e6e2c3", "#D9D9D9", "#4d97d9", "#fe6673", "#4daeb5", "#fbb8ac"};
%>
<div class="bs-docs-content container">
    <div class="row mt5">
        <ol class="breadcrumb">
            <li><a href="${basePath}/">首页</a></li>
            <li><a class="active">API</a></li>
            <%-- TODO 刷新服务放在哪儿？ --%>
            <%-- <li><a class="flush_service">刷新服务</a></li> --%>
        </ol>
    </div>


    <div>
        <div id="serviceGroupTabContent" class="tab-content">
            <% int index = 0;%>
            <div class="row">
                <div class="m10">
                    <c:forEach var="mock" items="${mockServiceList}">
                        <div class="col-sm-6 col-md-4 col-lg-3">
                            <div style="height: 310px;" class="thumbnail">
                                <a href="${basePath}/api/service/${mock.service}"
                                   title="${mock.simpleName}">
                                    <div style="width:100%; height:100px; background:<%=colors[index % colors.length]%>; color: white; text-align:center; font-size:20px; line-height:100px;">
                                        <span style="font-weight: bold; font-size: 20px;font-family: 'Apple Braille'"><%=index + 1%>${mock.simpleName}</span>
                                        <c:choose>
                                            <c:when test="${empty mock.simpleName}">
                                                <c:out value="${mock.simpleName}"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:out value="${mock.simpleName}"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </a>

                                <div style="text-align:center;" class="caption">
                                    <span style="font-size:16px;font-family: 'Apple Braille';color: aqua">
                                        <a href="${basePath}/api/service/${mock.simpleName}"
                                           title="${service.doc}">方法数：
                                            <c:out value="${mock.mockVoList.size()}"/> <br>
                                        </a>
                                    </span>
                                    <blockquote>
                                        <p style="font-size: 15px">
                                            <c:forEach var="mock" items="${mock.mockVoList}" begin="0" end="3">
                                                method：<br><code>${mock.method}</code><br>
                                            </c:forEach>
                                        </p>
                                    </blockquote>
                                </div>
                            </div>
                        </div>
                        <% index++; %>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../core/footer.jsp"/>
</body>
</html>
