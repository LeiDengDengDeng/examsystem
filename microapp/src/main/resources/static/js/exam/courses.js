/**
 * Created by deng on 2017/11/23.
 */
$(document).ready(function () {
    loadCoursesTable();
});

function loadCoursesTable() {
    $('#courses').dataTable({
        ajax: {
            url: "/courses/getAll",
            type: "GET",
            dataSrc: "data"
        },
        columns: [
            {data: "name"},
            {data: "profile"}
        ]
    });
}

function addCourse() {
    var name = $('#name').val();
    var profile = $('#profile').val();
    if (name == "" || profile == "") {
        failNotice("课程名称和介绍不能为空")
    } else {
        $.ajax({
            url: "/courses/addCourse",
            type: "POST",
            data: {
                name: name,
                profile: profile
            },
            timeout: 5000,
            success: function (data) {
                if (!data) {
                    failNotice("保存失败");
                }else{
                    location.reload(true);
                }
            },
            error: function (xhr, textStatus) {
                failNotice("网络或服务器错误");
            }
        })
    }
}