package com.hemaapp.dtyw.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hemaapp.dtyw.activity.CommodityInforActivity;
import com.hemaapp.dtyw.fragment.BuyCarFragment;
import com.hemaapp.dtyw.model.Car;
import com.hemaapp.dtyw.myapplication.R;
import com.hemaapp.hm_FrameWork.HemaAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/9/22.
 * 购物车的adapter
 */
public class BuyCarAdapter extends HemaAdapter {
    private BuyCarFragment fragment;
    private ArrayList<Car> cars;

    public BuyCarAdapter(Context mContext, BuyCarFragment fragment, ArrayList<Car> cars) {
        super(mContext);
        this.fragment = fragment;
        this.cars = cars;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }

    @Override
    public boolean isEmpty() {
        return cars == null || cars.size() == 0;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 1 : cars.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (isEmpty())
            return getEmptyView(parent);
        if (convertView == null) {
            convertView = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_buy_car_view, null);
            ViewHolder holder = new ViewHolder();
            findView(holder, convertView);
            convertView.setTag(R.id.TAG, holder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag(R.id.TAG);
        setView(holder, position);
        return convertView;
    }

    private class ViewHolder {
        LinearLayout layout1_in;
        ImageView check_select_img;
        ImageView chanp_img;
        TextView commod_name;
        TextView commod_moeny;
        ImageView subtarct;
        ImageView add;
        TextView add_or_subtract;
        TextView content_commid;
    }

    private void findView(ViewHolder holder, View view) {
        holder.layout1_in = (LinearLayout) view.findViewById(R.id.layout1_in);
        holder.check_select_img = (ImageView) view.findViewById(R.id.check_select_img);
        holder.chanp_img = (ImageView) view.findViewById(R.id.chanp_img);
        holder.commod_name = (TextView) view.findViewById(R.id.commod_name);
        holder.commod_moeny = (TextView) view.findViewById(R.id.commod_moeny);
        holder.subtarct = (ImageView) view.findViewById(R.id.subtarct);
        holder.add = (ImageView) view.findViewById(R.id.add);
        holder.add_or_subtract = (TextView) view.findViewById(R.id.add_or_subtract);
        holder.content_commid = (TextView) view.findViewById(R.id.content_commid);
    }

    private void setView(ViewHolder holder, int position) {
        Car car = cars.get(position);
        //判断是否选中
        if (car.isCheck())
            holder.check_select_img.setImageResource(R.mipmap.check_anzhuang_off);
        else
            holder.check_select_img.setImageResource(R.mipmap.check_anzhuang_on);

        //选中操作
        holder.check_select_img.setTag(R.id.TAG,car);
        holder.check_select_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car1 = (Car) v.getTag(R.id.TAG);
                fragment.selectCart(Integer.valueOf(car1.getId()));
            }
        });
        //名称
        if (!isNull(car.getGoodsname()))
            holder.commod_name.setText(car.getGoodsname());
        //总价价格
        if (!isNull(car.getPrice()))
            holder.commod_moeny.setText(car.getPrice());
        //数量
        holder.add_or_subtract.setText(car.getBuycount());
        //规格
        if (isNull(car.getPropertyname()))
            holder.content_commid.setVisibility(View.GONE);
        else {
            holder.content_commid.setVisibility(View.VISIBLE);
            holder.content_commid.setText(car.getPropertyname());
        }
        //改变价格
        //数量加
        holder.add.setTag(R.id.TAG, car);
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car1 = (Car) v.getTag(R.id.TAG);
                int num = 1 + Integer.valueOf(car1.getBuycount());
                fragment.changeNum(car1.getId(), String.valueOf(num), "3");
            }
        });
        //数量减
        holder.subtarct.setTag(R.id.TAG, car);
        holder.subtarct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car1 = (Car) v.getTag(R.id.TAG);
                if (car1.getBuycount().equals("1")) {
                    fragment.showTextDialog("数量最少为1哦");
                } else {
                    int num = Integer.valueOf(car1.getBuycount()) - 1;
                    fragment.changeNum(car1.getId(), String.valueOf(num), "3");
                }
            }
        });
        //查看商品详情
        holder.layout1_in.setTag(R.id.TAG,car);
        holder.layout1_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Car car1 = (Car) v.getTag(R.id.TAG);
                Intent intent = new Intent(fragment.getActivity(), CommodityInforActivity.class);
                intent.putExtra("id",car1.getKeyid());
                fragment.startActivity(intent);
            }
        });
        //删除
        holder.layout1_in.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Car car1 = (Car) v.getTag(R.id.TAG);
                fragment.showDelete(car1.getId());
                return false;
            }
        });
        //图片
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pinpai_def_img)
                .showImageForEmptyUri(R.mipmap.pinpai_def_img)
                .showImageOnFail(R.mipmap.pinpai_def_img).cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoader.getInstance().displayImage(car.getImgurl(), holder.chanp_img, options);

    }


}
