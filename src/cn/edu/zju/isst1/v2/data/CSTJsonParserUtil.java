package cn.edu.zju.isst1.v2.data;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsonorg.JsonOrgModule;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by i308844 on 7/15/14.
 */
public class CSTJsonParserUtil {

    private final static ObjectMapper mapper;

    private final static TypeFactory typeFactory;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JsonOrgModule());
        typeFactory = mapper.getTypeFactory();
    }

    public static <T> T readByte(byte[] data, Class<T> clazz) throws IOException {
        return mapper.readValue(data, clazz);
    }

    public static <T> T readString(String jsonString, Class<T> clazz) throws IOException {
        return mapper.readValue(jsonString, clazz);
    }

    public static <T> T readJsonObject(JSONObject jsonObject, Class<T> clazz) throws IOException {
        return mapper.readValue(jsonObject.toString(), clazz);
    }

    public static <T> List<T> readJsonArray(JSONArray jsonArray, Class<T> clazz)
            throws IOException {
        return readString(jsonArray.toString(), List.class, clazz);
    }

    public static Object readString(String jsonStr, JavaType javaType) throws JsonParseException,
            JsonMappingException, IOException {
        Object result = mapper.readValue(jsonStr, javaType);
        return result;
    }

    /**
     * 用于解析简单的集合类型
     */
    public static <T> List<T> readString(String jsonStr, Class<? extends List> collectionClazz,
            Class<T> elementClazz) throws JsonParseException, JsonMappingException, IOException {
        JavaType javaType = collectionToJavaType(collectionClazz, elementClazz);
        List<T> listResult = mapper.readValue(jsonStr, javaType);
        return listResult;
    }

    /**
     * 用于解析复合的集合类型
     */
    public static <T> List<T> readString(String jsonObj, Class<? extends List> collectionClazz,
            JavaType javaTypeElement) throws JsonParseException, JsonMappingException, IOException {
        JavaType javaType = collectionToJavaType(collectionClazz, javaTypeElement);
        List<T> listResult = mapper.readValue(jsonObj, javaType);
        return listResult;
    }


    public static <K, V> Map<K, V> readString(String jsonStr, Class<? extends Map> mapClazz,
            Class<K> keyClazz,
            Class<V> valueClazz) throws JsonParseException, JsonMappingException, IOException {
        JavaType javaType = mapToJavaType(mapClazz, keyClazz, valueClazz);
        Map<K, V> mapResult = mapper.readValue(jsonStr, javaType);
        return mapResult;
    }

    public static <K, V> Map<K, V> readString(String jsonStr, Class<? extends Map> mapClazz,
            JavaType keyJavaType,
            JavaType valueJavaType) throws JsonParseException, JsonMappingException, IOException {
        JavaType javaType = mapToJavaType(mapClazz, keyJavaType, valueJavaType);
        Map<K, V> mapResult = mapper.readValue(jsonStr, javaType);
        return mapResult;
    }

    /*以下是JavaType的转换方法*/

    /**
     * 简单类转换为JavaType对象
     */
    public static JavaType simpleClassToJavaType(Class<?> element) {
        JavaType javaType = typeFactory.uncheckedSimpleType(element);
        return javaType;
    }

    /**
     * 简单的集合类型（即List<T>中的泛型为简单类）转换为JavaType
     */
    public static JavaType collectionToJavaType(Class<? extends List> collectionClazz,
            Class<?> elementClazz) {
        JavaType javaType = typeFactory.constructParametricType(collectionClazz, elementClazz);
        return javaType;
    }

    /**
     * 复合的集合类型转换为JavaType的方法，比如List<T>中的泛型为List<T>,此处采用collectionToJavaType的2个方法进行递归解析
     * 递归思路为：将最里层的List<T>先转换为JavaType，则往外层得到List<JavaType>,接着继续解析得到该层的JavaType，重复操作，
     * 直到最外层，得到最终能用于解析的JavaType对象
     */
    public static JavaType collectionToJavaType(Class<? extends List> collectionClazz,
            JavaType elementJavaType) {
        JavaType javaType = typeFactory.constructParametricType(collectionClazz, elementJavaType);
        return javaType;
    }

    /**
     * 用于将简单的Map<K,V>转换为JavaType对象
     */
    public static JavaType mapToJavaType(Class<? extends Map> mapClazz, Class<?> keyClazz,
            Class<?> valueClazz) {
        JavaType javaType = typeFactory.constructMapType(mapClazz, keyClazz, valueClazz);
        return javaType;
    }

    /**
     * 用于将复合的Map<K,V>转换为JavaType对象
     */
    public static JavaType mapToJavaType(Class<? extends Map> mapClazz, JavaType keyJavaType,
            JavaType valueJavaType) {
        JavaType javaType = typeFactory.constructMapType(mapClazz, keyJavaType, valueJavaType);
        return javaType;
    }
}
