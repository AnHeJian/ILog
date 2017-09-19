package com.ILogDisplay.beans;

public class detaillocbean {
    private String name;
    private int uv;
    private int pv;
    private double live;

    public void setLive(double live) {
        this.live = live;
    }

    public double getLive() {
        return live;

    }

    public String getName() {
        return name;
    }

    public int getPv() {
        return pv;
    }

    public int getUv() {
        return uv;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPv(int pv) {
        this.pv = pv;
    }

    public void setUv(int uv) {
        this.uv = uv;
    }


}
