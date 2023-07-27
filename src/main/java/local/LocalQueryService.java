package local;

import interfaces.QueryService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalQueryService implements QueryService {

    private Map<String, Set<String>> map;

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
        return null;
    }
}
