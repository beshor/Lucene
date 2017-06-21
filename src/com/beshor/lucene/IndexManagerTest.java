package com.beshor.lucene;

import com.beshor.lucene.dao.BookDao;
import com.beshor.lucene.dao.BookDaoImpl;
import com.beshor.lucene.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.List;

/**
 * Created by hasee on 2017/6/20.
 */
public class IndexManagerTest {
    /**
     * 创建索引的方法
     * @author bz
     * @throws Exception
     */
    @Test
    public void testIndexCreate() throws Exception{
        //1.采集数据
        BookDao bookDao = new BookDaoImpl();
        List<Book> books = bookDao.queryBookList();
        //3.创建分析器
        Analyzer analyzer = new IKAnalyzer();
        //4.创建IndexWriterConfig配置信息类
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,analyzer);
        //5.创建Directory对象,声明索引库存储位置
        Directory directory = FSDirectory.open(new File("D:\\temp\\index"));
        //6.创建IndexWriter写入对象
        IndexWriter indexWriter = new IndexWriter(directory,config);
        //7.把Document写入到索引库中
        for(Book book:books){

            //2.创建Document文档对象
            Document document = new Document();

            Integer id = book.getId();
            String name = book.getName();
            String desc = book.getDesc();
            Float price = book.getPrice();
            String pic = book.getPic();

            //创建域,并将数据写入到文本域中
            document.add(new TextField("id",String.valueOf(id), Field.Store.YES));
            document.add(new TextField("name",name, Field.Store.YES));
            document.add(new TextField("price",String.valueOf(price), Field.Store.YES));
            document.add(new TextField("pic",pic, Field.Store.YES));
            document.add(new TextField("desc",desc, Field.Store.NO));


            //保存文档到索引库存(索引  保存  文档保存)
            indexWriter.addDocument(document);
        }
        //8.释放资源
        indexWriter.close();


    }


    @Test
    public void testQueryIndex()throws Exception{
        //1.创建Query搜索对象  精准查询
        Query query = new TermQuery(new Term("desc","lucene"));
        //2.创建Directory流对象   声明索引库位置
        Directory directory = FSDirectory.open(new File("D:\\temp\\index"));
        //3.创建索引读取对象IndexReader
        IndexReader indexReader = DirectoryReader.open(directory);
        //4.创建索引搜索对象IndexSearcher
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDoc = indexSearcher.search(query, 5);
        ScoreDoc[] scoreDocs = topDoc.scoreDocs;
        //5.使用索引搜索对象,执行搜索,返回结果集TopDocs
        for (ScoreDoc scoreDoc:scoreDocs){
            //6.解析结果集
            Document doc = indexSearcher.doc(scoreDoc.doc);

            System.out.println(doc.get("id"));
            System.out.println(doc.get("name"));
            System.out.println(doc.get("pic"));
            System.out.println(doc.get("price"));
            System.out.println(doc.get("desc"));
        }

        //7.释放资源
        indexReader.close();
    }


}
