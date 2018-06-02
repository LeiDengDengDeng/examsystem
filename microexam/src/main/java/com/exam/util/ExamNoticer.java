package com.exam.util;

/**
 * Created by disinuo on 17/11/27.
 */
public class ExamNoticer {

    public static String createEmailTitle(
            String examName,
            String examStartTime){
        return "【考试提醒-内含访问密码】你将在 "+examStartTime+" 有一场考试："+examName;
    }

    public static String createEmailContent(
            String examName,
            String examStartTime,
            String examEndTime,
            String userName,
            String password){
        return userName+"同学，你好！\n考试名称："+examName+
                "\n开始时间："+examStartTime+
                "\n结束时间："+examEndTime+
                "\n考试密码："+password+" 【请务必保存好！】"+
                "\n\n祝考试顺利~ ^_^ ";
    }
}
