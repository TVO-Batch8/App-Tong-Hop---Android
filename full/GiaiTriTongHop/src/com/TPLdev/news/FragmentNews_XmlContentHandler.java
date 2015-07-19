package com.TPLdev.news;

//sax handler
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FragmentNews_XmlContentHandler extends DefaultHandler {
	// CHINH LA MYSAXHANDLER
	// private static final String LOG_TAG = "XmlContentHandler";

	private boolean inOwner = false; // vao item
	// private boolean inDog = false;

	// accumulate the values
	private StringBuilder mStringBuilder = new StringBuilder();

	// new object
	private FragmentNews_ParsedDataSet mParsedDataSet = new FragmentNews_ParsedDataSet();

	// the list of data
	private List<FragmentNews_ParsedDataSet> mParsedDataSetList = new ArrayList<FragmentNews_ParsedDataSet>();

	/*
	 * Called when parsed data is requested.
	 */
	public List<FragmentNews_ParsedDataSet> getParsedData() {
		return this.mParsedDataSetList;
	}

	// Methods below are built in, we just have to do the tweaks.

	/*
	 * @Receive notification of the start of an element.
	 * 
	 * @Called in opening tags such as <Owner>
	 */
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {

		if (localName.equals("item")) {
			this.mParsedDataSet = new FragmentNews_ParsedDataSet();
			this.inOwner = true;
		}

		/*
		 * else if (localName.equals("Dog")) { this.mParsedDataSet = new
		 * ParsedDataSet(); this.inDog = true; }
		 */
	}

	/*
	 * @Receive notification of the end of an element.
	 * 
	 * @Called in end tags such as </Owner>
	 */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {

		// Owners
		if (this.inOwner == true && localName.equals("item")) {
			this.mParsedDataSetList.add(mParsedDataSet);
			mParsedDataSet.setParentTag("item");
			this.inOwner = false;
		} else if (this.inOwner == true && localName.equals("title")) {
			mParsedDataSet.setTitle(mStringBuilder.toString().trim());
		} else if (this.inOwner == true && localName.equals("description")) {
			mParsedDataSet.setDescription(mStringBuilder.toString().trim());
		} // else if (this.inOwner == true && localName.endsWith("content")) {
		else if (this.inOwner == true && localName.equals("link")) {
			mParsedDataSet.setLink(mStringBuilder.toString().trim());
		} else if (this.inOwner == true && localName.equals("pubDate")) {
			mParsedDataSet.setPubDate(mStringBuilder.toString().trim());
		} else if (this.inOwner == true && localName.equals("summaryImg")) {
			mParsedDataSet.setSummaryImg(mStringBuilder.toString().trim());
		} else {

		}
		mStringBuilder.setLength(0);
	}

	/*
	 * @Receive notification of character data inside an element.
	 * 
	 * @Gets be called on the following structure: <tag>characters</tag>
	 */
	@Override
	public void characters(char ch[], int start, int length) {
		// append the value to our string builder
		mStringBuilder.append(ch, start, length);
	}
}