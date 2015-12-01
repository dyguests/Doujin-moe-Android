package com.fanhl.doujinMoe.rest.model;

import com.fanhl.doujinMoe.model.Book;

import java.util.List;

/**
 * 存放状态,书籍列表
 * <p>
 * Created by fanhl on 15/12/1.
 */
public class FolderResponse {
    /**
     * success : true
     * message :
     * folders : [{"token":"a2ze9msz","title":"A Rainy Afternoon with Friends","objectcount":"20","main":true,"rating":"2.70","date":"11/29/2015"},{"token":"mrejuww9","title":"About-Face","objectcount":"24","main":true,"rating":"4.84","date":"11/29/2015"},{"token":"ywthbq8j","title":"Do You Know About Buddha's Face","objectcount":"24","main":true,"rating":"4.42","date":"11/29/2015"},{"token":"w8wrwmm4","title":"I Like My Sister","objectcount":"22","main":true,"rating":"3.94","date":"11/29/2015"},{"token":"wrevngbe","title":"Jelly Multiplication","objectcount":"20","main":true,"rating":"3.81","date":"11/29/2015"}]
     * complete : false
     */

    public boolean    success;
    public String     message;
    public boolean    complete;
    /**
     * token : a2ze9msz
     * title : A Rainy Afternoon with Friends
     * objectcount : 20
     * main : true
     * rating : 2.70
     * date : 11/29/2015
     */

    public List<Book> folders;
}
