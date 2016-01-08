package cn.oschina.net;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 14-3-6.
 */
public class TitleAdapter extends BaseAdapter {

    private List<Map<String, Object>> result ;
    private Context context;


    public TitleAdapter(Context context,List<Map<String, Object>> result) {
        this.result = result;
        this.context = context;
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int i) {
        return result.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

         view= LayoutInflater.from(context).inflate(R.layout.bbslist, null);
        TextView textView= (TextView) view.findViewById(R.id.title);
        textView.setText(result.get(i).get("title").toString());


        return view;
    }
}
