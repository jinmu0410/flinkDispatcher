/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jm.flink.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * String Utilities
 *
 * <p>Company: www.dtstack.com
 *
 * @author huyifan.zju@163.com
 */
public class StringUtil {

    public static final int STEP_SIZE = 2;

    public static final char[] hexChars = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * Handle the escaped escape charactor.
     *
     * <p>e.g. Turnning \\t into \t, etc.
     *
     * @param str The String to convert
     * @return the converted String
     */
    public static String convertRegularExpr(String str) {
        if (str == null) {
            return "";
        }

        String pattern = "\\\\(\\d{3})";

        Pattern r = Pattern.compile(pattern);
        while (true) {
            Matcher m = r.matcher(str);
            if (!m.find()) {
                break;
            }
            String num = m.group(1);
            int x = Integer.parseInt(num, 8);
            str = m.replaceFirst(String.valueOf((char) x));
        }
        str = str.replaceAll("\\\\t", "\t");
        str = str.replaceAll("\\\\r", "\r");
        str = str.replaceAll("\\\\n", "\n");

        return str;
    }

    /**
     * 16进制数组 转为hex字符串
     *
     * @param b
     * @return
     */
    public static String bytesToHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (byte value : b) {
            int hexVal = value & 0xFF;
            sb.append(hexChars[(hexVal & 0xF0) >> 4]);
            sb.append(hexChars[(hexVal & 0x0F)]);
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String hexString) {
        if (hexString == null) {
            return null;
        }

        int length = hexString.length();

        byte[] bytes = new byte[length / 2];
        for (int i = 0; i < length; i += STEP_SIZE) {
            bytes[i / 2] =
                    (byte)
                            ((Character.digit(hexString.charAt(i), 16) << 4)
                                    + Character.digit(hexString.charAt(i + 1), 16));
        }

        return bytes;
    }

    /**
     * Split the specified string delimiter --- ignored quotes delimiter
     *
     * @param str       待解析字符串,不考虑分割结果需要带'[',']','\"','\''的情况
     * @param delimiter 分隔符
     * @return 分割后的字符串数组 Example: "[dbo_test].[table]" => "[dbo_test, table]" Example:
     * "[dbo.test].[table.test]" => "[dbo.test, table.test]" Example: "[dbo.test].[[[tab[l]e]]"
     * => "[dbo.test, table]" Example："[\"dbo_test\"].[table]" => "[dbo_test, table]"
     * Example:"['dbo_test'].[table]" => "[dbo_test, table]"
     */
    public static List<String> splitIgnoreQuota(String str, char delimiter) {
        List<String> tokensList = new ArrayList<>();
        boolean inQuotes = false;
        boolean inSingleQuotes = false;
        int bracketLeftNum = 0;
        StringBuilder b = new StringBuilder(64);
        char[] chars = str.toCharArray();
        int idx = 0;
        for (char c : chars) {
            char flag = 0;
            if (idx > 0) {
                flag = chars[idx - 1];
            }
            if (c == delimiter) {
                if (inQuotes) {
                    b.append(c);
                } else if (inSingleQuotes) {
                    b.append(c);
                } else if (bracketLeftNum > 0) {
                    b.append(c);
                } else {
                    tokensList.add(b.toString());
                    b = new StringBuilder();
                }
            } else if (c == '\"' && '\\' != flag && !inSingleQuotes) {
                inQuotes = !inQuotes;
                // b.append(c);
            } else if (c == '\'' && '\\' != flag && !inQuotes) {
                inSingleQuotes = !inSingleQuotes;
                // b.append(c);
            } else if (c == '[' && !inSingleQuotes && !inQuotes) {
                bracketLeftNum++;
                // b.append(c);
            } else if (c == ']' && !inSingleQuotes && !inQuotes) {
                bracketLeftNum--;
                // b.append(c);
            } else {
                b.append(c);
            }
            idx++;
        }

        tokensList.add(b.toString());

        return tokensList;
    }

    /**
     * get String from inputStream
     *
     * @param input inputStream
     * @return String value
     * @throws IOException convert exception
     */
    public static String inputStream2String(InputStream input) throws IOException {
        StringBuilder stringBuffer = new StringBuilder();
        byte[] byt = new byte[1024];
        for (int i; (i = input.read(byt)) != -1; ) {
            stringBuffer.append(new String(byt, 0, i));
        }
        return stringBuffer.toString();
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword 需要转义特殊字符串的文本
     * @return 特殊字符串转义后的文本
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (org.apache.commons.lang.StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = {
                    "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|"
            };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    public static String toURLDecode(String value) throws UnsupportedEncodingException {
        return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
    }
}
