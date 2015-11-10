package com.fanhl.doujinMoe.api.form;

import com.fanhl.doujinMoe.model.Book;

import java.util.List;

public class NewestForm {

    /**
     * success : true
     * message :
     * top : {"token":"s33j1tsh","name":"Practical Exercises"}
     * artist : {"token":"7gm18aax","name":"Darabuchi"}
     * newest : [{"token":"q6muejvz","name":"Best Position","count":"24","rating":"4.50","date":"Nov 6th 2015"},{"token":"gyme9rx1","name":"Locker Girl Nanami-chan","count":"16","rating":"4.50","date":"Nov 6th 2015"}]
     */
    public boolean    success;
    public String     message;
    /**
     * token : s33j1tsh
     * name : Practical Exercises
     */
    public TokenName  top;
    /**
     * token : 7gm18aax
     * name : Darabuchi
     */
    public TokenName  artist;
    /**
     * token : q6muejvz
     * name : Best Position
     * count : 24
     * rating : 4.50
     * date : Nov 6th 2015
     */
    public List<Book> newest;

    public static class TokenName {
        public String token;
        public String name;
    }
}
