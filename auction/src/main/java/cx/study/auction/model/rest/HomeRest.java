package cx.study.auction.model.rest;

import android.util.Log;

import com.google.common.collect.Lists;

import org.json.JSONArray;

import java.util.List;

import cx.study.auction.bean.HomeItem;
import cx.study.auction.contants.HttpRest;
import cx.study.auction.model.rest.json2object.Json2HomeItem;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *
 * Created by cheng.xiao on 2017/4/14.
 */

public class HomeRest extends AbstractRest{

    public List<HomeItem> getHomeInfo() throws Exception {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url(HttpRest.HOME_PAGE_REST)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        String json = response.body().string();
        Log.e("dd", "getHomeInfo: " + json);
        List<HomeItem> homeItems = Lists.newArrayList();
       JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i ++){
            HomeItem homeItem = new Json2HomeItem().json2Object(jsonArray.optJSONObject(i));
            homeItems.add(homeItem);
        }
        return homeItems;
    }
}
