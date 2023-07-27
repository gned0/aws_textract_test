package local;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LocalDictionary {

    private static final String path = "res/dict.ser";

    public static void serializeDictionary(Map keywordDictionary) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(keywordDictionary);
            out.close();
            fileOut.close();
            System.out.println("HashMap serialized and saved to: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Map<String, Set<String>> deserializeDictionary() {
        HashMap<String, Set<String>> keywordDictionary = null;
        try {
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            keywordDictionary = (HashMap<String, Set<String>>) in.readObject();
            in.close();
            fileIn.close();
            System.out.println("Deserialized HashMap: " + keywordDictionary);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return keywordDictionary;
    }




}
