package com.fanhl.doujinMoe.model;

/**
 * 将一个对象连同一个index打包起来
 * <p>
 * Created by fanhl on 15/11/19.
 */
public class IndexItem<I> {
    public I   item;
    public int index;

    public IndexItem(I item, int index) {
        this.item = item;
        this.index = index;
    }
}
