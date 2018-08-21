package com.kylinc.xml.core;

import com.kylinc.xml.exception.XmlException;

public abstract class XmlNode {

    private XmlNode parentNode;


    public void setParentNode(XmlNode parentNode) {
        this.parentNode = parentNode;
    }

    public XmlNode getParentNode() {
        return parentNode;
    }

    public abstract String getAttribute(String key) throws XmlException;

    public abstract String getText() throws XmlException;
}
