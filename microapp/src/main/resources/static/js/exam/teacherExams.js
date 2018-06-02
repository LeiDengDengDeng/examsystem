/**
 * Created by deng on 2017/11/21.
 */
var initialUrl;
$(document).ready(function () {
    loadExamsTable();
});

$(window).load(function () {
    initialUrl = $('#downloadUrl').attr('href');
});

function loadExamsTable() {
    $('#exams').dataTable({
        order: [[2, "desc"]],
        ajax: {
            url: "/exams/getAllExams",
            type: "GET",
            dataSrc: "data"
        },
        columns: [
            {data: "name"},
            {data: "courseName"},
            {
                data: "startTime", render: function (data) {
                return data.substring(0, 19);
            }
            },
            {
                data: "endTime", render: function (data) {
                return data.substring(0, 19);
            }
            },
            {
                data: "totalScore", render: function (data, type, row) {
                return '<a href="#" onclick="openExamParams(\'' + row.id + '\',\'' + row.name + '\')">' + data + '分</a>';
            }
            },
            {
                data: "state", render: function (data, type, row) {
                if (data == "已结束") {
                    return '<button style="background-color: #cef6f4;border-radius: 5px;" onclick="openPapers(\'' + row.id + '\',\'' + row.name + '\')">查看成绩</button>';
                } else {
                    return data;
                }
            }
            }
        ]
    });
}

function openExamParams(examId, examName) {
    $('#examParamsTitle').text("【" + examName + "】考试参数");
    if ($('#examParamsTable').hasClass('dataTable')) {
        var examParamsTable = $('#examParamsTable').dataTable();
        examParamsTable.fnClearTable(); //清空一下table
        examParamsTable.fnDestroy(); //还原初始化了的datatable
    }
    $('#examParamsTable').dataTable({
        bAutoWidth: false,
        aLengthMenu: [5, 10],
        ajax: {
            url: "/exams/getExamParams",
            type: "GET",
            dataSrc: "data",
            data: {examId: examId}
        },
        columns: [
            {
                data: "score", render: function (data) {
                return data + "分";
            }
            },
            {
                data: "num", render: function (data) {
                return data + "题";
            }
            }
        ]
    });
    $("#examParams").modal();
}

function openPapers(examId, examName) {
    $('#downloadUrl').attr('href', initialUrl + examId + "/generateTranscript");
    $('#papersTitle').text("【" + examName + "】考生情况");
    if ($('#papersTable').hasClass('dataTable')) {
        var papersTable = $('#papersTable').dataTable();
        papersTable.fnClearTable(); //清空一下table
        papersTable.fnDestroy(); //还原初始化了的datatable
    }
    $('#papersTable').dataTable({
        bAutoWidth: false,
        aLengthMenu: [5, 10],
        ajax: {
            url: "/papers/getPapers",
            type: "GET",
            dataSrc: "data",
            data: {examId: examId}
        },
        columns: [
            {data: "studentId"},
            {
                data: "score", render: function (data) {
                return data + "分";
            }
            },
            {
                data: "examId", render: function (data, type, row) {
                return '<button style="background-color: #ffffff;border-radius: 5px;"><a target="_blank" href="/teacher/exams/' + data + '?studentId=' + row.studentId + '">查看试卷</a></button>';
            }
            }
        ]
    });
    $("#papers").modal();
}