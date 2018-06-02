/**
 * Created by deng on 2017/11/21.
 */
$(document).ready(function () {
    loadExamsTable();
});

function loadExamsTable() {
    $('#exams').dataTable({
        order: [[ 2, "desc" ]],
        ajax: {
            url: "/exams/getStudentExams",
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
                data: "totalScore", render: function (data) {
                return data + "分";
            }
            },
            {
                data: "state", render: function (data, type, row) {
                if (data == "未开始") {
                    return data;
                } else {
                    return '<button style="background-color: #cef6f4;border-radius: 5px;"><a target="_blank" href="/student/exams/' + row.id + '" style="color: black">' + data + '</a></button>';
                }
            }
            }
        ]
    });
}