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
    public void appendChild(XmlElement xmlElement) throws XmlException {
        childElements.addLast(xmlElement);
    }

    public String getTag() throws XmlException {
        return tag;
    }

    public void setTag(String tag) throws XmlException {
        this.tag = tag;
    }

    public boolean isEnd() throws XmlException {
        return isEnd;
    }

    public void setEnd(boolean end) throws XmlException {
        isEnd = end;
    }

    public XmlAttribute getXmlAttribute() throws XmlException {
        return xmlAttribute;
    }

    public void setXmlAttribute(XmlAttribute xmlAttribute) throws XmlException {
        this.xmlAttribute = xmlAttribute;
    }

    public XmlText getXmlText() throws XmlException {
        return xmlText;
    }

    public void setXmlText(XmlText xmlText) throws XmlException {
        this.xmlText = xmlText;
    }

    public LinkedList<XmlElement> getChildElements() throws XmlException {
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
