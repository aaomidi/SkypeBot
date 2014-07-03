package com.aaomidi.dev.skypebot.engine;


public interface Callback<T> {
    public void execute(T response);
}
