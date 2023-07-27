package local;

import local.LocalDictionary;

import java.util.Map;
import java.util.Set;

public class TestDictionary {

    public static void main(String args[]) {

        Map<String, Set<String>> map = LocalDictionary.deserializeDictionary();
        map.forEach((key, value) -> System.out.println(key));
    }

}
