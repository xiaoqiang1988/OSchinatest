package cn.oschina.net.spiddertask;

/**
 * 第二层数据爬取异步任务
 */
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

import cn.oschina.net.Community;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class SecondPageTask extends AsyncTask<String, Community, List<Community>> {

	public static final String TAG = "SecondPageTask";
	ProgressDialog pdialog;
	private Context mContext;
	private String fileName;
	private int currentCount=0;
//	private int pageNo = 1;
 
	private List<HashMap<String, Object>> mList;
	
	public SecondPageTask(Context mContext,List<HashMap<String, Object>> list,String fileName) {
		super();
		this.mContext = mContext;
		this.mList = list;
		this.fileName = fileName;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pdialog = new ProgressDialog(mContext);
		pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdialog.setTitle("正在获取小区数据...");
        pdialog.setMessage("正在请求....");
        pdialog.setMax(mList.size());
        pdialog.show();

	}
	
	@Override
	protected List<Community> doInBackground(String... params) {
		// TODO Auto-generated method stub
		List<Community> list = new ArrayList<Community>();
	
        	for (int j = 0; j < mList.size(); j++) {
                try {   
			Document doc = Jsoup.connect(
			        mList.get(j).get("url").toString())
			        .timeout(30000).userAgent("Mozilla/5.0 (Windows NT 6.1)").header("Content-Type", "application/x-www-form-urlencoded").post();
//            Element element = doc.select("ul.basic-parms").get(0);
//          .after("div.p2").before("div.p4")
//       //   Elements titleElement = element.select("span");
			//Elements elements = doc.select("div.infos-box");
//			.select("ul.basic-parms")
       //   Log.e(TAG,"html:"+mList.get(j).get("url")+elements.size());
//          Log.e(TAG,"html:"+doc.html()+ mList.get(j).get("url"));
           
			
	        Elements elements = doc.getElementsByClass("infos-box").select("li");
	        int lenth = elements.size();
	        if (lenth>0) {
	        	String name =doc.getElementsByClass("infos-box").select("h3").text();
	        	String houseNo =elements.get(1).select("span").text();
	        	String carNo =elements.get(3).select("span").text();
	        	String green =elements.get(2).select("span").text();
	        	String type =elements.get(4).select("span").text();
	        	Community community = new Community(name, houseNo, carNo, type, green);	
	        	list.add(community);
	        	currentCount = j;
	        	publishProgress(community);
			}
        		} catch (IOException e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        		}
/*          for(int i=0; i<titleElement.size();i++){
        	  HashMap<String,Object> hashMap = new HashMap<String, Object>();
              String title= titleElement.get(i).getElementsByTag("a").text();
              String linkHref = titleElement.get(i).getElementsByTag("a").attr("href");
              Log.d(TAG,"title====="+title);
              Log.d(TAG,"linkHref====="+linkHref);
              hashMap.put("title",title);  //鏍囬
              hashMap.put("url",linkHref);   //鏂囩珷url
              list.add(hashMap);
           }*/
	        	
			}

        
		return list;
	}
	
	@Override
	protected void onProgressUpdate(Community... communities) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(communities);
		pdialog.setProgress(currentCount);
		pdialog.setMessage(communities[0].getName()+communities[0].getHouseNumber()+communities[0].getType());
	}
	
	@Override
	protected void onPostExecute(List<Community> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
//		Iterator iter = result.entrySet().iterator();
		Toast.makeText(mContext, "第二层数据爬取完成,共爬去数据"+result.size()+"条", Toast.LENGTH_LONG).show();
		pdialog.dismiss();
/*		for (int i = 0; i < result.size(); i++) {
			String title = result.get(i).getName();
			String url = result.get(i).getType();
			Log.i(TAG, title+url);
		}*/
		new ExcelWriteTask(mContext, result).execute(fileName);
	}

}
