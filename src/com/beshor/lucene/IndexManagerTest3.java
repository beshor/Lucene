package com.beshor.lucene;

import com.beshor.lucene.dao.BookDao;
import com.beshor.lucene.dao.BookDaoImpl;
import com.beshor.lucene.pojo.Book;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
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
public class IndexManagerTest3 {
    /**
     * 创建索引的方法
     *
     * @throws Exception
     * @author bz
     */
    @Test
    public void testIndexCreate() throws Exception {
        //1.采集数据
        BookDao bookDao = new BookDaoImpl();
        List<Book> books = bookDao.queryBookList();
        //创建一个分词器
        Analyzer analyzer = new IKAnalyzer();
        //为他存储的目录设定一个位置
        Directory directory = FSDirectory.open(new File("D:\\temp\\index3"));
        //创建写入配置
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
        //创建索引写入器
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        for (Book book : books) {
            Document document = new Document();
            Integer id = book.getId();
            String name = book.getName();
            String pic = book.getPic();
            Float price = book.getPrice();
            String desc = book.getDesc();

            document.add(new TextField("id", String.valueOf(id), Field.Store.YES));
            document.add(new TextField("name", name, Field.Store.YES));
            document.add(new TextField("pic", pic, Field.Store.YES));
            document.add(new TextField("price", String.valueOf(price), Field.Store.YES));
            document.add(new TextField("desc", desc, Field.Store.YES));

            //将索引和文档存入索引库
            indexWriter.addDocument(document);
        }

        //释放资源
        indexWriter.close();
    }

    /**
     * 查询索引的方法
     *
     * @throws Exception
     */
    @Test
    public void testQueryIndex() throws Exception {
        //创建精确查询对象
        TermQuery query = new TermQuery(new Term("desc", "lucene"));

        //加载索引库位置
        Directory directory = FSDirectory.open(new File("D:\\temp\\index3"));

        //加载阅读器
        IndexReader reader = DirectoryReader.open(directory);
        //加载索引查找器
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        //按Score值排序获取前5
        TopDocs topDocs = indexSearcher.search(query, 5);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for (ScoreDoc scoreDoc : scoreDocs) {
            int doc = scoreDoc.doc;
            //通过ID获取完整文档
            Document document = indexSearcher.doc(doc);

            System.out.println(document.get("id"));
            System.out.println(document.get("name"));
            System.out.println(document.get("desc"));
            System.out.println(document.get("pic"));
            System.out.println(document.get("price"));
        }
        reader.close();
    }

}
