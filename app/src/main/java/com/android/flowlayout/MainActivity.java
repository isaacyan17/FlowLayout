package com.android.flowlayout;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

/**
 * @作者      jingqiang
 * @项目名称   FlowLayout
 * @包名 com.android.flowlayout
 * @创建时间 15/8/20 下午7:19
 * @描述   FlowLayout测试数据
 */

public class MainActivity extends Activity {

    private String[] mDatas = new String[] { "QQ", "视频", "放开那三国", "电子书", "酒店",
            "单机", "小说", "斗地主", "优酷", "网游", "WIFI万能钥匙", "播放器", "捕鱼达人2", "机票",
            "游戏", "熊出没之熊大快跑", "美图秀秀", "浏览器", "单机游戏", "我的世界", "电影电视", "QQ空间",
            "旅游", "免费游戏", "2048", "刀塔传奇", "壁纸", "节奏大师", "锁屏", "装机必备", "天天动听",
            "备份", "网盘", "海淘网", "大众点评", "爱奇艺视频", "腾讯手机管家", "百度地图", "猎豹清理大师",
            "谷歌地图", "hao123上网导航", "京东", "youni有你", "万年历-农历黄历", "支付宝钱包" };
    private FlowLayout mFlowLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowLayout=(FlowLayout)findViewById(R.id.mLayout);
        mFlowLayout.setPadding(10,10,10,10);

        Random random = new Random();
        for(int i =0;i<mDatas.length;i++)
        {
            TextView tv = new TextView(this);
            tv.setText(mDatas[i]);
            tv.setBackgroundColor(Color.GRAY);
            tv.setTextColor(Color.WHITE);
            tv.setPadding(5, 5, 5, 5);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(random.nextInt(16)+10);

            mFlowLayout.addView(tv);
        }
    }


}
