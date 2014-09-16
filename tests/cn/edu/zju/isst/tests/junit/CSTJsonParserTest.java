package cn.edu.zju.isst.tests.junit;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import cn.edu.zju.isst.tests.util.CSTFileUtil;
import cn.edu.zju.isst.tests.util.CSTPrinter;
import cn.edu.zju.isst.v2.event.campus.data.CSTCampusEvent;
import cn.edu.zju.isst.v2.globaldata.citylist.CSTCity;
import cn.edu.zju.isst.v2.event.city.event.data.CSTCityEvent;
import cn.edu.zju.isst.v2.event.city.event.data.CSTCityParticipant;
import cn.edu.zju.isst.v2.comment.data.CSTComment;
import cn.edu.zju.isst.v2.data.BasicUser;
import cn.edu.zju.isst.v2.data.CSTAlumni;
import cn.edu.zju.isst.v2.data.CSTExperience;
import cn.edu.zju.isst.v2.data.CSTJob;
import cn.edu.zju.isst.v2.data.CSTJsonParser;
import cn.edu.zju.isst.v2.data.CSTJsonParserUtil;
import cn.edu.zju.isst.v2.data.CSTKlass;
import cn.edu.zju.isst.v2.data.CSTLogin;
import cn.edu.zju.isst.v2.data.CSTMajor;
import cn.edu.zju.isst.v2.data.CSTMessage;
import cn.edu.zju.isst.v2.data.CSTNearby;
import cn.edu.zju.isst.v2.data.CSTParticipated;
import cn.edu.zju.isst.v2.data.CSTPublisher;
import cn.edu.zju.isst.v2.data.CSTRecommend;
import cn.edu.zju.isst.v2.data.CSTRestaurant;
import cn.edu.zju.isst.v2.data.CSTRestaurantMenu;
import cn.edu.zju.isst.v2.data.CSTTask;
import cn.edu.zju.isst.v2.user.data.CSTUser;

public class CSTJsonParserTest {

    @Test
    public void testReadCSTUser() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTUser()====");
        File file = new File("data/CSTUser.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTUser user = null;
        try {
            user = CSTJsonParserUtil.readByte(data, CSTUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(user);
        System.out.println(user.cityId);
        System.out.println(user.cityName);
        System.out.println(user.gender);
        System.out.println(user.jobTitle);
    }

    /*测试CSTJsonParse类对JsonArray的解析*/
    @Test
    public void testReadCSTPublisher() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTPublisher()====");
        File file = new File("data/CSTPublisher.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        String jsonStr = new String(data);
        JSONObject json = null;
        CSTPublisher publisher = new CSTPublisher();
        try {
            json = new JSONObject(jsonStr);
            publisher = (CSTPublisher) CSTJsonParser.parseJson(json, publisher);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(publisher.name);
        System.out.println(publisher.email);
        System.out.println(publisher.phoneNum);
        for (BasicUser i : (List<BasicUser>) publisher.itemList) {
            System.out.println(((CSTPublisher) i).qqNum);
        }
    }

    /*测试CSTJsonParse类对JsonObject的解析，且JavaBean中有域的类型不是Java简单类型*/
    @Test
    public void testReadCSTCityEvent() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTCityEvent()====");
        File file = new File("data/CSTCityEvent.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        String jsonStr = new String(data);
        JSONObject json = null;
        CSTCityEvent cityEvent = new CSTCityEvent();
        try {
            json = new JSONObject(jsonStr);
            cityEvent = (CSTCityEvent) CSTJsonParser.parseJson(json, cityEvent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println(cityEvent.publisher.qqNum);
    }

    /*测试CSTAlumni类*/
    @Test
    public void testReadCSTAlumni() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTAlumni()====");
        File file = new File("data/CSTAlumni.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTAlumni alumni = null;
        try {
            alumni = CSTJsonParserUtil.readByte(data, CSTAlumni.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(alumni.cityId);
        System.out.println(alumni.cityName);
        System.out.println(alumni.gender);
        System.out.println(alumni.jobTitle);

        System.out.println("====end====\n");
    }

    /*测试CSTCampusEvent类*/
    @Test
    public void testReadCSTCampusEvent() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTCampusEvent()====");
        File file = new File("data/CSTCampusEvent.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTCampusEvent campusAty = null;
        try {
            campusAty = CSTJsonParserUtil.readByte(data, CSTCampusEvent.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(campusAty);
        System.out.println("====end====\n");
    }

    /*测试CSTLogin类*/
    @Test
    public void testReadCSTLogin() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTLogin()====");
        File file = new File("data/CSTLogin.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTLogin login = null;
        try {
            login = CSTJsonParserUtil.readByte(data, CSTLogin.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(login);
        System.out.println("====end====\n");
    }

    /*测试CSTCity类*/
    @Test
    public void testReadCSTCity() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTCity()====");
        File file = new File("data/CSTCity.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTCity city = null;
        try {
            city = CSTJsonParserUtil.readByte(data, CSTCity.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(city);
        System.out.println("====end====\n");
    }

    /*测试CSTCityParticipant类*/
    @Test
    public void testReadCSTCityParticipant() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTCityParticipant()====");
        File file = new File("data/CSTCityParticipant.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTCityParticipant cityPart = null;
        try {
            cityPart = CSTJsonParserUtil.readByte(data, CSTCityParticipant.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(cityPart);
        System.out.println("====end====\n");
    }

    /*测试CSTComment类*/
    @Test
    public void testReadCSTComment() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTComment()====");
        File file = new File("data/CSTComment.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTComment comment = null;
        try {
            comment = CSTJsonParserUtil.readByte(data, CSTComment.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(comment);
        System.out.println("====end====\n");
    }

    /*测试CSTExperience类*/
    @Test
    public void testReadCSTExperience() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTExperience()====");
        File file = new File("data/CSTExperience.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTExperience experience = null;
        try {
            experience = CSTJsonParserUtil.readByte(data, CSTExperience.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(experience);
        System.out.println("====end====\n");
    }

    /*测试CSTJob类*/
    @Test
    public void testReadCSTJob() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTJob()====");
        File file = new File("data/CSTJob.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTJob job = null;
        try {
            job = CSTJsonParserUtil.readByte(data, CSTJob.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(job);
        System.out.println("====end====\n");
    }

    /*测试CSTKlass类*/
    @Test
    public void testReadCSTKlass() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTKlass()====");
        File file = new File("data/CSTKlass.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTKlass klass = null;
        try {
            klass = CSTJsonParserUtil.readByte(data, CSTKlass.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(klass);
        System.out.println("====end====\n");
    }

    /*测试CSTMajor类*/
    @Test
    public void testReadCSTMajor() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTMajor()====");
        File file = new File("data/CSTMajor.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTMajor major = null;
        try {
            major = CSTJsonParserUtil.readByte(data, CSTMajor.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(major);
        System.out.println("====end====\n");
    }

    /*测试CSTMessage类*/
    @Test
    public void testReadCSTMessage() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTMessage()====");
        File file = new File("data/CSTMessage.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTMessage message = null;
        try {
            message = CSTJsonParserUtil.readByte(data, CSTMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(message);
        System.out.println("====end====\n");
    }

    /*测试CSTNearby类*/
    @Test
    public void testReadCSTNearby() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTNearby()====");
        File file = new File("data/CSTNearby.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTNearby nearby = null;
        try {
            nearby = CSTJsonParserUtil.readByte(data, CSTNearby.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(nearby);
        System.out.println("====end====\n");
    }

    /*测试CSTParticipated类*/
    @Test
    public void testReadCSTParticipated() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTParticipated()====");
        File file = new File("data/CSTParticipated.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTParticipated participated = null;
        try {
            participated = CSTJsonParserUtil.readByte(data, CSTParticipated.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(participated);
        System.out.println("====end====\n");
    }

    /*测试CSTRecommend类*/
    @Test
    public void testReadCSTRecommend() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTRecommend()====");
        File file = new File("data/CSTRecommend.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTRecommend recommend = null;
        try {
            recommend = CSTJsonParserUtil.readByte(data, CSTRecommend.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(recommend);
        System.out.println("====end====\n");
    }

    /*测试CSTRestaurant类*/
    @Test
    public void testReadCSTRestaurant() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTRestaurant()====");
        File file = new File("data/CSTRestaurant.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTRestaurant restaurant = null;
        try {
            restaurant = CSTJsonParserUtil.readByte(data, CSTRestaurant.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(restaurant);
        System.out.println("====end====\n");
    }

    /*测试CSTRestaurantMenu类*/
    @Test
    public void testReadCSTRestaurantMenu() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTRestaurantMenu()====");
        File file = new File("data/CSTRestaurantMenu.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTRestaurantMenu restaurantMenu = null;
        try {
            restaurantMenu = CSTJsonParserUtil.readByte(data, CSTRestaurantMenu.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(restaurantMenu);
        System.out.println("====end====\n");
    }

    /*测试CSTTask类*/
    @Test
    public void testReadCSTTask() {
        System.out.println("\n====" + this.getClass() + " : testReadCSTTask()====");
        File file = new File("data/CSTTask.json");
        byte[] data = CSTFileUtil.fileToByte(file);
        CSTTask task = null;
        try {
            task = CSTJsonParserUtil.readByte(data, CSTTask.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        CSTPrinter.out(task);
        System.out.println("====end====\n");
    }
}
