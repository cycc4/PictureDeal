package com.mygdx.game.Logic.Tools;

public class StringTool {

    //获得指定长度的
    public static String getAssignmentLengthString(String string, int length, boolean isClip) {
        int size = string.length();
        if (size > length && isClip) {
            return string.substring(0, length);
        } else if (size < length) {
            StringBuffer stringBuffer = new StringBuffer(string);
            for (int i = 0, s = length - size; i < s; ++i) {
                stringBuffer.append(" ");
            }
            string = stringBuffer.toString();
        }
        return string;
    }
}
