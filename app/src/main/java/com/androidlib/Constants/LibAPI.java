package com.androidlib.Constants;

/**
 * Created by divyanshu.jain on 12/6/2017.
 */

public interface LibAPI {
    String BASE = "http://52.210.142.106:1234/";
    String DOCTORS = BASE + "doctors";
    String MOTHERS = BASE + "mothers";
    String NURSES = BASE + "nurses";
    String MILK_DEPARTMENTS = BASE + "milkdepartments";
    String BASE_MESSAGE = BASE + "messages/";
    String GET_CHAT = BASE_MESSAGE + "get";
    String POST_MESSAGE = BASE_MESSAGE + "post";
}
