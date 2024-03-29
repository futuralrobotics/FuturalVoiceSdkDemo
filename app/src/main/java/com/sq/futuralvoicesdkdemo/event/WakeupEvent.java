package com.jack.newvoice.event;

public class WakeupEvent {
    public boolean method;//true是用户点击唤醒；false是语音唤醒

    public WakeupEvent() {
        this(false);
    }

    public WakeupEvent(boolean method) {
        this.method = method;
    }
}