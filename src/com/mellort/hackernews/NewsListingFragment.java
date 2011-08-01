package com.mellort.hackernews;

import java.util.LinkedList;
import java.util.List;

import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.mellort.hackernews.NewsListing.NewsItem;
import com.mellort.hackernews.R;


public class NewsListingFragment extends ListFragment {	
	private NewsItemAdapter adapter;
	private FeedState feedState;
	private String nextId;
	
	public enum FeedState {
		POPULAR("http://api.ihackernews.com/page"),
		NEW("http://api.ihackernews.com/new");
		private final String url;
		FeedState(String url) { this.url = url; }
		public String getUrl() { return this.url; }
	}
	
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.news_listing, container);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState != null)
			feedState = FeedState.valueOf(
					savedInstanceState.getString("feedState", FeedState.POPULAR.toString()));
		if (feedState == null)
			feedState = feedState.POPULAR;
		
		this.setRetainInstance(true);
		
		adapter = new NewsItemAdapter(getActivity(), this,
        		R.layout.news_item, 
        		new LinkedList<NewsItem>());
        setListAdapter(adapter);
        
        clearList();
        downloadFeedAndUpdate();
	}
	
	private void downloadFeedAndUpdate() {
		Downloader d =  new Downloader(getActivity(), this);
        d.execute(feedState.getUrl());	
	}
	
	private void clearList () {
		this.adapter.clear();
		this.adapter.notifyDataSetChanged();
	}
	
	public void loadPopularFeed() {
		feedState = FeedState.POPULAR;
		clearList();
		downloadFeedAndUpdate();
	}
	
	public void loadNewFeed() {
		feedState = FeedState.NEW;
		clearList();
		downloadFeedAndUpdate();
	}
	
	public void addMoreNewsItems() {
		String nextUrl = feedState.getUrl() + "/" + nextId;
		Downloader d =  new Downloader(getActivity(), this);
        d.execute(nextUrl);	
		
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("feedState", feedState.toString());
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onListItemClick (ListView l, View v, int position, long id) {
		NewsArticleFragment articleView = (NewsArticleFragment) getFragmentManager().findFragmentById(R.id.newsarticle_fragment);
		articleView.setCurrentItem(adapter.getItem(position));
		articleView.loadArticle();
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}
}
