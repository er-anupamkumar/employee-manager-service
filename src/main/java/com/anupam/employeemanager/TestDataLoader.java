package com.anupam.employeemanager;

import com.cgi.card.taf.core.exceptions.TafCoreException;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Map;

import static java.util.Objects.isNull;

@Slf4j
@Component
public class TestDataLoader {

  public Map<String, Object> loadFromJsonFile(
      String fileName, String environment, String testCaseName) {

    var filePath = fileName + "-" + environment + ".json";
    var testDataJson = JsonUtil.jsonFromFile(filePath);
    if (isNull(testDataJson)) {
      throw new IllegalStateException("Unable to read test data from file:" + filePath);
    }
    if (StringUtils.isEmpty(environment) || StringUtils.isEmpty(testCaseName)) {
      throw new IllegalArgumentException(
          "Parameter environment and/or testCaseName is null or empty");
    }

    var testCaseJsonPath = "$.." + testCaseName;
    var testCaseDataArray =
        (JSONArray) JsonUtil.nodeObjectFromJsonPath(testDataJson, testCaseJsonPath);
    if (testCaseDataArray.isEmpty()) {
      String dataExtractionMessage =
          "No Data found with identifier :"
              + testCaseName
              + " found for Environment :"
              + environment;
      log.error(dataExtractionMessage);
      throw new TafCoreException(dataExtractionMessage);
    } else {
      try {
        return (Map<String, Object>) testCaseDataArray.get(0);
      } catch (Exception e) {
        String dataExtractionMessage = "No Data found with identifier :" + e.getMessage();
        throw new TafCoreException(dataExtractionMessage);
      }
    }
  }
}
