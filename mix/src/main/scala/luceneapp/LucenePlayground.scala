package luceneapp

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.document.Document
import org.apache.lucene.document.Field
import org.apache.lucene.document.TextField
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.index.IndexWriter
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.IndexSearcher
import org.apache.lucene.store.Directory
import org.apache.lucene.store.RAMDirectory

// https://www.baeldung.com/lucene
// https://coderlessons.com/articles/java/vvedenie-v-lucene
object LucenePlayground extends App {

  val memoryIndex: Directory = new RAMDirectory();
  val analyzer: StandardAnalyzer = new StandardAnalyzer();
  val indexWriterConfig: IndexWriterConfig = new IndexWriterConfig(analyzer);
  val writter: IndexWriter = new IndexWriter(memoryIndex, indexWriterConfig);
  val document: Document = new Document();

  val title = "Goodness of Tea"
  val body = "Discussing goodness of drinking herbal tea..."

  document.add(new TextField("title", title, Field.Store.YES));
  document.add(new TextField("body", body, Field.Store.YES));

  writter.addDocument(document);
  writter.close();

  def searchIndex(inField: String, queryString: String) = {
    val query = new QueryParser(inField, analyzer).parse(queryString)
    val indexReader = DirectoryReader.open(memoryIndex)
    val searcher = new IndexSearcher(indexReader)
    val topDocs = searcher.search(query, 10)

    val documents = new java.util.ArrayList[Document]

    topDocs.scoreDocs.foreach { scoreDoc =>
      documents.add(searcher.doc(scoreDoc.doc))
    }

    documents
  }
}
