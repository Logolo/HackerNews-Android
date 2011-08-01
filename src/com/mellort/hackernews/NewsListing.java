package com.mellort.hackernews;

import java.util.List;

public class NewsListing {
	public String nextId;
	public List<NewsItem> items;
	public String version;
	public String cachedOnUTC;
	
	public class NewsItem {
		public String title;
		public String url;
		public int id;
		public int commentCount;
		public int points;
		public String postedAgo;
		public String postedBy;
	}
}
