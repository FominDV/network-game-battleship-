package ru.fomin.battleship.common;

import java.util.SplittableRandom;
import java.util.Vector;

public class LibraryOfPrefixes {
    public static final String DELIMITER = "±";
    public static final String AUTH_ACCEPT = "/auth_accept";
    public static final String AUTH_DENIED = "/auth_denied";
    public static final String MSG_FORMAT_ERROR = "/msg_format_error";
    public static final String AUTH_REQUEST = "/auth_request";
    public static final String REGISTRATION = "/registration";
    public static final String SEARCH_OPPONENT = "/search_opponent";
    public static final String DISCONNECT_OPPONENT = "/disconnect_opponent";
    public static final String STOP_SEARCHING = "/stop_searching";
    public static final String MESSAGE_ABOUT_START_SEARCHING = "/message_about_start_searching";
    public static final String LIST_OF_DATA_MAP = "/list_of_data_map";


    public static String getAuthAccept(String nickname) {
        return AUTH_ACCEPT + DELIMITER + nickname;
    }

    public static String getAuthDenied() {
        return AUTH_DENIED;
    }

    public static String getMsgFormatError(String message) {
        return MSG_FORMAT_ERROR + DELIMITER + message;
    }

    public static String getAuthRequest(String login, String password) {
        return AUTH_REQUEST + DELIMITER + login + DELIMITER + password;
    }

    public static String getRegistrationRequest(String login, String password, String nickName) {
        return REGISTRATION + DELIMITER + login + DELIMITER + password + DELIMITER + nickName;
    }
    public static String getRegistrationAnswer(String answer) {
        return REGISTRATION + DELIMITER + answer;
    }

    public static String getSearchOpponent(String nickname) {
        return SEARCH_OPPONENT+DELIMITER+nickname;
    }


    public static String getDataOfMapList(Vector<String> dataMap) {
        String dataOfMapList=LIST_OF_DATA_MAP;
        for(String data: dataMap){
            dataOfMapList+=DELIMITER+data;
        }
        return dataOfMapList;
    }
}
