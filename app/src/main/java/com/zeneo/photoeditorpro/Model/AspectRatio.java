package com.zeneo.photoeditorpro.Model;

public class AspectRatio {

    private String name;
    private int width;
    private int hight;

    public AspectRatio(String name, int width, int hight) {
        this.name = name;
        this.width = width;
        this.hight = hight;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public int getHight() {
        return hight;
    }
}


