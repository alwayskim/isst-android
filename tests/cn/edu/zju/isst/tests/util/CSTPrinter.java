package cn.edu.zju.isst.tests.util;

import java.lang.reflect.Field;

/**
 * Created by i308844 on 7/21/14.
 */
public class CSTPrinter {

    public static void out(Object obj) {
        if (obj == null) {
            return;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        System.out.println(obj.getClass() + " {");
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                System.out.println("    " + field.getName() + " : " + field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("}");
    }
}
