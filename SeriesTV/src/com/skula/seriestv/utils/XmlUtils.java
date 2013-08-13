package com.skula.seriestv.utils;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class XmlUtils {

	public static String getStringFromUrl(String url) {  
        try { 
            DefaultHttpClient httpClient = new DefaultHttpClient(); 
            HttpPost httpPost = new HttpPost(url); 
  
            HttpResponse httpResponse = httpClient.execute(httpPost); 
            HttpEntity httpEntity = httpResponse.getEntity(); 
			
            return EntityUtils.toString(httpEntity);
        } catch (Exception e) {
			return null;
        }
    }	
	
	public static String getValue(Element item, String str) { 
		NodeList n = item.getElementsByTagName(str); 
		return getElementValue(n.item(0));
	} 
	  
	public static String getElementValue(Node elem) { 
		Node child; 
		if( elem != null){ 
		 if (elem.hasChildNodes()){ 
			 for( child = elem.getFirstChild(); child != null; child = child.getNextSibling() ){ 
				 if( child.getNodeType() == Node.TEXT_NODE  ){ 
					 return child.getNodeValue(); 
				 } 
			 } 
		 } 
		} 
		return ""; 
	}
}