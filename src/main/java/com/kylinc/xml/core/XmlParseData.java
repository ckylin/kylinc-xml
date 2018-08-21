package com.kylinc.xml.core;

import java.util.LinkedList;

public class XmlParseData {

    private LinkedList<XmlElement> xmlElements = new LinkedList<>();

    private LinkedList<XmlNode> dataNodes = new LinkedList<>();

    public LinkedList<XmlElement> getXmlElements() {
        return xmlElements;
    }

    public void setXmlElements(LinkedList<XmlElement> xmlElements) {
        this.xmlElements = xmlElements;
    }

    public LinkedList<XmlNode> getDataNodes() {
        return dataNodes;
    }

    public void setDataNodes(LinkedList<XmlNode> dataNodes) {
        this.dataNodes = dataNodes;
    }
}
