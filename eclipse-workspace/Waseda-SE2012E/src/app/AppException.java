/*
 * Copyright(C) 2007-2013 National Institute of Informatics, All rights reserved.
 */
package app;

import java.util.ArrayList;
import java.util.List;

/**
 * Common Exception class specific to HRS
 */
public class AppException extends Exception {

    // ジェネリクスを明示して型安全性を確保
    private List<String> detailMessages = new ArrayList<>();

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    // List<String> に修正
    public List<String> getDetailMessages() {
        return detailMessages;
    }

    public String getFormattedDetailMessages(String separator) {
        StringBuilder buffer = new StringBuilder();
        String message = getMessage();
        if (message != null) {
            buffer.append(message);
            buffer.append(separator);
        }

        List<String> detailMessages = getDetailMessages();
        if (!detailMessages.isEmpty()) {
            buffer.append("Detail:");
            buffer.append(separator);
            for (String msg : detailMessages) {
                buffer.append(msg);
                buffer.append(separator);
            }
        }
        return buffer.toString();
    }
}
