package util;

import java.util.Set;

public interface DynamoService {

    Set<String> getAllValues(boolean print);

    void putItemInTable(String keyword, String resume);

    Set<String> keywordQuery(String keyword);

}
