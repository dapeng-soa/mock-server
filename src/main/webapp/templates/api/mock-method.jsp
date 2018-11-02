<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <jsp:include page="../core/resource.jsp"/>
    <link rel="stylesheet" href="${basePath}/css/styles/monokai_sublime.css">
    <link rel="stylesheet" href="${basePath}/js/json/json.format.css">
    <link rel="stylesheet" href="${basePath}/css/service/service.css">
    <script src="${basePath}/js/highlight/8.8.0/highlight.min.js"></script>
    <script src="${basePath}/js/json/json.format.js"></script>
    <script src="${basePath}/js/formatmarked.js"></script>
    <script src="${basePath}/js/api/struct.js"></script>
    <script src="${basePath}/js/api/model.js"></script>
    <script src="${basePath}/js/api/enum.js"></script>
    <style></style>
</head>
<body>
<jsp:include page="../core/struct-model.jsp"/>
<jsp:include page="../core/scroll-top.jsp"/>
<jsp:include page="../core/header.jsp"/>
<script>
    $(function () {
        var urlToObj = window.basePath + "/api/enum/" + "${service.name}" + "/" + "${service.meta.version}" + "/jsonEnum";
        var urlToStr = window.basePath + "/api/enum/" + "${service.name}" + "/" + "${service.meta.version}" + "/jsonEnumString";

        $.get(urlToObj, function (data) {
            $("#enum-json-result-str").html(getFormatedJsonHTML(JSON.parse(data)));
            if (data === "{}") {
                $("#enum-json-result-str").hide() && $("#enum-json-copy-but").hide()
            }
        });

        $.get(urlToStr, function (data) {
            $("#enum-json-str-text").html(data);
        });

    });

    // copy json
    function copyText(obj) {
        $(obj).next().select();
        try {
            document.execCommand('copy');
            $(obj).html("Copied")
        } catch (e) {
            alert("复制枚举json失败,请更换浏览器重试！");
        }
    }


</script>
<div class="bs-docs-content container">
    <div class="row mt5">
        <ol class="breadcrumb">
            <li><a href="${basePath}/">首页</a></li>
            <li><a href="${basePath}/api/index.htm">API</a></li>
            <li><a class="active">${service.name}</a></li>
        </ol>
    </div>
    <div class="row">
        <%--左边导航栏,方法--%>
        <div class="col-sm-3 col-md-3">
            <div class="list-group">
                <c:forEach var="s" items="${mockVoList}">
                    <a class="list-group-item <%--list-group-item-danger--%>  ${ s.method == mockVoFirst.method ? 'active' : ''}"
                       href="${basePath}/api/service/${s.method}">
                        <h4 class="list-group-item-heading"
                            style="color: #009688;font-family: Ayuthaya">${s.simpleName}</h4>
                        <h3 class="list-group-item-text"
                            style="font-weight: bold;font-size: 15px;font-family: 'Apple Braille'">${s.method}</h3>
                    </a>
                </c:forEach>
            </div>
        </div>


        <div class="col-sm-9 col-md-9">
            <div class="page-header mt5">
                <h1 class="mt5">${service.doc}
                    <small>${service.name}</small>
                </h1>
            </div>

            <h3 id="service-coordinate">坐标</h3>
            <table class="table table-bordered">
                <thead>
                <tr class="breadcrumb">
                    <th>服务名</th>
                    <th>版本号</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>${service.namespace}.${service.name}</td>
                    <td>${service.meta.version}</td>
                </tr>
                </tbody>
            </table>


            <h3 id="service-structs">结构体列表</h3>
            <table class="table table-bordered">
                <thead>
                <tr class="breadcrumb">
                    <th style="text-align: center">#</th>
                    <th>结构体列表</th>
                    <th>简述</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="struct" items="${service.structDefinitions}" varStatus="vs">
                    <tr>
                        <td width="45px" style="text-align: center">${vs.index + 1}
                            <c:forEach items="${events}" var="event">
                                <c:set var="structName" value="${struct.namespace}.${struct.name }"/>
                                <c:if test="${event.event eq structName}">
                                    <span class="glyphicon glyphicon-bullhorn" style="cursor: pointer"
                                          title="事件"></span>
                                </c:if>
                            </c:forEach>
                        </td>
                        <td>
                            <a href="javascript:void(0)" class="service-struct-item"
                               onclick=getStructDetail('${service.name}','${service.meta.version}','${struct.namespace}','${struct.name}')>${struct.name}</a>
                                <%--<a href="${basePath}/api/struct/${service.name}/${service.meta.version}/${struct.namespace}.${struct.name}.htm">${struct.name}</a>--%>
                        </td>
                        <td data-marked-id="marked">${struct.doc}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <h3 id="service-enums-json"><a href="javascript:void(0)" class="toggle-json-str"
                                           onclick=$("#enum-json-result-str").toggle()&&$("#enum-json-copy-but").toggle()>枚举结构Json</a>
            </h3>
            <div style="position: relative">
                <a href="javascript:void(0)" title="点击复制" onclick="copyText(this)" id="enum-json-copy-but"
                   class="copy-but">copy</a>
                <textarea id="enum-json-str-text" class="json-str-text"></textarea>
                <div id="enum-json-result-str"
                     style="height:300px;padding:20px;border:solid 1px #ddd;border-radius:0;resize: none;overflow-y:auto;margin-bottom: 20px"></div>
            </div>
        </div>
    </div>
    <div class="right-menu-list">
        <ul>
            <li><a href="#service-coordinate">服务坐标</a></li>
            <li><a href="#service-methods">方法列表</a></li>
            <li><a href="#service-structs">结构体列表</a></li>
            <li><a href="#service-enums">枚举列表</a></li>
            <li><a href="#service-enums-json">枚举json</a></li>
            <li><a href="#service-events">事件清单</a></li>
        </ul>
    </div>
</div>
<jsp:include page="../core/footer.jsp"/>
</body>
</html>
