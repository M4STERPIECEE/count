package com.budget.backend.shared.constant;

public final class AppConstants {

    private AppConstants() {}

    public static final String API_V1_PATH = "/api/v1";
    public static final String DEFAULT_PAGE_SIZE = "20";

    public static final int MAX_SOURCE_LENGTH = 200;
    public static final int MAX_DESCRIPTION_LENGTH = 1000;
    public static final int MAX_CATEGORY_NAME_LENGTH = 100;
    public static final int MAX_EMPLOYER_LENGTH = 200;

    public static final int MIN_YEAR = 2000;
    public static final int MAX_YEAR = 2100;

    public static final int DEFAULT_TOP_REVENUES_LIMIT = 10;

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String PROFILE_DEV = "dev";
    public static final String PROFILE_PROD = "prod";
}
