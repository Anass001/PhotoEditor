package com.zeneo.photoeditorpro.Model;

public class Image {

    private String path;
    private String name;
    private String size;
    private String modifation;


    public Image(String path, String name, String size, String modifation) {
        this.path = path;
        this.name = name;
        this.size = size;
        this.modifation = modifation;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getModifation() {
        return modifation;
    }
}
