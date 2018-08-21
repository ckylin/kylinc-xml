package com.kylinc.xml.core;

import com.kylinc.xml.exception.XmlException;
import com.kylinc.xml.util.XmlParser;
import com.kylinc.xml.util.XPathUtil;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class XmlDocument extends XmlNode{

    private String rootTag;

    private LinkedList<XmlElement> childElements = new LinkedList<>();

    private XmlAttribute xmlAttribute = new XmlAttribute();

    private XmlText xmlText;

    public XmlAttribute getXmlAttribute() {
        return xmlAttribute;
    }

    public void setXmlAttribute(XmlAttribute xmlAttribute) {
        this.xmlAttribute = xmlAttribute;
    }

    public XmlText getXmlText() {
        return xmlText;
    }

    public void setXmlText(XmlText xmlText) {
        this.xmlText = xmlText;
    }

    public String getRootTag() {
        return rootTag;
    }

    public void setRootTag(String rootTag) {
        this.rootTag = rootTag;
    }

    private XmlDocument(){

    }

    public static XmlDocument createDocumentByXml(String xml) throws XmlException {
        XmlDocument xmlDocument = new XmlDocument();

        XmlParseData xmlData = XmlParser.parseXml(xml);

        Iterator<XmlElement> iterator = xmlData.getXmlElements().iterator();
        while(iterator.hasNext()){
            XmlElement xmlElement = iterator.next();
            if(xmlElement.getParentNode()==null){
                if(xmlDocument.getRootTag()==null)
                    xmlDocument.setRootTag(xmlElement.getTag());
                else
                    throw new XmlException("root tag must be only one");
            }else{
                if( xmlElement.getParentNode() instanceof XmlElement ) {
                    if(((XmlElement) xmlElement.getParentNode()).getTag().equals(xmlDocument.getRootTag()))
                        xmlDocument.appendChild(xmlElement);
                    else
                        ((XmlElement)(xmlElement.getParentNode())).appendChild(xmlElement);
                }
            }
        }

        Iterator<XmlNode> iterator2 = xmlData.getDataNodes().iterator();
        while(iterator2.hasNext()){
            XmlNode xmlNode = iterator2.next();
            if(xmlNode.getParentNode() instanceof XmlElement ) {
                if(((XmlElement) xmlNode.getParentNode()).getTag().equals(xmlDocument.getRootTag())) {
                    if (xmlNode instanceof XmlAttribute)
                        xmlDocument.getXmlAttribute().addAttributes(((XmlAttribute) xmlNode).getAttributes());

                    else if (xmlNode instanceof XmlText)
                        xmlDocument.setXmlText((XmlText) xmlNode);
                }else {
                    if (xmlNode instanceof XmlAttribute)
                        ((XmlElement) (xmlNode.getParentNode())).getXmlAttribute().addAttributes(((XmlAttribute) xmlNode).getAttributes());
                    else if (xmlNode instanceof XmlText)
                        ((XmlElement) (xmlNode.getParentNode())).setXmlText((XmlText) xmlNode);
                }
            }
        }
        return xmlDocument;
    }

    public void appendChild(XmlElement xmlElement){
        childElements.addLast(xmlElement);
    }

    public XmlElement xpath(String path) throws XmlException {
        List<XPath> xpathList = XPathUtil.parse(path);

        List<XmlElement> xmlElements = childElements;


        int n = 0;
        for(XPath xpath:xpathList){
            int num =0;

            String tag = xpath.getTag();
            int index = xpath.getIndex();
            if (xmlElements == null)
                throw new XmlException("your xpath is not found");

            Iterator<XmlElement> iterator = xmlElements.iterator();

            boolean isFound = false;
            while(iterator.hasNext()){
                XmlElement element = iterator.next();
                if(element==null)
                    continue;


                if(element.getTag().equals(tag) && index==num){
                    if(n==xpathList.size()-1){
                        return element;
                    }
                    xmlElements = element.getChildElements();
                    isFound = true;
                    break;
                }
                if(element.getTag().equals(tag))
                    num++;
            }

            if(!isFound)
                throw new XmlException("can't found element by the xpath");

            n++;
        }
        throw new XmlException("can't found element by the xpath");
    }

    @Override
    public String getAttribute(String key) throws XmlException {
        return xmlAttribute.getAttribute(key);
    }

    @Override
    public String getText() throws XmlException {
        return xmlText.getText().trim();
    }
}
