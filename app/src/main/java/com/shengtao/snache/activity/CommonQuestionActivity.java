package com.shengtao.snache.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.chat.chatUI.activity.ChatActivity;
import com.shengtao.utils.Session;
import com.shengtao.utils.VisitorMode;

/**
 * @package com.shengtao.snache.activity
 * Created by TT on 2015/12/18.
 * Description:常见问题
 */
public class CommonQuestionActivity extends BaseActivity implements View.OnClickListener {

    private Button btnService;
    private TextView tvTest;
    private TextView tvTest2;

    @Override
    protected int setLayout() {
        return R.layout.activity_common_question;
    }

    @Override
    protected void initView() {
//        InputStream face = this.getClass().getClassLoader().getResourceAsStream("assets/" + "STHeiti-Light.ttc");
        Typeface face = Typeface.createFromAsset(getAssets(), "STHeiti-Light.ttc");

        btnService = (Button) findViewById(R.id.btn_service);
        tvTest = (TextView) findViewById(R.id.tv_test);
        tvTest2 = (TextView) findViewById(R.id.tv_test2);
//        tvTest.setTextColor(getResources().getColor(Color.parseColor("#000000")));
//        tvTest.setTypeface(face);
//        tvTest2.setTypeface(face);
//        tvTest2.setTextColor(getResources().getColor(R.color.blue));
    }

//    public static void initSystemBar(Activity activity) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//
//            setTranslucentStatus(activity, true);
//
//        }
//
//        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//
//        tintManager.setStatusBarTintEnabled(true);
//
//// 使用颜色资源
//
//        tintManager.setStatusBarTintResource(R.color.status_color);
//
//    }


    @TargetApi(19)
    private static void setTranslucentStatus(Activity activity, boolean on) {

        Window win = activity.getWindow();

        WindowManager.LayoutParams winParams = win.getAttributes();

        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

        if (on) {

            winParams.flags |= bits;

        } else {

            winParams.flags &= ~bits;

        }

        win.setAttributes(winParams);

    }

    @Override
    protected void doBusiness() {
        btnService.setOnClickListener(this);
    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected Object getAvtionTitle() {
        TextView textView = new TextView(this);
        textView.setTextColor(getResources().getColor(R.color.yym_common_red));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        textView.setText("常见问题");
        return textView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_service:
                if(VisitorMode.isVistor(CommonQuestionActivity.this)){
                    return;
                }
                //进入环信客服热线
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                intent.putExtra("ID", Session.GetString("id"));
                intent.putExtra("userId", "15076661550");
                startActivity(intent);
            break;
        }
    }
}
