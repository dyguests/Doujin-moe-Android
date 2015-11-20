package com.fanhl.doujinMoe.util;

import android.support.annotation.NonNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by fanhl on 15/11/20.
 */
public class NumberUtil {
    @NonNull
    public static String formatPrefix(int index) {
        NumberFormat nf     = new DecimalFormat("0000");
        return nf.format(index + 1);
    }
}
