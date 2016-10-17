package com.wit.contacts.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wit.contacts.bean.NetUser;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wnw on 2016/10/17.
 */

public class LoginModelImp implements ILoginModel{

    private Context context;
    private NetUser user;
    private NetUserLoadingListener netUserLoadingListener;

    @Override
    public void loadNetUser(Context context, NetUser netUser, NetUserLoadingListener loadingListener) {
        this.context = context;
        sendRequestWithVolley(netUser.getPhone(), netUser.getPassword());
        this.netUserLoadingListener = loadingListener;

    }

    private void retData(){
        if(netUserLoadingListener != null){
            netUserLoadingListener.complete(user);
        }
    }

    /**
     * use volley to get the data
     * */
    private void sendRequestWithVolley(String phone, String password){
        String url = "http://119.29.182.235:8080/ssh_contacts/login?user.phone=" + phone+"&user.password="+password;
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseNetUserWithJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("wnw", "Error:" + error.getNetworkTimeMs() + error.getMessage());
            }
        });
        queue.add(request);
    }

    private void parseNetUserWithJSON(String response){
        try{
            JSONObject jsonObject = new JSONObject(response);

            JSONObject resultObject = jsonObject.getJSONObject("user");
            if(resultObject != null){
                user = new NetUser();
                user.setId(resultObject.getInt("id"));
                user.setPhone(resultObject.getString("phone"));
                user.setPhone2(resultObject.getString("phone2"));
                user.setPassword(resultObject.getString("password"));
                user.setEmail(resultObject.getString("email"));
                user.setName(resultObject.getString("name"));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        /**
         * 解析完后返回数据
         * */
        retData();
    }
}
