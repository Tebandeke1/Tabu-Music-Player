package com.example.tabuaimusicplayer;

public class MusicUtilies
{

    public String TimeUtilies ( int totalDuration){

        String TimeLable = "";

        int minutes = totalDuration/1000/60;
        int seconds = totalDuration /1000 % 60;

        TimeLable += minutes + ":";

        if(seconds < 10) TimeLable += "0";

        TimeLable += seconds;

        return TimeLable;

    }
}
