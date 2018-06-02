package com.exam.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpSession;

/**
 * Created by deng on 2017/8/5.
 */
@Controller
public class CommonViewController {
    @RequestMapping(value = "/login")
    public String getIndex(HttpSession session) {
        session.invalidate();
        return "index";
    }
}
