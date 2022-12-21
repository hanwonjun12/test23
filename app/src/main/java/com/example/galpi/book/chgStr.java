package com.example.galpi.book;

public class chgStr {
    static String[] temp;
    static String match = "[^\uAC00-\uD7A30-9a-zA-Z\\s]";
        public static String[] foo(String sentence){
        sentence=sentence.replaceAll(match,"");
        temp=sentence.split(" ");
        for(String s:temp){
            s.trim();
        }
        return temp;


    }
}
