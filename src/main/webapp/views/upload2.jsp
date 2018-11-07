<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>文件夹上传</title>
</head>

<script src="/static/common/bootstrap/js/jquery.min.js"></script>
<script src="/static/common/bootstrap/js/bootstrap.js"></script>
<script src="/static/upload/upload.js"></script>
<script src="/static/upload/webuploader.js"></script>


<link href="/static/upload/webuploader.css" rel="stylesheet"/>
<link href="/static/upload/style.css" rel="stylesheet"/>
<link href="/static/css/bootstrap/3.3.5/bootstrap.min.css" rel="stylesheet">
<link href="/static/css/bootstrap/3.3.5/bootstrap-theme.min.css" rel="stylesheet">

<body>

<form class="form-horizontal" role="form" id="fileUploadForm" action="/batch/upload2" name="fileUploadForm"
      method="post" enctype="multipart/form-data" charset="UTF-8">
    <div class="form-group">
        <input id="file" name="file" type="file" webkitdirectory><span id="msg" style="color:#F00"></span>
    </div>
    <button type="button" class="btn btn-primary" id="subButton" onclick="commit()">Submit</button>
</form>

</body>
<script>
    //页面提示信息
    var msg;
    //文件数量限制
    var filesCount = 2000;
    //文件夹大小限制 2000M
    var filesSize = 2147483648;
    //实际的文件数量
    var actual_filesCount = 0;
    //实际的文件夹大小
    var actual_filesSize = 0;

    function commit() {
        //判断是否选中文件夹
        var file = $("#fileFolder").val();
        if (file == '') {
            $("#msg").text('请选择要上传的文件夹');
            return;
        }


        $("#fileUploadForm").submit();

    }

    document.getElementById('fileFolder').onchange = function (e) {

        //判断是否选中文件
        var file = $("#fileFolder").val();
        if (file != '') {
            $("#msg").text('');
        }
        var files = e.target.files; // FileList
        //文件数量
        actual_filesCount = files.length;
        if (actual_filesCount > filesCount) {
            $("#msg").text("文件过多，单次最多可上传" + filesCount + "个文件");
            return;
        }
        for (var i = 0, f; f = files[i]; ++i) {
            actual_filesSize += f.size;
            if (actual_filesSize > filesSize) {
                $("#msg").text("单次文件夹上传不能超过" + filesSize / 1024 / 1024 + "M");
                return;
            }
        }
    };
</script>


</html>
