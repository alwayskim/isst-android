/**
 *
 */
package cn.edu.zju.isst.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import cn.edu.zju.isst.api.ArchiveCategory;
import cn.edu.zju.isst.api.JobCategory;
import cn.edu.zju.isst.api.UserCenterCategory;
import cn.edu.zju.isst.util.Judge;
import cn.edu.zju.isst.util.Lgr;

/**
 * @deprecated
 * 数据操作类
 *
 * @author theasir
 */
public class DataManager {

    /**
     * 以下字段为数据库表的记录
     */
    public static final String USER_IN_DB = "user";

    public static final String NEWS_LIST_IN_DB = "newslist";

    public static final String WIKI_LIST_IN_DB = "wikilist";

    public static final String STUD_LIST_IN_DB = "studlist";

    public static final String SCAC_LIST_IN_DB = "scaclist";

    public static final String RESTAURANT_LIST_IN_DB = "restaurantlist";

    public static final String EXPERIENCE_LIST_IN_DB = "expelist";

    public static final String MAJOR_LIST_IN_DB = "majorlist";

    public static final String CLASS_LIST_IN_DB = "classlist";

    public static final String CITY_LIST_IN_DB = "citylist";

    public static final String CLASSMATE_LIST_IN_DB = "classmatelist";

    public static final String EMPLOYMENT_LIST__IN_DB = "employmentlist";

    public static final String INTERNSHIP_LIST_IN_DB = "internship";

    public static final String RECOMMEND_LIST_IN_DB = "recommend";

    public static final String MYRECOMMEND_LIST_IN_DB = "myrecommend";

    public static final String MYEXPIENCE_LIST_IN_DB = "myexprience";

    public static final String MYACTIVITIES_LIST_IN_DB = "myactivites";

    public static final String MY_PUBLIC_LIST_IN_DB = "mypublicactivites";

    public static final String MY_PARTICIPATED_LIST_IN_DB = "myparticipatedactivites";


    /**
     * 同步登录接口返回数据
     *
     * @param currentUser 用户
     * @param context     用于加载DBHelper获取当前数据库
     */
    public static void syncCurrentUser(User currentUser) {
        if (currentUser.getId() >= 0 && !currentUser.getUsername().isEmpty()
                && !currentUser.getPassword().isEmpty()) {// TODO 更好的判断user有效的方法
            writeObjectToDB(USER_IN_DB, currentUser);
            Lgr.i("Write user to DB!");
        }
    }

    /**
     * 获取当前数据库中的用户对象
     *
     * @param context 用于加载DBHelper获取当前数据库
     * @return 当前数据库中的用户对象
     */
    public static User getCurrentUser() {
        Object object = objectFromDB(USER_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {// TODO better class cast method?
            User user = null;
            try {
                user = (User) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(user)) {
                return user;
            }
        }
        return null;
    }

    public static void deleteCurrentUser() {
        DBManager.getInstance().delete(USER_IN_DB);
    }

    public static void syncArchiveList(ArchiveCategory archiveCategory,
            List<Archive> newsList) {
        if (!Judge.isNullOrEmpty(newsList)) {
            writeObjectToDB(archiveCategory.getNameInDB(),
                    (Serializable) newsList);
            Lgr.i("Write archiveList to DB!");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Archive> getArchiveList(ArchiveCategory archiveCategory) {
        Object object = objectFromDB(archiveCategory.getNameInDB());
        if (!Judge.isNullOrEmpty(object)) {
            List<Archive> archiveList = null;
            try {
                archiveList = (List<Archive>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(archiveList)) {
                return archiveList;
            }
        }
        return null;
    }

    /**
     * 同步新闻列表接口返回数据
     *
     * @param wikiList 百科列表
     * @param context  用于加载DBHelper获取当前数据库
     */
    public static void syncWikiList(List<Archive> wikiList) {
        if (!Judge.isNullOrEmpty(wikiList)) {
            writeObjectToDB(WIKI_LIST_IN_DB, (Serializable) wikiList);
            Lgr.i("Write wikilist to DB!");
        }
    }

    /**
     * 获取当前数据库中的百科列表对象
     *
     * @param context 用于加载DBHelper获取当前数据库
     * @return 当前数据库中的百科列表对象
     */
    @SuppressWarnings("unchecked")
    public static List<Archive> getCurrentWikiList() {
        Object object = objectFromDB(WIKI_LIST_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {
            List<Archive> wikiList = null;
            try {
                wikiList = (List<Archive>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(wikiList)) {
                return wikiList;
            }
        }
        return null;
    }

    /**
     * 同步在校活动列表接口返回数据
     *
     * @param campusActivityList 新闻列表
     * @param context            用于加载DBHelper获取当前数据库
     */
    public static void syncCampusActivityList(
            List<CampusActivity> campusActivityList) {
        if (!Judge.isNullOrEmpty(campusActivityList)) {
            writeObjectToDB(SCAC_LIST_IN_DB, (Serializable) campusActivityList);
            Lgr.i("Write campusActivityList to DB!");
        }
    }

    /**
     * 获取当前数据库中的在校活动列表对象
     *
     * @param context 用于加载DBHelper获取当前数据库
     * @return 当前数据库中的新闻列表对象
     */
    @SuppressWarnings("unchecked")
    public static List<CampusActivity> getCampusActivityList() {
        Object object = objectFromDB(SCAC_LIST_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {
            List<CampusActivity> campusActivityList = null;
            try {
                campusActivityList = (List<CampusActivity>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(campusActivityList)) {
                return campusActivityList;
            }
        }
        return null;
    }

    public static void syncRestaurantList(List<Restaurant> restaurantList) {
        if (!Judge.isNullOrEmpty(restaurantList)) {
            writeObjectToDB(RESTAURANT_LIST_IN_DB,
                    (Serializable) restaurantList);
            Lgr.i("Write restaurantlist to DB!");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Restaurant> getRestaurantList() {
        Object object = objectFromDB(RESTAURANT_LIST_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {
            List<Restaurant> restaurantList = null;
            try {
                restaurantList = (List<Restaurant>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(restaurantList)) {
                return restaurantList;
            }
        }
        return null;
    }

    public static void syncMajorList(List<Major> majorList) {
        if (!Judge.isNullOrEmpty(majorList)) {
            writeObjectToDB(MAJOR_LIST_IN_DB, (Serializable) majorList);
            Lgr.i("Write majorList to DB!");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Major> getMajorList() {
        Object object = objectFromDB(MAJOR_LIST_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {
            List<Major> majorList = null;
            try {
                majorList = (List<Major>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(majorList)) {
                return majorList;
            }
        }
        return null;
    }

    public static void syncCityList(List<City> cityList) {
        if (!Judge.isNullOrEmpty(cityList)) {
            writeObjectToDB(CITY_LIST_IN_DB, (Serializable) cityList);
            Lgr.i("Write CityList to DB!");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<City> getCityList() {
        Object object = objectFromDB(CITY_LIST_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {
            List<City> cityList = null;
            try {
                cityList = (List<City>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(cityList)) {
                return cityList;
            }
        }
        return null;
    }

    public static void syncClassList(List<Klass> classList) {
        if (!Judge.isNullOrEmpty(classList)) {
            writeObjectToDB(CLASS_LIST_IN_DB, (Serializable) classList);
            Lgr.i("Write class List to DB!");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Klass> getClassList() {
        Object object = objectFromDB(CLASS_LIST_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {
            List<Klass> classMateList = null;
            try {
                classMateList = (List<Klass>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(classMateList)) {
                return classMateList;
            }
        }
        return null;
    }

    public static void syncClassMateList(List<User> classMateList) {
        if (!Judge.isNullOrEmpty(classMateList)) {
            writeObjectToDB(CLASSMATE_LIST_IN_DB, (Serializable) classMateList);
            Lgr.i("Write class List to DB!");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<User> getClassMateList() {
        Object object = objectFromDB(CLASSMATE_LIST_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {
            List<User> classList = null;
            try {
                classList = (List<User>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(classList)) {
                return classList;
            }
        }
        return null;
    }

    public static void syncJobList(JobCategory jobCategory, List<Job> jobList) {
        if (!Judge.isNullOrEmpty(jobList)) {
            writeObjectToDB(jobCategory.getNameInDB(), (Serializable) jobList);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Job> getJobList(JobCategory jobCategory) {
        Object object = objectFromDB(jobCategory.getNameInDB());
        if (!Judge.isNullOrEmpty(object)) {
            List<Job> jobList = null;
            try {
                jobList = (List<Job>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(jobList)) {
                return jobList;
            }
        }
        return null;
    }

    public static void syncUserCenterList(UserCenterCategory userCenterCategory,
            List<UserCenterList> userCenterList) {
        if (!Judge.isNullOrEmpty(userCenterList)) {
            writeObjectToDB(userCenterCategory.getNameInDB(), (Serializable) userCenterList);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<UserCenterList> getUserCenterList(UserCenterCategory userCenterCategory) {
        Object object = objectFromDB(userCenterCategory.getNameInDB());
        if (!Judge.isNullOrEmpty(object)) {
            List<UserCenterList> userCenterList = null;
            try {
                userCenterList = (List<UserCenterList>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(userCenterList)) {
                return userCenterList;
            }
        }
        return null;
    }


    public static void syncMyPublicActivityList(
            List<MyPublicActivity> publicActivityList) {
        if (!Judge.isNullOrEmpty(publicActivityList)) {
            writeObjectToDB(MY_PUBLIC_LIST_IN_DB, (Serializable) publicActivityList);
        }
    }


    @SuppressWarnings("unchecked")
    public static List<MyPublicActivity> getMyPublicActivityList() {
        Object object = objectFromDB(MY_PUBLIC_LIST_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {
            List<MyPublicActivity> publicActivityList = null;
            try {
                publicActivityList = (List<MyPublicActivity>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(publicActivityList)) {
                return publicActivityList;
            }
        }
        return null;
    }


    public static void syncMyParticipatedActivityList(
            List<MyParticipatedActivity> prticipatedActivityList) {
        if (!Judge.isNullOrEmpty(prticipatedActivityList)) {
            writeObjectToDB(MY_PARTICIPATED_LIST_IN_DB, (Serializable) prticipatedActivityList);
        }
    }


    @SuppressWarnings("unchecked")
    public static List<MyParticipatedActivity> getMyParticipatedActivityList() {
        Object object = objectFromDB(MY_PARTICIPATED_LIST_IN_DB);
        if (!Judge.isNullOrEmpty(object)) {
            List<MyParticipatedActivity> prticipatedActivityList = null;
            try {
                prticipatedActivityList = (List<MyParticipatedActivity>) object;
            } catch (ClassCastException e) {
                // TODO: handle exception
            }
            if (!Judge.isNullOrEmpty(prticipatedActivityList)) {
                return prticipatedActivityList;
            }
        }
        return null;
    }

    /**
     * 将目标对象序列化后写入当前数据库
     *
     * @param name   数据库表记录
     * @param object 目标对象
     */
    public static void writeObjectToDB(final String name,
            final Serializable object) {
        new Thread() {

            /*
             * (non-Javadoc)
             *
             * @see java.lang.Thread#run()
             */
            @Override
            public void run() {
                if (Judge.isNullOrEmpty(name) || Judge.isNullOrEmpty(object)) {
                    return;
                }
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(bos);
                    oos.writeObject(object);
                    Lgr.i("Write object!!!");
                    DBManager.getInstance().insertOrUpdate(name,
                            bos.toByteArray());
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

        }.start();
    }

    /**
     * 将数据库中的对象反序列化并读取出来
     *
     * @param name 数据库表记录
     * @return 目标对象
     */
    public static Serializable objectFromDB(final String name) {
        FutureTask<Serializable> task = new FutureTask<Serializable>(
                new Callable<Serializable>() {

                    @Override
                    public Serializable call() throws Exception {
                        Serializable object = null;
                        try {
                            byte[] data = DBManager.getInstance().get(name);
                            if (!Judge.isNullOrEmpty(data)) {
                                ByteArrayInputStream bis = new ByteArrayInputStream(
                                        data);
                                ObjectInputStream ois = new ObjectInputStream(
                                        bis);
                                object = (Serializable) ois.readObject();
                            }
                        } catch (IOException e) {
                            // TODO: handle exception
                        } catch (ClassNotFoundException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        return object;
                    }
                }
        );
        new Thread(task).start();
        try {
            return task.get();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ExecutionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}
