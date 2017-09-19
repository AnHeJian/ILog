package com.ILogDisplay.servlet;

import com.ILogDisplay.dao.UsrlocData;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "usrLocCityServlet")
public class usrLocCityServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        //接收前台数据：
        String usrinfo=request.getParameter("mark");//"2017-01-01,2017-01-07,北京,PV"
        System.out.println(usrinfo);
        String [] infosplit = usrinfo.split("\"");//infosplit[1]为: 2017-01-01,2017-01-07,北京,PV
        String [] infos = infosplit[1].split(",");//infos[0]为: 2017-01-01    infos[1]为：2017-01-07

        String [] sdatesplit = infos[0].split("-");//infos[0]分割为 2017 01 01
        String sdate = sdatesplit[0]+sdatesplit[1]+sdatesplit[2];//date为20170101
        String [] edatesplit = infos[1].split("-");//infos[1]分割为 2017 01 07
        String edate = edatesplit[0]+edatesplit[1]+edatesplit[2];//date为20170101

        String cityname = infos[2];
        String index = infos[3];//index为PV(UV)

        System.out.println(sdate+","+ edate+","+  cityname+","+  index);

        //向前台发送数据
        JSONArray json = UsrlocData.getCityData(sdate, edate, cityname, index);
        System.out.println(json);

        PrintWriter out = response.getWriter();
        out.println(json);
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
