package com.example.shashank.musicloud;



public class Track {

    public String songName;
    public String songArt;
    public String songId;
    public String downloadLink;


    Track() {

    }

    Track(String songName, String songArt, String songId, String downloadLink) {
        this.songArt = songArt;
        this.songName = songName;
        this.songId = songId;
        this.downloadLink = downloadLink;
    }

}
