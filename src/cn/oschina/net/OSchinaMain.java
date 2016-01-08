package cn.oschina.net;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Intent;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class OSchinaMain extends Activity {

    private static final String TAG ="OSchinaMain" ;
    private TextView textView;
    private LinearLayout ll_all;
    Map<String,SoftReference<List<Map<String, Object>>>> cacheDoc;

    /*public OSchinaMain(){
		MyCache cache = (MyCache) getApplicationContext();
        cacheDoc= cache.getCacheDoc();
	}*/
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        ll_all= (LinearLayout) findViewById(R.id.ll_all);
        textView= (TextView) findViewById(R.id.tv_readContent);
        Intent intent =getIntent();
        String url=intent.getStringExtra("url");
		PageTask task = new PageTask(this,url);
		task.execute("");
	}


	private class PageTask extends AsyncTask<String, Integer, List<Map<String, Object>>> {
		// 可变长的输入参数，与AsyncTask.exucute()对应
		ProgressDialog pdialog;
		MyCache cache ;
        String url;
		public PageTask(Context context,String url) {
			cache=(MyCache) context.getApplicationContext();
            this.url=url;
            Log.v("OSchina-","url=="+url);
			pdialog = new ProgressDialog(context, 0);
			pdialog.setTitle("正在连接请稍候....");
			pdialog.setButton("cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int i) {
					dialog.cancel();
				}
			});
			pdialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			pdialog.setMax(100);
			pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pdialog.show();

		}

		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {
			List<Map<String, Object>> arr = null;
			try {
				
				int count=1;
				int length=100;
					Log.v("OSchina-","不走缓存");
					arr = new ArrayList<Map<String,Object>>();
					Document doc = Jsoup.connect(
                            url)
						.timeout(8000).post();
                    Log.v("OSchina-","请求结束:"+(doc!=null));

                    //文章时间
                    String  contentTime=  doc.select("span.cb-title-fi").select("time.updated").get(0).text();
                    //作者
                    String author= doc.select("span.fn").get(0).text();
                    //标题
                    String title= doc.select("span.cb-title-fi").select("h1").get(0).text();
                    //文章内容（html 包含图片地址  p标签等）
                    String article = doc.select("article").attr("section", "articleBody").text();

                    Log.d("OSchina-","title:"+title);
                    Log.d("OSchina-","author:"+author);
                    Log.d("OSchina-","contentTime:"+contentTime);
                    Log.d("OSchina-","article:"+article);


                    HashMap<String,Object> hashMap = new HashMap<String, Object>();
                    hashMap.put("title",title);
                    hashMap.put("author",author);
                    hashMap.put("contentTime",contentTime);
                    hashMap.put("article",article);
                    arr.add(hashMap);
					cache.getCacheDoc().put("doc", new SoftReference<List<Map<String, Object>>>(arr));
					


			} catch (Exception e) {
				Log.e("doInBackground", "--"+e);
                Toast.makeText(OSchinaMain.this,"连接超时",1).show();
                finish();
            }
			return arr;

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {

             for(int i=0;i<result.size();i++){
                 String title = (String) result.get(i).get("title");
                 String author = (String) result.get(i).get("author");
                 String contentTime = (String) result.get(i).get("contentTime");
                 String article = (String) result.get(i).get("article");
                 textView.append(title+"\n");
                 textView.append(author+"\n");
                 textView.append(contentTime+"\n");
                 textView.append(article);
                 ll_all.removeAllViews();
                 ll_all.addView(textView);
             }

			// 返回HTML页面的内容

			pdialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			// 任务启动，可以在这里显示一个对话框，这里简单处理
			// message.setText(R.string.task_started);
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 更新进度
			//System.out.println("" + values[0]);
			// message.setText(""+values[0]);
			pdialog.setProgress(values[0]);
		}

	}


	
	


}
