package com.nina.webCollector.model;

public class DocumentWithBLOBs extends Document {
    private String content;

    private String contenthtml;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getContenthtml() {
        return contenthtml;
    }

    public void setContenthtml(String contenthtml) {
        this.contenthtml = contenthtml == null ? null : contenthtml.trim();
    }
}