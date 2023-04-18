package com.example.sino.enumtype;

/**
 * Created by root on 9/28/15.
 */
public enum SendingStatusEn {
    Pending(0), InProgress(1), Sent(2), Fail(3), AttachmentResuming(4);

    private int code;

    SendingStatusEn(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
