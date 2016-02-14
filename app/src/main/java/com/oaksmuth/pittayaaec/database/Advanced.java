package com.oaksmuth.pittayaaec.database;

import io.realm.RealmObject;

/**
 * Created by Oak on 14/2/2559.
 * If it's beyond Page 300+ so it'll be Advanced
 */
public class Advanced extends RealmObject{
    private String subTopic;

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }
}
