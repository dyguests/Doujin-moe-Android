package com.fanhl.doujinMoe.model;

/**
 * Created by fanhl on 15/11/6.
 */
public class Page {
    /*预览图(src链接)*/
    public String preview;
    /*大图(src链接)*/
    public String href;

    @Override
    public String toString() {
        return "Page(" + preview + "," + href + ")";
    }
}
