package cn.oschina.net;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private static final String TAG ="MainActivity" ;
    ListView listView;
    TitleAdapter mAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        listView= (ListView) findViewById(R.id.lv_main);
        new PageTask(this).execute();
    }


    private class PageTask extends AsyncTask<String, Integer, List<Map<String, Object>>> {
        // 鍙彉闀跨殑杈撳叆鍙傛暟锛屼笌AsyncTask.exucute()瀵瑰簲
        ProgressDialog pdialog;
        MyCache cache ;
        public PageTask(Context context) {
            cache=(MyCache) context.getApplicationContext();
            pdialog = new ProgressDialog(context, 0);
            pdialog.setTitle("conneting....");
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
            // pdialog.setCancelable(true);
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
                if(cache.getCacheDoc().containsKey("doc")){
                    SoftReference<List<Map<String, Object>>> soft= cache.getCacheDoc().get("doc");
                    arr = soft.get();
                    for(int i=1;i<=100;i++){
                        publishProgress((int) ((i / (float) length) * 100));
                    }
                    Log.v("OSchina-","read cache");
                }else{
                    Log.v("OSchina-","not read cache");
                    arr = new ArrayList<Map<String,Object>>();
//                    Document doc = Jsoup.connect(
//                            "http://fotomen.cn/")
//                            .timeout(30000).post();
                    Document doc = Jsoup.connect(
                            "http://www.anjuke.com/shanghai/cm/p1/")
                            .timeout(30000).post();
                    
                    Log.e("OSchina-","request over:"+(doc!=null)+doc.text());
//                    Elements titleElement = doc.select("div.cb-article-meta");
//                    Elements titleElement = doc.select("ul.p3");
                      Element element = doc.getElementById("content").select("ul.p3").get(0);
//                    .after("div.p2").before("div.p4")
                   
                    Elements titleElement = element.select("em");
                    Log.e("安居客","html:"+titleElement.size());
                    for(int i=0; i<titleElement.size();i++){
                        String title= titleElement.get(i).getElementsByTag("a").text();
                        String linkHref = titleElement.get(i).getElementsByTag("a").attr("href");
                        Log.d(TAG,"title====="+title);
                        Log.d(TAG,"linkHref====="+linkHref);
                        HashMap<String,Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("title",title);  //鏍囬
                        hashMap.put("url",linkHref);   //鏂囩珷url
                        arr.add(hashMap);
                     }
                    cache.getCacheDoc().put("doc", new SoftReference<List<Map<String, Object>>>(arr));
                }
            } catch (Exception e) {
                Log.e("doInBackground", "--"+e);
            }
            return arr;

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(final List<Map<String, Object>> result) {
            final List<Map<String, Object>> data = result;
         pdialog.dismiss();
                if(result!=null){
                    mAdapter=new TitleAdapter(MainActivity.this,result);
                    listView.setAdapter(mAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent(MainActivity.this,OSchinaMain.class);
                            intent.putExtra("url",data.get(i).get("url").toString());
                            startActivity(intent);
                        }
                    });
                }else{
                    Log.d(TAG,"result====null");
                }
        }

        @Override
        protected void onPreExecute() {
            // 浠诲姟鍚姩锛屽彲浠ュ湪杩欓噷鏄剧ず涓�涓璇濇锛岃繖閲岀畝鍗曞鐞�
            // message.setText(R.string.task_started);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // 鏇存柊杩涘害
            //System.out.println("" + values[0]);
            // message.setText(""+values[0]);
            pdialog.setProgress(values[0]);
        }
    }

}