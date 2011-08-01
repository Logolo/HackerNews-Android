package com.mellort.hackernews;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.util.Log;

public class FeedChangeTabListener implements ActionBar.TabListener {
	private NewsListingFragment newsFrag;
	public FeedChangeTabListener(NewsListingFragment newsFrag) {
		this.newsFrag = newsFrag;
	}
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
    	Log.d("Tabs", "hit " + tab.getText());
    	if (tab.getText().equals("Popular")) {
    		Log.d("Tabs", "hit 1");
    		newsFrag.loadPopularFeed();
    	} else if (tab.getText().equals("New")) {
    		Log.d("Tabs", "hit 2");
    		newsFrag.loadNewFeed();
    	}
    }

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		onTabSelected(tab, ft);
	}
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
}