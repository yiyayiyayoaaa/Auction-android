package cx.study.auction.model.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import cx.study.auction.BuildConfig;
import cx.study.auction.bean.User;
import cx.study.auction.model.rest.json2object.Json2User;

/**
 * 
 * Created by chengxiao on 2017/4/9.
 */

public class UserDao {
    private String USER = "user";
    SharedPreferences spf;
    public UserDao(Context context){
        spf = context.getSharedPreferences(BuildConfig.APPLICATION_ID,Context.MODE_PRIVATE);
    }

    /**
     * 保存用户信息到本地
     * @param user user
     */
    public void saveUser(User user){
        Gson gson = new Gson();
        if (Double.isNaN(user.getAccount())){
            user.setAccount(0d);
        }
        spf.edit().putString(USER,gson.toJson(user)).apply();
    }

    /**
     * 获取本地用户信息
     * @return User
     */
    public User getLocalUser(){
        String json = spf.getString(USER, null);
        User user = null;
        try {
           if (!TextUtils.isEmpty(json)){
               user = new Json2User().json2Object(new JSONObject(json));
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public void logout(){
        spf.edit().clear().apply();
    }
}
