package com.anupam.employeemanager;

import com.cgi.card.taf.core.exceptions.TafCoreException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.apache.commons.lang.StringUtils.isEmpty;

@Slf4j
public class JsonUtil {

  private JsonUtil() {}

  public static Object nodeObjectFromJsonPath(String json, String jsonPath) {
    if (isEmpty(json)) {
      throw new IllegalArgumentException("json specified is empty or null");
    }
    if (isEmpty(jsonPath)) {
      throw new IllegalArgumentException("jsonPath specified is empty or null");
    }
    try {
      return JsonPath.read(json, jsonPath);
    } catch (PathNotFoundException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new TafCoreException(ex.getMessage());
    }
  }

  public static JSONArray jsonArrayFromJsonPath(String json, String jsonPath) {
    if (isEmpty(json)) {
      throw new IllegalArgumentException("json specified is empty or null");
    }
    if (isEmpty(jsonPath)) {
      throw new IllegalArgumentException("jsonPath specified is empty or null");
    }
    try {
      JSONArray jsonArray = JsonPath.read(json, jsonPath);
      if (jsonArray.isEmpty()) {
        throw new TafCoreException("jsonPath: " + jsonPath + " is not present in json");
      }
      return jsonArray;
    } catch (PathNotFoundException ex) {
      throw ex;
    } catch (Exception ex) {
      throw new TafCoreException(ex.getMessage());
    }
  }

  public static String jsonFromFile(String filePath) {
    if (isEmpty(filePath)) {
      throw new IllegalArgumentException("filePath specified is empty or null");
    }
    try {
      return new String(Files.readAllBytes(Paths.get(filePath)));
    } catch (Exception ex) {
      throw new TafCoreException("Exception while reading filePath: " + filePath, ex);
    }
  }

  public static String prettyFormat(String json) {
    JSONObject jsonObject = new JSONObject(json);
    return jsonObject.toString(4);
  }

  public static String updateElementUsingJsonPath(String json, String jsonPath, String value) {
    var currentValue = (JSONArray) nodeObjectFromJsonPath(json, jsonPath);
    var jsonObject = new JSONObject(json);
    var updatedJson = JsonPath.parse(jsonObject.toString()).set(jsonPath, value);
    log.debug("Json Path :{} value changed from: {} to :{}", jsonPath, currentValue, value);
    return updatedJson.jsonString();
  }

  public static String deleteElementUsingJsonPath(String json, String jsonPath) {
    var currentValue = (JSONArray) nodeObjectFromJsonPath(json, jsonPath);
    var jsonObject = new JSONObject(json);
    log.debug("Json Path :{} with value: {} deleted from the json", jsonPath, currentValue);
    DocumentContext updatedJson = JsonPath.parse(jsonObject.toString()).delete(jsonPath);
    return updatedJson.jsonString();
  }
}
