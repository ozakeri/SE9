package com.example.sino.model;

import java.util.List;

public class ImageItem {
    private String path;
    private List<Integer> attachFileJsonArray;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Integer> getAttachFileJsonArray() {
        return attachFileJsonArray;
    }

    public void setAttachFileJsonArray(List<Integer> attachFileJsonArray) {
        this.attachFileJsonArray = attachFileJsonArray;
    }

    public ImageItem(String path) {
        this.path = path;
    }

    public ImageItem(List<Integer> attachFileJsonArray) {
        this.attachFileJsonArray = attachFileJsonArray;
    }
}
