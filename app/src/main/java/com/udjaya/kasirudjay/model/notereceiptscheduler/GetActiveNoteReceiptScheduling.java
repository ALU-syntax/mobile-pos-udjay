package com.udjaya.kasirudjay.model.notereceiptscheduler;

import java.util.List;

public class GetActiveNoteReceiptScheduling {

    private boolean success;
    private List<NoteReceiptScheduler> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<NoteReceiptScheduler> getData() {
        return data;
    }

    public void setData(List<NoteReceiptScheduler> data) {
        this.data = data;
    }
}
