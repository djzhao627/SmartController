<!DOCTYPE html>
<#assign ctx=request.contextPath>
<html>
<head>
    <meta charset="utf-8">
    <title>首页</title>
    <link href="${ctx}/css/bootstrap.css" rel="stylesheet">
    <link href="${ctx}/css/bootstrap-table.css" rel="stylesheet">
    <style>
        body {
            position: relative;
        }

        #message {
            position: absolute;
            width: 100%;
            top: 10px;
            left: 0;
            display: none;
        }
    </style>
</head>
<body>
<div id="message" class="row">
    <div class="alert alert-success col-md-6 col-md-offset-6" role="alert">更新成功！</div>
</div>
<!-- Modal -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="myModalLabel">Modal title</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="form">
                    <input type="hidden" name="deviceId" id="deviceId">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">设备名称</label>
                        <div class="col-sm-10">
                            <input id="deviceName" type="text" class="form-control" name="name"
                                   placeholder="设备名称">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">调节单位</label>
                        <div class="col-sm-10">
                            <input id="deviceUnit" type="text" name="unit" class="form-control" placeholder="调节单位">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">默认值</label>
                        <div class="col-sm-10">
                            <input id="deviceValue" type="text" name="value" class="form-control" placeholder="默认值">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                <button type="button" id="btn-submit" class="btn btn-primary">添加</button>
                <button type="button" id="btn-update" class="btn btn-primary">更新</button>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <h2>室内环境
        <button type="button" id="update-env" class="btn btn-success btn-xs">更新</button>
    </h2>
    <hr>
    <div class="row">
        <div class="col-md-3 text-center">
            <img width="40%" src="ic_leaf.png" alt="">
            <br>
            <br>
            <div class="dropdown">
                <input type="hidden" id="sound" value="安静"/>
                <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu" data-toggle="dropdown"
                        aria-haspopup="true" aria-expanded="true">
                    安静
                    <span class="caret"></span>
                </button>
                <ul class="dropdown-menu" aria-labelledby="dropdownMenu">
                    <li><a href="#" class="sound-env">安静</a></li>
                    <li><a href="#" class="sound-env">嘈杂</a></li>
                </ul>
            </div>
        </div>
        <div class="col-md-3 text-center">
            <img width="40%" src="ic_sun.png" alt="">
            <br>
            <br>
            <form class="form-inline">
                <div class="form-group">
                    <input id="light" type="text" class="form-control" placeholder="光线状态">
                </div>
            </form>
        </div>
        <div class="col-md-3 text-center">
            <img width="40%" src="ic_temperature.png" alt="">
            <br>
            <br>
            <form class="form-inline">
                <div class="input-group">
                    <input id="temperature" type="text" class="form-control" placeholder="室内温度">
                    <span class="input-group-addon">℃</span>
                </div>
            </form>
        </div>
        <div class="col-md-3 text-center">
            <img width="40%" src="ic_water_dot.png" alt="">
            <br>
            <br>
            <form class="form-inline">
                <div class="input-group">
                    <input id="water" type="text" class="form-control" placeholder="室内湿度">
                    <span class="input-group-addon">%</span>
                </div>
            </form>
        </div>
    </div>
    <div class="row">
        <h2>智能设备列表
            <button type="button" id="add" data-target="#myModal" data-toggle="modal" class="btn btn-primary btn-xs"
                    style="margin-left: 10px">新增
            </button>
            <button type="button" id="del" class="btn btn-success btn-xs">删除</button>
        </h2>
        <hr>
        <table id="table" data-toggle="table" data-side-pagination="server" data-search="true"
               data-pagination="true" data-query-params="queryParams" data-pagination-loop="false"
               data-click-to-select="true" data-url="${ctx}/index/getTableData">
            <thead>
            <tr>
                <th data-checkbox="true"></th>
                <th data-field="id" data-align="center" data-sortable="true">id</th>
                <th data-field="name" data-align="center">设备名</th>
                <th data-field="unit" data-align="center" data-sortable="true">调节单位</th>
                <th data-field="value" data-align="center">当前状态</th>
                <th data-field="operate" data-formatter="format" data-align="center">更新</th>
            </tr>
            </thead>
        </table>
    </div>
</div>
<script src="${ctx}/js/jquery.min.js"></script>
<script src="${ctx}/js/bootstrap.js"></script>
<script src="${ctx}/js/bootstrap-table.js"></script>
<script src="${ctx}/js/bootstrap-table-zh-CN.js"></script>
<script type="text/javascript">

    function format() {
        return "<a href='#'>编辑</a>"
    }

    function queryParams(params) {
        var query = {};
        query["limit"] = params.limit;//第几条数据开始
        query["offset"] = params.offset;//数据大小
        query["search"] = params.search;//搜索
        query["sort"] = params.sort;//搜索字段
        query["order"] = params.order;//排序
        return query;
    }

    $("#add").click(function () {
        $("#myModalLabel").text("新增");
        $("#btn-submit").show();
        $("#btn-update").hide();
    });

    //提交
    $("#btn-submit").click(function () {
        $.post("${ctx}/index/add", $('#form').serializeArray(), function (result) {
            $('#myModal').modal('hide');
            location.reload();
        })
    });

    //更新
    $("#btn-update").click(function () {
        $.post("${ctx}/index/update", $('#form').serializeArray(), function (result) {
            $('#myModal').modal('hide');
            location.reload();
        })
    });

    $("#del").click(function () {
        var select = $("#table").bootstrapTable('getSelections');
        //批量删除
        var ids = "";
        for (var i = 0; i < select.length; i++) {
            ids += "," + select[i].id;
        }
        ids = ids.substr(1);
        if (ids != "") {
            $.post("${ctx}/index/delete", {ids: ids}, function (result) {
                location.reload();
            })
        }
    });

    $('#table').on('click-cell.bs.table', function (e, field, value, row, $element) {
        if (field === 'operate') {
            $('#myModal').modal('show');
            $("#deviceId").val(row.id);
            $("#deviceName").val(row.name);
            $("#deviceUnit").val(row.unit);
            $("#deviceValue").val(row.value);
            $("#myModalLabel").text("更新");
            $("#btn-update").show();
            $("#btn-submit").hide();
        }
    });

    $.post("${ctx}/index/getEnv", {}, function (result) {
        let vals = result.split("|");
        $("#dropdownMenu").text(vals[0]);
        $("#sound").val(vals[0]);
        $("#light").val(vals[1]);
        $("#temperature").val(vals[2]);
        $("#water").val(vals[3]);
    });

    $(".sound-env").click(function () {
        let text = $(this).text();
        $("#sound").val(text);
        $("#dropdownMenu").text(text);
    });

    $("#update-env").click(function () {
        let value = [
            $("#sound").val(),
            $("#light").val(),
            $("#temperature").val(),
            $("#water").val()]
            .join("|");
        $.post("${ctx}/index/updateEnv",
            {
                value
            },
            function (result) {
                if (result > 0) {
                    $("#message").fadeIn();
                    setTimeout(function () {
                        $("#message").fadeOut();
                    }, 2000);
                }
            });
    });

</script>
</body>
</html>