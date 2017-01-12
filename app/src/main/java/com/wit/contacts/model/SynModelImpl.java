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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by wnw on 2017/1/1.
 */

public class SynModelImpl implements ISynModel {

    private Context context;
    private CurrentUserDAO currentUserDAO;
    private GroupDao groupDao;
    private UserDao userDao;

    public SynModelImpl(){
        currentUserDAO = new CurrentUserImpl();
        groupDao = new GroupDaoImp();
        userDao = new UserDaoImp();

    }
    CurrentUser currentUser = null;
    List<Group> groups = null;
    List<User>  users = null;
    SynLoadingListener loadingListener;

    boolean isInsertGrouped = false;
    boolean isInsertUsered = false;

    @Override
    public void syn(Context context, SynLoadingListener synLoadingListener) {
        this.context = context;
        this.loadingListener = synLoadingListener;
        currentUser = null;
        groups = null;
        users = null;

        currentUser = currentUserDAO.selectCurrentUser();
        groups = groupDao.selectAllGroup();
        users = userDao.selectAllUser();

        isInsertGrouped = false;
        isInsertUsered =false;
        deleteAll();

    }
    private void deleteAll(){
        String url = "http://119.29.182.235:8080/ssh_contacts/deleteGroupByUseIdAction?userId="+currentUser.getId();
        Log.d("wnw", url);
        sendDeleteRequestWithVolley(url);
    }

    private void insertGroup(){
        int groupLength = groups.size();
        Log.d("wnwGlentth", groupLength+"");
        for(int i = 0; i < groupLength; i++){
            String url2 = "http://119.29.182.235:8080/ssh_contacts/insertGroupAction?id="+ groups.get(i).getId()+
                    "&name="+groups.get(i).getName() + "&userId=" +currentUser.getId();
            Log.d("wnw1", url2);
            sendInsertGroupRequestWithVolley(url2);
        }
        if(groupLength == 0){
            loadingListener.complete();
        }
    }

    private void insertUsers(){
        int userLength = users.size();
        Log.d("wnwUlentth", userLength+"");
        for(int i = 0; i < userLength; i++){
            User user = users.get(i);
            String url2 = "http://119.29.182.235:8080/ssh_contacts/insertContactsAction?id="+
                    user.getId() + "&name=" + user.getName() +"&phone="+user.getPhone() +
                    "&phonemore=" + user.getPhoneMore() +"&email=" + user.getEmail()+
                    "&position="+user.getPosition() + "&groupId="+user.getGroupId();
            Log.d("wnw", url2);
            sendInsertUserRequestWithVolley(url2);
        }
        loadingListener.complete();
    }

    /**
     * use volley to get the data
     * */
    private void sendInsertUserRequestWithVolley(String add){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, add, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("wnw", "Yes");
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
    private void sendDeleteRequestWithVolley(String add){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, add, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("wnw", "Yes");
                if(!isInsertGrouped){
                    insertGroup();
                    isInsertGrouped = true;
                }
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
    private void sendInsertGroupRequestWithVolley(String add){
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, add, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("wnw", "Yes");
                if(!isInsertUsered){
                    insertUsers();
                    isInsertUsered = true;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("wnw", "Error:" + error.getNetworkTimeMs() + error.getMessage());
            }
        });
        queue.add(request);
    }
}
