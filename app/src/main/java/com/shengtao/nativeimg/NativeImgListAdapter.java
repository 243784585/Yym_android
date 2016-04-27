package com.shengtao.nativeimg;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.shengtao.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by CC on 2015/1/23.
 */
public class NativeImgListAdapter extends BaseAdapter {

	private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	private ArrayList<String> data;
	private Context context;
	public HashMap<Integer, View> map = new HashMap<Integer, View>();
	protected LayoutInflater mInflater;
	private int maxGroup = 3;
	private DisplayImageOptions options;
	public NativeImgListAdapter(Context ctx, ArrayList<String> arrayList) {
		this.context = ctx;
		this.data = arrayList;
		mInflater = LayoutInflater.from(context);
		options = new DisplayImageOptions.Builder()
				// 初始化默认图片
				.showImageOnLoading(R.drawable.moren)
						// 加载异常时显示图片
				.showImageOnFail(R.drawable.moren)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
//				.displayer(new RoundedBitmapDisplayer(5))
						// Uri地址为空时显示图片
				.showImageForEmptyUri(R.drawable.moren)
				.build();
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (map.get(position) == null)
		{
			final ViewHolder viewHolder;
			final String path = data.get(position);

			convertView = mInflater.inflate(R.layout.native_img_grid_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (NativeImageView) convertView.findViewById(R.id.child_image);
			viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.child_checkbox);
			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new NativeImageView.OnMeasureListener() {

				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			if (path.equals(NativeImageActivity.groupList.get(path))) {
				viewHolder.mCheckBox.setChecked(true);
			}
			viewHolder.mImageView.setTag(path);
			viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						if(NativeImageActivity.groupList.size() == maxGroup) {
							buttonView.setChecked(false);
							Toast.makeText(context, "最多只能选择" + maxGroup + "张照片", Toast.LENGTH_SHORT).show();
						} else {
							if(!mSelectMap.containsKey(position) || !mSelectMap.get(position)){
								addAnimation(viewHolder.mCheckBox);
							}
							NativeImageActivity.groupList.put(path, path);
						}
					}else{
						NativeImageActivity.groupList.remove(path);
					}
				}
			});
			if (position == 0) {
				viewHolder.mImageView.setImageResource(R.drawable.open_camera);
				viewHolder.mCheckBox.setVisibility(View.GONE);
			} else {
				ImageLoader.getInstance().displayImage("file:///" + path, viewHolder.mImageView, options);
			}
			convertView.setTag(viewHolder);
			map.put(position, convertView);
		} else {
			convertView = map.get(position);
		}

		return convertView;
	}

	/**
	 * 给CheckBox加点击动画，利用开源库nineoldandroids设置动画
	 * @param view
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void addAnimation(View view){
		float [] vaules = new float[]{0.5f, 0.6f, 0.7f, 0.8f, 0.9f, 1.0f, 1.1f, 1.2f, 1.3f, 1.25f, 1.2f, 1.15f, 1.1f, 1.0f};
		AnimatorSet set = new AnimatorSet();
		set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules),
				ObjectAnimator.ofFloat(view, "scaleY", vaules));
		set.setDuration(150);
		set.start();
	}

	public static class ViewHolder{
		public NativeImageView mImageView;
		public CheckBox mCheckBox;
	}
}
