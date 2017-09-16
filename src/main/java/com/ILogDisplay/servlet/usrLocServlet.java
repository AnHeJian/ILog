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

        //接收前台数据：
        String usrinfo=request.getParameter("usrinfo");//接收到的usrinfo为："2017-01-01,PV"
        System.out.println(usrinfo);
        String [] infosplit = usrinfo.split("\"");
        String [] infos = infosplit[1].split(",");//infosplit[1]为: 2017-01-01 PV
        String [] datesplit = infos[0].split("-");//infos[0]分割为 2017 01 01
        String date = datesplit[0]+datesplit[1]+datesplit[2];//date为20170101
        String index = infos[1];//index为PV(UV)

        System.out.println(date+"  "+index);


        //向前台发送数据
        JSONArray json = UsrlocData.getUsrlocData(date);//接口的参数应该为date和index，都是用户的选择，现在还只写了date。。。
        System.out.println(json);

        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
        out.close();

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }
}
