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

public class RegisterModelImp implements IRegisterModel {
    private Context context;
    private NetUserRegisterListener netUserRegisterListener;
    private boolean returnData = false;

    @Override
    public void registerNetUser(Context context, NetUser netUser, NetUserRegisterListener netUserRegisterListener) {
        this.context =context;
        this.netUserRegisterListener = netUserRegisterListener;
        sendRequestWithVolley(netUser.getPhone(), netUser.getName(), netUser.getPassword());
    }

    private void retData(){
        if(netUserRegisterListener != null){
            netUserRegisterListener.complete(returnData);
        }
    }

    /**
     * use volley to get the data
     * */
    private void sendRequestWithVolley(String phone,String name,  String password){
        String url = "http://119.29.182.235:8080/ssh_contacts/register?user.phone="+ phone
                +"&user.name=" + name +"&user.password=" + password;
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
            returnData = jsonObject.getBoolean("ret");
        }catch (JSONException e){
            e.printStackTrace();
        }

        /**
         * 解析完后返回数据
         * */
        retData();
    }
}
