package com.shengtao.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.shengtao.R;

import java.util.HashMap;


public abstract class MyDialog extends AlertDialog implements OnClickListener {

    private Context mContext;
    private HashMap<String, String> hashMap;
    private TextView tv_title;
    private TextView tv_cancel;
    private TextView tv_submit;

    protected MyDialog(Context context, HashMap<String, String> hashMap) {
        super(context);
        mContext = context;
        this.hashMap = hashMap;
        init();
    }

    protected MyDialog(Context context, boolean cancelable,
                       OnCancelListener cancelListener, HashMap<String, String> hashMap) {
        super(context, cancelable, cancelListener);
        mContext = context;
        this.hashMap = hashMap;
        init();
    }

    private void init() {
        show();
        setContentView(R.layout.dialog_update);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_submit = (TextView) findViewById(R.id.tv_submit);

        tv_title.setText(hashMap.get("title"));
        tv_cancel.setText(hashMap.get("cancel"));
        tv_submit.setText(hashMap.get("submit"));

        tv_cancel.setOnClickListener(this);
        tv_submit.setOnClickListener(this);

//		String gravity = hashMap.get("gravity") == null ? "center" : hashMap.get("gravity");
//        if (gravity.equals("left")) {
//            tv_title.setGravity(Gravity.LEFT);
//        } else {
//            tv_title.setGravity(Gravity.CENTER);
//        }

    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.tv_cancel:
                dialogCancel();
                dismiss();
                break;

            case R.id.tv_submit:
                dialogSubmit();
                dismiss();
                break;
        }
    }

    public abstract void dialogSubmit();
    public void dialogCancel(){}
}
