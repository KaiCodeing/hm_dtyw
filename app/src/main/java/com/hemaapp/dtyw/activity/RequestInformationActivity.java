package com.hemaapp.dtyw.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.model.Requirement;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaArrayResult;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.showlargepic.ShowLargePicActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import xtom.frame.util.XtomSharedPreferencesUtil;

/**
 * Created by lenovo on 2017/2/28.
 * 需求详情
 */
public class RequestInformationActivity extends DtywActivity {
    private TextView requset_title;
    private ImageButton back_button;//差号
    private TextView title_text;
    private Button next_button;
    private ImageView call_tel;
    private TextView brand_name;
    private TextView type_text;
    private TextView address_text;
    private TextView tel_text;
    private TextView content;
    private LinearLayout add_img;
    private String requestId;
    private Requirement requirement;
    private String keytype;
    private View view_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_request_infor);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt() {
        //  String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().requirementGet(requestId);
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REQUIREMENT_GET:
                showProgressDialog("获取需求详情");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REQUIREMENT_GET:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REQUIREMENT_GET:
                HemaArrayResult<Requirement> result = (HemaArrayResult<Requirement>) hemaBaseResult;
                requirement = result.getObjects().get(0);
                setData();
                break;
        }
    }

    /**
     * 填充数据
     */
    private void setData() {
        requset_title.setText(requirement.getName());
        brand_name.setText(requirement.getBrand());
        type_text.setText(requirement.getProperty());
        address_text.setText(requirement.getAddress());
        tel_text.setText(requirement.getPhone());
        if ("2".equals(keytype)) {
            call_tel.setVisibility(View.GONE);
            view_show.setVisibility(View.GONE);
        } else {
            call_tel.setVisibility(View.VISIBLE);
            view_show.setVisibility(View.VISIBLE);
        }
        if (isNull(XtomSharedPreferencesUtil.get(mContext, "username"))) {
        } else {
            String userid = DtywApplication.getInstance().getUser().getId();
            if (userid.equals(requirement.getCid())) {
                call_tel.setVisibility(View.GONE);
                view_show.setVisibility(View.GONE);
            } else {
                call_tel.setVisibility(View.VISIBLE);
                view_show.setVisibility(View.VISIBLE);
            }
        }
        //拨打电话
        call_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + requirement.getPhone());
                intent.setData(data);
                startActivity(intent);
            }
        });
        content.setText(requirement.getMemo());
        //加载图片
        if (requirement.getImgItems() == null || requirement.getImgItems().size() == 0) {
        } else {
            add_img.removeAllViews();
            for (int i=0;i< requirement.getImgItems().size();i++
                    ) {
                View view = LayoutInflater.from(mContext).inflate(R.layout.view_add_request, null);
                ImageView im = (ImageView) view.findViewById(R.id.add_img);
                String path =  requirement.getImgItems().get(i).getImgurlbig();
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.mipmap.pinpai_def_img)
                        .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                        .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565).build();
                ImageLoader.getInstance().displayImage(path, im, options);
                im.setTag(R.id.TAG,i);
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = (int) v.getTag(R.id.TAG);
                        Intent it = new Intent(mContext, ShowLargePicActivity.class);
                        it.putExtra("position", position);
                        it.putExtra("images",  requirement.getImgItems());
                        it.putExtra("titleAndContentVisible", false);
                        mContext.startActivity(it);
                    }
                });

                add_img.addView(view);
            }
        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REQUIREMENT_GET:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case REQUIREMENT_GET:
                showTextDialog("获取需求详情失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        requset_title = (TextView) findViewById(R.id.requset_title);
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (Button) findViewById(R.id.next_button);
        call_tel = (ImageView) findViewById(R.id.call_tel);
        brand_name = (TextView) findViewById(R.id.brand_name);
        type_text = (TextView) findViewById(R.id.type_text);
        address_text = (TextView) findViewById(R.id.address_text);
        tel_text = (TextView) findViewById(R.id.tel_text);
        content = (TextView) findViewById(R.id.content);
        add_img = (LinearLayout) findViewById(R.id.add_img);
        view_show = findViewById(R.id.view_show);
    }

    @Override
    protected void getExras() {
        requestId = mIntent.getStringExtra("requestId");
        keytype = mIntent.getStringExtra("keytype");
    }

    @Override
    protected void setListener() {
        back_button.setImageResource(R.mipmap.back_img);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("需求详情");
        next_button.setVisibility(View.INVISIBLE);

    }
}
