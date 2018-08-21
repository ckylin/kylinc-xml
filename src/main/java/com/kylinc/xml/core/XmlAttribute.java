package com.kylinc.xml.core;

import com.kylinc.xml.exception.XmlException;

import java.util.HashMap;
import java.util.Map;

public class XmlAttribute extends XmlNode{

    private Map<String,String> data = new HashMap<>();

    public void addAttribute(String key,String value){
        data.put(key,value);
    }

    public void addAttributes(Map<String,String> attr){
        data.putAll(attr);
    }

    public String getAttribute(String key){
        return data.get(key);
    }

    @Override
    public String getText() throws XmlException {
        throw new XmlException("XmlAttribute can't return text");
    }

    public Map<String,String> getAttributes(){
        return data;
    }

}
