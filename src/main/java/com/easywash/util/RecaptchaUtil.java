package com.easywash.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import java.io.StringReader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import org.w3c.dom.Document;

public class RecaptchaUtil {

	public static final Map<String, String> RECAPTCHA_ERROR_CODE = new HashMap<>();
    static {
    	RECAPTCHA_ERROR_CODE.put("missing-input-secret", 
    			"The secret parameter is missing");
    	RECAPTCHA_ERROR_CODE.put("invalid-input-secret", 
    			"The secret parameter is invalid or malformed");
    	RECAPTCHA_ERROR_CODE.put("missing-input-response", 
    			"The response parameter is missing");
    	RECAPTCHA_ERROR_CODE.put("invalid-input-response", 
    			"The response parameter is invalid or malformed");
    	RECAPTCHA_ERROR_CODE.put("bad-request", 
    			"The request is invalid or malformed");
    }
          public static String decode(String url)  
            {  
                       try {  
                            String prevURL="";  
  
                         String decodeURL=url;  
                            while(!prevURL.equals(decodeURL))  
  
                         {  
                                 prevURL=decodeURL;  
                                decodeURL=URLDecoder.decode( decodeURL, "UTF-8" );  
   
                         }  
                             return decodeURL;  
                        } catch (UnsupportedEncodingException e) {  
                             return "Issue while decoding" +e.getMessage();  
   
                    }  
  
          }  
             public static String encode(String url)  
         
                {  
                                    try {  
                                         String encodeURL=URLEncoder.encode( url, "UTF-8" );  
         
                               return encodeURL;  
                                    } catch (UnsupportedEncodingException e) {  
         
                               return "Issue while encoding" +e.getMessage();  
                                  }  
                         }  
             public static Document convertStringToXMLDocument(String xmlString) 
             {
                 //Parser that produces DOM object trees from XML content
                 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                  
                 //API to obtain DOM Document instance
                 DocumentBuilder builder = null;
                 try
                 {
                     //Create DocumentBuilder with default configuration
                     builder = factory.newDocumentBuilder();
                      
                     //Parse the content to Document object
                     Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
                     return doc;
                 } 
                 catch (Exception e) 
                 {
                     e.printStackTrace();
                 }
                 return null;
             }
             public static String addClosingTagInInputElement(String input)
             {
            	 return input;
             }
}