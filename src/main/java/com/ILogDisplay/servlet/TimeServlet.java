package com.ILogDisplay.servlet;

import com.ILogDisplay.dao.TimeData;
import com.ILogDisplay.dao.UsrlocData;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "TimeServlet")
public class TimeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //接收前台数据：
        response.setContentType("text/html; charset=utf-8");
        String send=request.getParameter("send");
        String []infosplit1=send.split("\"");
        String []infosplit=infosplit1[1].split(",");
        String []timesplit=infosplit[0].split("-");
        String time=timesplit[0]+timesplit[1]+timesplit[2];
        String city;
        if(infosplit.length==2){
            city=infosplit[1];
        }
        else{city=infosplit[1]+infosplit[2];}
        PrintWriter out = response.getWriter();

        //向前台传递数据
        JSONArray json = TimeData.timeGet(time,city);
        System.out.println(json);
        out.println(json);
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
