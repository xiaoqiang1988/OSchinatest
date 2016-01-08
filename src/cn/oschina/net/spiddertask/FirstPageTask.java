package cn.oschina.net.spiddertask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class FirstPageTask extends AsyncTask<String, Integer, List<HashMap<String, Object>>> {

	public static final String TAG = "FirstPageTask";
	ProgressDialog pdialog;
	private Context mContext;
//	private int pageNo = 1;
	private String urlHead = "";
	private String urlFoot = "/"; 
	private String city;
	private int startPage,endPage;
	public FirstPageTask(Context mContext,String city,int start,int end) {
		super();
		this.mContext = mContext;
		this.startPage = start;
		this.endPage = end;
		this.city = city;
		this.urlHead = "http://www.anjuke.com/"+city+"/cm/p";
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
        pdialog = ProgressDialog.show(mContext, "系统提示", "正在请求....");

	}
	
	@Override
	protected List<HashMap<String, Object>> doInBackground(String... params) {
		// TODO Auto-generated method stub
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();
        try {   	
        	for (int j = startPage; j <= endPage; j++) {
			Document doc = Jsoup.connect(
			        getRequestUrl(j))
			        .timeout(30000).post();
            Element element = doc.getElementById("content").select("ul.p3").get(0);
//          .after("div.p2").before("div.p4")
          Elements titleElement = element.select("em");
          Log.e(TAG,"html:"+titleElement.size());
          for(int i=0; i<titleElement.size();i++){
        	  HashMap<String,Object> hashMap = new HashMap<String, Object>();
              String title= titleElement.get(i).getElementsByTag("a").text();
              String linkHref = titleElement.get(i).getElementsByTag("a").attr("href");
              Log.d(TAG,"title====="+title);
              Log.d(TAG,"linkHref====="+linkHref);
              hashMap.put("title",title);  //鏍囬
              hashMap.put("url",linkHref);   //鏂囩珷url
              list.add(hashMap);
           }
          publishProgress(j);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return list;
	}
	
	@Override
	protected void onProgressUpdate(Integer... pageNo) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(pageNo);
	
		 pdialog.setMessage("正在请求第"+pageNo[0]+"页");
	}
	
	@Override
	protected void onPostExecute(List<HashMap<String, Object>> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		Iterator iter = result.entrySet().iterator();
		Toast.makeText(mContext, "第一层数据爬区完成,共爬去数据"+result.size()+"条", Toast.LENGTH_LONG).show();
		pdialog.dismiss();
		for (int i = 0; i < result.size(); i++) {
			String title = result.get(i).get("title").toString();
			String url = result.get(i).get("url").toString();
			Log.i(TAG, title+url);
		}
		new SecondPageTask(mContext, result,city+startPage+"-"+endPage).execute("");
	}
	private String getRequestUrl(int pageNo){
		
		return urlHead+pageNo+urlFoot;
		
	}
}
