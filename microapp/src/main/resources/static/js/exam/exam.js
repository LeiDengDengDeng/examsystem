/**
 * Created by deng on 2017/10/31.
 */
var paperId;

// 用于在错误页面自动返回考试列表页面
var second = 5;
var interval;

var examInfo;

$(document).ready(function () {
    loadExamInfo();
});

function loadExamInfo() {
    var examId = window.location.href.substring(window.location.href.lastIndexOf('/') + 1);
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
                    examInfo = jsonData.data;

                    $("#notice").hide();
                    $("#exam").show();

                    $("#name").text(jsonData.data.name);
                    $("#course").text(jsonData.data.courseName);
                    $("#startTime").text(jsonData.data.startTime.substring(0, 19));
                    $("#endTime").text(jsonData.data.endTime.substring(0, 19));
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

/**
 * 倒计时五秒后返回考试列表页面
 */
function errorBack() {
    if (second == 0) {
        window.location.href = "/student/exams";
        clearInterval(interval);
    }
    $("#secondContent").text(second + "秒后返回考试列表页面...");
    second--;
}

function startExam() {
    var examId = window.location.href.substring(window.location.href.lastIndexOf('/') + 1);
    var code = $("#code").val();

    $.ajax({
        url: "/papers/generatePaper",
        type: "POST",
        data: {
            examId: examId,
            code: code
        },
        timeout: 5000,
        success: function (data) {
            if (data.success) {
                var paperData = JSON.parse(data.data);
                if (paperData.handed) {
                    failNotice("您已提前交卷");
                } else {
                    $("#exam").hide();
                    $("#paper").show();
                    paperId = paperData.paperId;
                    generatePaperPage(paperData.questions);
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
 * 加载考试试题页面
 */
function generatePaperPage(questions) {
    countTime();
    $("#paperName").text(examInfo.name);
    for (var i = 0; i < questions.length; i++) {
        // 生成每题
        var question = questions[i];
        var questionHtml = "";
        if (question.isMarked) {
            questionHtml += ('<div class="card question" style="background-color: #cef6f4" id="question_' + question.questionId + '">');
        } else {
            questionHtml += ('<div class="card question" id="question_' + question.questionId + '">');
        }
        questionHtml += ('<div class="content"><div class="row"><p class="col-lg-1 score">(' + question.score + '分)</p><p class="col-lg-7 title">' + (i + 1) + '、' + question.description.substring(1, question.description.length - 1) + '</p><div class="col-lg-2 pull-right"><span class="title pull-right"> ');
        if (question.isMarked) {
            questionHtml += ('<input id="check_' + question.questionId + '" class="checkbox" name="check" type="checkbox" onchange="markQuestion(this,' + question.questionId + ')" checked>');
        } else {
            questionHtml += ('<input id="check_' + question.questionId + '" class="checkbox" name="check" type="checkbox" onchange="markQuestion(this,' + question.questionId + ')">');
        }
        questionHtml += ('<label for="check_' + question.questionId + '" class="trigger"></label>&nbsp;&nbsp;标记</span></div></div> <hr/>');

        var letters = ["A", "B", "C", "D"];
        for (var x = 0; x < question.options.length; x++) {
            var option = question.options[x];
            questionHtml += '<div class="row option"><span class="col-lg-1 score">';
            if (option.isSelected) {
                questionHtml += '<input type = "checkbox" onchange="selectOption(this,' + option.optionId + ')" checked>'
            } else {
                questionHtml += '<input type = "checkbox" onchange="selectOption(this,' + option.optionId + ')">'
            }
            questionHtml += '</span><p class="col-lg-8">' + letters[x] + '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;' + option.content.substring(1, option.content.length - 1) + '</p></div>';
        }
        questionHtml += ('</div></div>');
        $("#questions").append(questionHtml);

        // 生成对应汇总按钮
        var numBtnDiv = $('<div></div>');
        numBtnDiv.addClass("col-lg-2")
        var numBtn = $('<button></button>');
        numBtn.addClass("num-btn center-block");
        if (question.isMarked) {
            numBtn.addClass("blue");
        }
        numBtn.text(i + 1);
        numBtn.attr("id", "numBtn_" + question.questionId);
        numBtn.attr("onclick", 'anchor("question_' + question.questionId + '")');
        numBtn.appendTo(numBtnDiv);
        numBtnDiv.appendTo("#anchors");
    }
}

function markQuestion(questionInput, questionId) {
    var marked = questionInput.checked;
    if (marked) {
        questionInput.parentNode.parentNode.parentNode.parentNode.parentNode.style = "background-color: #cef6f4";
        $("#numBtn_" + questionId).addClass("blue");
    } else {
        questionInput.parentNode.parentNode.parentNode.parentNode.parentNode.style = "";
        $("#numBtn_" + questionId).removeClass("blue");
    }
    $.ajax({
        url: "/papers/markQuestion",
        type: "POST",
        data: {
            paperId: paperId,
            questionId: questionId,
            marked: marked
        },
        timeout: 5000,
        success: function (data) {
            if (!data) {
                failNotice("保存失败");
            }
        },
        error: function (xhr, textStatus) {
            failNotice("网络或服务器错误");
        }
    })
}

function selectOption(optionInput, optionId) {
    var selected = optionInput.checked;
    $.ajax({
        url: "/papers/selectOption",
        type: "POST",
        data: {
            paperId: paperId,
            optionId: optionId,
            selected: selected
        },
        timeout: 5000,
        success: function (data) {
            if (!data) {
                failNotice("保存失败");
            }
        },
        error: function (xhr, textStatus) {
            failNotice("网络或服务器错误");
        }
    })
}

function handPaper() {
    $.ajax({
        url: "/papers/hand",
        type: "POST",
        data: {
            paperId: paperId
        },
        timeout: 5000,
        success: function (data) {
            if (data) {
                location.reload(true);
            } else {
                failNotice("交卷失败");
            }
        },
        error: function (xhr, textStatus) {
            failNotice("网络或服务器错误");
        }
    })
}

function anchor(id) {
    $('#' + id)[0].scrollIntoView(true);
}

function getFinishedPaper() {
    var examId = window.location.href.substring(window.location.href.lastIndexOf('/') + 1);

    $.ajax({
        url: "/papers/getPaper",
        type: "GET",
        data: {
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

/**
 * 考试时间倒计时
 */
function countTime() {
    // 剩余时间
    var leftTime = new Date(examInfo.endTime).getTime() - new Date().getTime();

    var d, h, m, s;
    if (leftTime >= 0) {
        d = Math.floor(leftTime / 1000 / 60 / 60 / 24);
        h = addZero(Math.floor(leftTime / 1000 / 60 / 60 % 24));
        m = addZero(Math.floor(leftTime / 1000 / 60 % 60));
        s = addZero(Math.floor(leftTime / 1000 % 60));
    }

    // 每秒执行一次
    setTimeout(countTime, 1000);

    var timeStr = "&nbsp;" + d + "天" + "&nbsp;" + h + ":" + m + ":" + s;
    $('#time').html(timeStr);
}

/**
 * 给不足两位的数补一个零
 * @param i
 * @returns {*}
 */
function addZero(i) {
    if (i < 10) {
        i = "0" + i;
    }
    return i;
}