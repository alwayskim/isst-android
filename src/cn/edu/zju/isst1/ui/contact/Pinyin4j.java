package cn.edu.zju.isst1.ui.contact;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.Comparator;
import java.util.Locale;

import cn.edu.zju.isst1.db.User;


public class Pinyin4j {

    /**
     * 获取一个汉字的拼音，大写
     */
    public static String getHanyuPinyi(char word) {
        String contactPinYinString;
        if (word >= 'a' && word <= 'z') {
            contactPinYinString = String.valueOf(word);
        } else {
            contactPinYinString = concatPinyinStringArray(
                    PinyinHelper.toHanyuPinyinStringArray(word));
        }
        return String.valueOf(contactPinYinString.charAt(0)).toUpperCase(Locale.ENGLISH);

    }

    /**
     * 将拼音数组合并成一个字符串
     */
    private static String concatPinyinStringArray(String[] pinyinArray) {
        StringBuffer pinyinSbf = new StringBuffer();
        if ((pinyinArray != null) && (pinyinArray.length > 0)) {
            for (int i = 0; i < pinyinArray.length; i++) {
                pinyinSbf.append(pinyinArray[i]);
            }
        }
        return pinyinSbf.toString();
    }

    /**
     * 汉字按照拼音排序的比较器
     *
     * @author yyy
     */
    public static class PinyinComparator implements Comparator<Object> {

        public int compare(Object o1, Object o2) {
            char c1 = (((User) o1).getName()).charAt(0);
            char c2 = (((User) o2).getName()).charAt(0);
            return concatPinyinStringArray(
                    PinyinHelper.toHanyuPinyinStringArray(c1)).compareTo(
                    concatPinyinStringArray(PinyinHelper
                            .toHanyuPinyinStringArray(c2))
            );
        }
    }
}

