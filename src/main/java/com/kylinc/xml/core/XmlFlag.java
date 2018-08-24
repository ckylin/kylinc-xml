package com.kylinc.xml.core;

public enum XmlFlag {

    none("无"),
    startTag("开始标识"),
    endTag("结束标识"),
    commentStartTag("备注开始标识"),
    commentEndTag("备注结束标识"),
    attrKey("属性名称标识"),
    attrValue("属性值标识"),
    attrValueFrom("属性值开始标识"),
    attrValueTo("属性值结束标识"),
    text("文本标识");

    private String name;
    XmlFlag(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
