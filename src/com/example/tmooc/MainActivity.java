package com.example.tmooc;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

public class MainActivity extends TabActivity {
    private TabHost tabHost;
 
	//内容Intent
	private Intent homeIntent;
	private Intent studyIntent;
	private Intent selfIntent;
	

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);//设置TabHost使用的布局文件
        
        tabHost=this.getTabHost();
        tabHost.setFocusable(true);
        prepareIntent();
        setupIntent();
        
    }
    
    private void setupIntent(){
    	tabHost.addTab(buildTabSpec("课程",R.drawable.icon_home, homeIntent));
    	tabHost.addTab(buildTabSpec("学习",R.drawable.icon_study, studyIntent));
    	tabHost.addTab(buildTabSpec("个人",R.drawable.icon_self, selfIntent));
    	
    }

	private TabSpec buildTabSpec(String tag, int icon, Intent intent) {
		View view = View.inflate(MainActivity.this, R.layout.tab, null);
		((ImageView)view.findViewById(R.id.tab_im_icon)).setImageResource(icon);
	    ((TextView)view.findViewById(R.id.tab_tv_text)).setText(tag);
		return tabHost.newTabSpec(tag)
        		.setIndicator(view)
        		.setContent(intent);
	}

	private void prepareIntent() {
		homeIntent=new Intent(this, HomeActivity.class);
		studyIntent=new Intent(this, StudyIntent.class);
		selfIntent=new Intent(this, SelfActivity.class);
		
	}

}