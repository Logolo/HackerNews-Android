package com.mellort.hackernews;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mellort.hackernews.NewsListing.NewsItem;
import com.mellort.hackernews.R;

public class NewsItemAdapter extends ArrayAdapter<NewsItem>{
	private Context context;
	private int resourceId;
	public List<NewsItem> items;
	private LayoutInflater inflater;
	private NewsListingFragment newsFrag;
	private int previousTotalSize;
	private final static int ENDLESS_SCROLL_BUFFER_SIZE = 12;

	public NewsItemAdapter(Context context, NewsListingFragment newsFrag, 
			int resourceId, List<NewsItem> items) {
		super(context, resourceId, items);

		this.newsFrag = newsFrag;
		this.context = context;
		this.resourceId = resourceId;
		this.items = items;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public void clear() {
		super.clear();
		previousTotalSize = -1;
	}

	static class ViewHolder {
		TextView text;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		int currentSize = items.size();
		if (position + ENDLESS_SCROLL_BUFFER_SIZE >= currentSize 
				&& previousTotalSize != currentSize) {
			Log.d("Scroll", "hit getView check innter");
			previousTotalSize = currentSize;
			newsFrag.addMoreNewsItems();
		}

		if (convertView == null) {
//			convertView = inflater.inflate(R.layout.news_item, null);
			convertView = inflater.inflate(R.layout.news_item, parent, false);
			holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.item_text);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (position%2 == 0) {
			convertView.setBackgroundColor(android.R.color.black);
		} else {
			convertView.setBackgroundColor(android.R.color.white);
		}
		
		holder.text.setText(items.get(position).title);
		
		return convertView;
	}
}
