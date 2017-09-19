package com.ILogDisplay.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;

import com.ILogDisplay.beans.BrandBean;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



public class BrandData {

    //连接数据庫
    public static Statement sqlConfig() {
        Connection conn;
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://192.168.222.132:3306/logs?useUnicode=true&characterEncoding=UTF-8";
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

        String querySQL = "select "+ type +",count(*) as userNum from (SELECT phone_number," + type + " from "+timeStart+"_b";

        for(int time =Integer.valueOf(timeStart)+1;time<=Integer.valueOf(timeEnd);time++) {
            querySQL += " UNION SELECT phone_number,"+type+" from "+time+"_b";
        }
        querySQL += ") AS a group by "+type+" order by userNum desc";
        try {
            ResultSet rs = sqlConfig().executeQuery(querySQL);
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

        String querySQL = "select "+ type +",count(*) as userNum from (SELECT phone_number," + type + " from "+timeStart+"_b where city in (select citycode from city where cityname like'" +privance+"%')";

        for(int time =Integer.valueOf(timeStart)+1;time<=Integer.valueOf(timeEnd);time++) {
            querySQL += " UNION SELECT phone_number,"+type+" from "+time+"_b where city in (select citycode from city where cityname like'" +privance+"%')";
        }
        querySQL += ") AS a group by "+type+" order by userNum desc";
        try {
            ResultSet rs = sqlConfig().executeQuery(querySQL);
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

        String querySQL = "select model,count(*) as userNum from (SELECT phone_number,model from "+timeStart+"_b where brand="+"'"+mbrand;
        for(int time =Integer.valueOf(timeStart)+1;time<=Integer.valueOf(timeEnd);time++) {
            querySQL += "' UNION SELECT phone_number,model from "+time+"_b where brand='"+mbrand;
        }
        querySQL += "'"+") AS a group by model order by userNum desc";
        try {
            ResultSet rs = sqlConfig().executeQuery(querySQL);
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

        resJSON = JSONArray.fromObject(newArray);
        System.out.println(resJSON);
        return resJSON;
    }




    //查询某省指定品牌某段时间的型号总数
    //返回格式：[{"name":"联想A6800","value":1},{"name":"联想VIBE K5 Plus ","value":1},{"name":"联想K6","value":1}]
    public static JSONArray queryModelPrivence(String privence,String mbrand,String timeStart,String timeEnd){
        ArrayList<BrandBean> array = new ArrayList<BrandBean>();
        JSONArray resJSON =new JSONArray();

        String querySQL = "select model,count(*) as userNum from (SELECT phone_number,model,city from "+timeStart+"_b where brand="+"'"+mbrand;
        for(int time =Integer.valueOf(timeStart)+1;time<=Integer.valueOf(timeEnd);time++) {
            querySQL += "' UNION SELECT phone_number,model,city from "+time+"_b where brand='"+mbrand;
        }
        querySQL += "'"+") AS a where city in (select citycode from city where cityname like '"+privence+"%')" +
                " group by model order by userNum desc";

        try {
            ResultSet rs = sqlConfig().executeQuery(querySQL);
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

        resJSON = JSONArray.fromObject(newArray);
        System.out.println(resJSON);
        return resJSON;
    }




    //查询全国某段时间的品牌数量变化
    //返回格式：[[{"name":"华为","value":12},{"name":"三星","value":10},{"name":"中兴","value":9},{"name":"LG","value":8},{"name":"联想","value":7}],
    // [{"name":"华为","value":12},{"name":"三星","value":10},{"name":"中兴","value":9},{"name":"LG","value":8},{"name":"联想","value":7}]]
    public static JSONArray queryDay(String location,String brand,String timeStart,String timeEnd){

        ArrayList<String> array = new ArrayList<String>();
        JSONArray resJSON =new JSONArray();
        array.add(brand);
        for(int time =Integer.valueOf(timeStart);time<=Integer.valueOf(timeEnd);time++) {
            String sql;
            if(location.equals("全国"))
                sql = "SELECT count(*) as userNum from "+time+"_b where brand='"+brand+"'";
            else
                sql = "SELECT count(*) as userNum from "+time+"_b where brand='"+brand+
                        "' and city in (SELECT citycode from city WHERE cityname like '"+location+"%')";

            try {
                ResultSet rs = sqlConfig().executeQuery(sql);
                while (rs.next()) {
                    array.add(rs.getString("userNum"));
                    //System.out.println(brandBean);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        resJSON = JSONArray.fromObject(array);
        //System.out.println(resJSON);
        return resJSON;
    }



    //查询各个省某段时间的品牌数量变化
    //返回格式：[["华为","12","12","12"],["三星","10","10","10"],["中兴","9","9","9"],["LG","8","8","8"],["联想","7","7","7"]]
    public static JSONArray queryPrivenceDay(String privance,String type,String timeStart,String timeEnd){
        if(type.equals("品牌"))
            type = "brand";
        if(type.equals("型号"))
            type = "model";


        ArrayList<BrandBean> array = new ArrayList<BrandBean>();
        ArrayList<ArrayList<BrandBean>> arrayLists = new ArrayList<ArrayList<BrandBean>>();
        JSONArray resJSON =new JSONArray();

        for(int time =Integer.valueOf(timeStart);time<=Integer.valueOf(timeEnd);time++) {
            System.out.println("time:"+time);
            String querySQL = "select " + type + ",count(*) as userNum from "+time+"_b where city in (select citycode from city where cityname like'" +privance+"%')" +
                    " group by "+type+" order by userNum desc";
            try {
                ResultSet rs = sqlConfig().executeQuery(querySQL);
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

        resJSON = JSONArray.fromObject(newArray);
        System.out.println(resJSON);
        return resJSON;
    }


    public static JSONArray queryCountryDetail(String location,String timeStart,String timeEnd){
        JSONArray brandJson = new JSONArray();
        if(location.equals("全国"))
            brandJson = queryCountry("品牌",timeStart,timeEnd);
        else
            brandJson = queryPrivence(location,"品牌",timeStart,timeEnd);

        ArrayList<JSONArray> arrayLists = new ArrayList<JSONArray>();
        int size = brandJson.size();
        if(size>5)
            size = 5;
        for (int i = 0; i < size; i++) {
            //System.out.println(brandJson.getString(i));
            JSONObject jsobj = brandJson.getJSONObject(i);
            JSONArray js = queryDay(location,jsobj.getString("name"),timeStart,timeEnd);
            //System.out.println(js);
            arrayLists.add(js);
        }

        System.out.println(JSONArray.fromObject(arrayLists));
        return JSONArray.fromObject(arrayLists);
    }



    public static void main(String[] args){
        BrandData a = new BrandData();
        //BrandData.queryCountry("品牌","20170101","20170103");
        //a.queryPrivence("北京","品牌","20170101","20170103");
        //BrandData.queryModelCountry("华为","20170101","20170102");
        //BrandData.queryPrivence("江苏","品牌","20170101","20170107");
        //queryCountryDay("华为","20170101","20170107");
        queryCountryDetail("贵州","20170101","20170103");
    }
}









