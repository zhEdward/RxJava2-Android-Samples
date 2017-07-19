package com.rxjava2.android.samples.model;

/**
 * Created by edward on 2017/7/19.
 */

public class MessageCodeBean extends GithubBean {
    /**
     * code : 20006
     * message : 手机号码格式有误
     * result : 1
     */

    private int code;
    private String message;
    private String result;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return code+":"+message+"****"+result;
    }
}
