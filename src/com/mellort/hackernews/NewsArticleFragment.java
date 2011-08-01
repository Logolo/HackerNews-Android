package com.mellort.hackernews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;

import com.mellort.hackernews.NewsListing.NewsItem;

public class NewsArticleFragment extends WebViewFragment {
	private static final String INSTAPAPER_BASE_URL = "http://www.instapaper.com/text?u=";
	private static final String HACKER_NEWS_BASE_URL = "http://news.ycombinator.com/item?id=";
	
	private NewsItem currentItem;
	private String currentUrl;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.getWebView().restoreState(savedInstanceState);

		WebView webView = this.getWebView();
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		//settings.setBuiltInZoomControls(true);
		settings.setEnableSmoothTransition(true);
		settings.setSaveFormData(true);
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
	}
	
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		this.getWebView().saveState(outState);
	}
	
	public void setCurrentItem(NewsItem item) {
		this.currentItem = item;
	}
	
	private void loadUrl(String url) {
		if (url == currentUrl)
			return;
		this.getWebView().loadUrl(url);
		this.currentUrl = url;
	}
	
	/*
	 * Try to load best view for the url. Instapaper doesn't play
	 * nice with news.ycomb or stack exchange, or raw images, so
	 * just load the site.
	 */
	public void loadArticle() {
		Log.d("Article", "hit1");
		if (currentItem == null)
			return;
		Log.d("Article", "hit2");
		String url = currentItem.url;
		String title = currentItem.title;
		
		// add base to relative url ie Ask HN posts
		if (url.startsWith("/")) {
			currentItem.url = HACKER_NEWS_BASE_URL + currentItem.id;
			loadWeb();
		} else if (url.startsWith("http://news.ycombinator.com")) {
			loadWeb();
		} else if (url.endsWith(".png") || url.endsWith(".jpeg") ||
				url.endsWith(".gif")) {
			loadWeb();
		} else if (url.contains("stackexchange.com")) {
			loadWeb();
		} else if (title.toLowerCase().contains("[video]")) {
			loadWeb();
		} else {
			loadText();
		}
	}

	public void loadText() {
		if (currentItem == null)
			return;
		String url = INSTAPAPER_BASE_URL + Uri.encode(currentItem.url);
		loadUrl(url);
	}
	
	public void loadComments() {
		if (currentItem == null)
			return;
		String url = HACKER_NEWS_BASE_URL + currentItem.id;
		loadUrl(url);
	}
	
	public void loadWeb() {
		if (currentItem == null)
			return;
		loadUrl(currentItem.url);
	}
	
	public void loadExternal() {
		if (currentItem == null)
			return;
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(currentItem.url));
		getActivity().startActivity(intent);
	}
}
