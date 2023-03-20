package com.mygdx.game.Draw;

public class MyString {

    public static String getEquidistanceString(String string, int distance){
        int length = string.length();
        if(length < distance){
            StringBuffer sb = new StringBuffer();
            sb.append(string);
            for(int i=0, s=distance - length; i<s; ++i){
                sb.append(" ");
            }
            return sb.toString();
        }
        return string;
    }
}
