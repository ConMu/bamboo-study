package com.conmu.bamboo.createpattern.prototype.abs;

/**
 * @author mucongcong
 * @date 2022/08/15 15:44
 * @since
 **/
public abstract class Shape implements Cloneable{
    private String id;
    protected String type;

    protected abstract void draw();

    public String getType(){
        return type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public Object clone() {
        Object clone = null;
        try {
            clone = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
