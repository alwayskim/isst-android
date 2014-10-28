/**
 *
 */
package cn.edu.zju.isst.db;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.zju.isst.api.AlumniApi;
import cn.edu.zju.isst.net.CSTResponse;
import cn.edu.zju.isst.net.RequestListener;
import cn.edu.zju.isst.util.Lgr;

import static cn.edu.zju.isst.constant.Constants.STATUS_REQUEST_SUCCESS;

/**
 * @deprecated
 * @author theasir
 */
public class GlobalDataCache {


    public static void cacheCityList(Object o) {
        Lgr.i("cache", "CITY");
        List<City> cityList = new ArrayList<City>();
        AlumniApi.getCityList(new BaseListRequestListener<City>(cityList,
                City.class));
    }

    public static void cacheClassList(Object o) {
        Lgr.i("cache", "CLASS");
        List<Klass> classList = new ArrayList<Klass>();
        AlumniApi.getClassesList(new BaseListRequestListener<Klass>(classList,
                Klass.class));
    }

    public static void cacheMajorList(Object o) {
        Lgr.i("cache", "MAJOR");
        List<Major> majorList = new ArrayList<Major>();
        AlumniApi.getMajorList(new BaseListRequestListener<Major>(majorList,
                Major.class));
    }

    private static class BaseListRequestListener<T> implements RequestListener {

        int status;

        List<T> list;

        Class<T> clazz;

        public BaseListRequestListener(List<T> list, Class<T> clazz) {
            this.list = list;
            this.clazz = clazz;
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onComplete(Object result) {
            Lgr.i("cache", result.toString());
            try {
                status = ((JSONObject) result).getInt("status");
                if (status == STATUS_REQUEST_SUCCESS) {
                    JSONArray jsonArray = ((JSONObject) result)
                            .getJSONArray("body");
                    list.clear();
                    fillList(jsonArray);
                    if (clazz.equals(City.class)) {
                        DataManager.syncCityList((List<City>) list);
                    } else if (clazz.equals(Klass.class)) {
                        DataManager.syncClassList((List<Klass>) list);
                    } else if (clazz.equals(Major.class)) {
                        DataManager.syncMajorList((List<Major>) list);
                    }
                }
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

        @Override
        public void onHttpError(CSTResponse response) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onException(Exception e) {
            // TODO Auto-generated method stub

        }

        private void fillList(JSONArray jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    list.add(clazz.getConstructor(JSONObject.class)
                            .newInstance(jsonArray.get(i)));
                } catch (InstantiationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

}
