package com.znkj.rchl_hz.utils;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.znkj.rchl_hz.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * addView统一管理类
 * 
 */
public class AddViewUtils
{


	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public Map<String,String> addLinear(String name, String code, LinearLayout linelayout, Context context){
		linelayout.setPadding(DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5));
		Map<String,String> map = new HashMap<String,String>();
		map = addView(linelayout,name,code,context);
		return map;
	}
	public void removeLinear(LinearLayout linelayout){
		linelayout.setPadding(0,0,0,0);
		linelayout.removeAllViews();
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private Map<String,String> addView(final LinearLayout lineLayout, String name, String code, Context context) {
		//文本lauout
		final LinearLayout layout2=new LinearLayout(context);
		layout2.setOrientation(LinearLayout.HORIZONTAL);
		layout2.setBackgroundColor(Color.WHITE);
		layout2.setPadding(DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5));
		LinearLayout.LayoutParams params_layout2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		params_layout2.setMargins(DensityUtil.dip2px(context,90),0,DensityUtil.dip2px(context,20),0);
		layout2.setLayoutParams(params_layout2);
		//分割线layout
		final LinearLayout layout_line=new LinearLayout(context);
		LinearLayout.LayoutParams params_line = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				DensityUtil.dip2px(context,1));
		params_line.setMargins(DensityUtil.dip2px(context,105),0,DensityUtil.dip2px(context,20),0);
		layout_line.setBackgroundColor(Color.parseColor("#D7D7D7"));
		layout_line.setOrientation(LinearLayout.VERTICAL);
		layout_line.setLayoutParams(params_line);

		//text
		final TextView showText = new TextView(context);
		showText.setTextColor(Color.GRAY);
		showText.setTextSize(14);
		//showText.setId(10001);//设置 id
		showText.setText(name);
		showText.setBackgroundColor(Color.WHITE);
		showText.setGravity(Gravity.CENTER_VERTICAL);
		showText.setHeight(DensityUtil.dip2px(context,40));
		showText.setWidth(DensityUtil.dip2px(context,140));

		//value_text
		final EditText vlaueText = new EditText(context);
		vlaueText.setTextColor(Color.GRAY);
		vlaueText.setTextSize(14);
		//vlaueText.setId(10001);//设置 id
		vlaueText.setInputType(InputType.TYPE_CLASS_NUMBER);
		vlaueText.setText("1");//设置可输入的最大值

		Map<String,String> map = new HashMap<String,String>();
		vlaueText.setTag(code);// 设置tag
		map.put(code,"1");// 默认数量为1

		vlaueText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
		vlaueText.setBackgroundColor(Color.WHITE);
		vlaueText.setGravity(Gravity.CENTER);
		vlaueText.setHeight(DensityUtil.dip2px(context,40));
		vlaueText.setWidth(DensityUtil.dip2px(context,40));
		vlaueText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//输入内容之前你想做什么
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//输入的时候你想做什么
			}
			public void afterTextChanged(Editable s) {
				//输入之后你想做什么
				map.put((String)vlaueText.getTag(),s.toString());// 默认数量为1
			}
		});

		//创建按钮
		ImageView btn_del = new ImageView(context);
		ImageView btn_add = new ImageView(context);
		Drawable del = context.getDrawable(R.drawable.del);
		Drawable add = context.getDrawable(R.drawable.add);
		btn_del.setImageDrawable(del);
		btn_add.setImageDrawable(add);
		LinearLayout.LayoutParams btn_params = new LinearLayout.LayoutParams(DensityUtil.dip2px(context,20),
				ViewGroup.LayoutParams.WRAP_CONTENT);
		btn_params.gravity = Gravity.CENTER_HORIZONTAL;
		btn_del.setLayoutParams(btn_params);
		btn_add.setLayoutParams(btn_params);

		//点击按钮
		btn_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("".equalsIgnoreCase(vlaueText.getText().toString())||null==vlaueText.getText().toString()) {
					vlaueText.setText("1");
				}else {
					if (1 != Integer.valueOf(vlaueText.getText().toString())) {
						vlaueText.setText((Integer.valueOf(vlaueText.getText().toString()) - 1) + "");
					} else {
						Toast.makeText(context, "该值不能小于1", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		btn_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if ("".equalsIgnoreCase(vlaueText.getText().toString())||null==vlaueText.getText().toString()) {
					vlaueText.setText("0");
				}else {
					if (999 != Integer.valueOf(vlaueText.getText().toString())) {
						vlaueText.setText((Integer.valueOf(vlaueText.getText().toString()) + 1) + "");
					} else {
						Toast.makeText(context, "该值不能大于999", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});

		//添加文本到布局
		layout2.addView(showText );
		layout2.addView(btn_del);
		layout2.addView(vlaueText);
		layout2.addView(btn_add);
		// 动态添加按钮到主布局
		lineLayout.addView(layout2);
		lineLayout.addView(layout_line);

		return map;
	}


}