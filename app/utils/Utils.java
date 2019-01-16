package utils;

import java.io.IOException;
import java.io.StringWriter;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
	static ObjectMapper objectMapper = new ObjectMapper();
	public static String getJsonStringFromObject(Object obj) throws JsonGenerationException,
	JsonMappingException, IOException {
		StringWriter writer = new StringWriter();
		objectMapper.writeValue(writer, obj);
		return writer.toString();
	}
	
	public static JsonNode convertObjectToJsonNode(Object object) {
		JsonNode jsonAdObject = objectMapper.valueToTree(object);
		return jsonAdObject;
	}

	static public Integer generateThreadId() {
		Long currentEpocTime = Instant.now().getEpochSecond();
		Long randomNumber = currentEpocTime/987+(currentEpocTime%10000000);
		return randomNumber.intValue();
	}
	static public Integer generateMailId() {
		Long currentEpocTime = Instant.now().getEpochSecond();
		Long randomNumber = currentEpocTime/458+(currentEpocTime%100000);
		return randomNumber.intValue();
	}
	
}
