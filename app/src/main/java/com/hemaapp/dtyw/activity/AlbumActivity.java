package com.hemaapp.dtyw.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywUtil;
import com.hemaapp.dtyw.adapter.AlbumAdapter;
import com.hemaapp.dtyw.config.DtywConfig;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;

import java.io.IOException;
import java.util.ArrayList;

import xtom.frame.image.load.XtomImageTask;
import xtom.frame.util.XtomFileUtil;
import xtom.frame.util.XtomImageUtil;

public class AlbumActivity extends DtywActivity {
	private ImageView titleLeft;
	private TextView titleTxt;
	private TextView titleRight;
	private GridView grid;
	private LinearLayout mLL;
	private TextView finishBtn;
	private TextView numTxt;

	private int limitCount;
	private ArrayList<String> mImgs = new ArrayList<String>();// 鎵?湁鍥剧墖
	private AlbumAdapter adapter;
	private ArrayList<String> hsvImgs = new ArrayList<String>();// 鍘嬬缉鍚庡浘鐗?
	private ArrayList<String> beforeImgs = new ArrayList<String>();// 鍘嬬缉鍓嶅浘鐗?

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_album);
		super.onCreate(savedInstanceState);
		getImgs();
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cancelProgressDialog();
			setAdapter();
		}
	};

	private void setAdapter() {
		if (mImgs.size() == 0) {
			Toast.makeText(getApplicationContext(), "擦，一张图片都没找到",
					Toast.LENGTH_SHORT).show();
			return;
		}

		adapter = new AlbumAdapter(mContext, mImgs, limitCount);
		grid.setAdapter(adapter);
	}

	private void getImgs() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}

		showProgressDialog("正在加载...");

		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = AlbumActivity.this
						.getContentResolver();
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);
				Log.e("TAG", mCursor.getCount() + "");

				while (mCursor.moveToNext()) {
					// 鑾峰彇鍥剧墖鐨勮矾寰?
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.e("TAG", path);

					if (mImgs.contains(path))
						continue;
					else
						mImgs.add(path);
				}

				mCursor.close();
				mHandler.sendEmptyMessage(0x110);
			}
		}).start();
	}

	@Override
	protected void callBeforeDataBack(HemaNetTask netTask) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void callAfterDataBack(HemaNetTask netTask) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void callBackForServerSuccess(HemaNetTask netTask,
			HemaBaseResult baseResult) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void callBackForServerFailed(HemaNetTask netTask,
			HemaBaseResult baseResult) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void callBackForGetDataFailed(HemaNetTask netTask, int failedType) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void findView() {
		titleLeft = (ImageView) findViewById(R.id.title_left);
		titleTxt = (TextView) findViewById(R.id.title_text);
		titleRight = (TextView) findViewById(R.id.title_right);
		grid = (GridView) findViewById(R.id.album_grid);
		mLL = (LinearLayout) findViewById(R.id.album_content);
		finishBtn = (TextView) findViewById(R.id.album_finish);
		numTxt = (TextView) findViewById(R.id.album_num);
	}

	@Override
	protected void getExras() {
		limitCount = mIntent.getIntExtra("limit", 0);
	}

	@Override
	protected void setListener() {
		titleLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		titleTxt.setText("相册");
		titleRight.setText("取消");
		titleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		finishBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.putExtra("images", hsvImgs);
				setResult(RESULT_OK, it);
				finish();
			}
		});
	}

	public void changeHsv(String path, boolean isAdd) {
		if (isAdd) {
			beforeImgs.add(path);
			new CompressPicTask().execute(path);
		} else {
			int index = beforeImgs.indexOf(path);
			beforeImgs.remove(path);
			hsvImgs.remove(index);
			mLL.removeViewAt(index);
			if (hsvImgs.size() == 0) {
				numTxt.setVisibility(View.INVISIBLE);
			} else {
				numTxt.setText("" + hsvImgs.size());
			}
		}
	}

	private void addHsv(String path) {
		LinearLayout ll = new LinearLayout(mContext);
		ImageView img = new ImageView(mContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				mLL.getHeight(), LayoutParams.MATCH_PARENT);
		lp.setMargins(DtywUtil.dip2px(mContext, 2), 0,
				DtywUtil.dip2px(mContext, 2), 0);
		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		img.setScaleType(ScaleType.CENTER_CROP);
		imageWorker.loadImage(new XtomImageTask(img, path, mContext));
		ll.addView(img, lp);
		mLL.addView(ll);
	}

	private class CompressPicTask extends AsyncTask<String, Void, Integer> {
		String compressPath;

		@Override
		protected Integer doInBackground(String... params) {
			try {
				String path = params[0];
				String savedir = XtomFileUtil.getTempFileDir(mContext);
				compressPath = XtomImageUtil.compressPictureWithSaveDir(path,
						DtywConfig.IMAGE_HEIGHT, DtywConfig.IMAGE_WIDTH,
						DtywConfig.IMAGE_QUALITY, savedir, mContext);
				return 0;
			} catch (IOException e) {
				return 1;
			}
		}

		@Override
		protected void onPreExecute() {
			// showProgressDialog("姝ｅ湪鍘嬬缉鍥剧墖");
		}

		@Override
		protected void onPostExecute(Integer result) {
			cancelProgressDialog();
			switch (result) {
			case 0:
				hsvImgs.add(compressPath);
				addHsv(compressPath);
				numTxt.setVisibility(View.VISIBLE);
				numTxt.setText("" + hsvImgs.size());
				break;
			case 1:
				showTextDialog("图片压缩失败");
				break;
			}
		}
	}
}
