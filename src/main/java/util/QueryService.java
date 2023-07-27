package util;

import java.util.Set;

public interface QueryService {

    void closeService();
    void updateDictionary();
    Set<String> lookupKeyword(String keyword);
    void printDictionary();

}
