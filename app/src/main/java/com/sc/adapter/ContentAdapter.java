package com.sc.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.orhanobut.logger.Logger;
import com.sc.testdemo.R;
import com.sc.utils.DBUtils;
import com.sc.utils.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suchun on 2017/10/17.
 */
public class ContentAdapter extends BaseAdapter{
    Context con;
    private List<Map<String, Object>> mdata;
    public Map<String, Object> etValue  = new HashMap<>();
    public List<Map<String, Object>> list;
    private LayoutInflater inflater;
    int type;
    DBUtils dbUtils = new DBUtils();

    public ContentAdapter(Context context, List<Map<String,Object>> list){
        this.con = context;
        this.list =list;
        inflater = (LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getItemViewType(int position){
      Map<String, Object> map = list.get(position);
        return Integer.parseInt(map.get("type").toString());
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        if(position >= getCount()){
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         final ViewHolder viewHolder ;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_name, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Map<String, Object> map = list.get(position);
        type = getItemViewType(position);
        switch (type){
            case 1:
                viewHolder.tv.setText("操作工");
                viewHolder.et.setText(map.get("text").toString());
               // data.add(position, map.get("text").toString());
                break;
            case 2:
                viewHolder.tv.setText(map.get("text").toString());
                viewHolder.et.setTag(position);

                try {
                    //viewHolder.et.addTextChangedListener(null);
                    viewHolder.et.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            int position = (Integer) viewHolder.et.getTag();
                            if (s != null && !"".equals(s.toString())) {
                                //int position = (Integer) viewHolder.et.getTag();
                                list.get(position).put("value", s.toString());
                                //etValue.put("inputvalue", s.toString());// 当EditText数据发生改变的时候存到data变量中
                                Logger.i(s.toString());
                                //load();
                                //data.add(position,s.toString());
                                String staff = funResult(position);
                                //Logger.i(staff);
                                System.out.println("员工姓名=========="+staff);
                                String partloc = ((String) map.get("text"));
                                Logger.i(partloc);
                                dbUtils.updateCount(staff, partloc, s.toString());
                            }
                        }
                    });
                    Object value = list.get(position).get("value");
                    if(value!=null && !"".equals(value)){
                        viewHolder.et.setText(value.toString());
                    }else {
                        viewHolder.et.setText("");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
        }
        return convertView;
    }
    public String funResult(int position){
        String result = null;
        int pos=position;
        for( pos=position; pos>=0; pos-- ){
            if(1 == getItemViewType(pos)){
                Map<String, Object> map = this.getItem(pos);
                result = ((String) map.get("text"));
                break;
            }
        }
        return result;
    }
    /* public void load() {
        int k;
       data = new ArrayList<>();
        //count = new ArrayList<>();
        if(list.get(getCount()-1).get("value")!=null && !list.get(getCount()-1).get("value").equals("")){
            for (int i = 0; i < list.size(); i++) {
                int type = ((int) list.get(i).get("type"));
                if (type == 1) {
                    data.add(((String) list.get(i).get("text")));
                } else if (type == 2) {
                    String value = ((String) list.get(i).get("value"));
                    if(value!=null && !value.equals("")){
                        data.add(value);
                    }else {
                        break;
                    }
                }
            }
        }}
        if(data.size() == list.size()) {
            for (int j = 0; j < data.size(); j++) {
                for (k = j; k < j + 4; k++) {
                    count = new ArrayList<String>();
                    String str = data.get(k);
                    count.add(str);
                }
                dbUtils.insertName(count.get(0), count.get(1), count.get(2), count.get(3));
                j = k;
            }
        }
    }*/
}
