package org.baali.demos.lucene.test;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.LimitTokenCountAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Before;

public class IndexingTest
{
	protected String[] ids = { "1", "2" };
	protected String[] unIndexed = { "Netherlands", "Italy" };
	protected String[] unStored = { "Amsterdam has lots of bridges",
			"Venice has lots of canals" };
	protected String[] text = { "Amsterdam", "Venice" };

	private Directory directory;

	@Before
	void setup() throws IOException
	{
		directory = new RAMDirectory();
		IndexWriter indexWriter = getWriter();

		for (int i = 0; i < ids.length; i++)
		{
			Document document = new Document();
			document.add(new StringField("id", ids[i], Field.Store.YES));
			
		}
	}

	private IndexWriter getWriter() throws IOException
	{
		Analyzer whiteSpaceAnalyser = new WhitespaceAnalyzer(Version.LUCENE_47);
		Analyzer limitTokenAnalyser = new LimitTokenCountAnalyzer(
				whiteSpaceAnalyser, Integer.MAX_VALUE);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47,
				limitTokenAnalyser);
		return new IndexWriter(directory, config);
	}

}
