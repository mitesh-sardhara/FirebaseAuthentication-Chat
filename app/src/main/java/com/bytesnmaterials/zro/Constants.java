package com.bytesnmaterials.zro;

/**
 * Created by jehandad.kamal on 7/3/2016.
 */
public class Constants {

    //public static final String DEFAULT_AUTH_PASSWORD = "Zerofirebase123#$";
    public static final String AUTH_PASSWORD = "password";

    //Shared Preferences
    public static final String PREFS_NAME = "ZERO_PREFERENCES";

    public static final String KEY_IS_LOGGED_IN = "isUserLoggedIn";
    public static final String KEY_LOGGED_IN_USER_ID = "loggedInUserId";
    public static final String KEY_LOGGED_IN_USER = "loggedInUser";

    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_UID = "uId";
    public static final String KEY_USER_EMAIL = "email";
    public static final String KEY_USER_STATUS = "status";
    public static final String KEY_USER_PASSWORD = "password";
    public static final String KEY_USER_FULL_NAME = "fullName";
    public static final String KEY_USER_NICK_NAME = "nickName";
    public static final String KEY_AUTH_PROVIDER = "authProvider";
    public static final String KEY_REGISTRATION_DATE_TIME = "registrationDateTime";
    public static final String KEY_COUNTRY = "country";

    public static final String URL_LOCATION_FROM_IP = "http://ip-api.com/json";

    public static final String STATUS_VERIFIED = "ACTIVE";
    public static final String STATUS_PENDING = "PENDING";

    public static final String NODE_USERS = "users";
    public static final String NODE_CHAT_HUBS = "chatHubs";
    public static final String NODE_CHAT_HUB_MEMBERS = "chatHubMembers";
    public static final String NODE_CHAT_HUB_MESSAGES = "chatHubMessages";
    public static final String NODE_USER_CHAT = "userChats";
    public static final String NODE_RECIPIENT_CHAT = "recipientChats";

    public static final String AUTH_GOOGLE = "Google";
    public static final String AUTH_FACEBOOK = "Facebook";
    public static final String AUTH_EMAIL = "Email";
    public static final String SELECTED_AUTH_PROVIDER = "selectedAuthProvider";

    /*Chat Feature*/
    public static final int REQUEST_CREATE_NEW_CHAT = 99;
    public static final int REQUEST_RESUME_CHAT = 98;

    public static final String KEY_SELECTED_CHAT_ID = "selectedChatId";
    public static final String KEY_SELECTED_CHAT_NAME = "selectedChatName";
    public static final String KEY_SELECTED_CHAT_TYPE = "selectedChatType";
    public static final String KEY_SELECTED_RECIPIENT_UID = "selectedRecipientUid";
    public static final String KEY_SELECTED_RECIPIENT_NAME = "selectedRecipientName";
    public static final String KEY_CREATE_RESUME_CHAT = "createResumeChat";
    public static final String KEY_CHAT_FROM = "chatFrom";

    public static final int CHAT_TYPE_PERSONAL = 1;
    public static final int CHAT_TYPE_GROUP = 2;
    public static final int FROM_USERS = 88;
    public static final int FROM_CHATS = 89;


}