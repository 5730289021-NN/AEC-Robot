package com.oaksmuth.pittayaaec.database;

import io.realm.RealmObject;

/**
 * Created by Oak on 14/2/2559.
 * It's the meaning of all words in ()
 */
public class Remark extends RealmObject{
    private String English;
    private String Thai;

    public String getEnglish() {
        return English;
    }

    public void setEnglish(String english) {
        English = english;
    }

    public String getThai() {
        return Thai;
    }

    public void setThai(String thai) {
        Thai = thai;
    }
}
