package com.udjaya.kasirudjay.model.notereceiptscheduler;

public class NoteReceiptScheduler {
    private int id;
    private String name;
    private String message;
    private String start;
    private String end;
    private String list_outlet_id;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getList_outlet_id() {
        return list_outlet_id;
    }

    public void setList_outlet_id(String list_outlet_id) {
        this.list_outlet_id = list_outlet_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
