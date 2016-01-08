package cn.oschina.net.spiddertask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import cn.oschina.net.Community;

public class ExcelWriteTask extends AsyncTask<String, String, String> {

	private Context mContext;
	private List<Community> list;
	private ArrayList<CurCell> mArrayList = new ArrayList<CurCell>(); 
	private ProgressDialog pdialog;
	 
	
	public ExcelWriteTask(Context mContext, List<Community> list) {
		super();
		this.mContext = mContext;
		this.list = list;
		mArrayList = new ArrayList<CurCell>();
		for (int i = 0; i < list.size(); i++) {
			Community community = list.get(i);
			for (int j = 0; j < 5; j++) {
				CurCell cell = new CurCell();
				cell.row = i;
				cell.col = j;
				cell.content = community.getProperty(j);
				mArrayList.add(cell);
			}
					
		}
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		 pdialog = ProgressDialog.show(mContext, "正在写入Excel...", "正在请求....");
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		WriteExcel(params[0]);
		return null;
	}
	
	@Override
	protected void onProgressUpdate(String... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
		pdialog.setMessage(values[0]);
	}
	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		Toast.makeText(mContext, "写入Excel数据完成！请到SD卡根目录下查看", Toast.LENGTH_LONG).show();
		pdialog.dismiss();
	}

	private void WriteExcel(String sheetName){  
        try {  
        	File file = new File("/mnt/sdcard/"+sheetName+".xls");
        	if (!file.exists()) {
				file.createNewFile();
			}
            WritableWorkbook mWorkbook = Workbook.createWorkbook(file);  
            WritableSheet mSheet = mWorkbook.createSheet(sheetName, 0);  
//            WritableSheet mSheet2 = mWorkbook.createSheet("test", 2);  
            for(CurCell mCurCell : mArrayList){  
                Label mLabel = new Label(mCurCell.col, mCurCell.row, mCurCell.content);  
                mSheet.addCell(mLabel);  
//                publishProgress(mCurCell.content);
            }  
            int row = mArrayList.get(mArrayList.size() - 1).row;  
            int col = mArrayList.get(mArrayList.size() - 1).col;  
            jxl.write.Number number = new jxl.write.Number(col,row,55.123);  
//            mSheet2.addCell(number);  
            mWorkbook.write();  
            mWorkbook.close();  
        } catch (RowsExceededException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (WriteException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
	
	
	private class CurCell{  
        int row;  
        int col;  
        String content;  
    }  

}
