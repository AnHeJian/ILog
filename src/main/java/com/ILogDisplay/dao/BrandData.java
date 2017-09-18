package com.ILogDisplay.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import com.ILogDisplay.beans.BrandBean;
import net.sf.json.JSONArray;

public class BrandData {
        public static Connection conn;
    //连接数据庫
    public static Statement sqlConfig() {

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.168.233.135:3306/logs?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String psw = "123456";

        Statement stmt = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, psw);
            stmt = conn.createStatement();

        } catch (Exception e) {
            System.out.println("");
            e.printStackTrace();
        }
        return stmt;
    }


    //查询全国各品牌型号总数
    //返回格式：[{"brand_model":"华为","num":12,"proportion":0},{"brand_model":"三星","num":10,"proportion":0}]
    public static JSONArray queryCountry(String type,String timeStart,String timeEnd){
        if(type.equals("品牌"))
            type = "brand";
        if(type.equals("型号"))
            type = "model";


        ArrayList<BrandBean> array = new ArrayList<BrandBean>();
        JSONArray resJSON =new JSONArray();
        Statement stmt = sqlConfig();

        String querySQL = "select "+ type +",count(*) as userNum from (SELECT phone_number," + type + " from "+timeStart+"_b";

        for(int time =Integer.valueOf(timeStart)+1;time<=Integer.valueOf(timeEnd);time++) {
            querySQL += " UNION SELECT phone_number,"+type+" from "+time+"_b";
        }
        querySQL += ") AS a group by "+type+" order by userNum desc";
            try {
                ResultSet rs = stmt.executeQuery(querySQL);
                while (rs.next()) {
                    BrandBean brandBean = new BrandBean();
                    brandBean.setName(rs.getString(type));
                    brandBean.setValue(rs.getInt("userNum"));
                    //System.out.println(brandBean);
                    array.add(brandBean);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }


        ArrayList<BrandBean> newArray = new ArrayList<BrandBean>();
        int size = 0;

        if(array.size()>10) {
            for (int i = 0; i < array.size(); i++) {
                if (i < 10) {
                    newArray.add(array.get(i));
                } else {
                    size += array.get(i).getValue();
                }
            }
            newArray.add(new BrandBean("其他", size));
        }
        else
            newArray = array;

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

        resJSON = JSONArray.fromObject(newArray);
        System.out.println(resJSON);
        return resJSON;
    }



    //查询各个省某段时间的品牌总数
    //返回格式：[{"name":"荣耀8","value":2},{"name":"华为NCE-TL10","value":1},{"name":"华为Mate 8 Small","value":1},
    // {"name":"华为Y7 Prime","value":1},{"name":"华为G629","value":1}]
    public static JSONArray queryPrivence(String privance,String type,String timeStart,String timeEnd){
        if(type.equals("品牌"))
            type = "brand";
        if(type.equals("型号"))
            type = "model";


        ArrayList<BrandBean> array = new ArrayList<BrandBean>();
        JSONArray resJSON =new JSONArray();
        Statement stmt = sqlConfig();

        String querySQL = "select "+ type +",count(*) as userNum from (SELECT phone_number," + type + " from "+timeStart+"_b where city in (select citycode from city where cityname like'" +privance+"%')";

        for(int time =Integer.valueOf(timeStart)+1;time<=Integer.valueOf(timeEnd);time++) {
            querySQL += " UNION SELECT phone_number,"+type+" from "+time+"_b where city in (select citycode from city where cityname like'" +privance+"%')";
        }
        querySQL += ") AS a group by "+type+" order by userNum desc";
        try {
            ResultSet rs = stmt.executeQuery(querySQL);
            while (rs.next()) {
                BrandBean brandBean = new BrandBean();
                brandBean.setName(rs.getString(type));
                brandBean.setValue(rs.getInt("userNum"));
                //System.out.println(brandBean);
                array.add(brandBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        ArrayList<BrandBean> newArray = new ArrayList<BrandBean>();
        int size = 0;

        if(array.size()>10) {
            for (int i = 0; i < array.size(); i++) {
                if (i < 10) {
                    newArray.add(array.get(i));
                } else {
                    size += array.get(i).getValue();
                }
            }
            newArray.add(new BrandBean("其他", size));
        }
        else
            newArray = array;

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

        resJSON = JSONArray.fromObject(newArray);
        System.out.println(resJSON);
        return resJSON;
    }




    //查询全国指定品牌某段时间的型号总数
    //返回格式：[[{"brand_model":"中兴","num":2,"proportion":0},{"brand_model":"vivo","num":1,"proportion":0}],
    //          [{"brand_model":"LG","num":1,"proportion":0}]]
    public static JSONArray queryModelCountry(String mbrand,String timeStart,String timeEnd){
        ArrayList<BrandBean> array = new ArrayList<BrandBean>();
        JSONArray resJSON =new JSONArray();
        Statement stmt = sqlConfig();

        String querySQL = "select model,count(*) as userNum from (SELECT phone_number,model from "+timeStart+"_b where brand="+"'"+mbrand;
        for(int time =Integer.valueOf(timeStart)+1;time<=Integer.valueOf(timeEnd);time++) {
            querySQL += "' UNION SELECT phone_number,model from "+time+"_b where brand='"+mbrand;
        }
        querySQL += "'"+") AS a group by model order by userNum desc";
        try {
            ResultSet rs = stmt.executeQuery(querySQL);
            while (rs.next()) {
                BrandBean brandBean = new BrandBean();
                brandBean.setName(rs.getString("model"));
                brandBean.setValue(rs.getInt("userNum"));
                //System.out.println(brandBean);
                array.add(brandBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<BrandBean> newArray = new ArrayList<BrandBean>();

        if(array.size()>5) {
            for (int i = 0; i < 5; i++) {
                newArray.add(array.get(i));
            }
        }
        else
            newArray = array;

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

        resJSON = JSONArray.fromObject(newArray);
        System.out.println(resJSON);
        return resJSON;
    }




    //查询某省指定品牌某段时间的型号总数
    //返回格式：[{"name":"联想A6800","value":1},{"name":"联想VIBE K5 Plus ","value":1},{"name":"联想K6","value":1}]
    public static JSONArray queryModelPrivence(String privence,String mbrand,String timeStart,String timeEnd){
        ArrayList<BrandBean> array = new ArrayList<BrandBean>();
        JSONArray resJSON =new JSONArray();
        Statement stmt = sqlConfig();

        String querySQL = "select model,count(*) as userNum from (SELECT phone_number,model,city from "+timeStart+"_b where brand="+"'"+mbrand;
        for(int time =Integer.valueOf(timeStart)+1;time<=Integer.valueOf(timeEnd);time++) {
            querySQL += "' UNION SELECT phone_number,model,city from "+time+"_b where brand='"+mbrand;
        }
        querySQL += "'"+") AS a where city in (select citycode from city where cityname like '"+privence+"%')" +
                " group by model order by userNum desc";

        try {
            ResultSet rs = stmt.executeQuery(querySQL);
            while (rs.next()) {
                BrandBean brandBean = new BrandBean();
                brandBean.setName(rs.getString("model"));
                brandBean.setValue(rs.getInt("userNum"));
                //System.out.println(brandBean);
                array.add(brandBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<BrandBean> newArray = new ArrayList<BrandBean>();

        if(array.size()>5) {
            for (int i = 0; i < 5; i++) {
                newArray.add(array.get(i));
            }
        }
        else
            newArray = array;

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

        resJSON = JSONArray.fromObject(newArray);
        System.out.println(resJSON);
        return resJSON;
    }




    //查询全国某段时间的品牌数量变化
    //返回格式：[[{"name":"华为","value":12},{"name":"三星","value":10},{"name":"中兴","value":9},{"name":"LG","value":8},{"name":"联想","value":7}],
    // [{"name":"华为","value":12},{"name":"三星","value":10},{"name":"中兴","value":9},{"name":"LG","value":8},{"name":"联想","value":7}]]
    public static JSONArray queryCountryDay(String timeStart,String timeEnd){

        ArrayList<ArrayList<BrandBean>> arrayLists = new ArrayList<ArrayList<BrandBean>>();
        JSONArray resJSON =new JSONArray();
        Statement stmt = sqlConfig();

        for(int time =Integer.valueOf(timeStart);time<=Integer.valueOf(timeEnd);time++) {
            String sql = "SELECT brand,count(*) as userNum from "+time+"_b GROUP BY brand ORDER BY userNum desc limit 5";
            ArrayList<BrandBean> array = new ArrayList<BrandBean>();
            try {
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    BrandBean brandBean = new BrandBean();
                    brandBean.setName(rs.getString("brand"));
                    brandBean.setValue(rs.getInt("userNum"));
                    //System.out.println(brandBean);
                    array.add(brandBean);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            arrayLists.add(array);
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

        resJSON = JSONArray.fromObject(arrayLists);
        System.out.println(resJSON);
        return resJSON;
    }



    //查询各个省某段时间的品牌数量变化
    //返回格式：
    public static JSONArray queryPrivenceDay(String privance,String type,String timeStart,String timeEnd){
        if(type.equals("品牌"))
            type = "brand";
        if(type.equals("型号"))
            type = "model";


        ArrayList<BrandBean> array = new ArrayList<BrandBean>();
        ArrayList<ArrayList<BrandBean>> arrayLists = new ArrayList<ArrayList<BrandBean>>();
        JSONArray resJSON =new JSONArray();
        Statement stmt = sqlConfig();

        for(int time =Integer.valueOf(timeStart);time<=Integer.valueOf(timeEnd);time++) {
            System.out.println("time:"+time);
            String querySQL = "select " + type + ",count(*) as userNum from "+time+"_b where city in (select citycode from city where cityname like'" +privance+"%')" +
                    " group by "+type+" order by userNum desc";
            try {
                ResultSet rs = stmt.executeQuery(querySQL);
                while (rs.next()) {
                    BrandBean brandBean = new BrandBean();
                    brandBean.setName(rs.getString(type));
                    brandBean.setValue(rs.getInt("userNum"));
                    //System.out.println(brandBean);
                    array.add(brandBean);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            arrayLists.add(array);
        }

        ArrayList<BrandBean> newArray = new ArrayList<BrandBean>();
        int size = 0;

        if(array.size()>10) {
            for (int i = 0; i < array.size(); i++) {
                if (i < 10) {
                    newArray.add(array.get(i));
                } else {
                    size += array.get(i).getValue();
                }
            }
            newArray.add(new BrandBean("其他", size));
        }
        else
            newArray = array;

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

        resJSON = JSONArray.fromObject(newArray);
        System.out.println(resJSON);
        return resJSON;
    }



    public static void main(String[] args){
        BrandData a = new BrandData();
        //BrandData.queryCountry("品牌","20170101","20170101");
        //a.queryPrivence("北京","品牌","20170101","20170103");
        //BrandData.queryModelCountry("华为","20170101","20170102");
        //BrandData.queryPrivence("江苏","品牌","20170101","20170107");
        System.out.println(queryCountryDay("20170101","20170107"));
    }
}









