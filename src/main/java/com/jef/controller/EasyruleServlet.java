package com.jef.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author tufujie
 * @date 2023/8/21
 */
@WebServlet("/easyRule/index")
public class EasyruleServlet extends HttpServlet {

    static final String SUSPICIOUS = "suspicious";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        if (isSuspicious(request)) {
            out.print("访问被拒绝！");
        } else {
            out.print("欢迎!");
        }
    }

    private boolean isSuspicious(HttpServletRequest request) {
        return request.getAttribute(SUSPICIOUS) != null;
    }
}