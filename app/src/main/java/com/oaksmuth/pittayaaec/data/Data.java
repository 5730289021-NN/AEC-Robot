package com.oaksmuth.pittayaaec.data;

import com.orm.SugarRecord;

/**
 * Created by Oak on 6/3/2559.
 */
public class Data extends SugarRecord {
    //ID	Topic	Subtopic	Number	Question	Answer	English 1	Thai 1	English 2	Thai 2	English 3	Thai 3	English 4	Thai 4	English 5	Thai 5
    private long ID;
    public Long getId() {
        return ID;
    }

    String Topic;
    String SubTopic;
    int Number;
    String Question;
    String Answer;
    String English1;
    String Thai1;
    String English2;
    String Thai2;
    String English3;
    String Thai3;
    String English4;
    String Thai4;
    String English5;
    String Thai5;
}
