package helper;

import java.util.*;

public class LanguageManager {
    private static ResourceBundle bundle;

    public static void setLanguage(String langCode) {
        Locale locale = new Locale(langCode);
        bundle = ResourceBundle.getBundle("lang.messages", locale);
    }

    public static String get(String key) {
        if (bundle == null) setLanguage("id"); // default
        return bundle.getString(key);
    }
}
