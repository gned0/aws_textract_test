package interfaces;

import java.util.Set;

public interface QueryService {

    void closeService();
    void updateDictionary();
    Set<String> lookupKeyword(String keyword);

}
