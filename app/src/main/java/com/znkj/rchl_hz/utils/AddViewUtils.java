package com.znkj.rchl_hz.utils;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.InputFilter;
import android.text.InputType;
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
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * addView统一管理类
 * 
 */
public class AddViewUtils
{
	private ImageView sj_btn;
	private ImageView del_btn;
	private int id_num = 1000;
	private List<ImageView> sj_btn_list = new ArrayList<ImageView>();
	private List<View> view_list = new ArrayList<View>();
	private Drawable sj_f;
	private Drawable sj_t;


	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void addInfoLinear(Context context, LinearLayout linearlayout){
		linearlayout.setPadding(DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5));
		addInfoView(linearlayout,context);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void addLinear(String name, LinearLayout linelayout, Context context){
		linelayout.setPadding(DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5),DensityUtil.dip2px(context,5));
		addView(linelayout,name,context);
	}
	public void removeLinear(LinearLayout linelayout){
		linelayout.setPadding(0,0,0,0);
		linelayout.removeAllViews();
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void addView(final LinearLayout lineLayout, String name, Context context) {
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
		vlaueText.setText("0");
		//设置可输入的最大值
		vlaueText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
		vlaueText.setBackgroundColor(Color.WHITE);
		vlaueText.setGravity(Gravity.CENTER);
		vlaueText.setHeight(DensityUtil.dip2px(context,40));
		vlaueText.setWidth(DensityUtil.dip2px(context,40));

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
					vlaueText.setText("0");
				}else {
					if (0 != Integer.valueOf(vlaueText.getText().toString())) {
						vlaueText.setText((Integer.valueOf(vlaueText.getText().toString()) - 1) + "");
					} else {
						Toast.makeText(context, "该值不能小于0", Toast.LENGTH_SHORT).show();
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

	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	private void addInfoView(final LinearLayout lineLayout, Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		View layout = inflater.inflate(R.layout.add_view_item, null);
		if(id_num==2000){
			id_num = 0;
		}
		layout.setTag("layout"+id_num);
		view_list.add(layout);
		//Log.e("cccccccc=====",""+id_num);
		sj_f = context.getDrawable(R.drawable.sj_f);
		sj_t = context.getDrawable(R.drawable.sj_t);
		sj_btn = (ImageView)layout.findViewById(R.id.sj);
		del_btn = (ImageView)layout.findViewById(R.id.del_btn);
		sj_btn.setTag("sj"+id_num);
		del_btn.setTag(id_num);
		del_btn.setOnClickListener(vClick);
		sj_btn_list.add(sj_btn);
		id_num++;
		sj_btn.setOnClickListener(pChildClick);
		//分割线
		final LinearLayout layout_line=new LinearLayout(context);
		LinearLayout.LayoutParams params_line = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				DensityUtil.dip2px(context,10));
		//params_line.setMargins(DensityUtil.dip2px(context,105),0,DensityUtil.dip2px(context,20),0);
		//layout_line.setBackgroundColor(Color.parseColor("#D7D7D7"));
		layout_line.setOrientation(LinearLayout.VERTICAL);
		layout_line.setLayoutParams(params_line);
		final LinearLayout layout_ad=new LinearLayout(context);
		LinearLayout.LayoutParams params_ad = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		layout_ad.setOrientation(LinearLayout.VERTICAL);
		layout_ad.setLayoutParams(params_ad);
		layout_ad.addView(layout);
		layout_ad.addView(layout_line);
		//加入布局
		lineLayout.addView(layout_ad);
	}

	View.OnClickListener pChildClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			final Object tag = v.getTag();
			if (tag!=null){
				ImageView temp_imgv = v.findViewWithTag(tag);
				temp_imgv.setImageDrawable(sj_t);
				for (int ii=0;ii<sj_btn_list.size();ii++){
					String iitag = (String)sj_btn_list.get(ii).getTag();
					if(!iitag.equalsIgnoreCase((String)tag)){
						ImageView temp_imgv1 = sj_btn_list.get(ii);
						temp_imgv1.setImageDrawable(sj_f);
					}
				}
			}
		}
	};

	View.OnClickListener vClick = new View.OnClickListener(){
		public void onClick(View v) {
			final Object tag = v.getTag();
			if (tag!=null){
				ImageView temp_imgv = v.findViewWithTag(tag);
				ViewGroup view = (ViewGroup)temp_imgv.getParent();
				if(null!=view){
					ViewGroup view_pa = (ViewGroup)view.getParent();
					ViewGroup view_pa_p = (ViewGroup)view_pa.getParent();
					view_pa_p.removeView(view_pa);
				}
			}
		}
	};


}