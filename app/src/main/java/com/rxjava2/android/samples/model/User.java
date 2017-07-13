package com.rxjava2.android.samples.model;

/**
 * Created by amitshekhar on 27/08/16.
 */
public class User extends GithubBean{
    public long id;
    public String firstName;
    public String lastName;
    //如果和RESTful api 返回的 字段对应，则直接解析其 value
    public String login;
    public  String name;
    public  String company;
    //变量名 没有 与 RESTful对应的key对应，其将使用java自身的默认值
    public  int followed=-100;

    @Override
    public String toString() {
        return login+","+name+","+company+",following"+followed;
    }
}

/*{
        "login": "daimajia",
        "id": 2503423,
        "avatar_url": "https://avatars2.githubusercontent.com/u/2503423?v=3",
        "gravatar_id": "",
        "url": "https://api.github.com/users/daimajia",
        "html_url": "https://github.com/daimajia",
        "followers_url": "https://api.github.com/users/daimajia/followers",
        "following_url": "https://api.github.com/users/daimajia/following{/other_user}",
        "gists_url": "https://api.github.com/users/daimajia/gists{/gist_id}",
        "starred_url": "https://api.github.com/users/daimajia/starred{/owner}{/repo}",
        "subscriptions_url": "https://api.github.com/users/daimajia/subscriptions",
        "organizations_url": "https://api.github.com/users/daimajia/orgs",
        "repos_url": "https://api.github.com/users/daimajia/repos",
        "events_url": "https://api.github.com/users/daimajia/events{/privacy}",
        "received_events_url": "https://api.github.com/users/daimajia/received_events",
        "type": "User",
        "site_admin": false,
        "name": "代码家",
        "company": "Beijing Normal University",
        "blog": "https://weibo.com/daimajia",
        "location": "Beijing, China",
        "email": null,
        "hireable": true,
        "bio": "Yeah, make a good App .",
        "public_repos": 62,
        "public_gists": 8,
        "followers": 17483,
        "following": 238,
        "created_at": "2012-10-07T02:40:06Z",
        "updated_at": "2017-06-28T04:12:57Z"
        }
        */