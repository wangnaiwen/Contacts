package com.wit.contacts.model;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wit.contacts.bean.CurrentUser;
import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.NetUser;
import com.wit.contacts.bean.User;
import com.wit.contacts.dao.CurrentUserDAO;
import com.wit.contacts.dao.CurrentUserImpl;
import com.wit.contacts.dao.GroupDao;
import com.wit.contacts.dao.GroupDaoImp;
import com.wit.contacts.dao.UserDao;
import com.wit.contacts.dao.UserDaoImp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by wnw on 2016/10/17.
 */

public class LoginModelImp implements ILoginModel{

    private CurrentUserDAO currentUserDAO;
    private GroupDao groupDao;
    private UserDao userDao;

    private Context context;
    private NetUser user;
    private NetUserLoadingListener netUserLoadingListener;

    @Override
    public void loadNetUser(Context context, NetUser netUser, NetUserLoadingListener loadingListener) {
        currentUserDAO = new CurrentUserImpl();
        groupDao = new GroupDaoImp();
        userDao = new UserDaoImp();

        this.context = context;
        sendRequestWithVolley(netUser.getPhone(), netUser.getPassword());
        this.netUserLoadingListener = loadingListener;
    }

    private void retData(){

        if(user != null){
            CurrentUser currentUser = new CurrentUser();
            currentUser.setId(user.getId());
            currentUser.setName(user.getName());
            currentUser.setPhone(user.getPhone());
            currentUser.setPhone2(user.getPhone2());
            currentUser.setEmail(user.getEmail());
            currentUser.setPassword(user.getPassword());
            currentUserDAO.insertUser(currentUser);
            sendRequestWithVolley();
        }else{
            if(netUserLoadingListener != null){
                netUserLoadingListener.complete(user);
            }
        }
    }

    /**
     * use volley to get the data
     * */

    private void sendRequestWithVolley(String phone, String password){
        String url = "http://119.29.182.235:8080/ssh_contacts/login?user.phone=" + phone+"&user.password="+password;
        Log.d("wnwLogin", url);
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response){
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


    /**
     * use volley to get the data
     * */
    private void sendRequestWithVolley(){
        String url = "http://119.29.182.235:8080/ssh_contacts/selectGroupByUserIdAction?userId="+user.getId();
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseAllUserWithJSON(response);
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

    private void parseAllUserWithJSON(String response){
        try{
            Log.d("responseWnw",response);
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.getString("result").equals("null")){
                if(netUserLoadingListener != null){
                    netUserLoadingListener.complete(user);
                }
                return;
            }
            JSONArray allUsers = jsonObject.getJSONArray("result");
            int length = allUsers.length();
            for(int i = 0; i < length; i++){
                JSONObject object = allUsers.getJSONObject(i);
                JSONArray object1 = null;
                JSONObject object2 = null;

                if(!object.getString("contactsList").equals("null")){
                    object1 = object.getJSONArray("contactsList");
                }

                if(!object.getString("groupBean").equals("null")){
                    object2 = object.getJSONObject("groupBean");
                    Group group = new Group();
                    group.setId(object2.getInt("id"));
                    group.setName(object2.getString("name"));
                    groupDao.insertGroup(group);
                }
                int newGroupId = groupDao.SelectFinalGroupId();
                if(object1 != null){
                    int jsonLength = object1.length();
                    for(int j = 0; j < jsonLength; j++){
                        JSONObject object3 = object1.getJSONObject(j);
                        User user = new User();
                        user.setId(object3.getInt("id"));
                        user.setName(object3.getString("name"));
                        user.setPhone(object3.getString("phone"));
                        user.setPhoneMore(object3.getString("phonemore"));
                        user.setEmail(object3.getString("email"));
                        user.setPosition(object3.getString("position"));
                        user.setGroupId(newGroupId);
                        userDao.insertUser(user);
                    }
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        if(netUserLoadingListener != null){
            netUserLoadingListener.complete(user);
        }
    }
}
