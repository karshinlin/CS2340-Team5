package edu.gatech.teamraid.ratastic.Model;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedHashMap;

/**
 * Created by karshinlin on 10/24/17.
 */

public class LinkedHashMapAdapter<T> extends BaseAdapter {
    private LinkedHashMap<String, RatSighting> mData = new LinkedHashMap<String, RatSighting>();
    private String[] mKeys;
    public LinkedHashMapAdapter(LinkedHashMap<String, RatSighting> data){
        mData  = data;
        mKeys = mData.keySet().toArray(new String[data.size()]);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(mKeys[position]);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        String key = mKeys[pos];
        String Value = getItem(pos).toString();

        //do your view stuff here
        return convertView;
    }
}
