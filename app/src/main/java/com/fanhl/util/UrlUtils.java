package com.fanhl.util;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * @author fanhl
 * @date 2014-9-17 下午3:52:28
 */
public class UrlUtils {
    public static final String UTF_8 = "UTF-8";

    public static String urlEncode(String s) {
        return urlEncode(s, UTF_8);
    }

    public static String[] urlEncode(String... ss) {
        if (ss == null) {
            return null;
        }

        String[] resultStrings = new String[ss.length];

        for (int i = 0; i < ss.length; i++) {
            resultStrings[i] = urlEncode(ss[i], UTF_8);
        }

        return resultStrings;
    }

    public static String urlEncode(String s, String charsetName) {
        String result = null;
        try {
            result = URLEncoder.encode(s, charsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将url与params拼在一起
     *
     * @param url
     * @param values
     * @return
     */
    public static String getUrlWithParams(String url, String... values) {
        //add fanhl 20150819
        for (int i = 0; i < values.length; i++) {
            if (values[i] == null) {
                values[i] = "";//这里是为了防止 null时抛异常
            }
        }

        return String.format(url, (Object[]) urlEncode(values));
//		return String.format(url, (Object[]) values);
    }

//    /**
//     * 将url与params拼在一起
//     *
//     * @param url
//     * @param nameValuePairs
//     * @return
//     */
//    public static String getUrlWithParams(String url, NameValuePair... nameValuePairs) {
//        // 加参数
//        StringBuilder sb = new StringBuilder();
//        sb.append(url);
//        if (nameValuePairs != null && nameValuePairs.length > 0) {
//
//            // 若url已有?就不再加了
//            boolean hasQuestionMark;
//            if (hasQuestionMark = (url.indexOf('?') < 0)) {
//                sb.append("?");
//            }
//            for (int i = 0; i < nameValuePairs.length; i++) {
//                if (i > 0 || hasQuestionMark) {
//                    sb.append("&");
//                }
//                // FIXME 以后确认是否需要encode
//                sb.append(String.format("%s=%s", nameValuePairs[i].getName(),
//                        nameValuePairs[i].getValue()));
//            }
//        }
//        return sb.toString();
//    }
}
