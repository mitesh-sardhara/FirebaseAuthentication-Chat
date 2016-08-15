package com.bytesnmaterials.zro.services;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to show failed state regarding api access and data fetch or authentication.
 *
 * @author mitesh
 * @version 1.0
 * @since 22/7/16
 */
public class ZeroErrorService {

    public static final int USER_NOT_FOUND_FOR_EMAIL = -11;
    public static final int USER_SIGNIN_WITH_FACEBOOK_ERROR = -12;
    public static final int USER_SIGNIN_WITH_GOOGLE_ERROR = -13;
    public static final int USER_MULTIPLE_FOUND_FOR_EMAIL = -14;
    public static final int USER_EMAIL_ALREADY_USED_IN_ANOTHER_ACCOUNT = -15;
    public static final int USER_PASSWORD_INVALID = -16;
    public static final int UNABLE_TO_SEND_RESET_LINK = -17;

    public static final String KEY_USER_NOT_FOUND_FOR_EMAIL = "user_not_found";
    public static final String KEY_USER_MULTIPLE_FOUND_FOR_EMAIL = "user_multiple_found";
    public static final String KEY_USER_SIGNIN_WITH_FACEBOOK_ERROR = "user_facebook_signin_error";
    public static final String KEY_USER_SIGNIN_WITH_GOOGLE_EROOR = "user_google__signin_error";
    public static final String KEY_USER_EMAIL_ALREADY_USED_IN_ANOTHER_ACCOUNT = "email_already_used_in_another_account";
    public static final String KEY_USER_PASSWORD_INVALID = "password_invalid";
    public static final String KEY_UNABLE_TO_SEND_RESET_LINK = "unable_reset_password";

    public static final int UNKNOWN = -9999;

    private static final Map<Integer, String> errorReasons = new HashMap();
    private static final Map<Integer, String> errorReasons_ToLog = new HashMap();
    private static Map<String, Integer> errorCodes = null;

    private final int code;
    private final String message;
    private final String details;

    public ZeroErrorService(int code, String message) {
        this(code, message, (String)null);
    }

    public ZeroErrorService(int code, String message, String details) {
        this.code = code;
        this.message = message;
        this.details = details == null?"":details;
    }

    public static ZeroErrorService fromCode(int code) {
        if(!errorReasons.containsKey(Integer.valueOf(code))) {
            throw new IllegalArgumentException("Invalid error code: " + code);
        } else {
            String message = (String)errorReasons.get(Integer.valueOf(code));
            return new ZeroErrorService(code, message, (String)null);
        }
    }

    public static ZeroErrorService fromStatus(String status) {
        return fromStatus(status, (String)null);
    }

    public static ZeroErrorService fromStatus(String status, String reason) {
        return fromStatus(status, reason, (String)null);
    }

    public static ZeroErrorService fromStatus(String status, String reason, String details) {
        Integer code = (Integer)errorCodes.get(status.toLowerCase());
        if(code == null) {
            code = Integer.valueOf(-999);
        }

        String message = reason == null?(String)errorReasons.get(code):reason;
        return new ZeroErrorService(code.intValue(), message, details);
    }

    public static ZeroErrorService fromException(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        String reason = (String)errorReasons.get(Integer.valueOf(-11)) + stringWriter.toString();
        return new ZeroErrorService(USER_NOT_FOUND_FOR_EMAIL, reason);
    }

    public int getCode() {
        return this.code;
    }

    public void doLog(){
        //todo: Log error in the server
    }

    public String getMessage() {
        return this.message;
    }

    public String getDetails() {
        return this.details;
    }

    public String toString() {
        return "Error: " + this.message;
    }

    public ZeroException toException() {
        return new ZeroException("error: " + this.message);
    }

    static {
        errorReasons.put(Integer.valueOf(USER_NOT_FOUND_FOR_EMAIL), "No user account was found for the given email address.");
        errorReasons_ToLog.put(Integer.valueOf(USER_NOT_FOUND_FOR_EMAIL), "No user account was found for the given email address.");

        errorReasons.put(Integer.valueOf(USER_MULTIPLE_FOUND_FOR_EMAIL), "Multiple user accounts was found for the given email address.");
        errorReasons_ToLog.put(Integer.valueOf(USER_MULTIPLE_FOUND_FOR_EMAIL), "Multiple user accounts was found for the given email address.");

        errorReasons.put(Integer.valueOf(USER_SIGNIN_WITH_FACEBOOK_ERROR), "Facebook signin error.");
        errorReasons_ToLog.put(Integer.valueOf(USER_SIGNIN_WITH_FACEBOOK_ERROR), "Facebook signin error.");

        errorReasons.put(Integer.valueOf(USER_SIGNIN_WITH_GOOGLE_ERROR), "Google signin error.");
        errorReasons_ToLog.put(Integer.valueOf(USER_SIGNIN_WITH_GOOGLE_ERROR), "Google signin error.");

        errorReasons.put(Integer.valueOf(USER_EMAIL_ALREADY_USED_IN_ANOTHER_ACCOUNT), "The email address is already in use by another account.");
        errorReasons_ToLog.put(Integer.valueOf(USER_EMAIL_ALREADY_USED_IN_ANOTHER_ACCOUNT), "The email address is already in use by another account.");

        errorReasons.put(Integer.valueOf(USER_PASSWORD_INVALID), "The password is invalid or the user does not have a password.");
        errorReasons_ToLog.put(Integer.valueOf(USER_PASSWORD_INVALID), "The password is invalid or the user does not have a password.");

        errorReasons.put(Integer.valueOf(UNABLE_TO_SEND_RESET_LINK), "Failed to send reset password link.");
        errorReasons_ToLog.put(Integer.valueOf(UNABLE_TO_SEND_RESET_LINK), "Failed to send reset password link.");


        errorCodes = new HashMap();
        errorCodes.put(KEY_USER_NOT_FOUND_FOR_EMAIL, Integer.valueOf(USER_NOT_FOUND_FOR_EMAIL));
        errorCodes.put(KEY_USER_MULTIPLE_FOUND_FOR_EMAIL, Integer.valueOf(USER_MULTIPLE_FOUND_FOR_EMAIL));
        errorCodes.put(KEY_USER_SIGNIN_WITH_FACEBOOK_ERROR, Integer.valueOf(USER_SIGNIN_WITH_FACEBOOK_ERROR));
        errorCodes.put(KEY_USER_SIGNIN_WITH_GOOGLE_EROOR, Integer.valueOf(USER_SIGNIN_WITH_GOOGLE_ERROR));
        errorCodes.put(KEY_USER_EMAIL_ALREADY_USED_IN_ANOTHER_ACCOUNT, Integer.valueOf(USER_EMAIL_ALREADY_USED_IN_ANOTHER_ACCOUNT));
        errorCodes.put(KEY_USER_PASSWORD_INVALID, Integer.valueOf(USER_PASSWORD_INVALID));
        errorCodes.put(KEY_UNABLE_TO_SEND_RESET_LINK, Integer.valueOf(UNABLE_TO_SEND_RESET_LINK));
    }


}
