package com.bytesnmaterials.zro.services;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to show successive state regarding api access and data fetch or authentication.
 *
 * @author mitesh
 * @version 1.0
 * @since 22/7/16
 */
public class ZeroSuccessService {

    public static final int USER_REGISTERED_SUCCESSFULLY = 10;

    private static final Map<Integer, String> successMessages = new HashMap();

    private final int code;
    private final String message;
    private final String details;

    public ZeroSuccessService(int code, String message) {
        this(code, message, (String)null);
    }

    public ZeroSuccessService(int code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details == null?"":details;
    }

    public static ZeroSuccessService fromCode(int code) {
        if(!successMessages.containsKey(Integer.valueOf(code))) {
            throw new IllegalArgumentException("Invalid error code: " + code);
        } else {
            String message = (String)successMessages.get(Integer.valueOf(code));
            return new ZeroSuccessService(code, message, (String)null);
        }
    }

    public String getMessage() {
        return this.message;
    }

    public String getDetails() {
        return this.details;
    }

    static {
        successMessages.put(Integer.valueOf(USER_REGISTERED_SUCCESSFULLY), "User has been registered successfully.");
    }

}
