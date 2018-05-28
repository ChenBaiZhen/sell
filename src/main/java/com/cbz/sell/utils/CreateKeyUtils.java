package com.cbz.sell.utils;

import java.util.Random;

/**
 * 生成唯一主键
 */

public class CreateKeyUtils {
    public static String  createKey() {
        Random random = new Random();
        Integer i=random.nextInt(9000000)+1000000;
        return System.currentTimeMillis()+String.valueOf(i);
    }

}
