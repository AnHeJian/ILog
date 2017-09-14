package com.ILogDisplay.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.ILogDisplay.dao.UsrlocData;
import net.sf.json.JSONArray;

@javax.servlet.annotation.WebServlet(name = "usrLocServlet")
public class usrLocServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        System.out.println("hhhhhh");
        JSONArray json = UsrlocData.getUsrlocData("20170103");
        System.out.println(json);

        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
        out.close();
    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
