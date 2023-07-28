package local;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TestDictionary {

    public static void main(String[] args) {

        Map<String, Set<String>> map = new HashMap<>();
        /* Set.of("ciao", "come", "va").forEach(e -> {
            if (map.containsKey(e)) {
                // Key exists, get the corresponding Set and add the value to it
                map.get(e).add("prova.pdf");
            } else {
                // Key does not exist, create a new Set with the value and put it in the HashMap
                Set<String> newSet = new HashSet<>();
                newSet.add("prova.pdf");
                map.put(e, newSet);
            }
        }); */
        LocalDictionary.serializeDictionary(map);
        Map<String, Set<String>> mapOut = LocalDictionary.deserializeDictionary();
        mapOut.forEach((key, value) -> System.out.println(key));
    }

}
