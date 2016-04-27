package com.shengtao.chat.chatUI.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.easemob.EMValueCallBack;
import com.easemob.chat.EMGroupInfo;
import com.easemob.chat.EMGroupManager;
import com.shengtao.R;

import java.util.List;

/**
 * Created by Administrator on 11/10/2015.
 */
public class PersonToGroup extends BaseActivity {
    private EditText mToGroup;
    private Button mToGroupButton;

    private List<EMGroupInfo> groupInfoArrayList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.person_to_group);

        EMGroupManager.getInstance().asyncGetAllPublicGroupsFromServer(new EMValueCallBack<List<EMGroupInfo>>() {

            @Override
            public void onSuccess(List<EMGroupInfo> value) {
                // TODO Auto-generated method stub
                groupInfoArrayList = value;
            }

            @Override
            public void onError(int error, String errorMsg) {
                // TODO Auto-generated method stub

            }
        });

        mToGroup=(EditText)findViewById(R.id.person_name);
        mToGroupButton=(Button) findViewById(R.id.person_commit);
        mToGroupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<groupInfoArrayList.size();i++){

                    EMGroupInfo emGroupInfo=groupInfoArrayList.get(i);

                    //判断是否有这个群

                    if(emGroupInfo.getGroupName().equals(mToGroup.getText().toString())){

                            Intent intent = new Intent(PersonToGroup.this, ChatActivity.class);
                            // it is group chat
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                            intent.putExtra("groupId", emGroupInfo.getGroupId());
                            startActivityForResult(intent, 0);

                        break;
                    }

                    //找不到该群
                    if(i==groupInfoArrayList.size()-1){
                        Toast.makeText(PersonToGroup.this,"没有此群",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

   /* private String groupId;
    List<String> emGroupList;
    public boolean isGroupUser(String Id){
        groupId=Id;

        new Thread(new Runnable() {
            public void run() {

                EMGroup emGroup= null;
                try {
                    emGroup = EMGroupManager.getInstance().getGroupFromServer(groupId);
                } catch (EaseMobException e) {
                    e.printStackTrace();
                }
                emGroupList=emGroup.getMembers();
            }
        }).start();

        if(!emGroupList.contains("13555555555")){
            Toast.makeText(PersonToGroup.this,"不是该群成员",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }*/
}
