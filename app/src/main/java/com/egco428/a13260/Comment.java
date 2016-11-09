package com.egco428.a13260;

/**
 * Created by Thammarit on 9/11/2559.
 */
public class Comment {

    private long id;
    private String comment;
    private String date;
    private String first;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {this.date = date;}

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {this.first = first;}


    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return comment;
    }
}
