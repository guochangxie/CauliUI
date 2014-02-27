package org.cauli.ui.selenium.source;

import com.auto.ui.element.ElementInfo;
import com.auto.ui.element.TempElement;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tianqing.wang
 */
public class XMLParse {
    private Document document;
    private Logger logger= Logger.getLogger(XMLParse.class);
    public XMLParse(String name){
        //System.out.println(name);
        SAXReader saxReader=new SAXReader();
        saxReader.setEncoding("UTF-8");
        InputStream inputStream=XMLParse.class.getClassLoader().getResourceAsStream(name);
        try {
            if(inputStream==null){
                logger.error("没有找到page类相对应的资源文件");
            }else{
                this.document=saxReader.read(inputStream);
            }
        } catch (DocumentException e) {
            throw new RuntimeException("读取文件失败",e);
        }
    }

    public TempElement getTempElment(String id){
    	//System.out.println(id);
        return parseElement(getElementById(id));
    }

    public Element getElementById(String id){
        return (Element) this.document.selectSingleNode("//element[@id='"+id+"']");
    }

    private TempElement parseElement(Element element){
        String id=element.attributeValue("id");
        String by=element.attributeValue("by");
        String xpath=element.attributeValue("xpath");
        ElementInfo elementInfo=new ElementInfo(id,by,xpath);
        return new TempElement(elementInfo);
    }

    public Map<String,TempElement> getTempElements(){
        List<Element> elements =  this.document.selectNodes("//element[@id='\"+id+\"']");
        Map<String,TempElement> map = new HashMap<String,TempElement>();
        for(Element element:elements){
            String id=element.attributeValue("id");
            String by=element.attributeValue("by");
            String xpath=element.attributeValue("xpath");
            ElementInfo elementInfo=new ElementInfo(id,by,xpath);
            map.put(id,elementInfo.ToTempElement());
        }
        return map;

    }

}
