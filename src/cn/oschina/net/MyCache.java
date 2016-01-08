package cn.oschina.net;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;

public class MyCache extends Application {
	private Map<String, SoftReference<List<Map<String, Object>>>> cacheDoc;

	public Map<String, SoftReference<List<Map<String, Object>>>> getCacheDoc() {
		return cacheDoc;
	}

	public void setCacheDoc(Map<String, SoftReference<List<Map<String, Object>>>> cacheDoc) {
		this.cacheDoc = cacheDoc;
	}

	public void onCreate() {
		super.onCreate();
		cacheDoc = cache();
	}
	
	public HashMap<String, SoftReference<List<Map<String, Object>>>> cache(){
		return new HashMap<String, SoftReference<List<Map<String, Object>>>>();
	}
}

