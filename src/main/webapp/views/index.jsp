<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8"/>
    <link rel="stylesheet" href="/static/common/bootstrap/scss/bootstrap-system.css">
    <link rel="stylesheet" href="/static/common/plugins/validation/css/validationEngine.jquery.css">
    <style>
        .btns-wrap dt {
            padding: 5px 0;
        }

        .btns-wrap dd {
            margin-bottom: 10px;
        }

        #interfaceTabs [aria-expanded="true"] {
            background: #FFF;
            color: #333;
        }
    </style>
    <link rel="stylesheet" href="/static/common/json/s.css">
    <script src="/static/common/bootstrap/js/jquery.min.js"></script>
    <script src="/static/common/bootstrap/js/bootstrap.js"></script>
    <script src="/static/common/js/toolbar.js"></script>
    <script src="/static/common/plugins/jQuery.cookie/jQuery.cookie.js"></script>

    <script src="/static/common/plugins/validation/js/jquery.validationEngine-zh_CN.js"></script>

    <script src="/static/common/plugins/validation/js/jquery.validationEngine.js"></script>
    <script type="text/javascript" src="/static/common/plugins/artdialog/jquery.artDialog.js"></script>
    <script type="text/javascript"
            src="/static/common/plugins/gasparesganga-jquery-loading-overlay/src/loadingoverlay.min.js"></script>

    <script type="text/javascript">
        $(function () {
            function showDemoDialog(withTitle) {
                var opts = {
                    title: withTitle ? '详情统计' : false,
                    //title: false,
                    width: 500,
                    height: 220,
                    padding: '0 20px',
                    isOuterBoxShadow: false,
                    //isClose: false,
                    content: '<center>hello world</center><p>API 文档见http://lab.seaning.com/_doc/API.html</p>',
                    lock: true,
                    fixed: true,
                };
                var btnOpt;
                if (withTitle) {
                    btnOpt = {
                        button: [
                            {
                                name: '常规按钮',
                                callback: function () {
                                    console.log('常规按钮..');
                                }
                            },
                            {
                                name: '蓝色按钮',
                                className: 'btn-process',
                                callback: function () {
                                    this.content('你同意了').time(2);
                                    return false;
                                },
                                focus: true
                            },
                            {
                                name: '红色按钮',
                                className: 'btn-important',
                                callback: function () {
                                    alert('你不同意')
                                }
                            },
                            {
                                name: '被禁用按钮',
                                disabled: true
                            }
                        ]
                    }
                } else {
                    btnOpt = {
                        cancel: function () {
                        },
                        cancelVal: '关闭',
                        okCssClass: 'btn-save',
                        ok: function () {
                            console.log('ok..');
                        },
                        okVal: '确认',
                    }
                }
                $.dialog($.extend(opts, btnOpt));
            }

            $("#btn-open-dialog").click(function () {
                showDemoDialog(true);
            });
            $("#btn-open-dialog-without-titlebar").click(function () {
                showDemoDialog();
            });
            $('#formId').validationEngine({validateNonVisibleFields: true});
            $('.J_common_success').click(function () {
                $.commonTips('已成功上架该产品', 'success', 3)
            })
            $('.J_common_info').click(function () {
                $.commonTips('已成功上架该产品', 'info', 3)
            })
            $('.J_common_danger').click(function () {
                $.commonTips('已成功上架该产品', 'danger', 2)
            })
            // $('.page-content-inner').scrollTop(3811);
        });
    </script>
    <title>
        接口列表 </title>
</head>
<body>
<div class="page-wrapper">
    <div class="page-sidebar">
        <a href="index.html" class="logo"></a>
        <ul class="nav" id="nav">
            <li>
                <a href="#nav2" data-toggle="collapse" data-parent="#nav" aria-expanded="true">
                    <!-- 此处的 aria-expanded="true" 和下面 ul 上面的in需要共存 -->
                    <i class="gm-icon gm-peer-plan"></i>
                    <span>项目管理</span>
                </a>
                <ul class="collapse in" id="nav2">
                    <li class=""><a href="/">项目列表</a></li>
                    <li class=""><a href="/project/add">添加项目</a></li>
                </ul>
            </li>
            <li>
                <a href="#nav3" data-toggle="collapse" data-parent="#nav" aria-expanded="true">
                    <!-- 此处的 aria-expanded="true" 和下面 ul 上面的in需要共存 -->
                    <i class="gm-icon gm-peer-plan"></i>
                    <span>接口管理</span>
                </a>
                <ul class="collapse in" id="nav3">
                    <li class="open"><a href="/interface/index">接口管理</a></li>
                    <li class=""><a href="/interface/add">添加接口</a></li>
                </ul>
            </li>
            <li>
                <a href="#nav4" data-toggle="collapse" data-parent="#nav" aria-expanded="true">
                    <!-- 此处的 aria-expanded="true" 和下面 ul 上面的in需要共存 -->
                    <i class="gm-icon gm-peer-plan"></i>
                    <span>Swagger管理</span>
                </a>
                <ul class="collapse in" id="nav3">
                    <li class=""><a href="/swagger/index">Swagger</a></li>
                </ul>
            </li>
            <li>
                <a href="#nav4" data-toggle="collapse" data-parent="#nav" aria-expanded="true">
                    <!-- 此处的 aria-expanded="true" 和下面 ul 上面的in需要共存 -->
                    <i class="gm-icon gm-peer-plan"></i>
                    <span>系统管理</span>
                </a>
                <ul class="collapse in" id="nav3">
                    <li class=""><a href="/system/add">系统参数设置</a></li>
                </ul>
            </li>
        </ul>
    </div>
    <div class="page-content-wrapper">
        <div class="page-title clearfix">
            <div class="pull-left">
                <button type="button" class="btn btn-default" id="nav-button"><i class="gm-icon gm-menu"></i></button>
                <h2>
                    mock-server
                    <small class="lang_switcher">
                        切换语言：<i class="fa fa-retweet"></i><a href="javascript:;" _href="/system/lang/zh-cn"
                                                             id="chinese">中文</a>&nbsp;
                        <a href="javascript:;" _href="/system/lang/en" id="english">English</a>
                    </small>
                </h2>
            </div>


        </div>
        <script>
            $('#chinese,#english').click(function () {
                $.getJSON($(this).attr('_href')).done(function (data) {
                    if (data && data.errno === 0) {
                        location.reload();
                    } else {
                        $.commonTips(data.errmsg, 'danger', 1500);
                    }
                }).fail(function (err) {
                    alert(err.message)
                });
                return false;
            })
        </script>
        <div class="page-content-outer">
            <div class="page-content-inner">
                <div class="page-content">


                    <div class="page-main">


                        <div class="row">
                            <!--table-->
                            <div class="col-md-12">
                                <form action="">
                                    <div class="item">
                                        <div class="item-head clearfix">
                                            <div class="pull-right form-inline">
                                                <div class="input-group">
                                                </div>
                                                <div class="input-group">
                                                    <div class="has-feedback">
                                                        <input type="hidden" name="project_id" value="">
                                                        <input type="text" class="form-control" placeholder="接口路径或名称"
                                                               name="keyword" value="">
                                                        <i class="fa fa-times-circle form-control-feedback"
                                                           data-role="button"></i>
                                                    </div>
                                                    <span class="input-group-btn">
                        <button class="btn btn-info" type="submit">搜索</button>
                        </span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                                <div class="item">
                                    <div class="item-head clearfix">
                                        <h4 class="pull-left">全部接口</h4>


                                        <button style="padding-right: 20px;float: right" data-is-proxy="1"
                                                id="oer_all_proxy" class="btn btn-info" type="button">关闭全部二次代理
                                        </button>
                                        <span style="padding-right: 20px;float: right">
                            <a class="btn btn-info" href="/interface/add/">添加接口</a>
                                                        </span>
                                    </div>
                                    <div class="item-main">
                                        <table class="table table-bordered table-hover">
                                            <thead>
                                            <tr>
                                                <th>ID</th>
                                                <th>项目名称</th>
                                                <th>请求类型</th>
                                                <th>接口名称</th>

                                                <th>接口地址</th>
                                                <th>二次代理</th>
                                                <th>操作</th>
                                            </tr>
                                            </thead>
                                            <tbody>

                                            <tr>
                                                <td>108</td>
                                                <td>OrderService</td>
                                                <td>get</td>
                                                <td>扦样区车辆列表</td>


                                                <td><a style="color: #ff7e1d;" target="_blank"
                                                       href="/postman?mockid=108">/mobile/qycl/list</a>
                                                    <p style="display: block"
                                                       class="J_proxy_all_tips J_proxy_tips_108">代理开启时，接口将从：[
                                                        <span style="color: red;">
                                      http://47.93.62.181:8034/mobile/qycl/list</span>]，返回数据<br>若此地址无法获取到数据，则会等待超时或报错
                                                    </p>
                                                </td>
                                                <td class="proxy_state">已开启</td>
                                                <td><a class="deleteLnk" href="/interface/delete?mockid=108">删除</a>
                                                    <a href="/interface/edit?mockid=108">修改</a>
                                                    <a href="/interface/add?iscopy=1&mockid=108">复制</a>
                                                    <a method="get" class="J_lnk"
                                                       href="/53ec905f7c90b64278d5/mobile/qycl/list">查看</a>
                                                    <a href="javascript:;" mockid="108" class="oper_proxy"
                                                       data-is-proxy="1">

                                                        关闭二次代理

                                                    </a>
                                                </td>
                                            </tr>

                                            <tr>
                                                <td>109</td>
                                                <td>OrderService</td>
                                                <td>get</td>
                                                <td>createOrder</td>


                                                <td><a style="color: #ff7e1d;" target="_blank"
                                                       href="/postman?mockid=109">/createOrder</a>
                                                    <p style="display: none"
                                                       class="J_proxy_all_tips J_proxy_tips_109">代理开启时，接口将从：[
                                                        <span style="color: red;">
                                      http://47.93.62.181:8034/createOrder</span>]，返回数据<br>若此地址无法获取到数据，则会等待超时或报错</p>
                                                </td>
                                                <td class="proxy_state">已关闭</td>
                                                <td><a class="deleteLnk" href="/interface/delete?mockid=109">删除</a>
                                                    <a href="/interface/edit?mockid=109">修改</a>
                                                    <a href="/interface/add?iscopy=1&mockid=109">复制</a>
                                                    <a method="get" class="J_lnk"
                                                       href="/53ec905f7c90b64278d5/createOrder">查看</a>
                                                    <a href="javascript:;" mockid="109" class="oper_proxy"
                                                       data-is-proxy="0">

                                                        开启二次代理

                                                    </a>
                                                </td>
                                            </tr>

                                            <tr>
                                                <td>107</td>
                                                <td>testa</td>
                                                <td>get</td>
                                                <td>login</td>


                                                <td><a style="color: #ff7e1d;" target="_blank"
                                                       href="/postman?mockid=107">/api/login</a>
                                                    <p style="display: none"
                                                       class="J_proxy_all_tips J_proxy_tips_107">代理开启时，接口将从：[
                                                        <span style="color: red;">
                                      http://47.93.62.181:8034/api/login</span>]，返回数据<br>若此地址无法获取到数据，则会等待超时或报错</p>
                                                </td>
                                                <td class="proxy_state">已关闭</td>
                                                <td><a class="deleteLnk" href="/interface/delete?mockid=107">删除</a>
                                                    <a href="/interface/edit?mockid=107">修改</a>
                                                    <a href="/interface/add?iscopy=1&mockid=107">复制</a>
                                                    <a method="get" class="J_lnk"
                                                       href="/a6954963f823c2b93241/api/login">查看</a>
                                                    <a href="javascript:;" mockid="107" class="oper_proxy"
                                                       data-is-proxy="0">

                                                        开启二次代理

                                                    </a>
                                                </td>
                                            </tr>

                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <!--table end-->
                        </div>
                        <form action="" method="" id="locationForm" target="_blank">
                            <input type="hidden" name="_method" value=""/>
                        </form>
                        <script src="/static/module/interface.js"></script>
                        <script>
                            window.interface.init({
                                LN: {
                                    "LN": "chinese",
                                    "languageSwitcher": "切换语言：",
                                    "info": "提示信息",
                                    "goBack": "返回上一步",
                                    "sidebar": {
                                        "projectAdmin": "项目管理",
                                        "projectList": "项目列表",
                                        "addProject": "添加项目",
                                        "apiAdmin": "接口管理",
                                        "addApi": "添加接口",
                                        "systemAdmin": "系统管理",
                                        "globalParamsSetting": "系统参数设置",
                                        "swaggerAdmin": "Swagger管理",
                                        "swagger": "Swagger"
                                    },
                                    "project": {
                                        "index": {
                                            "projectList": "项目列表",
                                            "addProject": "添加项目",
                                            "projectName": "项目名称",
                                            "projectInterfacePrefix": "项目接口前缀",
                                            "proxyPrefix": "代理路径",
                                            "swaggerUrl": "Swagger路径",
                                            "actions": "操作",
                                            "delete": "删除",
                                            "edit": "修改",
                                            "details": "查看接口",
                                            "deleteTips": "删除项目无法恢复且其项目下的接口不会同步删除,你确定删除该项目么?"
                                        },
                                        "add": {
                                            "editProject": "修改项目",
                                            "addProject": "添加项目",
                                            "projectName": "项目名称",
                                            "projectInterfacePrefix": "项目接口前缀",
                                            "prefixExclude": "不能为以下关键词开头，因为以下关键词已经被系统占用:",
                                            "projectProxyUrl": "项目二次代理Url",
                                            "swaggerUrl": "项目swagger路径",
                                            "format": "格式如",
                                            "save": "保存项目"
                                        },
                                        "controller": {
                                            "title": "项目列表",
                                            "projectIsNotExist": "项目不存在",
                                            "deleteSuccess": "删除成功",
                                            "deleteFail": "删除失败",
                                            "idIsNotExist": "ID不存在",
                                            "projectNameIsExist": "项目名称已存在",
                                            "dataIsEmpty": "数据为空:点击返回列表",
                                            "editSuccess": "修改成功",
                                            "editFail": "修改失败",
                                            "actionError": "操作失败",
                                            "addSuccess": "添加成功",
                                            "returnProjectList": "返回项目列表",
                                            "editAgain": "再次修改",
                                            "add": "再次添加"
                                        }
                                    },
                                    "interface": {
                                        "index": {
                                            "searchPlaceHolder": "接口路径或名称",
                                            "search": "搜索",
                                            "closeAllProxies": "关闭全部二次代理",
                                            "openAllProxies": "开启全部二次代理",
                                            "openProxy": "开启二次代理",
                                            "closeProxy": "关闭二次代理",
                                            "addInterface": "添加接口",
                                            "projectName": "项目名称",
                                            "interfaceName": "接口名称",
                                            "methodType": "请求类型",
                                            "interfaceUrl": "接口地址",
                                            "againProxy": "二次代理",
                                            "actions": "操作",
                                            "closed": "已关闭",
                                            "opened": "已开启",
                                            "delete": "删除",
                                            "edit": "修改",
                                            "clone": "复制",
                                            "detail": "查看",
                                            "deleteTips": "删除后无法恢复,你确定删除该接口么?",
                                            "controllerProjectName": "全部接口",
                                            "openProxyTipsPrefix": "代理开启时，接口将从：",
                                            "openProxyTipsPostfix": "，返回数据<br>若此地址无法获取到数据，则会等待超时或报错"
                                        },
                                        "add": {
                                            "addApi": "添加API",
                                            "editApi": "修改API",
                                            "baseInfoTab": "基本信息",
                                            "requestTab": "请求参数设置",
                                            "responseTab": "响应参数设置",
                                            "proxyTab": "代理设置",
                                            "testTab": "接口测试",
                                            "save": "保存",
                                            "paramValue": "参数值",
                                            "paramSpecification": "参数说明",
                                            "baseInfo": {
                                                "belongTo": "所属项目",
                                                "interfaceName": "接口名称",
                                                "placeholder": "请输入接口名称"
                                            },
                                            "request": {
                                                "methodTypes": "请求类型",
                                                "interfaceUrl": "接口地址",
                                                "proxyPrefixTips": "此项目下所有接口统一加上前缀",
                                                "urlFormat": "支持RESTful格式，示例如：a/:id/:name",
                                                "getParamsTitle": "Get参数说明",
                                                "getParamsTip": "说明：接口地址中【?】query参数说明",
                                                "headersParamsTitle": "请求header参数说明",
                                                "headersParamsTips": "说明：发送请求的header信息参数说明",
                                                "requestHeadersTitle": "请求header信息",
                                                "requestHeadersTips": "说明：发送请求的header信息参数值",
                                                "urlMatchMode": "Url匹配方式",
                                                "exactMatch": "精准匹配",
                                                "exactMatchTips": "精准匹配时,只有路径完全一致时才会匹配",
                                                "incompleteMatch": "只匹配【?】前部分",
                                                "incompleteMatchTips": "只匹配【?】则以下都会匹配:website/articles/comment<br>website/articles/comment?post_id=xx&rrr=vvv...",
                                                "requestBodyTitleDesc": "请求 body 参数说明",
                                                "requestBodyTitleTips": "post,put等非URL带的参数，如post表单提交的数据字段",
                                                "requestBodyTitle": "请求 body 参数"
                                            },
                                            "response": {
                                                "responseTitleDesc": "响应数据参数说明",
                                                "responseContentTitle": "响应数据",
                                                "responseStatusCode": "返回状态码",
                                                "responseStatusCodeTips": "返回状态码，默认200",
                                                "delayTime": "延迟返回数据",
                                                "delayTimeTips": "延迟单位为毫秒，默认0<br>注意：请求数据本身会消耗一定时间，所以返回数据的时候会大于设置的延迟时间",
                                                "mockjsTitle": "开启mockjs",
                                                "open": "开启",
                                                "close": "关闭",
                                                "mockjsSite": "mockjs官网",
                                                "responseHeadersDesc": "响应header参数说明",
                                                "responseHeadersTitle": "响应header参数",
                                                "responseHeadersTips": "添加返回接口的 header信息"
                                            },
                                            "proxy": {
                                                "globalProxyUrl": "全局二次代理Url路径",
                                                "projectProxyUrl": "项目二次代理Url",
                                                "proxyUrl": "二次代理Url",
                                                "openProxyTipsPrefix": "开启时，接口将从：",
                                                "openProxyTipsPostfix": "返回数据",
                                                "closeProxyTips": "关闭时，将从响应数据设置后返回模拟数据",
                                                "format": "格式如:http://192.168.1.2/",
                                                "proxySwitch": "开启二次代理",
                                                "close": "关闭",
                                                "open": "开启"
                                            },
                                            "test": {}
                                        },
                                        "controller": {
                                            "projectName": "全部接口",
                                            "APIList": "接口列表",
                                            "cloneError": "复制的数据不存在",
                                            "deleteSuccess": "删除成功",
                                            "idIsNotExist": "ID不存在",
                                            "dataIsEmpty": "数据为空:点击返回列表",
                                            "apiIsExist": "修改失败:接口地址已存在：",
                                            "RESTfulApiIsExist": "修改失败:相同规则的RESTful接口地址已存在：",
                                            "editSuccess": "修改成功",
                                            "editFail": "修改失败",
                                            "returnList": "返回列表",
                                            "details": "查看接口",
                                            "actionError": "操作失败",
                                            "addApiIsExist": "添加失败:接口地址已存在：",
                                            "addRESTfullApiIsExist": "添加失败:相同规则的RESTful接口地址已存在：",
                                            "addSuccess": "添加成功",
                                            "mockIdIsEmpty": "mock id 为空",
                                            "proxyIsEmpty": "is_proxy为空",
                                            "cloneFailed": "复制失败",
                                            "editAgain": "返回修改"
                                        }
                                    },
                                    "system": {
                                        "systemSettings": "系统设置",
                                        "globalProxyUrl": "全局二次代理前缀",
                                        "globalProxyUrlTips": "必须设置。<br>作用1：在二次代理开启时，用于模拟数据与真实数据之间切换。<br>假设：全局二次代理前缀：[http://192.168.1.2/]<br>接口地址为：[/api/a/b]，二次代理关闭时，获取模拟数据，二次代理开启时，将获取[http://192.168.1.2/api/a/b]的数据。<br>作用2：假设接口：[/api/dddd],并没有在系统中创建，系统将会请求[http://192.168.1.2/api/dddd],并返回数据",
                                        "format": "格式如:http://192.168.1.2/",
                                        "ProxyHeaders": "代理header设置",
                                        "BlackList": "启用黑名单",
                                        "BlackListPlaceHolder": "header黑名单，一行一个",
                                        "BlackListDesc": "当启用黑名单时，二次代理开启后，当前指定header字段将不会被传递到二次代理的服务上,为空时，默认值为 host 和 accept-encoding两个header字段，<br>原因为：如果请求的url域名与代理的 host 不一致时，二次代理会报错<br>accept-encoding:代理后，容易出现乱码",
                                        "WhiteList": "启用白名单",
                                        "WhiteListPlaceHolder": "header白名单，一行一个",
                                        "WhiteListDesc": "当启用白名单时，二次代理开启后，将只传递当前指定header字段到二次代理服务上",
                                        "save": "保存全局设置",
                                        "controller": {
                                            "dataIsEmpty": "请设置全局参数",
                                            "updateSuccess": "设置成功",
                                            "updateFailed": "操作失败",
                                            "langIsEmpty": "错误：参数 lang 为空",
                                            "hostIsEmpty": "错误：参数 Host为空",
                                            "portIsEmpty": "错误：参数 port 为空",
                                            "databaseIsEmpty": "错误：数据库名称 为空",
                                            "userIsEmpty": "错误：数据用户名为空",
                                            "editAgain": "返回修改"
                                        },
                                        "init": {
                                            "initDataBase": "初始化数据库",
                                            "database": "数据库名称",
                                            "user": "数据库用户名",
                                            "password": "数据库密码",
                                            "start": "开始初始化"
                                        }
                                    },
                                    "api": {
                                        "multipleInterfaceError": "有多个接口使用了此路径",
                                        "headerFormatError": "response header信息格式错误",
                                        "proxyIsEmptyError": "没有获取二次代理Url",
                                        "globalProxyIsEmptyError": "没有获取全局二次代理Url",
                                        "getProxyDataError": "获取数据错误,可能是接口不存在,或参数错误,错误信息:"
                                    },
                                    "swagger": {"swaggerAdmin": "Swagger管理", "swagger": "Swagger"},
                                    "validate_required": "{name} can not be blank",
                                    "validate_contains": "{name} need contains {args}",
                                    "validate_equals": "{name} need match {args}",
                                    "validate_different": "{name} nedd not match {args}",
                                    "validate_after": "{name} need a date that's after the {args} (defaults to now)",
                                    "validate_alpha": "{name} need contains only letters (a-zA-Z)",
                                    "validate_alphaDash": "{name} need contains only letters and dashes(a-zA-Z_)",
                                    "validate_alphaNumeric": "{name} need contains only letters and numeric(a-zA-Z0-9)",
                                    "validate_alphaNumericDash": "{name} need contains only letters, numeric and dash(a-zA-Z0-9_)",
                                    "validate_ascii": "{name} need contains ASCII chars only",
                                    "validate_base64": "{name} need a valid base64 encoded",
                                    "validate_before": "{name} need a date that's before the {args} (defaults to now)",
                                    "validate_byteLength": "{name} need length (in bytes) falls in {args}",
                                    "validate_creditcard": "{name} need a valid credit card",
                                    "validate_currency": "{name} need a valid currency amount",
                                    "validate_date": "{name} need a date",
                                    "validate_decimal": "{name} need a decimal number",
                                    "validate_divisibleBy": "{name} need a number that's divisible by {args}",
                                    "validate_email": "{name} need an email",
                                    "validate_fqdn": "{name} need a fully qualified domain name",
                                    "validate_float": "{name} need a float in {args}",
                                    "validate_fullWidth": "{name} need contains any full-width chars",
                                    "validate_halfWidth": "{name} need contains any half-width chars",
                                    "validate_hexColor": "{name} need a hexadecimal color",
                                    "validate_hex": "{name} need a hexadecimal number",
                                    "validate_ip": "{name} need an IP (version 4 or 6)",
                                    "validate_ip4": "{name} need an IP (version 4)",
                                    "validate_ip6": "{name} need an IP (version 6)",
                                    "validate_isbn": "{name} need an ISBN (version 10 or 13)",
                                    "validate_isin": "{name} need an ISIN (stock/security identifier)",
                                    "validate_iso8601": "{name} need a valid ISO 8601 date",
                                    "validate_in": "{name} need in an array of {args}",
                                    "validate_notIn": "{name} need not in an array of {args}",
                                    "validate_int": "{name} need an integer",
                                    "validate_min": "{name} need an integer greater than {args}",
                                    "validate_max": "{name} need an integer less than {args}",
                                    "validate_length": "{name} need length falls in {args}",
                                    "validate_minLength": "{name} need length is max than {args}",
                                    "validate_maxLength": "{name} need length is min than {args}",
                                    "validate_lowercase": "{name} need is lowercase",
                                    "validate_mobile": "{name} need is a mobile phone number",
                                    "validate_mongoId": "{name} need is a valid hex-encoded representation of a MongoDB ObjectId",
                                    "validate_multibyte": "{name} need contains one or more multibyte chars",
                                    "validate_url": "{name} need an URL",
                                    "validate_uppercase": "{name} need uppercase",
                                    "validate_variableWidth": "{name} need contains a mixture of full and half-width chars",
                                    "validate_order": "{name} need a valid sql order string",
                                    "validate_field": "{name} need a valid sql field string",
                                    "validate_image": "{name} need a valid image file",
                                    "validate_startWith": "{name} need start with {args}",
                                    "validate_endWidth": "{name} need end with {args}",
                                    "validate_string": "{name} need a string",
                                    "validate_array": "{name} need an array",
                                    "validate_boolean": "{name} need a boolean",
                                    "validate_object": "{name} need an object"
                                }
                            })
                        </script>

                    </div>
                </div>
                <div class="page-footer">
                    <ul class="pull-left">
                        <li>github：github.com/flftfqwxf</li>
                        <li>qq：120377576</li>
                    </ul>
                </div>
            </div>
        </div>
    </div>
</div>
<script src="/static/common/js/common.js"></script>
<script>
    window.common.init()
</script>
<script type="text/javascript">var cnzz_protocol = (("https:" == document.location.protocol) ? " https://" : " http://");
document.write(unescape("%3Cspan id='cnzz_stat_icon_1261788850'%3E%3C/span%3E%3Cscript src='" + cnzz_protocol + "s11.cnzz.com/z_stat.php%3Fid%3D1261788850' type='text/javascript'%3E%3C/script%3E"));</script>
</body>
</html>