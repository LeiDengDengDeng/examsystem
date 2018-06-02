/**
 * Created by deng on 2017/10/15.
 */
function studentTabClick() {
    if ($("#registryTab").hasClass("active")) {
        $("#registryTab").removeClass("active");
    }
    $("#studentTabPage").show();
    $("#registryTabPage").hide();
}

function registryTabClick() {
    $("#studentTab").removeClass("active");
    $("#studentTabPage").hide();
    $("#registryTabPage").show();
}

function loginSubmit() {
    $.ajax({
        url: "/users/login",
        type: "POST",
        data: {
            username: $("#username").val(),
            password: $("#password").val()
        },
        timeout: 5000,
        success: function (data) {
            if (data.success) {
                var role = data.data;
                if (role == "student") {
                    $(location).attr('href', '/student/homepage');
                } else if (role == "teacher") {
                    $(location).attr('href', '/teacher/homepage');
                }
            } else {
                failNotice("登录失败");
            }
        },
        error: function (xhr, textStatus) {
        }
    })
}

function registerSubmit() {
    $("#registryTab").addClass("active");

    var username = $("#rUsername").val();
    var password = $("#rPassword").val();
    var name = $("#name").val();
    var code = $("#code").val();

    var emailReg = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
    if (username == "" || !emailReg.test(username) || password == "" || name.length == "" || code.length == "") {
        failNotice("输入不合法");
    } else {
        $.ajax({
            url: "/users/register",
            type: "POST",
            data: {
                username: username,
                password: password,
                name: name,
                code: code
            },
            timeout: 5000,
            success: function (data) {
                var jsonData = JSON.parse(data);
                if (jsonData.success) {
                    successNotice("注册成功");
                    $("#rUsername").val("")
                    $("#rPassword").val("")
                    $("#name").val("")
                    $("#code").val("")

                } else {
                    failNotice(jsonData.msg);
                }
            },
            error: function (xhr, textStatus) {
                failNotice("网络或服务器错误");
            }
        })
    }
}
