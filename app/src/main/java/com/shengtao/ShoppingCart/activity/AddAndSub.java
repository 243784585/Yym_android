package com.shengtao.ShoppingCart.activity;

import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.shengtao.R;

public class AddAndSub extends LinearLayout {

    private Context context;
    private ImageView iv_sub;
    private ImageView iv_add;
    private EditText et01;
    private OnNumChangeListener onNumChangeListener;
    private int num;// editText中的数值
    public boolean isTen=false;

    public AddAndSub(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        num = 0;
        intview();
    }

    public AddAndSub(Context context, int num) {
        super(context);
        this.context = context;
        this.num = num;
    }

    public AddAndSub(Context context) {
        super(context);
        this.context = context;
        num = 0;
        intview();
    }

    private void intview() {
        View view = View.inflate(getContext(), R.layout.add_view, null);
        addView(view);
        iv_sub = (ImageView) view.findViewById(R.id.iv_sub);
        iv_add = (ImageView) view.findViewById(R.id.iv_add);
        iv_add.setTag("+");
        iv_sub.setTag("-");
        et01 = (EditText) view.findViewById(R.id.et01);
        et01.setFilters(new InputFilter[] { new InputFilter.LengthFilter(9) });
        et01.setText(String.valueOf(num));
        setViewListener();
    }

    /**
     * 设置editText中的值
     *
     * @param num
     */
    public void setNum(int num) {
        this.num = num;
        et01.setText(String.valueOf(num));
    }

    /**
     * 获取editText中的值
     *
     * @return
     */
    public int getNum() {
        if (et01.getText().toString() != null|| !et01.getText().toString().equals("")) {
            return Integer.parseInt(et01.getText().toString());
        } else {
            return 0;
        }
    }

    public void setOnNumChangeListener(OnNumChangeListener onNumChangeListener) {
        this.onNumChangeListener = onNumChangeListener;
    }

    /**
     * 设置文本变化相关监听事件
     */
    private void setViewListener() {
        iv_sub.setOnClickListener(new OnButtonClickListener());
        iv_add.setOnClickListener(new OnButtonClickListener());
        et01.addTextChangedListener(new OnTextChangeListener());
    }

    /**
     * 加减按钮事件监听器
     */
    class OnButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            String numString = et01.getText().toString();
            if (numString == null) {
                num = 1;
                et01.setText("1");
            } else {
                if (v.getTag().equals("+")) {

                    if (isTen) {
                        if ((num = num + 10) < 10) {
                            num =num-10;
                            Toast.makeText(context, "请输入一个大于10的数字",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            et01.setText(String.valueOf(num));
                            if (onNumChangeListener != null) {
                                onNumChangeListener
                                        .onNumChange(AddAndSub.this, num);
                            }

                        }
                    }else {

                        if (++num < 1) // 先加，再判断
                        {
                            num--;
                            Toast.makeText(context, "请输入一个大于1的数字",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            et01.setText(String.valueOf(num));
                            if (onNumChangeListener != null) {
                                onNumChangeListener
                                        .onNumChange(AddAndSub.this, num);
                            }
                        }
                    }


                } else if (v.getTag().equals("-")) {
                    if (isTen) {
                        if ((num = num - 10) < 10) {
                            num =num+10;
                            Toast.makeText(context, "请输入一个大于10的数字",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            et01.setText(String.valueOf(num));
                            if (onNumChangeListener != null) {
                                onNumChangeListener
                                        .onNumChange(AddAndSub.this, num);
                            }

                        }
                    }else {
                        if (--num < 1) // 先减，再判断
                        {

                            num++;

                            Toast.makeText(context, "请输入一个大于1的数字",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            et01.setText(String.valueOf(num));
                            if (onNumChangeListener != null) {
                                onNumChangeListener
                                        .onNumChange(AddAndSub.this, num);
                            }
                        }


                    }
                }
            }
        }
    }

    /**
     * EditText输入变化事件监听器
     *
     *
     */
    class OnTextChangeListener implements TextWatcher {

        @Override
        public void afterTextChanged(Editable s) {
            String numString = s.toString().trim();
            if (numString == null || numString.equals("")) {
                num = 1;
                if (onNumChangeListener != null) {
                    onNumChangeListener.onNumChange(AddAndSub.this, num);
                }
            } else {
                int numInt = Integer.parseInt(numString);
               if (numInt < 1) {
                    Toast.makeText(context, "请输入一个大于1的数字", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // 设置EditText光标位置 为文本末端
                    et01.setSelection(et01.getText().toString().length());
                    num = numInt;
                    if (onNumChangeListener != null) {
                        onNumChangeListener.onNumChange(AddAndSub.this, num);
                    }
                }
            }
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {

        }

    }

    public interface OnNumChangeListener {
        /**
         * 输入框中的数值改变事件
         *
         * @param view
         *            整个AddAndSubView
         * @param num
         *            输入框的数值
         */
        public void onNumChange(View view, int num);
    }

}
