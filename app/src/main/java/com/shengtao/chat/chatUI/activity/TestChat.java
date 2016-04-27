package com.shengtao.chat.chatUI.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shengtao.R;


public class TestChat extends BaseChatActivity implements View.OnClickListener{

    private EditText mNamaEdit;
    private EditText mPassEdit;
    private EditText mToNameEdit;

    private Button mChatButton;//直接不加好友聊天
    private Button mContactButton;//到会话
    private Button mRegisterButton;//用现有的用户名密码注册




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_chat);





        mNamaEdit=(EditText) findViewById(R.id.name);
        mPassEdit=(EditText) findViewById(R.id.password);
        mToNameEdit=(EditText) findViewById(R.id.to_name);
        mChatButton=(Button) findViewById(R.id.chat);
        mContactButton=(Button) findViewById(R.id.contanck);
        mRegisterButton=(Button) findViewById(R.id.register);


        mChatButton.setOnClickListener(this);
        mContactButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);



    }



    @Override
    public void onClick(View v) {



        String Name=mNamaEdit.getText().toString();
        String Pass=mPassEdit.getText().toString();
        String toName=mToNameEdit.getText().toString();







        if(v.equals(mChatButton))
            ChatToSomeone(Name,Pass,toName);
        else if(v.equals(mContactButton))
            login(Name, Pass);
        else if(v.equals(mRegisterButton))
            RegisterEase(Name, Pass);
    }





}