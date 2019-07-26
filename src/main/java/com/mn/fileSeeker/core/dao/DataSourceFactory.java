package com.mn.fileSeeker.core.dao;


import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataSourceFactory {
    private static volatile DruidDataSource instance;
    private DataSourceFactory(){

    }
    public static DataSource getInstance(){
        if(instance == null){
            synchronized (DataSource.class){
                if(instance == null){
                    instance = new DruidDataSource();
//                    连接MySQL的配置
//                    instance.setUrl("jdbc:mysql://127.0.0.1:3306/fileSeeker");
//                    instance.setUsername("root");
//                    instance.setPassword("1111");
//                    instance.setDriverClassName("com.mysql.jdbc.Driver");

//                    连接H2数据库的配置
                    /*
                     * mysql -> h2 :
                     * 1.改依赖
                     * 2.该驱动相关
                     * 3.删除MySQL创建数据库的语句
                     * 4.提供数据库表的初始化
                     * 5.在EverythingManager里对数据库初始化
                     */
                    instance.setTestWhileIdle(false);
                    instance.setDriverClassName("org.h2.Driver");
                    String path = System.getProperty("user.dir")+ File.separator+"fileSeeker";
                    instance.setUrl("jdbc:h2:"+path);
                    databaseInit(false);//数据库创建完成之后，初始化表结构

                }
            }
        }
        return instance;
    }
    //数据库表的初始化
    public static void databaseInit(boolean buildIndex){
        //把database.sql 变成sql语句
        StringBuilder sb = new StringBuilder();
        try(InputStream in = DataSourceFactory.class.getClassLoader().getResourceAsStream("database.sql");
        ){
            if(in != null){
                try(BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
                    String line = null;
                    while((line = reader.readLine()) != null){
                        sb.append(line);
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {
                throw new RuntimeException("database.sql script cannot load,please check it");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        String sql = sb.toString();
        try(
                Connection connection = getInstance().getConnection();
                ) {
            if(buildIndex){
                try(PreparedStatement statement = connection.prepareStatement(" drop table if exists thing; ");){
                    statement.executeUpdate();
                }catch(SQLException e){
                    e.printStackTrace();
                }
            }
            try(PreparedStatement statement = connection.prepareStatement(sql);){
                statement.executeUpdate();
            }catch (SQLException e){
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
/*
    public static void main(String[] args) {
        DataSource dataSource = DataSourceFactory.getInstance();
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement("insert into thing (name,path,depth,file_type) values (?,?,?,?)")
        ){
                statement.setString(1,"fileSeekerTest.txt");
                statement.setString(2,"C:\\Users\\Lenovo\\Desktop\\bite笔记\\fileSeekerTest.txt");
                statement.setInt(3,5);
                statement.setString(4,"DOC");
                statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
*/
}
