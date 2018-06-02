/**
 * Created by deng on 2017/11/21.
 */
$(document).ready(function () {
    var firstGroupId = loadGroupList();
    loadStudentsTable(firstGroupId);
});

function loadGroupList() {
    var firstGroupId = "";
    $.ajax({
        type: 'GET',
        url: "/groups/getAll",
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.success) {
                var groups = data.data;
                var select = $("#groups");
                var sSelect = $("#sGroups");
                for (var i = 0; i < groups.length; i++) {
                    select.append("<option id='" + groups[i].id + "' value='" + groups[i].name + "'>"
                        + groups[i].name + "</option>");
                    sSelect.append("<option id='" + groups[i].id + "' value='" + groups[i].name + "'>"
                        + groups[i].name + "</option>");
                }
                $("#groups").selectpicker('refresh')
                if (groups.length > 0) {
                    firstGroupId = groups[0].id;
                }
            }
        }
    });
    return firstGroupId;
}

function loadStudentsTable(groupId) {
    if ($('#students').hasClass('dataTable')) {
        var studentsTable = $('#students').dataTable();
        studentsTable.fnClearTable(); //清空一下table
        studentsTable.fnDestroy(); //还原初始化了的datatable
    }
    $('#students').dataTable({
        aaSorting: [[1, "asc"]], //默认的排序方式，第2列，升序排列
        ajax: {
            url: "/users/getStudentsByGroup",
            type: "GET",
            dataSrc: "data",
            data: {groupId: groupId}
        },
        columns: [
            {data: "username"},
            {data: "name"},
            {data: "code"},
            {
                data: "username", render: function (data) {
                return "<button onclick='deleteStudentFromGroup(\"" + data + "\")' class='btn btn-fill'>删除</button>";
            }
            }
        ]
    });
}

function deleteStudentFromGroup(studentId) {
    $.ajax({
        url: "/users/deleteStudentFromGroup",
        type: "POST",
        data: {
            groupId: $('#sGroups option:selected').attr('id'),
            username: studentId
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