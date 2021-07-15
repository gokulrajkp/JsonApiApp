package com.example.jsonapiapp.room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Recipe implements Serializable {

    @PrimaryKey
    int id;

    @ColumnInfo(name = "width")
    int width;

    @ColumnInfo(name = "height")
    int height;


    @ColumnInfo(name = "t_author")
    String author;

    @ColumnInfo(name = "img_url")
    String imgurl;

    @ColumnInfo (name = "downloadurl")
    String downloadurl;

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImageUrl() {
        return imgurl;
    }

    public void setImageUrl(String imageUrl) {
        this.imgurl = imageUrl;
    }

    public String getDownloadUrl() {
        return downloadurl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadurl = downloadUrl;
    }

}
