package cn.oschina.net;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.oschina.net.spiddertask.FirstPageTask;

public class MySpidderActivity extends Activity {

	private Button btn_start;
	private EditText et_city,et_pageStart,et_pageEnd,et_mainUrl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
	}
	private void initViews() {
		// TODO Auto-generated method stub
		btn_start = (Button)findViewById(R.id.btn_start);
		et_city = (EditText)findViewById(R.id.et_city);
		et_pageStart = (EditText)findViewById(R.id.et_page_start);
		et_pageEnd = (EditText)findViewById(R.id.et_page_end);
		et_mainUrl = (EditText)findViewById(R.id.et_url_main);
		

		
		btn_start.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String cityName = et_city.getText().toString();
				int startPage,endPage;
				if (et_pageEnd.getText().toString().equals("")||et_pageEnd.getText().toString().equals("")) {
					Toast.makeText(MySpidderActivity.this, "请输要爬取数据的起始页", Toast.LENGTH_LONG).show();
					return;
				}else {
					startPage = Integer.valueOf(et_pageStart.getText().toString());
					endPage = Integer.valueOf(et_pageEnd.getText().toString());
					if (startPage>endPage||startPage<=0) {
						Toast.makeText(MySpidderActivity.this, "起始页必须小于等于终止页且要大于零", Toast.LENGTH_LONG).show();
						return;
					}
					FirstPageTask firstPageTask = new FirstPageTask(MySpidderActivity.this,cityName,startPage,endPage);
					firstPageTask.execute("");
				}

			}
		});
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			DialogUtil.showDialog(MySpidderActivity.this,"确定要退出系统?", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}
		return super.onKeyDown(keyCode, event);
	}

}
