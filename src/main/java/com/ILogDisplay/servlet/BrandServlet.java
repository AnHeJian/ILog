package com.ILogDisplay.servlet;

import com.ILogDisplay.dao.BrandData;
import net.sf.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;




@WebServlet(name = "BrandServlet")
public class BrandServlet extends HttpServlet {
    //传入日期，返回日期的字符串:20170101
    public static String getFormatDate(Calendar date){
        String year = date.get(Calendar.YEAR) + "";
        String month = date.get(Calendar.MONTH) + "";
        String day = (date.get(Calendar.DATE)) + "";

        if(date.get(Calendar.MONTH)<10)
            month = "0" + date.get(Calendar.MONTH);
        if(date.get(Calendar.DATE)<10)
            day = "0" + (date.get(Calendar.DATE));
        return year+month+day;
    }

    //将2017-1-1格式化为20170101
    public static String formatDateStr(String str){
        System.out.println(str);
        String[] split = str.split("-");
        String y = split[0];
        String m = split[1];
        String d = split[2];
        System.out.println("hhh:"+y+m+d);
        return y+m+d;
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=utf-8");
        request.setCharacterEncoding("utf-8");

        //查询品牌或者型号
        String method = request.getParameter("method");
        //是否固定时间
        String fixedTime = request.getParameter("fixedTime");
        //查询地区
        String location = request.getParameter("location");
        location=new String(location.getBytes("ISO-8859-1"),"UTF-8");
        //返回前台数据
        JSONArray json = null;


        String startTime;
        String endTime;


        System.out.println(method+"|"+location+"|"+fixedTime);
        if (method.equals("brandQuery")) {

            if(fixedTime.equals("0")){
                startTime = formatDateStr(request.getParameter("startDate"));
                endTime = formatDateStr(request.getParameter("endDate"));
            }
            else{
                Calendar date = Calendar.getInstance();
                //假设今天的时间
                date.set(2017,01,8);

                date.add(Calendar.DATE,-1);
                endTime = getFormatDate(date);
                date.add(Calendar.DATE,1-Integer.valueOf(fixedTime));
                startTime = getFormatDate(date);
            }


            System.out.println(startTime+" | "+endTime);
            //向前台发送数据

            if(location.equals("全国"))
                json = BrandData.queryCountry("品牌", startTime, endTime);
            else
                json = BrandData.queryPrivence(location,"品牌",startTime, endTime);
            System.out.println("1:"+json);

            PrintWriter out = response.getWriter();
            out.println(json);
            out.flush();
            out.close();
        }



        if(method.equals("modelQuery")){

            if(fixedTime.equals("0")){
                startTime = request.getParameter("startDate");
                endTime = request.getParameter("endDate");
            }
            else{
                Calendar date = Calendar.getInstance();
                //假设今天的时间
                date.set(2017,01,8);

                date.add(Calendar.DATE,-1);
                endTime = getFormatDate(date);
                date.add(Calendar.DATE,1-Integer.valueOf(fixedTime));
                startTime = getFormatDate(date);
            }


            String mbrand = request.getParameter("brand");
            mbrand = new String(mbrand.getBytes("ISO-8859-1"),"UTF-8");
            System.out.println(startTime+" | "+endTime+" | "+ mbrand);

            //向前台发送数据
            if(location.equals("全国"))
                json = BrandData.queryModelCountry(mbrand, startTime, endTime);
            else
                json = BrandData.queryModelPrivence(location,mbrand,startTime, endTime);
            System.out.println(json);

            PrintWriter out = response.getWriter();
            out.println(json);
            out.flush();
            out.close();
        }




        if(method.equals("DbrandQuery")){
            if(fixedTime.equals("0")){
                startTime = formatDateStr(request.getParameter("startDate"));
                endTime = formatDateStr(request.getParameter("endDate"));
            }
            else{
                Calendar date = Calendar.getInstance();
                //假设今天的时间
                date.set(2017,01,8);

                date.add(Calendar.DATE,-1);
                endTime = getFormatDate(date);
                date.add(Calendar.DATE,1-Integer.valueOf(fixedTime));
                startTime = getFormatDate(date);
            }

            System.out.println(startTime+" | "+endTime);
            //向前台发送数据

            json = BrandData.queryCountryDetail(location,startTime, endTime);
            System.out.println("1:"+json);

            PrintWriter out = response.getWriter();
            out.println(json);
            out.flush();
            out.close();


        }
    }
}











