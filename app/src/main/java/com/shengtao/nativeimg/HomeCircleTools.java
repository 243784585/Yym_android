package com.shengtao.nativeimg;

import android.content.Context;
import android.os.Environment;

/**
*
*
* @ClassName SettingDetailActivity
*/
public class HomeCircleTools {
	/**
	 * 检查是否存在SDCard
	 * @return
	 */
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}

	public static String getAbsolutePath(Context context) {
		return context.getApplicationContext().getFilesDir().getAbsolutePath();
	}
}
