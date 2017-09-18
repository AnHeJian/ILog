package com.ILogDisplay.beans;


public class BrandBean {
    private String name = null;  //品牌或型号
    private int value = 0;    //数量
    //private int proportion = 0;    //占比

    public BrandBean(){};
    public BrandBean(String name,int value){
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String toString(){
        return "brand_model:"+name+","+"num:"+value;
    }
}
