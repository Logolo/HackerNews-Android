package com.mellort.hackernews;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.mellort.hackernews.NewsListing.NewsItem;

public class Downloader extends AsyncTask<String, Integer, NewsListing> {
	public static Gson gson = new Gson();
	private NewsListingFragment listingFrag;
	private Context context;
	
	public Downloader(Context context, NewsListingFragment listingFrag) {
		this.listingFrag = listingFrag;
		this.context = context;
	}

	@Override
	protected NewsListing doInBackground(String... urls) {
		String path = urls[0];
		Log.d("Downloader", "downloading " + path);
		try {
			// 1. code to load form file
//			InputStream is = this.context.getAssets().open("page.json");
//			NewsListing listing = gson.fromJson(new InputStreamReader(is), NewsListing.class);
			
			// 2. Code to load form url
			URL url = new URL(path);
			URLConnection urlc = url.openConnection();
			urlc.connect();
			NewsListing listing = gson.fromJson(new InputStreamReader(url.openStream()), NewsListing.class);
			
			return listing;
		} catch(Exception e) {
			Log.e("Downloader", "Error", e);
		}
		return null;
	}
	
	@Override
	public void onPostExecute(NewsListing listing) {
		if (listing != null) {
			// TODO: fix
			@SuppressWarnings("unchecked")
			ArrayAdapter<NewsItem> adapter = (ArrayAdapter<NewsItem>) listingFrag.getListAdapter();
			for (NewsItem item : listing.items) {
				Log.d("Downloader", "added: " + item.title);
				adapter.add(item);
			}
			adapter.notifyDataSetChanged();
			
			listingFrag.setNextId(listing.nextId);
		} else {
			Log.d("Downloader", "post: listing was null");
		}
	}

}
