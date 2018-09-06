package com.kylinc.xml.util;

import com.kylinc.xml.core.*;
import com.kylinc.xml.exception.XmlException;

import java.util.*;

public class XmlParser {

    public static XmlParseData parseXml(String xml) throws XmlException {

        //检查标签是否缺失
        List<String> tagCheckList = new ArrayList<>();

        //获取标签信息
        List<String> attrList = new ArrayList<>();

        LinkedList<XmlElement> xmlElements = new LinkedList<>();

        LinkedList<XmlNode> dataNodes = new LinkedList<>();

        char[] chars = xml.toCharArray();

        int offset = 0;
        int last = 0;

        XmlFlag flag = XmlFlag.none;
        for(char c:chars){
            if(c=='<'){
                if(flag!=XmlFlag.commentStartTag) {
                    if (offset + 1 >= chars.length) {
                        throw new RuntimeException("position " + offset + " token error");
                    }

                    if (chars[offset + 1] == '/') {
                        if (flag==XmlFlag.text && last < offset) {

                            XmlText xmlText = new XmlText();
                            xmlText.setText(xml.substring(last, offset));
                            Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                            while (iterator.hasNext()) {
                                XmlElement element = iterator.next();
                                if (element instanceof XmlComment) {
                                    continue;
                                }
                                if (!element.isEnd()) {
                                    xmlText.setParentNode(element);
                                    dataNodes.add(xmlText);
                                    break;
                                }
                            }
                            if (!xmlElements.getLast().isEnd()) {
                                xmlText.setParentNode(xmlElements.getLast());
                            } else {
                                throw new XmlException("position " + offset + " token error");
                            }
                        }
                        last = offset + 2;
                        flag = XmlFlag.endTag;


                    } else if (chars[offset + 1] == '!' && chars[offset + 2] == '-' && chars[offset + 3] == '-') {
                        last = offset + 4;
                        flag = XmlFlag.commentStartTag;
                    } else if ((chars[offset + 1] >= 65 && chars[offset+1] <= 90) || (chars[offset + 1] >= 97 && chars[offset+1] <= 122)) {
                        last = offset + 1;
                        flag = XmlFlag.startTag;
                    } else {
                        if(flag!=XmlFlag.text && flag!=XmlFlag.none)
                        throw new XmlException("position " + offset + " token error");
                    }
                }
            }

            if(c=='>'){
                if(flag!=XmlFlag.commentStartTag) {
                    if (chars[offset - 1] == '/') {
                        if (!xmlElements.getLast().isEnd()) {
                            if(flag != XmlFlag.startTag) {
                                xmlElements.getLast().setEnd(true);
                                tagCheckList.remove(tagCheckList.size() - 1);
                            }else{
                                XmlElement xmlElement = new XmlElement();
                                xmlElement.setTag(xml.substring(last, offset - 1));
                                xmlElement.setEnd(true);
                                Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                                while (iterator.hasNext()) {
                                    XmlElement element = iterator.next();
                                    if(element instanceof XmlComment){
                                        continue;
                                    }
                                    if (!element.isEnd()) {
                                        xmlElement.setParentNode(element);
                                        break;
                                    }
                                }
                                xmlElements.addLast(xmlElement);

                            }
                        } else {
                            XmlElement xmlElement = new XmlElement();
                            xmlElement.setTag(xml.substring(last, offset - 1));
                            xmlElement.setEnd(true);
                            Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                            while (iterator.hasNext()) {
                                XmlElement element = iterator.next();
                                if(element instanceof XmlComment){
                                    continue;
                                }
                                if (!element.isEnd()) {
                                    xmlElement.setParentNode(element);
                                    break;
                                }
                            }
                            xmlElements.addLast(xmlElement);
                        }
                        flag = XmlFlag.none;

                    } else {
                        if (flag==XmlFlag.endTag) {
                            Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                            String tag = xml.substring(last, offset);

                            while (iterator.hasNext()) {
                                XmlElement xmlElement = iterator.next();
                                if(xmlElement instanceof XmlComment)
                                    continue;
                                if (xmlElement.getTag().equals(tag)) {
                                    if (!xmlElement.isEnd()) {
                                        xmlElement.setEnd(true);
                                        break;
                                    } else {
                                        throw new XmlException("position " + offset + " token error");
                                    }

                                }

                            }
                            if (tagCheckList.get(tagCheckList.size() - 1).equals(xml.substring(last, offset))) {
                                tagCheckList.remove(tagCheckList.size() - 1);
                            } else {
                                throw new XmlException("position " + offset + " token error");
                            }
                        } else if (flag==XmlFlag.attrKey || flag==XmlFlag.attrValue) {
                            flag = XmlFlag.text;
                        } else if (flag==XmlFlag.text) {

                        }else{
                            tagCheckList.add(xml.substring(last, offset));
                            XmlElement xmlElement = new XmlElement();
                            xmlElement.setTag(xml.substring(last, offset));
                            Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                            while (iterator.hasNext()) {
                                XmlElement element = iterator.next();
                                if(element instanceof XmlComment){
                                    continue;
                                }
                                if (!element.isEnd()) {
                                    xmlElement.setParentNode(element);
                                    break;
                                }
                            }
                            xmlElements.add(xmlElement);

                            flag = XmlFlag.text;
                        }
                    }
                    last=offset+1;
                }else{
                    if(chars[offset-1]=='-' && chars[offset-2]=='-'){
                        XmlComment xmlComment = new XmlComment();
                        xmlComment.setComment(xml.substring(last,offset-2));

                        Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                        while (iterator.hasNext()) {
                            XmlElement element = iterator.next();
                            if(element instanceof XmlComment){
                                continue;
                            }
                            if (!element.isEnd()) {
                                xmlComment.setParentNode(element);
                                break;
                            }
                        }

                        xmlElements.add(xmlComment);

                        flag = XmlFlag.commentEndTag;

                        last = offset+1;
                    }
                }


            }

            if(c==' ' && offset-1>=0 && chars[offset-1]!=' '){
                if(flag==XmlFlag.startTag){
                    XmlElement xmlElement = new XmlElement();
                    xmlElement.setTag(xml.substring(last,offset));
                    Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                    while(iterator.hasNext()){
                        XmlElement element = iterator.next();
                        if(element instanceof XmlComment){
                            continue;
                        }
                        if(!element.isEnd()){
                            xmlElement.setParentNode(element);
                            break;
                        }
                    }
                    xmlElements.add(xmlElement);

                    tagCheckList.add(xml.substring(last, offset));
                    last = offset+1;

                    flag = XmlFlag.attrKey;
                }
//                else if(status.equals("endTag")){
//
//                }else if(status.equals("attrKey")){
//
//                }else if(status.equals("attrValue")){
//
//
//                }else if(status.equals("text")){
//
//                }
            }

            if(c=='='){
                if(flag==XmlFlag.attrKey){
                    String attrKey = xml.substring(last,offset).trim();
                    if(attrKey==null || attrKey.equals("")){
                        throw new RuntimeException("position "+offset+" token error");
                    }
                    attrList.add(attrKey);
                    last = offset+1;
                    flag=XmlFlag.attrValueFrom;
                }
            }

            if(c=='\"' || c=='\''){
                if(flag==XmlFlag.attrValueFrom){
                    flag = XmlFlag.attrValueTo;

                    last = offset+1;
                }else if(flag==XmlFlag.attrValueTo){
                    attrList.add(xml.substring(last,offset));

                    XmlAttribute xmlAttribute = null;
                    if(!xmlElements.getLast().isEnd()) {

                        if(!dataNodes.isEmpty() && dataNodes.getLast() instanceof XmlAttribute){
                            if(dataNodes.getLast().getParentNode()==xmlElements.getLast()){
                                xmlAttribute = (XmlAttribute) dataNodes.getLast();
                            }
                        }
                        if(xmlAttribute == null){
                            xmlAttribute = new XmlAttribute();
                            xmlAttribute.setParentNode(xmlElements.getLast());
                            dataNodes.add(xmlAttribute);
                        }

                        xmlAttribute.addAttribute(attrList.get(attrList.size()-2),attrList.get(attrList.size()-1));

                    }
                    else
                        throw new XmlException("position "+offset+" token error");

                    last = offset+1;
                    flag = XmlFlag.attrKey;
                }
            }
            offset++;
        }

        XmlParseData xmlData = new XmlParseData();
        xmlData.setXmlElements(xmlElements);
        xmlData.setDataNodes(dataNodes);

        if(!tagCheckList.isEmpty()){
            throw new XmlException(tagCheckList+" is not closed");
        }

        return xmlData;
    }


    public static XmlStatement parseStatement(String xml) throws XmlException {
        boolean isState = false;
        XmlStatement xmlStatement = new XmlStatement();
        if(xml.startsWith("<?xml")){
            int start = 5;
            for(int offset=start;offset<xml.length();offset++){
                if(xml.charAt(offset)=='>' && xml.charAt(offset-1)=='?'){
                    isState = true;
                    String xmlStateStr = xml.substring(start,offset-1);

                    String[] xmlStateConfs = xmlStateStr.split("\\s+");
                    for(String xmlStateConf:xmlStateConfs){
                        String[] strs = xmlStateConf.split("=");
                        if(strs.length>1){
                            String key = strs[0].replaceAll("\"","").trim();
                            String value = strs[1].replaceAll("\"","").trim();

                            if(key.equals("version")){
                                xmlStatement.setVersion(value);
                            }else if(key.equals("encoding")){
                                xmlStatement.setEncoding(value);
                            }else if(key.equals("standalone")){
                                xmlStatement.setStandalone(value);
                            }
                        }
                    }
                    xmlStatement.setOffset(offset);

                    return xmlStatement;
                }
            }

            if(!isState){
                throw new XmlException("xml statement is not closed,can't find end tag");
            }
        }else{
            char[] chars = xml.toCharArray();
            boolean findFirst =false;
            int start = 0;
            for(int offset=0;offset<chars.length;offset++){

                if(chars[offset]=='<'){
                    start = offset;
                    findFirst = true;
                }

                if((chars[offset]==' ' || chars[offset]=='>' || chars[offset]=='/')&& start<offset && findFirst){
                    String tag = xml.substring(start,offset);
                    if(tag.startsWith("<?xml"))
                        throw new XmlException("xml statement is not at first");

                    break;
                }
            }
        }

        return xmlStatement;
    }
}
