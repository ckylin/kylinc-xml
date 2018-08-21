package com.kylinc.xml.core;


import com.kylinc.xml.exception.XmlException;

import java.util.LinkedList;

public class XmlElement extends XmlNode {

    private String tag;

    private boolean isEnd;

    private XmlAttribute xmlAttribute = new XmlAttribute();

    private XmlText xmlText;

    private LinkedList<XmlElement> childElements = new LinkedList<>();

    //添加子节点
    public void appendChild(XmlElement xmlElement){
        childElements.addLast(xmlElement);
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isEnd() {
        return isEnd;
    }

    public void setEnd(boolean end) {
        isEnd = end;
    }

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

    public LinkedList<XmlElement> getChildElements() {
        return childElements;
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
