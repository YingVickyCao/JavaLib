package com.hades.example.java.lib;

import org.junit.Test;

public class DateUtilTest {

    @Test
    public void compareDate() {
        DateUtil dateUtil = new DateUtil();
        System.out.println(dateUtil.compare("2019-03-07 10:08:00:316", "2019-03-07 10:18:47:081"));
    }
}