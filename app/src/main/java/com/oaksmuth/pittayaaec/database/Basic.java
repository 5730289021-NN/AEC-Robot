package com.oaksmuth.pittayaaec.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Oak on 14/2/2559.
 * Standard Object
 */
public class Basic extends RealmObject{
    @PrimaryKey
    private int id;
    private int no;
    private String topic;
    private String question;
    private String answer;
    private String subQuestion;
    private String subAnswer;
    private RealmList<Remark> remarks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSubQuestion() {
        return subQuestion;
    }

    public void setSubQuestion(String subQuestion) {
        this.subQuestion = subQuestion;
    }

    public String getSubAnswer() {
        return subAnswer;
    }

    public void setSubAnswer(String subAnswer) {
        this.subAnswer = subAnswer;
    }

    public RealmList<Remark> getRemarks() {
        return remarks;
    }

    public void setRemarks(RealmList<Remark> remarks) {
        this.remarks = remarks;
    }
}
