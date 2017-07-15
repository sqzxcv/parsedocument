package com.nina.webCollector.LLHelper;

/**
 * Created by shengqiang on 2017/7/10.
 */

import cn.edu.hfut.dmic.contentextractor.News;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MYSQLManager{
    //定义成员变量
    Connection conn =null;
    Statement  stmt =null;
    ResultSet  rst  =null;

    //创建数据库连接对象
    //默认的MySQL格式为 url = "jdbc:mysql://localhost:3306/nina?" + "user=root&password=Anhuiqiang851&useUnicode=true&characterEncoding=UTF8";
    public  void getDefaultDBConnection() {

        this.getConnection("jdbc:mysql://localhost:3306/nina?" + "user=root&password=Anhuiqiang851&useUnicode=true&characterEncoding=UTF8");
    }

    public void getConnection(String url){

        //加载数据库驱动
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载MySQL驱动程序");
        }
        catch(ClassNotFoundException e){
            System.out.println("加载数据库驱动失败:\n"+e.toString());
            return;
        }
        //开始连接数据库
        try{
            conn =DriverManager.getConnection(url);
            //System.out.println("连接成功");
        }
        catch (SQLException e) {
            System.out.println("获取连接失败" + e.toString());
            return;
        }
    }

    //得到SQL执行对象
    public Statement getStatement(){
        try{
            stmt =conn.createStatement();
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return stmt =null;
        }
        return stmt;
    }

    public int create(String sqlCreate) {
        return this.create(this.stmt,sqlCreate);
    }
    //创建记录,即向数据库中插入数据[insert into table1(field1,field2) values(value1,value2)]
    public int create(Statement stmt,String sqlCreate){
        int nRecord=0;
        try{
            nRecord =stmt.executeUpdate(sqlCreate);
            System.out.println("插入成功");
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        return nRecord;
    }

    //查询记录 [select * from table1 where 范围]
    public ResultSet read(Statement stmt,String sqlSelect){
        ResultSet rst =null;
        try{
            rst =stmt.executeQuery(sqlSelect);
            System.out.println("查询成功");
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        return rst;
    }

    //更新记录[update table1 set field1=value1 where 范围]
    public int update(Statement stmt,String sqlUpdate){
        int nRecord=0;
        try{
            nRecord =stmt.executeUpdate(sqlUpdate);
            System.out.println("更新成功");
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        return nRecord;
    }

    //删除记录[delete from table1 where 范围]
    public int delete(Statement stmt,String sqlDelete){
        int nRecord=0;
        try{
            nRecord =stmt.executeUpdate(sqlDelete);
            System.out.println("删除成功");
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        return nRecord;
    }

    //关闭结果集
    public void closeResultSet(ResultSet rst){
        try{
            rst.close();
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return;
        }
    }

    //关闭SQL语句执行对象
    public void closeStatement() {
        this.closeStatement(this.stmt);
    }

    public void closeStatement(Statement stmt){
        try{
            stmt.close();
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return;
        }
    }

    //断开与数据库的连接
    public void closeConnection() {

        this.closeConnection(this.conn);
    }

    public void closeConnection(Connection conn){
        try{
            conn.close();
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return;
        }
    }

    public void saveNews2DB(News news) {

        try {
            String jv = "`jfd`";
            this.getDefaultDBConnection();
            this.getStatement();
            int result = this.create(String.format("insert into document(url,content,news_time,contentHtml,title,collect_time) values('%s','%s',%d,'%s','%s',%d) " +
                            "ON DUPLICATE KEY UPDATE content= '%s', news_time=%d,contentHtml='%s',title='%s',collect_time=%d",
                    news.getUrl(),news.getContent(), DateUtil.date2TimeStamp(news.getTime(), "yyyy-MM-dd"),
                    news.getContentElement().html(),news.getTitle(),DateUtil.timeStamp(),
                    news.getContent(), DateUtil.date2TimeStamp(news.getTime(), "yyyy-MM-dd"),
                    news.getContentElement().html(),news.getTitle(),DateUtil.timeStamp()));
            if (result != 0) {
                System.out.format("文章[%s]插入成功",news.getTitle());
            } else {
                System.out.format("文章[%s]插入失败",news.getTitle());
            }
            this.closeStatement();
            this.closeConnection();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}