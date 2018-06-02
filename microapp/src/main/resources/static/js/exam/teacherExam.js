/**
 * Created by deng on 2017/12/6.
 */
$(document).ready(function () {
    loadExamInfo();
});

function loadExamInfo() {
    var examId = window.location.href.substring(window.location.href.lastIndexOf('/') + 1,window.location.href.lastIndexOf('?'));
    $.ajax({
        url: "/exams/getExam",
        type: "GET",
        data: {
            examId: examId
        },
        timeout: 5000,
        success: function (data) {
            var jsonData = JSON.parse(data);
            if (jsonData.success) {
                if (jsonData.data.state == "未开始") {
                    noticeShow("考试未开始 TUT");
                } else if (jsonData.data.state == "已结束") {
                    examInfo = jsonData.data;
                    getFinishedPaper();
                } else {
                    noticeShow("考试进行中 TUT");
                }
            } else {
                noticeShow("考试不存在 TUT");
            }
        },
        error: function (xhr, textStatus) {
            failNotice("网络或服务器错误");
        }
    })
}

/**
 * 显示错误提示
 * @param content 错误提示内容
 */
function noticeShow(content) {
    $("#notice").show();
    $("#noticeContent").text(content);
    interval = setInterval("errorBack()", 1000)
}

function getFinishedPaper() {
    var examId = window.location.href.substring(window.location.href.lastIndexOf('/') + 1,window.location.href.lastIndexOf('?'));
    var studentId = window.location.href.substring(window.location.href.lastIndexOf('=') + 1);

    $.ajax({
        url: "/papers/getPaperByTeacher",
        type: "GET",
        data: {
            studentId: studentId,
            examId: examId
        },
        timeout: 5000,
        success: function (data) {
            var jsonData = JSON.parse(data);
            if (jsonData.success) {
                var paperData = JSON.parse(jsonData.data);
                if (paperData.score == undefined) {
                    // 考试已结束但试卷未批改完成
                    noticeShow("试卷正在疯狂批改中 TUT");
                } else {
                    $("#notice").hide();
                    $("#finishedPaper").show();
                    $("#myScore").html("&nbsp;得分：" + paperData.score + "分／总分：" + examInfo.totalScore + "分");
                    generateFinishedPaperPage(paperData.questions);
                }
            } else {
                failNotice("验证失败");
            }
        },
        error: function (xhr, textStatus) {
            failNotice("网络或服务器错误");
        }
    })
}

/**
 * 加载已批改的试卷页面
 */
function generateFinishedPaperPage(questions) {
    $("#finishedPaperName").text(examInfo.name);
    for (var i = 0; i < questions.length; i++) {
        // 生成每题
        var question = questions[i];
        var questionHtml = "";
        if (question.isScored) {
            questionHtml += ('<div class="card question">');
        } else {
            questionHtml += ('<div class="card question" style="background-color: #fff3f3">');
        }
        questionHtml += ('<div class="content"><div class="row"><p class="col-lg-1 score">(' + question.score + '分)</p><p class="col-lg-7 title">' + (i + 1) + '、' + question.description.substring(1, question.description.length - 1) + '</p></div> <hr/>');

        var rightAnswers = ""; // 正答
        var myAnswers = ""; // 考生答案
        var letters = ["A", "B", "C", "D"];
        for (var x = 0; x < question.options.length; x++) {
            var option = question.options[x];
            questionHtml += '<div class="row option"><span class="col-lg-1 score">';
            if (option.isSelected) {
                questionHtml += '<input type = "checkbox" disabled checked>'
                myAnswers += letters[x] + "、";
            } else {
                questionHtml += '<input type = "checkbox" disabled>'
            }
            questionHtml += '</span><p class="col-lg-8">' + letters[x] + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + option.content.substring(1, option.content.length - 1) + '</p></div>';

            if (option.isRight) {
                rightAnswers += letters[x] + "、";
            }
        }
        rightAnswers = rightAnswers.substring(0, rightAnswers.length - 1);
        questionHtml += '<hr/>';

        if (question.isScored) {
            // 答对
            questionHtml += '<div class="row" style="color: #23b348"><p style="margin-left: 20px;">正确答案：' + rightAnswers + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;你答对了</p></div>';
        } else if (myAnswers.length == 0) {
            // 未作答
            questionHtml += '<div class="row" style="color: #ff4000"><p style="margin-left: 20px;">正确答案：' + rightAnswers + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;你没有选择任何选项</p></div>';
        } else {
            // 答错
            myAnswers = myAnswers.substring(0, myAnswers.length - 1);
            questionHtml += '<div class="row" style="color: #ff4000"><p style="margin-left: 20px;">正确答案：' + rightAnswers + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;你错选为：' + myAnswers + '</p></div>';
        }

        questionHtml += ('</div></div>');
        $("#finishedQuestions").append(questionHtml);
    }
}