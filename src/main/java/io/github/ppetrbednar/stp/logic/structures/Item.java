package io.github.ppetrbednar.stp.logic.structures;

public class Item {

    private Integer uid;
    private String title;

    public Item(Integer uid, String title) {
        this.uid = uid;
        this.title = title;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title + " (" + uid + ")";
    }
}
