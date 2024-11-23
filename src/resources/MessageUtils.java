
package resources;

import java.util.ResourceBundle;

public class MessageUtils {
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("messages");

    public static String getMessage(String key, Object... params) {
        String message = BUNDLE.getString(key);
        if (params != null && params.length > 0) {
            return java.text.MessageFormat.format(message, params);
        }
        return message;
    }
}
