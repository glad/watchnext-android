package com.glad.watchnext.data.provider.themoviedb.remote.retrofit.model.common;

/**
 * Created by Gautam Lad
 */
public final class TmdbEnum {
    public enum GenderType {
        GENDER_NOTSET,
        GENDER_MALE,
        GENDER_FEMALE
    }

    public enum ReleaseType {
        UNKONWN,
        PREMIERE, /* 1 */
        THEATRICAL_LIMITED, /* 2 */
        THEATRICAL, /* 3 */
        DIGITAL, /* 4 */
        PHYSICAL, /* 5 */
        TV, /* 6 */
    }
}
