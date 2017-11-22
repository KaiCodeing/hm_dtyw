package com.hemaapp.dtyw.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hemaapp.dtyw.DtywActivity;
import com.hemaapp.dtyw.DtywApplication;
import com.hemaapp.dtyw.DtywHttpInformation;
import com.hemaapp.dtyw.adapter.MessageAdapter;
import com.hemaapp.dtyw.model.Notice;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaNetTask;
import com.hemaapp.hm_FrameWork.result.HemaBaseResult;
import com.hemaapp.hm_FrameWork.result.HemaPageArrayResult;
import com.hemaapp.hm_FrameWork.view.RefreshLoadmoreLayout;

import java.util.ArrayList;

import xtom.frame.util.XtomToastUtil;
import xtom.frame.view.XtomListView;
import xtom.frame.view.XtomRefreshLoadmoreLayout;

/**
 * Created by lenovo on 2016/9/18.
 * 消息盒子
 */
public class MessageActivity extends DtywActivity {
    private RefreshLoadmoreLayout refreshLoadmoreLayout;
    private XtomListView listview;
    private ProgressBar progressbar;
    private ImageButton back_button;
    private TextView title_text;
    private ImageButton next_button;
    private String type = "1";
    private RadioGroup radiogroup;
    private Integer page = 0;
    private View view1;
    private View view2;
    private ArrayList<Notice> notices = new ArrayList<Notice>();
    private MessageAdapter adapter;
    private ViewHoler holer;
    private DeleteView deleteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_message);
        super.onCreate(savedInstanceState);
        inIt();
    }

    private void inIt() {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().noticeList(token, type, String.valueOf(page));
    }

    @Override
    protected void callBeforeDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                showProgressDialog("获取消息信息");
                break;
            case NOTICE_SAVEOPERATE:
                showProgressDialog("保存操作信息");
                break;
        }
    }

    @Override
    protected void callAfterDataBack(HemaNetTask hemaNetTask) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                cancelProgressDialog();
                progressbar.setVisibility(View.GONE);
                refreshLoadmoreLayout.setVisibility(View.VISIBLE);

                break;
            case NOTICE_SAVEOPERATE:
                cancelProgressDialog();
                break;
        }
    }

    @Override
    protected void callBackForServerSuccess(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                HemaPageArrayResult<Notice> result3 = (HemaPageArrayResult<Notice>) hemaBaseResult;
                ArrayList<Notice> notices = result3.getObjects();
                String page2 = hemaNetTask.getParams().get("page");
                if ("0".equals(page2)) {// 刷新
                    refreshLoadmoreLayout.refreshSuccess();
                    this.notices.clear();
                    this.notices.addAll(notices);

                    DtywApplication application = DtywApplication.getInstance();
                    int sysPagesize = application.getSysInitInfo()
                            .getSys_pagesize();
                    if (notices.size() < sysPagesize) {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                    } else {
                        refreshLoadmoreLayout.setLoadmoreable(true);
                        // leftRE = true;
                    }
                } else {// 更多
                    refreshLoadmoreLayout.loadmoreSuccess();
                    if (notices.size() > 0)
                        this.notices.addAll(notices);
                    else {
                        refreshLoadmoreLayout.setLoadmoreable(false);
                        // leftRE = false;
                        XtomToastUtil.showShortToast(this, "已经到最后啦");
                    }
                }
                freshData();
                break;
            case NOTICE_SAVEOPERATE:
                inIt();
                break;
        }
    }

    private void freshData() {
        if (adapter == null) {
            adapter = new MessageAdapter(mContext, notices, MessageActivity.this, type);

            adapter.setEmptyString("暂无系统消息");

            listview.setAdapter(adapter);
        } else {
            adapter.setNotices(notices);
            if ("1".equals(type))
            adapter.setEmptyString("暂无系统消息");
            else
            adapter.setEmptyString("暂无订单消息");
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void callBackForServerFailed(HemaNetTask hemaNetTask, HemaBaseResult hemaBaseResult) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                showTextDialog(hemaBaseResult.getMsg());
                break;
        }
    }

    @Override
    protected void callBackForGetDataFailed(HemaNetTask hemaNetTask, int i) {
        DtywHttpInformation information = (DtywHttpInformation) hemaNetTask.getHttpInformation();
        switch (information) {
            case NOTICE_LIST:
                String page = hemaNetTask.getParams().get("page");
                if ("0".equals(page)) {
                    refreshLoadmoreLayout.refreshFailed();
                } else {
                    refreshLoadmoreLayout.loadmoreFailed();
                }
                showTextDialog("获取消息失败，请稍后重试");
                break;
        }
    }

    @Override
    protected void findView() {
        refreshLoadmoreLayout = (RefreshLoadmoreLayout) findViewById(R.id.refreshLoadmoreLayout);
        listview = (XtomListView) findViewById(R.id.listview);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        back_button = (ImageButton) findViewById(R.id.back_button);
        title_text = (TextView) findViewById(R.id.title_text);
        next_button = (ImageButton) findViewById(R.id.next_button);
        radiogroup = (RadioGroup) findViewById(R.id.radiogroup);
        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
    }

    @Override
    protected void getExras() {

    }

    @Override
    protected void setListener() {
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title_text.setText("消息盒子");
        //清空已读
        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelect();
            }
        });
        /**
         * 选择消息类型
         */
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.button1:
                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        type = "1";
                        page = 0;
                        inIt();
                        break;
                    case R.id.button2:
                        type = "2";
                        page = 0;
                        view2.setVisibility(View.VISIBLE);
                        view1.setVisibility(android.view.View.INVISIBLE);
                        inIt();
                        break;
                }
            }
        });
        refreshLoadmoreLayout.setOnStartListener(new XtomRefreshLoadmoreLayout.OnStartListener() {
            @Override
            public void onStartRefresh(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page = 0;
                inIt();
            }

            @Override
            public void onStartLoadmore(XtomRefreshLoadmoreLayout xtomRefreshLoadmoreLayout) {
                page++;
                inIt();
            }
        });
    }

    //
    private class ViewHoler {
        TextView camera_text;
        TextView album_text;
        TextView textView1_camera;
    }

    private void showSelect() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_camera, null);
        holer = new ViewHoler();
        holer.album_text = (TextView) view.findViewById(R.id.album_text);
        holer.camera_text = (TextView) view.findViewById(R.id.camera_text);
        holer.textView1_camera = (TextView) view.findViewById(R.id.textView1_camera);
        holer.camera_text.setText("全部设为已读");
        holer.album_text.setText("清空消息列表");
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        //设为已读
        holer.camera_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String token = DtywApplication.getInstance().getUser().getToken();
                getNetWorker().noticeSaveoperate(token, "0", type);
                page = 0;
                popupWindow.dismiss();
            }
        });
        //清空
        holer.album_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                showDelete(null);

            }

        });
        holer.textView1_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
     //   popupWindow.showAsDropDown(findViewById(R.id.pop_layout_bottom));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    private class DeleteView {
        TextView close_pop;
        TextView yas_pop;
        TextView text;
        TextView iphone_number;
    }

    private void showDelete(final String id) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popwindo_right_left, null);
        deleteView = new DeleteView();
        deleteView.close_pop = (TextView) view.findViewById(R.id.close_pop);
        deleteView.yas_pop = (TextView) view.findViewById(R.id.yas_pop);
        if (!isNull(id)) {
            deleteView.text = (TextView) view.findViewById(R.id.text);
            deleteView.iphone_number = (TextView) view.findViewById(R.id.iphone_number);
            deleteView.text.setText("确定要删除该条消息吗？");
            deleteView.iphone_number.setText("一旦删除将不能找回");

        }
        final PopupWindow popupWindow = new PopupWindow(view,
                RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.MATCH_PARENT);
        deleteView.close_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        deleteView.yas_pop.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      String token = DtywApplication.getInstance().getUser().getToken();
                                                      if (isNull(id)) {
                                                          if (type.equals("1"))
                                                              getNetWorker().noticeSaveoperate(token, "0", "3");
                                                          else
                                                              getNetWorker().noticeSaveoperate(token, "0", "4");
                                                      } else {
                                                          getNetWorker().noticeSaveoperate(token, id, "5");
                                                      }
                                                      page = 0;
                                                      popupWindow.dismiss();
                                                  }


                                              }

        );
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setWidth(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(RadioGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new

                BitmapDrawable()

        );
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        // popupWindow.showAsDropDown(findViewById(R.id.ll_item));
        popupWindow.setAnimationStyle(R.style.PopupAnimation);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * 删除某一条数据
     *
     * @param id
     */
    public void deleteMessge(String id) {
//        String token = DtywApplication.getInstance().getUser().getToken();
//        getNetWorker().noticeSaveoperate(token, id, "5");
        showDelete(id);
    }

    /**
     * 已读信息
     *
     * @param id
     */
    public void yiduMessage(String id) {
        String token = DtywApplication.getInstance().getUser().getToken();
        getNetWorker().noticeSaveoperate(token, id, "6");
    }
}
