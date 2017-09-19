package com.ILogDisplay.dao;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.ILogDisplay.beans.detaillocbean;
import com.ILogDisplay.beans.locbean;
import net.sf.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;

public class UsrlocData {
    public static Connection conn;
    public static Statement sqlConfig() {

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

    public static JSONArray getSqlData(String date, String index) {
        String tablename = "UsrLoc" + date;
        String [] provinces = {"北京","天津","上海","重庆","河北","河南","云南","辽宁","黑龙江","湖南"
                ,"安徽","山东","新疆","江苏","浙江","江西","湖北","广西","甘肃","山西","内蒙古","陕西","吉林",
                "福建","贵州","广东","青海","西藏","四川","宁夏","海南","台湾","香港","澳门"};

        //Map<String, Object> map = new HashMap<String, Object>();

        ArrayList<detaillocbean> dlbarray = new ArrayList<detaillocbean>();
        JSONArray resJSON =new JSONArray();
        ArrayList<locbean> lbarray = new ArrayList<locbean>();

        Statement stmt = sqlConfig();
        for (String province : provinces) {
            String usrlocsql = "SELECT times FROM " + tablename + " JOIN CityInfo ci ON city = ci.citycode AND ci.cityname LIKE '" + province + "%';";
            int pvsum = 0;
            int uvsum = 0;
            double live = .0;



            try {
                ResultSet rs = stmt.executeQuery(usrlocsql);

                while (rs.next()) {
                    pvsum = pvsum+rs.getInt(1);
                    uvsum = uvsum+1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return JSONArray.fromObject("[]");
            }

            live = pvsum ==0 ? 0 : new BigDecimal(100*(float)uvsum / pvsum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            if(!index.equals("BOTH")) {
                locbean lb = new locbean();
                lb.setName(province);

                double sum ;
                if(!index.equals("LIVE"))
                    sum = index.equals("PV") ? pvsum : uvsum;
                else {
                    sum = pvsum == 0 ? 0 : live;
                }
                lb.setValue(sum);
                lbarray.add(lb);
            }
            else{
                detaillocbean dlb = new detaillocbean();
                dlb.setName(province);
                dlb.setPv(pvsum);
                dlb.setUv(uvsum);
                dlb.setLive(pvsum == 0 ? 0 : live);

                dlbarray.add(dlb);
            }
        }

        if(stmt!= null)
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(conn!= null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        resJSON = index.equals("BOTH") ? JSONArray.fromObject(dlbarray) : JSONArray.fromObject(lbarray);
        return resJSON;
    }

    public static JSONArray getUsrlocData(String date, String index) {
        return getSqlData(date, index);
    }
    public static JSONArray getDetaillocData(String date) {
        return getSqlData(date, "BOTH");
    }

    public static JSONArray getCityData(String sdate, String edate, String cityname, String index){
        Statement stmt = sqlConfig();
        JSONArray resJSON =new JSONArray();
        ArrayList<locbean> lbarray = new ArrayList<locbean>();
        for(int time =Integer.valueOf(sdate);time<=Integer.valueOf(edate);time++) {
            String tablename = "UsrLoc"+time;
            String sql = "SELECT times FROM " +tablename + " JOIN CityInfo ci ON city = ci.citycode AND ci.cityname LIKE '" + cityname + "%';";
            int pvsum = 0;
            int uvsum = 0;
            double live = .0;

            try {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    pvsum = pvsum+rs.getInt(1);
                    uvsum = uvsum+1;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return JSONArray.fromObject("[]");
            }

            live = pvsum == 0 ? 0 : new BigDecimal(100*(float)uvsum / pvsum).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            locbean lb = new locbean();
            lb.setName(String.valueOf(time));

            double sum ;
            if(!index.equals("LIVE"))
                sum = index.equals("PV") ? pvsum : uvsum;
            else {
                sum = pvsum == 0 ? 0 : live;
            }
            lb.setValue(sum);
            lbarray.add(lb);
        }

        if(stmt!= null)
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if(conn!= null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        resJSON = JSONArray.fromObject(lbarray);
        return resJSON;
    }

    public static void main(String[] args) {
        //System.out.println(getUsrlocData("20170105","BOTH"));
        System.out.println(getCityData("20170105","20170107","江苏","LIVE"));
    }
}
