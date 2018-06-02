/**
 * Created by deng on 2017/11/23.
 */
$(document).ready(function () {
    loadGroupsTable();
});

function loadGroupsTable() {
    $('#groups').dataTable({
        ajax: {
            url: "/groups/getAll",
            type: "GET",
            dataSrc: "data"
        },
        columns: [
            {data: "name"},
            {
                data: "id", render: function (data) {
                return "<button onclick='deleteGroup(" + data + ")' class='btn btn-fill'>删除</button>"
            }
            }
        ]
    });
}

function addGroup() {
    var name = $('#name').val();
    if (name == "") {
        failNotice("班级名称不能为空")
    } else {
        $.ajax({
            url: "/groups/add",
            type: "POST",
            data: {
                name: name
            },
            timeout: 5000,
            success: function (data) {
                var jsonData = JSON.parse(data);
                if (!jsonData.success) {
                    failNotice("班级名称不能重复");
                } else {
                    location.reload(true);
                }
            },
            error: function (xhr, textStatus) {
                failNotice("网络或服务器错误");
            }
        })
    }
}

function deleteGroup(groupId) {
    $.ajax({
        url: "/groups/delete",
        type: "POST",
        data: {
            groupId: groupId
        },
        timeout: 5000,
        success: function (data) {
            var jsonData = JSON.parse(data);
            if (!jsonData.success) {
                failNotice("删除失败");
            } else {
                location.reload(true);
            }
        },
        error: function (xhr, textStatus) {
            failNotice("网络或服务器错误");
        }
    })
}