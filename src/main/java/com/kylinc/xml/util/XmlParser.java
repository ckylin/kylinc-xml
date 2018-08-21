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

        String status = "startTag";
        for(char c:chars){
            if(c=='<'){

                if(offset+1>=chars.length) {
                    throw new RuntimeException("position "+offset+" token error");
                }

                if (chars[offset + 1] == '/') {
                    if(status.equals("text") && last<offset){

                        XmlText xmlText = new XmlText();
                        xmlText.setText(xml.substring(last,offset));
                        Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                        while(iterator.hasNext()){
                            XmlElement element = iterator.next();
                            if(!element.isEnd()){
                                xmlText.setParentNode(element);
                                dataNodes.add(xmlText);
                                break;
                            }
                        }
                        if(!xmlElements.getLast().isEnd()) {
                            xmlText.setParentNode(xmlElements.getLast());
                        }else{
                            throw new XmlException("position " + offset + " token error");
                        }
                    }
                    last = offset + 2;
                    status = "endTag";


                } else {
                    last = offset + 1;
                    status = "startTag";
                }
            }

            if(c=='>'){
                if(chars[offset-1]=='/'){
                    if(!xmlElements.getLast().isEnd()) {
                        xmlElements.getLast().setEnd(true);
                        tagCheckList.remove(tagCheckList.size()-1);
                    }else {
                        XmlElement xmlElement = new XmlElement();
                        xmlElement.setTag(xml.substring(last,offset-1));
                        xmlElement.setEnd(true);
                        Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                        while(iterator.hasNext()){
                            XmlElement element = iterator.next();
                            if(!element.isEnd()){
                                xmlElement.setParentNode(element);
                                break;
                            }
                        }
                        xmlElements.addLast(xmlElement);
                    }


                }else{
                    if(status.equals("endTag")) {
                        Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                        String tag = xml.substring(last,offset);

                        while(iterator.hasNext()) {
                            XmlElement xmlElement = iterator.next();
                            if(xmlElement.getTag().equals(tag)){
                               if(!xmlElement.isEnd()) {
                                   xmlElement.setEnd(true);
                                   break;
                               }else{
                                   throw new XmlException("position " + offset + " token error");
                               }

                            }

                        }
                        if (tagCheckList.get(tagCheckList.size() - 1).equals(xml.substring(last, offset))) {
                            tagCheckList.remove(tagCheckList.size() - 1);
                        } else {
                            throw new XmlException("position " + offset + " token error");
                        }
                    }else if(status.equals("attrKey") || status.equals("attrValue")){
                        status = "text";
                    }else{
                        tagCheckList.add(xml.substring(last, offset));
                        XmlElement xmlElement = new XmlElement();
                        xmlElement.setTag(xml.substring(last,offset));
                        Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                        while(iterator.hasNext()){
                            XmlElement element = iterator.next();
                            if(!element.isEnd()){
                                xmlElement.setParentNode(element);
                                break;
                            }
                        }
                        xmlElements.add(xmlElement);

                        status = "text";
                    }
                }

                last=offset+1;
            }

            if(c==' ' && chars[offset-1]!=' '){
                if(status.equals("startTag")){
                    XmlElement xmlElement = new XmlElement();
                    xmlElement.setTag(xml.substring(last,offset));
                    Iterator<XmlElement> iterator = xmlElements.descendingIterator();
                    while(iterator.hasNext()){
                        XmlElement element = iterator.next();
                        if(!element.isEnd()){
                            xmlElement.setParentNode(element);
                            break;
                        }
                    }
                    xmlElements.add(xmlElement);

                    tagCheckList.add(xml.substring(last, offset));
                    last = offset+1;

                    status = "attrKey";
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
                if(status.equals("attrKey")){
                    String attrKey = xml.substring(last,offset).trim();
                    if(attrKey==null || attrKey.equals("")){
                        throw new RuntimeException("position "+offset+" token error");
                    }
                    attrList.add(attrKey);
                    last = offset+1;
                    status="attrValueFrom";
                }
            }

            if(c=='\"' || c=='\''){
                if(status.equals("attrValueFrom")){
                    status = "attrValueTo";

                    last = offset+1;
                }else if(status.equals("attrValueTo")){
                    attrList.add(xml.substring(last,offset));

                    XmlAttribute xmlAttribute = null;
//                    xmlAttribute.setKey(attrList.get(attrList.size()-2));
//                    xmlAttribute.setValue(attrList.get(attrList.size()-1));
                    if(!xmlElements.getLast().isEnd()) {

                        if(dataNodes.getLast() instanceof XmlAttribute){
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
                    status = "attrKey";
                }
            }
            offset++;
        }

        XmlParseData xmlData = new XmlParseData();
        xmlData.setXmlElements(xmlElements);
        xmlData.setDataNodes(dataNodes);


        return xmlData;
    }

    public static void main(String[] args) throws XmlException {
        String xml = "<notes>\n" +
                "    <note>\n" +
                "        <to>George</to>\n" +
                "        <from>John</from>\n" +
                "        <heading id = \"=test\"  name = \"test\">Reminder</heading>\n" +
                "        <body>Don't forget the meeting!</body>\n" +
                "    </note>\n" +
                "    <note>\n" +
                "        <to>George</to>\n" +
                "        <from>John</from>\n" +
                "        <heading>Reminder</heading>\n" +
                "        <body id='2323232'>" +
                "           <property name='message'>" +
                "           Don't forget the meeting!" +
                "           </property>" +
                "        </body>\n" +
                "    </note>\n" +
                "</notes>";

        parseXml(xml);
    }
}
