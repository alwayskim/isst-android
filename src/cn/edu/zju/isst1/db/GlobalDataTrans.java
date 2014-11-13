/**
 *
 */
package cn.edu.zju.isst1.db;

import java.util.List;

/**
 * @deprecated
 * @author theasir
 */
public class GlobalDataTrans {

    public static String getCityNameForId(int cityId) {
        List<City> cityList = DataManager.getCityList();
        for (City city : cityList) {
            if (cityId == city.getId()) {
                return city.getName();
            }
        }
        return "其他";
    }

    public static int getCityIdForName(String cityName) {
        List<City> cityList = DataManager.getCityList();
        for (City city : cityList) {
            if (cityName.equals(city.getName())) {
                return city.getId();
            }
        }
        return 0;
    }
}
