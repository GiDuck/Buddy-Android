package com.example.gdtbg.buddy.VO;

/**
 * Created by gdtbg on 2017-11-22.
 */

public class ActivityVO {
    String content;
    Long time;

    public ActivityVO(String content, Long time) {
        this.content = content;
        this.time = time;
    }

    public ActivityVO() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
