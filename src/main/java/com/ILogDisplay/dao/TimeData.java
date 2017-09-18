package com.ILogDisplay.dao;

import com.ILogDisplay.beans.Timedata;
import net.sf.json.JSONArray;

import java.sql.*;
import java.util.*;

public class TimeData {

    public static String driverName = "com.mysql.jdbc.Driver";

    public static JSONArray timeGet(String time, String city) {                                                          //参数为时间与城市，返回JSON格式数据
        Connection con = null;
        JSONArray resJSON = new JSONArray();
        try {
            String url = "jdbc:mysql://192.168.233.135:3306/logs?useUnicode=true&characterEncoding=utf-8";
            String user = "root";
            String pwd = "123456";
            Class.forName(driverName);
            con = DriverManager.getConnection(url, user, pwd);                       //注意数据库名称，此处为logs
            Statement stmt = con.createStatement();

            String tablename = "Time"+time;
            System.out.println(tablename);
            String getContent = "SELECT * FROM " + tablename + " WHERE city in(SELECT citycode from city_code where cityname=\"" + city+"\")";
            System.out.println(getContent);
            ResultSet res = stmt.executeQuery(getContent);
            ArrayList<Timedata> datalist=new ArrayList<Timedata>();
            for(int i=0;i<24;i++) {
                datalist.add(new Timedata());
                datalist.get(i).setHour(i);
            }
            int i=0;
            while (res.next()) {
                int hour = res.getInt("time");
                datalist.get(hour).addPVnum(res.getInt("num"));
                datalist.get(hour).addUVnum(1);
            }

            resJSON = JSONArray.fromObject(datalist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resJSON;


    }


   public static void main(String args[]){
        System.out.print(timeGet("20170105","北京市"));
    }
}
