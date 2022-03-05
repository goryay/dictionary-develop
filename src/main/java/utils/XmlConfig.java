package utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;

public class XmlConfig {
    String xmlFile;
    
    public XmlConfig() {
        xmlFile = "config.xml";
    }

    public XmlConfig(String path) {
        xmlFile = path;
    }

    //Получение параметра по ключам
    public String getByKey(String key){
        try {
            File fXmlFile = new File(xmlFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("config");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                Element eElement = (Element) nNode;

                if (eElement.getAttribute("name").equals(key))
                    return eElement.getAttribute("value");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
