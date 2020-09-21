package com.xmdd.algorithmpro;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xmdd.algorithmpro.base.MyBeseActivity;
import com.xmdd.algorithmpro.myutils.MyToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MyBeseActivity {

    private Context mContext;
    private List<AlgorithmBean> algorithmList = new ArrayList<AlgorithmBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);

        algorithmList.add(new AlgorithmBean(R.mipmap.ic_algorithm_1, "洗牌算法", "洗牌算法本质是对数组元素的随机重排，用到的地方很多，如：双色球选号、数字华容道、舒尔特表格等"));
//        algorithmList.add(new AlgorithmBean(R.mipmap.ic_algorithm_1, "", ""));
        ListView lv_main_algorithm = findViewById(R.id.lv_main_algorithm);
        lv_main_algorithm.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return algorithmList.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                ViewHolder viewHolder;
                if (convertView == null) {
                    view = View.inflate(mContext, R.layout.activity_main_item, null);
                    viewHolder = new ViewHolder();
                    viewHolder.iv_algorithm_icon = view.findViewById(R.id.iv_algorithm_icon);
                    viewHolder.tv_algorithm_name = view.findViewById(R.id.tv_algorithm_name);
                    viewHolder.tv_algorithm_msg = view.findViewById(R.id.tv_algorithm_msg);
                    view.setTag(viewHolder);
                } else {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag();
                }

                viewHolder.iv_algorithm_icon.setImageResource(algorithmList.get(position).aIcon);
                viewHolder.tv_algorithm_name.setText(algorithmList.get(position).aName);
                viewHolder.tv_algorithm_msg.setText(algorithmList.get(position).aMsg);
                return view;
            }

            class ViewHolder {
                ImageView iv_algorithm_icon;
                TextView tv_algorithm_name;
                TextView tv_algorithm_msg;
            }

        });

        lv_main_algorithm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlgorithmBean bean = algorithmList.get(position);
//                MyToast.showToast("点击了" + position);
                startActivity(new Intent(getActivity(),ShuffleAlgorithmActivity.class));
            }
        });

    }


}