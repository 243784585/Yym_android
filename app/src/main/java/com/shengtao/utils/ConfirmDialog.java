package com.shengtao.utils;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shengtao.R;

/**
 * <p>
 * Title: ConfirmDialog
 * </p>
 * <p>
 * Description:自定义Dialog（参数传入Dialog样式文件，Dialog布局文件）
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * 
 * @author archie
 * @version 1.0
 */
public abstract class ConfirmDialog extends Dialog implements View.OnClickListener {
	int layoutRes;// 布局文件
	Context context;
	/** 确定按钮 **/
	private Button confirmBtn;
	/** 取消按钮 **/
	private Button cancelBtn;
	private TextView tvSubTitle;
	private View vSplit;
	private TextView tvTitle;

	public ConfirmDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public ConfirmDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * 自定义主题及布局的构造方法
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public ConfirmDialog(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(layoutRes);
		// 根据id在布局中找到控件对象
		confirmBtn = (Button) findViewById(R.id.btn_confirm);
		cancelBtn = (Button) findViewById(R.id.btn_cancel);
		tvSubTitle = (TextView)findViewById(R.id.tv_sub_title);
		vSplit = findViewById(R.id.v_split);


		// 设置按钮的文本颜色
		confirmBtn.setTextColor(context.getResources().getColor(R.color.dialog_btn_color));
		cancelBtn.setTextColor(context.getResources().getColor(R.color.dialog_btn_color));

		// 为按钮绑定点击事件监听器
		confirmBtn.setOnClickListener(this);
		cancelBtn.setOnClickListener(this);
	}

	/**
	 * 设置标题
	 * @param title 标题内容
	 * @param color 字体颜色(资源id)
	 * @param unit 字体大小单位
	 * @param textSize 字体大小
	 * @return
	 */
	public ConfirmDialog setTitle(String title, Integer color, Float textSize, Integer unit){
		tvTitle = (TextView) findViewById(R.id.tv_title_confirm);
		tvTitle.setText(title);
		if(color != null) {
			tvTitle.setTextColor(color);
		}
		if(textSize != null) {
			tvTitle.setTextSize(unit == null?null:unit,textSize);
		}
		return this;
	}

	/**
	 * 设置副标题可见性(默认可见)
	 * @param flag false为设置不可见,true为可见
	 * @return
	 */
	public ConfirmDialog setSubTitleVisible(boolean flag){
		if(!flag)
			tvSubTitle.setVisibility(View.GONE);
		else
			tvSubTitle.setVisibility(View.VISIBLE);
		return this;
	}

	/**
	 * 设置副标题
	 * @param subTitle 副标题内容
	 * @param color 字体颜色
	 * @param unit 字体大小单位
	 * @param textSize 字体大小
	 * @return
	 */
	public ConfirmDialog setSubTitle(String subTitle, Integer color, Float textSize, Integer unit){
		tvSubTitle.setText(subTitle);
		if(color != null) {
			tvSubTitle.setTextColor(color);
		}
		if(textSize != null) {
			tvSubTitle.setTextSize(unit == null ? null : unit, textSize);
		}
		return this;
	}

	/**
	 * 设置取消按钮的可见性(默认为可见)
	 * @param flag	true为可见,false为不可见
	 * @return
	 */
	public ConfirmDialog setCancleVisible(boolean flag){
		if(!flag) {
			cancelBtn.setVisibility(View.GONE);
			vSplit.setVisibility(View.GONE);
		}else{
			cancelBtn.setVisibility(View.VISIBLE);
			vSplit.setVisibility(View.VISIBLE);
		}
		return this;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_confirm:
			confirm();
			break;
		case R.id.btn_cancel:
			cancle();
			break;
		}
	}


	/** 点击了确定 */
	public abstract void confirm();
	/** 点击了取消 */
	public abstract void cancle();
}