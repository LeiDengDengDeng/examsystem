/**
 * Created by deng on 2017/11/21.
 */
$(document).ready(function () {
    loadQuestionsTable();
    loadCourseList();
});

function loadQuestionsTable() {
    $('#questions').dataTable({
        aaSorting: [[1, "asc"]], //默认的排序方式，第2列，升序排列
        ajax: {
            url: "/questions/getAll",
            type: "GET",
            dataSrc: "data"
        },
        columns: [
            {data: "description"},
            {data: "score"},
            {data: "courseName"},
            {
                data: "optionNum", render: function (data, type, row) {
                return '<a href="#" onclick="openOptions(\'' + row.description + '\',' + row.id + ')">' + data + '</a>';
            }
            }
        ]
    });
}

function openOptions(description, questionId) {
    $('#optTitle').text(description);
    if ($('#optionsTable').hasClass('dataTable')) {
        var optionsTable = $('#optionsTable').dataTable();
        optionsTable.fnClearTable(); //清空一下table
        optionsTable.fnDestroy(); //还原初始化了的datatable
    }
    $('#optionsTable').dataTable({
        bAutoWidth: false,
        aLengthMenu: [5, 10],
        ajax: {
            url: "/questions/getOptionsByQuestionId",
            type: "GET",
            dataSrc: "data",
            data: {questionId: questionId}
        },
        columns: [
            {data: "content"},
            {
                data: "right", render: function (data) {
                if (data) {
                    return "正确";
                } else {
                    return "错误";
                }
            }
            }
        ]
    });
    $("#options").modal();
}

function loadCourseList() {
    $.ajax({
        type: 'GET',
        url: "/courses/getAll",
        dataType: 'json',
        success: function (data) {
            if (data.success) {
                var courseData = data.data;
                var select = $("#courses");
                for (var i = 0; i < courseData.length; i++) {
                    select.append("<option id='" + courseData[i].courseId + "' value='" + courseData[i].name + "'>"
                        + courseData[i].name + "</option>");
                }
                $("#courses").selectpicker('refresh')
            }
        }
    });
}