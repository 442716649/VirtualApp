package com.lody.virtual;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {

    @Test
    public void testNotificationTag() {
        String tag = "com.test:hello 23k@2";
        if (tag != null) {
            Pattern pattern = Pattern.compile("^([a-zA-Z\\.]+):([^@]+?)@(\\d+)$");
            Matcher m = pattern.matcher(tag);
            if(m.find()){
                System.out.println(m.groupCount());
                System.out.println(m.group(1));
                System.out.println(m.group(2));
                System.out.println(m.group(3));
            }
        }
    }
}
