/**
 * 
 */
package cn.edu.zju.isst.db;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.zju.isst.util.Judgement;

/**
 * 商家解析类
 * 
 * @author theasir
 * 
 */
public class Restaurant implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -840848870091650489L;

	/**
	 * 以下字段详见服务器接口文档
	 */
	private int id;
	private String name;
	private String picture;
	private String address;
	private String hotline;
	private String businessHours;
	private String description;
	private String content;

	/**
	 * 默认值初始化并更新
	 * 
	 * @param jsonObject
	 *            数据源
	 * @throws JSONException
	 *             未处理异常
	 */
	public Restaurant(JSONObject jsonObject) throws JSONException {
		id = -1;
		name = "";
		hotline = "";
		picture = "";
		address = "";
		businessHours = "";
		description = "";
		content = "";
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
			if (Judgement.isValidJsonValue("id", jsonObject)) {
				id = jsonObject.getInt("id");
			}
			if (Judgement.isValidJsonValue("name", jsonObject)) {
				name = jsonObject.getString("name");
			}
			if (Judgement.isValidJsonValue("picture", jsonObject)) {
				picture = jsonObject.getString("picture");
			}
			if (Judgement.isValidJsonValue("address", jsonObject)) {
				address = jsonObject.getString("address");
			}
			if (Judgement.isValidJsonValue("hotline", jsonObject)) {
				hotline = jsonObject.getString("hotline");
			}
			if (Judgement.isValidJsonValue("businessHours", jsonObject)) {
				businessHours = jsonObject.getString("businessHours");
			}
			if (Judgement.isValidJsonValue("description", jsonObject)) {
				description = jsonObject.getString("description");
			}
			if (Judgement.isValidJsonValue("content", jsonObject)) {
				content = jsonObject.getString("content");
			}
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the picture
	 */
	public String getPicture() {
		return picture;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the hotline
	 */
	public String getHotline() {
		return hotline;
	}

	/**
	 * @return the businessHours
	 */
	public String getBusinessHours() {
		return businessHours;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
}
