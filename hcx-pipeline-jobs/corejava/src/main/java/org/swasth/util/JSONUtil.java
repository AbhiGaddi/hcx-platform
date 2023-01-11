package org.swasth.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JSONUtil {

   public static ObjectMapper mapper = new ObjectMapper();


    public static String serialize(Object obj) throws JsonProcessingException {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T deserialize(String value, Class<T> clazz) throws Exception {
        return mapper.readValue(value, clazz);
    }
    public static <T> T deserialize(byte[] json, TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(json, typeReference);
    }


    public static String encodeBase64String(String input){
        return  Base64.getEncoder().encodeToString(input.getBytes());
    }

    public static <T> T decodeBase64String(String encodedString, Class<T> clazz) throws Exception {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);
        return deserialize(decodedString, clazz);
    }

    public static HashMap<String,Object> parsePayload(String encodedPayload) throws Exception {
        String[] strArray = encodedPayload.split("\\.");
        if (strArray.length> 0 && strArray.length == Constants.PAYLOAD_LENGTH) {
            HashMap<String,Object> event = new HashMap<>();
            event.put(Constants.PROTECTED, strArray[0]);
            event.put(Constants.ENCRYPTED_KEY, strArray[1]);
            event.put(Constants.IV, strArray[2]);
            event.put(Constants.CIPHERTEXT, strArray[3]);
            event.put(Constants.TAG, strArray[4]);
            return event;
        }
        else throw new Exception("payload is not complete");
    }

      public static String createPayloadByValues(Map<String,Object> payload){

        String encodedString = "";
          for (Object objValue : payload.values()) {
              encodedString = encodedString + objValue + ".";
          }
          //Remove the last . after adding all the values
          encodedString = encodedString.substring(0, encodedString.length() - 1);
          return encodedString;
      }

}
