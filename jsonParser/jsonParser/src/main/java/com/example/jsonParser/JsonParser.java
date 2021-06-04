package com.example.jsonParser;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class JsonParser {

    @SneakyThrows
    public Object parse(String json, @NonNull Class jsonClass) {
        Object jsonObject = jsonClass.newInstance();
        Map<String, String> jsonMap = new HashMap<>();

        //Process JSON String
        jsonMap = generateJsonMap(json);

        //Set field values
        List<Field> declaredFields = Arrays.asList(jsonClass.getDeclaredFields());
        Map<String, String> finalJsonMap = jsonMap;
        declaredFields.forEach(
                (field) -> {
                    setData(field, jsonObject, finalJsonMap.get(field.getName()));
                });
        System.out.println(jsonObject.toString());
        return jsonObject;
    }

    @SneakyThrows
    private void setData(Field field,Object object, String value){

        field.setAccessible(true);
        switch (field.getType().getTypeName()){
            case "java.lang.String":
                field.set(object,value);
                break;
            case "int":
            case "java.lang.Integer":
                field.setInt(object,Integer.parseInt(value));
                break;
            case "byte":
            case "java.lang.Byte":
                field.setByte(object,Byte.parseByte(value));
                break;
            case "short":
            case "java.lang.Short":
                field.setShort(object,Short.parseShort(value));
                break;
            case "long":
            case "java.lang.Long":
                field.setLong(object,Long.parseLong(value));
                break;
            case "float":
            case "java.lang.Float":
                field.setFloat(object,Float.parseFloat(value));
                break;
            case "double":
            case "java.lang.Double":
                field.setDouble(object,Double.parseDouble(value));
                break;
            case "boolean":
            case "java.lang.Boolean":
                field.setBoolean(object,Boolean.parseBoolean(value));
                break;
            case "char":
            case "java.lang.Character":
                field.setChar(object,value.charAt(0));
                break;
            default:
                field.set(object, parse(value,Class.forName(field.getType().getTypeName())));
                break;
        }
    }

    private Map<String, String> generateJsonMap(String jsonString){

        String classValue = new String();
        String classKey = new String();
        boolean innerJson = false;

        Map<String, String> jsonMap = new HashMap<>();
        List<String> split = Arrays.asList(jsonString.replaceFirst("[{]", "").replaceFirst("[}]", "").trim().split(","));
        for (String spli : split) {
            String[] split1 = spli.split(":", 2);
            if (split1[1].contains("{")) {
                innerJson =true;
                classKey = split1[0].replaceAll("\"", "").trim();
                classValue = split1[1].trim();
            }
            else if (spli.contains("}")){
                innerJson = false;
                classValue = String.join(",",classValue, spli);
                jsonMap.put(classKey,classValue);
                classKey = " ";
                classValue = " ";
            }
            else if (innerJson){
                classValue = String.join(",",classValue, spli);
            }
            else {
                jsonMap.put(split1[0].replaceAll("\"", "").trim(), split1[1].replaceAll("\"", "").trim());
            }
        }
        System.out.println(jsonMap);
        return jsonMap;
    }
}