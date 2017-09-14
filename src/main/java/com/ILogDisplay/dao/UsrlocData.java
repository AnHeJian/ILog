package com.ILogDisplay.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ILogDisplay.beans.locbean;
import net.sf.json.JSONArray;

public class UsrlocData {
    public static Statement sqlConfig() {
        Connection conn;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.168.233.135:3306/test?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String psw = "123456";

        Statement st = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, psw);
            st = conn.createStatement();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return st;
    }

    public static JSONArray getUsrlocData(String data) {
        String tablename = "UsrLoc" + data;
        String [] provinces = {"北京","天津","上海","重庆","河北","河南","云南","辽宁","黑龙江","湖南"
                ,"安徽","山东","新疆","江苏","浙江","江西","湖北","广西","甘肃","山西","内蒙古","陕西","吉林",
                "福建","贵州","广东","青海","西藏","四川","宁夏","海南","台湾","香港","澳门"};

        //Map<String, Object> map = new HashMap<String, Object>();
        ArrayList<locbean> array = new ArrayList<locbean>();

        JSONArray resJSON =new JSONArray();
        for (String province : provinces) {
            String sql = "SELECT times FROM " + tablename + " JOIN CityInfo ci ON city = ci.citycode AND ci.cityname LIKE '" + province + "%';";
            int pvsum = 0;
            locbean lb = new locbean();
            //int uvsum = 0;
            try {
                ResultSet rs = sqlConfig().executeQuery(sql);
                while (rs.next()) {
                    pvsum = pvsum+rs.getInt(1);
                    //uvsum = uvsum+1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //return pvsum;
            //map.put(province, pvsum);
            lb.setName(province);
            lb.setValue(pvsum);
            array.add(lb);
            resJSON = JSONArray.fromObject(array);
            //resJSON = JSONArray.fromObject(map);
        }
        return resJSON;
    }
    public static void main(String[] args) {
        System.out.println(getUsrlocData("20170103"));
    }
}
