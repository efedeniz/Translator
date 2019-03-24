package com.example.efede.translator;

public class Word {
    private String target,source;
    private long time;

    public Word(){

    }

    public Word(String target,String source,long time){
        this.target = target;
        this.source = source;
        this.time = time;

    }


    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
