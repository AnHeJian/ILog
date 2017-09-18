package com.ILogData.test;
/*
对数据进行分析后 调入到mysql中
 */
import com.ILogData.Hdfs;
import com.ILogData.HiveData;
import com.ILogData.MysqlData;
import com.ILogData.mr.TimeMR;
import org.apache.log4j.BasicConfigurator;

import java.io.File;


public class TimeTest {
    public static void main(String[] args) {
        //BasicConfigurator.configure();
        //change to your file path
        String filePath = "/opt/ilogdata";

        System.out.println("Loading and mr usrlocation start");
        File file = new File(filePath);
        for(File singlefile : file.listFiles()){
            if(singlefile.isFile() && !singlefile.isHidden()){
                String filename = singlefile.toString();
                System.out.println(filename);
               // Hdfs.uploadLocalfileHdfs(filename);
                System.out.println("upload hdfs success");
                try {
                    //TimeMR.runner(filename);
                    System.out.println("MR success");
                   // Hdfs.downloadtoLocalfile(filename);
                    System.out.println("download hdfs success");
                    MysqlData.mysqlLoadData(filename);
                    System.out.println("import mysql success");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
