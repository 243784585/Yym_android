package com.shengtao.snache.activity;

import android.view.View;
import android.widget.TextView;

import com.shengtao.R;
import com.shengtao.baseview.BaseActivity;
import com.shengtao.utils.DateTimeUtil;
import com.shengtao.utils.LogUtil;

/**
 * @package com.shengtao.snache.activity
 * Created by TT on 2015/12/15.
 * Description:中奖消息详情
 */
public class MessageWinningDetails extends BaseActivity {


    private TextView tvGoodName;
    private TextView tvLuck;
    private TextView tvTitle;

    @Override
    protected int setLayout() {
        return R.layout.activity_winning_message_details;
    }

    @Override
    protected void initView() {
        String open_time = DateTimeUtil.timestamp3Time(getIntent().getStringExtra("open_time"));
        String goods_current_num = getIntent().getStringExtra("goods_current_num");
        String zgoods_name = getIntent().getStringExtra("zgoods_name");
        LogUtil.e("position",getIntent().getStringExtra("zgoods_name"));
        String lucky_id = getIntent().getStringExtra("lucky_id");

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvLuck = (TextView) findViewById(R.id.tv_luck_nummber);
        tvGoodName = (TextView) findViewById(R.id.tv_goods_name);

        tvTitle.setText("【"+open_time+"】恭喜您中奖了！您的信息与收货地址已发送给商家，如收货地址为空或有误请尽快联系在线客服！如无特殊情况，商家将在三个工作日内发货，敬请期待！感谢您对1圆梦一路的支持！");
        tvLuck.setText(lucky_id);
        tvGoodName.setText("第"+goods_current_num+"期　"+zgoods_name);
    }

    @Override
    protected void doBusiness() {

    }

    @Override
    public int getHeaderType() {
        return 1;
    }

    @Override
    protected String getAvtionTitle() {
        return "中奖消息详情";
    }

    @Override
    public void onClick(View view) {

    }
}
