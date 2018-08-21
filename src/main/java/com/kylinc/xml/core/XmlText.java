package com.kylinc.xml.core;

import com.kylinc.xml.exception.XmlException;

public class XmlText extends XmlNode {


    private String text;

    @Override
    public String getAttribute(String key) throws XmlException {
        throw new XmlException("XmlText can't return attribute");
    }

    public String getText() {
        return text.trim();
    }

    public void setText(String text) {
        this.text = text;
    }
}
