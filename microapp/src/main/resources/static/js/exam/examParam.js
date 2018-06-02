/**
 * Created by deng on 2017/11/23.
 */
var scoreNum = 1;
var scoreAndNumLimit = {};
var scoreFlag = true;
var studentFlag = true;

$(document).ready(function () {
    loadCourseList();
    loadGroupList();
    loadScore();
    loadStudents();
});

function loadCourseList() {
    $.ajax({
        type: 'GET',
        url: "/courses/getAll",
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.success) {
                var courseData = data.data;
                var select = $("#courses");
                for (var i = 0; i < courseData.length; i++) {
                    select.append("<option id='" + courseData[i].courseId + "'>"
                        + courseData[i].name + "</option>");
                }
            }
        }
    });
}

function loadGroupList() {
    $.ajax({
        type: 'GET',
        url: "/groups/getAll",
        dataType: 'json',
        async: false,
        success: function (data) {
            if (data.success) {
                var groupData = data.data;
                var select = $("#groups");
                for (var i = 0; i < groupData.length; i++) {
                    select.append("<option id='" + groupData[i].id + "'>"
                        + groupData[i].name + "</option>");
                }
            }
        }
    });
}

function addParam() {
    scoreNum++;
    $("#addBtn").remove();
    var scoreDiv = $('<div class="col-md-5" style="padding-left: 15px"> <select id="score_' + scoreNum + '" class="form-control border-input"></select> </div>');
    var numDiv = $('<div class="col-md-5"> <input id="num_' + scoreNum + '" type="number" class="form-control border-input"> </div>');
    var addBtn = $('<div class="col-md-2" id="addBtn"> <button class="form-control btn btn-fill"onclick="addParam()">添加</button> </div>');
    scoreDiv.appendTo('#params');
    numDiv.appendTo('#params');
    addBtn.appendTo('#params');
    loadScore();
}

function loadScore() {
    var courseId = $('#courses option:selected').attr('id');
    $("#questionCourse").html("&nbsp;所属课程：" + $('#courses option:selected').val());

    scoreAndNumLimit = {};
    $.ajax({
        type: 'GET',
        url: "/questions/getScoresByCourseId",
        dataType: 'json',
        data: {courseId: courseId},
        success: function (data) {
            if (data.success) {
                var scoreData = data.data;
                // score组件从1开始计数
                for (var n = 1; n <= scoreNum; n++) {
                    var scoreSelect = $("#score_" + n);
                    scoreSelect.empty();
                    for (var i = 0; i < scoreData.length; i++) {
                        scoreSelect.append("<option>" + scoreData[i].score + "分</option>");
                    }
                }

                var scoreContentDiv = $("#scoreContent");
                scoreContentDiv.empty();
                for (var i = 0; i < scoreData.length; i++) {
                    scoreAndNumLimit[scoreData[i].score] = scoreData[i].num;
                    var scoreHtml = '<li><div class="row"><div class="col-xs-4" style="text-align: center"><label>' + scoreData[i].score + '分</label></div><div class="col-xs-6">' + scoreData[i].num + '道</div></div></li>';
                    scoreContentDiv.append(scoreHtml);
                }
                if (scoreData.length == 0) {
                    var scoreHtml = '<li><div class="row"><div class="col-xs-8 col-xs-offset-1"><label><a href="/teacher/questions">暂无题目，立即导入</a></label></div></div></li>';
                    scoreContentDiv.append(scoreHtml);
                    scoreFlag = false;
                    $("#submitBtn").attr("disabled", true);
                } else {
                    scoreFlag = true;
                    $("#submitBtn").attr("disabled", !(scoreFlag && studentFlag));
                }
            }
        }
    });
}

function loadStudents() {
    var groupId = $('#groups option:selected').attr('id');
    $("#studentGroup").html("&nbsp;所属班级：" + $('#groups option:selected').val());

    $.ajax({
        type: 'GET',
        url: "/users/getStudentsByGroup",
        dataType: 'json',
        data: {groupId: groupId},
        success: function (data) {
            if (data.success) {
                var studentData = data.data;

                var studentContentDiv = $("#studentContent");
                studentContentDiv.empty();
                for (var i = 0; i < studentData.length; i++) {
                    var studentHtml = '<li><div class="row"><div class="col-xs-6 col-xs-offset-1">' + studentData[i].username + '</div></div></li>';
                    studentContentDiv.append(studentHtml);
                }
                if (studentData.length == 0) {
                    var studentHtml = '<li><div class="row"><div class="col-xs-8 col-xs-offset-1"><label><a href="/teacher/students">暂无学生，立即导入</a></label></div></div></li>';
                    studentContentDiv.append(studentHtml);
                    studentFlag = false;
                    $("#submitBtn").attr("disabled", true);
                } else {
                    studentFlag = true;
                    $("#submitBtn").attr("disabled", !(scoreFlag && studentFlag));
                }
            }
        }
    });
}

function submitExamParams() {
    var timeReg = /^(\d{4})\-(\d{2})\-(\d{2}) (\d{2}):(\d{2})$/;
    var startTime = $("#startTime").val().replace("T", " ");
    var endTime = $("#endTime").val().replace("T", " ");
    if ($("#name").val().length == 0 || !timeReg.test(startTime) || !timeReg.test(endTime)) {
        failNotice("考试参数不完整");
        return;
    }

    var scoreAndNumMap = {};
    for (var n = 1; n <= scoreNum; n++) {
        var score = $("#score_" + n).find("option:selected").val();
        score = score.substring(0, score.length - 1);
        var numInput = $("#num_" + n).val();

        if (score.length == 0 || numInput.length == 0 || numInput > scoreAndNumLimit[score] || numInput <= 0) {
            failNotice("题数输入不合法");
            return;
        }

        scoreAndNumMap[score] = numInput;
    }
    var courseId = $("#courses option:selected").attr("id");
    var groupId = $("#groups option:selected").attr("id");
    $.ajax({
        type: 'POST',
        url: "/exams/create",
        dataType: 'json',
        data: {
            examName: $("#name").val(),
            courseId: courseId,
            groupId: groupId,
            startTime: startTime.replace("T", " "),
            endTime: endTime.replace("T", " "),
            questions: scoreAndNumMap
        },
        success: function (data) {
            if (data.success) {
                successNotice("生成考试成功");

                $("input").val("");
            }
        }
    });
}