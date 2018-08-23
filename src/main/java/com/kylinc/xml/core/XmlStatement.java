package com.kylinc.xml.core;

public class XmlStatement {

    private String version = "1.0";

    private String encoding = "UTF-8";

    private String standalone="yes";

    private int offset=-1;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getStandalone() {
        return standalone;
    }

    public void setStandalone(String standalone) {
        this.standalone = standalone;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
