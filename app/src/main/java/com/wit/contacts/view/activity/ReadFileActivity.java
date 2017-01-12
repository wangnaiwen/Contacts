package com.wit.contacts.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wit.contacts.R;
import com.wit.contacts.bean.Group;
import com.wit.contacts.bean.User;
import com.wit.contacts.dao.GroupDao;
import com.wit.contacts.dao.GroupDaoImp;
import com.wit.contacts.dao.UserDao;
import com.wit.contacts.dao.UserDaoImp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wnw on 2016/10/19.
 */

public class ReadFileActivity extends Activity implements View.OnClickListener{

    private ImageView back;
    private Button sure;
    private TextView pickGroup;

    private String mCurrentGroup = null;
    private int mCurrentGroupId;
    String filePath = "/mnt/sdcard/Contacts.txt";

    private GroupDao groupDao;
    private UserDao userDao;
    List<Group> groups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readfile);
        initView();
    }

    private void initView(){
        back = (ImageView)findViewById(R.id.back_arrow);
        sure = (Button)findViewById(R.id.sure);
        pickGroup = (TextView)findViewById(R.id.pick_group);

        back.setOnClickListener(this);
        sure.setOnClickListener(this);
        pickGroup.setOnClickListener(this);

        groupDao = new GroupDaoImp();
        userDao = new UserDaoImp();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back_arrow:
                finish();
                break;
            case R.id.sure:
                if(mCurrentGroup == null){
                    Toast.makeText(this, "请选择一个分组", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "正在拼命加载中", Toast.LENGTH_SHORT).show();
                    readFile();
                }
                break;
            case R.id.pick_group:
                showDialog();
                break;
            default:
                break;
        }
    }

    private AlertDialog myDialog;

    private void showDialog(){
        final List<String> list = new ArrayList<>();
        groups = groupDao.selectAllGroup();
        for(int  i = 0; i < groups.size(); i++){
            list.add(groups.get(i).getName());
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        final LinearLayout linearLayout =(LinearLayout) inflater.inflate(R.layout.dialog_pick_group,null);
        ListView listView = (ListView)linearLayout.findViewById(R.id.lv_dialog);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.dialog_lv_item, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCurrentGroup = list.get(i);
                pickGroup.setText(mCurrentGroup);
                mCurrentGroupId = groups.get(i).getId();
                myDialog.dismiss();
            }
        });

        myDialog = new AlertDialog.Builder(this).create();
        myDialog.setView(linearLayout);
        myDialog.show();
    }
    private void pickFile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("**/*//*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            //filePath = uri.getPath();
            Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
        }else{
        }
    }*/

    private void readFile(){
        try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                boolean saveIndex = true;
                User user = new User();;
                while((lineTxt = bufferedReader.readLine()) != null){
                    Log.d("wnw", lineTxt);
                    if(saveIndex){
                        user.setGroupId(mCurrentGroupId);
                        user.setName(lineTxt.trim());
                        saveIndex = false;
                    }else{
                        user.setPhone(lineTxt.trim());
                        User user1 = user;
                        userDao.insertUser(user1);
                        saveIndex = true;
                    }
                }
                read.close();
                Toast.makeText(this, "以保存到本地数据库中", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Log.d("wnw", "文件不存在");
                Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.d("wnw","读取文件内容出错");
            Toast.makeText(this, "读取文件内容出错", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
