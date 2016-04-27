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
import android.widget.GridView;

import com.shengtao.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by CC on 2015/1/23.
 */
public class SingleImgListAdapter extends BaseAdapter {

	private Point mPoint = new Point(0, 0);//用来封装ImageView的宽和高的对象
	private HashMap<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();
	private ArrayList<String> data;
	private Context context;
	private HashMap<Integer, View> map = new HashMap<Integer, View>();
	protected LayoutInflater mInflater;
	DisplayImageOptions options;

	public SingleImgListAdapter(Context ctx, ArrayList<String> arrayList, GridView single_image_grid) {
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

			convertView = mInflater.inflate(R.layout.single_img_grid_child_item, null);
			viewHolder = new ViewHolder();
			viewHolder.mImageView = (NativeImageView) convertView.findViewById(R.id.single_child_image);
			//用来监听ImageView的宽和高
			viewHolder.mImageView.setOnMeasureListener(new NativeImageView.OnMeasureListener() {

				@Override
				public void onMeasureSize(int width, int height) {
					mPoint.set(width, height);
				}
			});
			viewHolder.mImageView.setTag(path);
			if (position == 0) {
				viewHolder.mImageView.setImageResource(R.drawable.open_camera);
			} else {
				ImageLoader.getInstance().displayImage("file://" + path, viewHolder.mImageView, options);
//				Bitmap bitmap = NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageLoader.NativeImageCallBack() {
//					@Override
//					public void onImageLoader(Bitmap bitmap, String path) {
//						ImageView mImageView = (ImageView) mGridView.findViewWithTag(path);
//						if (bitmap != null && mImageView != null) {
//							mImageView.setImageBitmap(bitmap);
//						}
//					}
//				});

//				if (bitmap != null) {
//					viewHolder.mImageView.setImageBitmap(bitmap);
//				} else {
//					viewHolder.mImageView.setImageResource(R.drawable.friends_sends_pictures_no);
//				}
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
	}
}