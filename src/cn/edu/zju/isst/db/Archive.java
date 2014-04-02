/**
 * 
 */
package cn.edu.zju.isst.db;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.isst.util.Judgement;

/**
 * 归档解析类
 * 
 * @author theasir
 * 
 */
public class Archive implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3086117832107474161L;

	/**
	 * 以下字段详见服务器接口文档
	 */
	private int id;
	private String title;
	private String description;
	private long updatedAt;
	private int publisherId;
	private Publisher publisher;
	private String content;
	private String updateTimeString;

	/**
	 * 默认值初始化并更新
	 * 
	 * @param jsonObject
	 *            数据源
	 * @throws JSONException
	 *             未处理异常
	 */
	public Archive(JSONObject jsonObject) throws JSONException {
		id = -1;
		title = "";
		description = "";
		updatedAt = 0;
		publisher = new Publisher(new JSONObject("{}"));// TODO 怎样初始化比较好？
		publisherId = publisher.getId();
		content = "";
		updateTimeString = "";
		update(jsonObject);
	}

	/**
	 * 更新数据，强制判断设计
	 * 
	 * @param jsonObject
	 *            数据源
	 * @throws JSONException
	 *             未处理异常
	 */
	public void update(JSONObject jsonObject) throws JSONException {
		if (!Judgement.isNullOrEmpty(jsonObject)) {
			if (jsonObject.has("id")
					&& !Judgement.isNullOrEmpty(jsonObject.get("id"))) {
				id = jsonObject.getInt("id");
			}
			if (jsonObject.has("title")
					&& !Judgement.isNullOrEmpty(jsonObject.get("title"))) {
				title = jsonObject.getString("title");
			}
			if (jsonObject.has("description")
					&& !Judgement.isNullOrEmpty(jsonObject.get("description"))) {
				description = jsonObject.getString("description");
			}
			if (jsonObject.has("updatedAt")
					&& !Judgement.isNullOrEmpty(jsonObject.get("updatedAt"))) {
				updatedAt = jsonObject.getLong("updatedAt");
				updateTimeString = timeLongToString(updatedAt);
			}
			if (jsonObject.has("user")) {
				Object tryObject = jsonObject.get("user");
				if (!Judgement.isNullOrEmpty(tryObject)
						&& !tryObject.equals("") && !tryObject.equals("null")) {
					publisher = new Publisher(jsonObject.getJSONObject("user"));
				}
			}
			if (jsonObject.has("userId")
					&& !Judgement.isNullOrEmpty(jsonObject.get("userId"))) {
				publisherId = jsonObject.getInt("userId");
			} else {
				publisherId = publisher.getId();
			}
			if (jsonObject.has("content")
					&& !Judgement.isNullOrEmpty(jsonObject.get("content"))) {
				content = jsonObject.getString("content");
			}
		}
	}

	private String timeLongToString(long time) {
		if (time > 0) {
			Date date = new Date(time);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
			return df.format(date);
		}
		return "";
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the updateAt
	 */
	public long getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * @return the publisherId
	 */
	public int getPublisherId() {
		return publisherId;
	}

	/**
	 * @return the publisher
	 */
	public Publisher getPublisher() {
		return publisher;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @return the dateTimeString
	 */
	public String getDateTimeString() {
		return updateTimeString;
	}

}
