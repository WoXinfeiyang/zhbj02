package com.fxj.news.base;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/*
 * 封装一个通用的BaseAdapter基类，主要用于ListView对象的Adapter
 * */
public abstract class DZBaseAdapter<T,Q> extends BaseAdapter {

	public Context context;
	public List<T> list;
	public Q view;
	
	public DZBaseAdapter(Context context, List<T> list, Q view) {
		this.context = context;
		this.list = list;
		this.view = view;
	}
	
	public DZBaseAdapter(Context context, List<T> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
