package com.rxjava2.android.samples.model;

/**
 * Created by Edward on 2017/7/12 17:26.
 * 自动生成的模版
 */

/**获取某个用户所加入的组织*/
public class UserOrganationBean {
    private String login;
    private int id;
    private String url;
    private String repos_url;
    private String events_url;
    private String hooks_url;
    private String members_url;
    private String public_members_url;
    private String avatar_url;
    private String description;

    @Override
    public String toString() {
        return  login+":"+url;
    }
}
