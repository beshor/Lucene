package com.beshor.lucene.dao;

import com.beshor.lucene.pojo.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 查询原始数据
 *
 * Created by hasee on 2017/6/21.
 */
public class BookDaoImpl implements BookDao {
    @Override
    public List<Book> queryBookList() {
        //数据库链接
        Connection connection = null;
        //预编译statement
        PreparedStatement preparedStatement = null;
        //结果集
        ResultSet resultSet = null;
        //图书列表
        ArrayList<Book> list = new ArrayList<>();

        try {
            //加载数据库驱动
            Class.forName("com.mysql.jdbc.Driver");
            //连接数据库驱动
            connection = DriverManager.getConnection("jdbc:mysql:///lucene51","root","root");
            //SQl语句
            String sql = "select * from book";
            //创建preparedStatement
            preparedStatement = connection.prepareStatement(sql);
            //获取结果集
            resultSet = preparedStatement.executeQuery();
            //结果集解析
            while (resultSet.next()){
                Book book = new Book();
                book.setId(resultSet.getInt("id"));
                book.setName(resultSet.getString("name"));
                book.setPrice(resultSet.getFloat("price"));
                book.setPic(resultSet.getString("pic"));
                book.setDesc(resultSet.getString("description"));

                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
