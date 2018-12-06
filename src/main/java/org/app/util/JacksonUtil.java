package main.java.org.app.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONObject;

public class JacksonUtil {
	public static ObjectMapper objectMapper;

	public static <T> T readValue(String jsonStr, Class<T> valueType) {
		if(objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return (T)objectMapper.readValue(jsonStr, valueType);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static <T> T readValue(String jsonStr, TypeReference<T> valueTypeRef) {
		if(objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return (T)objectMapper.readValue(jsonStr, valueTypeRef);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String toJSON(Object object) {
		if(objectMapper == null) {
			objectMapper = new ObjectMapper();
		}
		try {
			return objectMapper.writeValueAsString(object);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String extractJsonValue(String msgJson, String key) {
		new JSONObject();
		JSONObject jsonObject = JSONObject.fromObject(msgJson);
		
		if((!jsonObject.has(key)) || (jsonObject.getString(key) == null)) {
			return "";
		}
		return jsonObject.getString(key);
	}
	
	public static <T> T extractJsonArray(String msgJson, String key, Class<T> valueType) {
		new JSONObject();
		JSONObject jsonObject = JSONObject.fromObject(msgJson);
		
		if((!jsonObject.has(key)) || (jsonObject.getString(key) == null)) {
			return null;
		}
		String value = jsonObject.getString(key).toString();
		
		if((value == null) || (value.equals(""))) {
			value = "[]";
		}
		
		return (T)readValue("{" + key + "\":" + value + "}",valueType);

	}
	
	public static void main(String[] args) {}
	
	
	
}


























