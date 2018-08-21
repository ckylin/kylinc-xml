package com.kylinc.xml.util;

import com.kylinc.xml.core.XPath;
import com.kylinc.xml.exception.XmlException;

import java.util.ArrayList;
import java.util.List;

public class XPathUtil {

    public static List<XPath> parse(String xmlpath) throws XmlException {
        String[] paths = xmlpath.split("\\.");

        List<XPath> xpathList = new ArrayList<>();
        for(int i=0;i<paths.length;i++){
            String path = paths[i];
            XPath xpath = new XPath();
            int start =0;
            for(int j=0;j<path.length();j++){
                if(path.charAt(j)=='['){
                    xpath.setTag(path.substring(start,j));
                    start = j+1;
                }

                int index = 0;
                if(path.charAt(j)==']' && start<j){
                    try {
                        index = Integer.valueOf(path.substring(start, j));

                    }catch (Exception e){
                        throw new XmlException("parse xpath error，xpath:"+xmlpath+",error point is "+j+",error:"+e.getMessage());
                    }

                }

                xpath.setIndex(index);
            }
            if(xpath.getTag()==null){
                xpath.setTag(path);
            }

            xpathList.add(xpath);
        }

        return xpathList;
    }
}
