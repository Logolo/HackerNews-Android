package com.mellort.hackernews;

import com.mellort.hackernews.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class HackerNewsActivity extends Activity {
	private NewsArticleFragment webFrag;
	private NewsListingFragment newsFrag;
	private boolean hideListing;
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null)
			hideListing = savedInstanceState.getBoolean("hideListing", false);
		
        if (hideListing) {
        	showListingFragment(false);
        }
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.main);
        
        webFrag = (NewsArticleFragment) getFragmentManager().findFragmentById(R.id.newsarticle_fragment);
        newsFrag = (NewsListingFragment) getFragmentManager().findFragmentById(R.id.newslisting_fragment);
        
        Configuration c = getResources().getConfiguration();
        if (c.orientation == Configuration.ORIENTATION_PORTRAIT) {
        	getActionBar().setDisplayShowTitleEnabled(false);
        } else {
        	getActionBar().setDisplayShowTitleEnabled(true);
        }

        
        // setup Action Bar for tabs
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // remove the activity title to make space for tabs
       // actionBar.setDisplayShowTitleEnabled(false);

        actionBar.addTab(actionBar.newTab().setText("Popular")
        		.setTabListener(new FeedChangeTabListener(newsFrag)));
        actionBar.addTab(actionBar.newTab().setText("New")
        		.setTabListener(new FeedChangeTabListener(newsFrag)));
    }
    
    public void showListingFragment(boolean show) {
    	FragmentTransaction ft = getFragmentManager().beginTransaction();
    	if (show) {
    		ft.show(newsFrag);
    	} else {
    		ft.hide(newsFrag);
    	}
    	
    	ft.commit();
    }
    
    public void toggleListingFragment() {
    	FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (newsFrag.isHidden()) {
            ft.show(newsFrag);
            this.hideListing = false;
        } else {
            ft.hide(newsFrag);
            this.hideListing = true;
        }
        ft.commit();
    }
    
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("hideListing", hideListing);
	}

    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu, menu);
      return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.menu_view_text:
            	webFrag.loadText();
            	return true;
            case R.id.menu_view_web:
            	webFrag.loadWeb();
            	return true;
            case R.id.menu_view_comments:
            	webFrag.loadComments();
            	return true;
            case R.id.menu_view_external:
            	webFrag.loadExternal();
            	return true;
            case R.id.menu_toggle_listing:
            	toggleListingFragment();
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}