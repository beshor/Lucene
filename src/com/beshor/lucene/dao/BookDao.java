package com.beshor.lucene.dao;

import com.beshor.lucene.pojo.Book;

import java.util.List;

/**
 * Created by hasee on 2017/6/21.
 */
public interface BookDao {

    /**
     * 查询所有的book数据
     */
    List<Book> queryBookList();

}
