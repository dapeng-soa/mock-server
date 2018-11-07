<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<title>WebUploader文件上传示例</title>


<script src="/static/common/bootstrap/js/jquery.min.js"></script>
<script src="/static/common/bootstrap/js/bootstrap.js"></script>
<script src="/static/upload/upload.js"></script>
<script src="/static/upload/webuploader.js"></script>


<link href="/static/upload/webuploader.css" rel="stylesheet"/>
<link href="/static/upload/style.css" rel="stylesheet"/>
<link href="/static/css/bootstrap/3.3.5/bootstrap.min.css" rel="stylesheet">
<link href="/static/css/bootstrap/3.3.5/bootstrap-theme.min.css" rel="stylesheet">

</head>

<body onload="">
<div id="wrapper">
    <div id="container">
        <!--头部，相册选择和格式选择-->
        <div id="uploader">
            <div class="queueList">
                <div id="dndArea" class="placeholder">
                    <div id="filePicker"></div>
                </div>
            </div>
            <div class="statusBar" style="display:none;">
                <span id="shareUrl"></span>
                <div class="progress">
                    <span class="text">0%</span>
                    <span class="percentage"></span>
                </div>
                <div class="info"></div>
                <div class="btns">
                    <div id="filePicker2"></div>
                    <div class="uploadBtn">开始上传</div>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>
