package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Currency {
    private String code;
    private String name;
    private double index;

    static Map<String, String> names = new HashMap<>(Map.ofEntries(
            Map.entry("USD", "Доллар"),
            Map.entry("EUR", "Евро")
    ));
    public static int nextUpdateTime = 0;
    public static String[] lastValues;
    public static List<String> values = new ArrayList<String>();

    public Currency createCurr(String code, double index){
        if (code.length() == 3 && index > 0){
            this.code = code;
            this.name = names.getOrDefault(code, code);
            this.index = 1 / index;
            return this;
        }
        else{
            return null;
        }
    }

    public String getCode(){
        return this.code;
    }
    public String getName(){
        return this.name;
    }
    public static Map<String,String> getNames(){
        return names;
    }

    public static void addValue(String key, String name){
        if (key.length() == 3){
                names.put(key, name);
                values.add(key);
                System.out.println("Значение записано");
        }
    }

    @Override
    public String toString(){
        return code + " " + name + "\t" + index;
    }


}
