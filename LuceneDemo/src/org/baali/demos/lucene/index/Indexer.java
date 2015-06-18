package org.baali.demos.lucene.index;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class Indexer
{
	private IndexWriter writer;

	public Indexer(String dir) throws IOException
	{
		Directory idxDir = FSDirectory.open(new File(dir));
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, new StandardAnalyzer(
				Version.LUCENE_47));
		writer = new IndexWriter(idxDir, config);
	}

	public int index(String dataDir, FileFilter filter) throws IOException
	{
		File[] files = new File(dataDir).listFiles();
		for(File f : files)
		{
			if(!f.isDirectory() && !f.isHidden() && f.exists() && f.canRead() && 
					(filter == null || filter.accept(f)))
			{
				indexFile(f);
			}
		}
		return writer.numDocs();
	}

	public void close() throws IOException
	{
		writer.close();
	}

	private void indexFile(File f) throws IOException
	{
		System.out.println("Indexing.. " + f.getCanonicalPath());
		Document document =  getDocument(f);
		writer.addDocument(document);
	}

	private Document getDocument(File f) throws IOException
	{
		Document document = new Document();
		document.add(new TextField("content", new FileReader(f)));
		document.add(new StringField("filename", f.getName(), Field.Store.YES));
		document.add(new StringField("fullpath", f.getCanonicalPath(), Field.Store.YES));
		
		
		return document;
	}
	
	private static class TextFilesFilter implements FileFilter
	{
		@Override
		public boolean accept(File pathname)
		{
			// TODO Auto-generated method stub
			return pathname.getName().endsWith(".txt");
		}		
	}

	public static void main(String[] args) throws IOException
	{

		long start = System.currentTimeMillis();
		Indexer indexer = new Indexer("data");
		int indexFilesCount = 0;
		try
		{
			indexFilesCount = indexer.index("files", new TextFilesFilter());
		}
		catch(Exception e)
		{
			System.out.println("Error on indexing in main method..");
		}
		finally
		{
			indexer.close();
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Indexed files: " + indexFilesCount + " Time: " + (end - start) + " milliseconds....");
		
	}

}
