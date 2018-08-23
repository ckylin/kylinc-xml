package com.kylinc.xml.core;

import com.kylinc.xml.exception.XmlException;

import java.util.LinkedList;

public class XmlComment extends XmlElement {

    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getAttribute(String key) throws XmlException {
        throw new XmlException("XmlComment not support getAttribute");
    }

    @Override
    public String getText() throws XmlException {
        throw new XmlException("XmlComment not support getText");
    }

    public void appendChild(XmlElement xmlElement) throws XmlException {
        throw new XmlException("XmlComment not support appendChild");
    }

    public String getTag() throws XmlException {
        throw new XmlException("XmlComment not support getTag");
    }

    public void setTag(String tag) throws XmlException {
        throw new XmlException("XmlComment not support setTag");
    }

    public boolean isEnd() throws XmlException {
        return true;
    }

    public void setEnd(boolean end) throws XmlException {
        throw new XmlException("XmlComment not support setEnd");
    }

    public XmlAttribute getXmlAttribute() throws XmlException {
        throw new XmlException("XmlComment not support getXmlAttribute");
    }

    public void setXmlAttribute(XmlAttribute xmlAttribute) throws XmlException {
        throw new XmlException("XmlComment not support setXmlAttribute");
    }

    public XmlText getXmlText() throws XmlException {
        throw new XmlException("XmlComment not support getXmlText");
    }

    public void setXmlText(XmlText xmlText) throws XmlException {
        throw new XmlException("XmlComment not support setXmlText");
    }

    public LinkedList<XmlElement> getChildElements() throws XmlException {
        throw new XmlException("XmlComment not support getChildElements");
    }
}
