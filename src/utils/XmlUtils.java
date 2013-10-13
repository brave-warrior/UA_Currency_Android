/**
 * Copyright Khmelenko Lab
 * Author: Dmytro Khmelenko
 */
package utils;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Provides static methods for working with XML file
 * 
 * @author Dmytro Khmelenko
 * 
 */
public final class XmlUtils {

	/**
	 * Denied constructor
	 */
	private XmlUtils() {
	}

	/**
	 * Gets DOM structure
	 * 
	 * @param aXml
	 *            XML string
	 * @return DOM structure
	 */
	public static Document getDomElement(String aXml) {
		Document doc = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder db = dbf.newDocumentBuilder();

			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(aXml));
			doc = db.parse(is);

		} catch (ParserConfigurationException e) {
			return null;
		} catch (SAXException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
		// return DOM
		return doc;
	}

	/**
	 * Gets value by tag name
	 * 
	 * @param aParent
	 *            Parent item
	 * @param aItem
	 *            Tag name
	 * @return Value
	 */
	public static String getValue(Element aParent, String aItem) {
		NodeList n = aParent.getElementsByTagName(aItem);
		return XmlUtils.getElementValue(n.item(0));
	}

	/**
	 * Gets item value
	 * 
	 * @param aElem
	 *            Element
	 * @return Value
	 */
	public static final String getElementValue(Node aElem) {
		Node child;
		if (aElem != null) {
			if (aElem.hasChildNodes()) {
				for (child = aElem.getFirstChild(); child != null; child = child
						.getNextSibling()) {
					if (child.getNodeType() == Node.TEXT_NODE) {
						return child.getNodeValue();
					}
				}
			}
		}
		return "";
	}

}
