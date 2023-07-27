package local;

import interfaces.QueryService;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LocalQueryService implements QueryService {

    private final Map<String, Set<String>> map;

    public LocalQueryService() {
        map = LocalDictionary.deserializeDictionary();
        this.updateDictionary();
    }

    @Override
    public synchronized void closeService() {
        LocalDictionary.serializeDictionary(map);
    }

    @Override
    public synchronized void updateDictionary() {

    }

    @Override
    public synchronized Set<String> lookupKeyword(String keyword) {
        return map.containsKey(keyword) ? map.get(keyword) : new HashSet<>();
    }

    private void addValue(String keyword, String resume) {
        if (map.containsKey(keyword)) {
            // Key exists, get the corresponding Set and add the value to it
            map.get(keyword).add(resume);
        } else {
            // Key does not exist, create a new Set with the value and put it in the HashMap
            Set<String> newSet = new HashSet<>();
            newSet.add(resume);
            map.put(keyword, newSet);
        }
    }
}
