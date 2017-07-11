package com.zzti.fengongge.imagepickerdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import com.zzti.fengongge.imagepickerdemo.model.TestBean;
import com.zzti.fengongge.imagepickerdemo.view.CustomImageView;
import com.zzti.fengongge.imagepickerdemo.view.NineGridlayout;
import com.zzti.fengyongge.imagepicker.PhotoPreviewActivity;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;
import java.util.ArrayList;
import java.util.List;
/**
 * @author fengyongge
 * @Description
 */
public class PreViewActivity extends AppCompatActivity {

    ListView lv;
    Adapter adapter;
    List<TestBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view);
        lv = (ListView) findViewById(R.id.lv);
        adapter = new Adapter();
        lv.setAdapter(adapter);
        loadMore();
    }

    public void loadMore(){
        TestBean testBean = new TestBean();
        List<String> list1 = new ArrayList<>();
        list1.add("https://ws1.sinaimg.cn/large/610dc034ly1fgj7jho031j20u011itci.jpg");
        testBean.setPic(list1);
        list.add(testBean);

        TestBean testBean1= new TestBean();
        List<String> list2 = new ArrayList<>();
        list2.add("https://ws1.sinaimg.cn/large/610dc034ly1fgbbp94y9zj20u011idkf.jpg");
        list2.add("https://ws1.sinaimg.cn/large/d23c7564ly1fg7ow5jtl9j20pb0pb4gw.jpg");
        list2.add("https://ws1.sinaimg.cn/large/610dc034ly1fgj7jho031j20u011itci.jpg");
        testBean1.setPic(list2);
        list.add(testBean1);

        adapter.notifyDataSetChanged();
    }


    public class Adapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int postion) {
            return list.get(postion);
        }

        @Override
        public long getItemId(int postion) {
            return postion;
        }

        @Override
        public View getView(final int positon, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(PreViewActivity.this).inflate(R.layout.activity_pre_view_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.iv_oneimage  = (CustomImageView) convertView.findViewById(R.id.iv_oneimage);
            holder.iv_ngrid_layout  = (NineGridlayout)convertView. findViewById(R.id.iv_ngrid_layout);
            
            if (listIsEmpty(list.get(positon).getPic())) {
                holder.iv_ngrid_layout.setVisibility(View.GONE);
                holder.iv_oneimage.setVisibility(View.GONE);
            } else {

                if (list.get(positon).getPic().size() == 1) {

                    holder.iv_oneimage.setTag(list.get(positon).getPic());
                    holder.iv_oneimage.setVisibility(View.VISIBLE);
                    holder.iv_ngrid_layout.setVisibility(View.GONE);
                    holder.iv_oneimage.setImageUrl(list.get(positon).getPic().get(0));

                    holder.iv_oneimage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub

                            List<PhotoModel> tempList = new ArrayList<PhotoModel>();

                            for (int i = 0; i <list.get(positon).getPic().size() ; i++) {
                                PhotoModel photoModel = new PhotoModel();
                                photoModel.setOriginalPath(list.get(positon).getPic().get(i));
                                tempList.add(photoModel);
                            }

                            Bundle bundle = new Bundle();
                            bundle.putSerializable("photos",(ArrayList<PhotoModel>)tempList);
                            bundle.putInt("position", 0);
                            bundle.putBoolean("isSave",true);
                            CommonUtils.launchActivity(PreViewActivity.this, PhotoPreviewActivity.class, bundle);
                        }
                    });
                } else {
                    holder.iv_oneimage.setVisibility(View.GONE);
                    holder.iv_ngrid_layout.setVisibility(View.VISIBLE);
                    final ArrayList<String> list1 = (ArrayList<String>) list.get(positon).getPic();

                    List<PhotoModel> tempList = new ArrayList<PhotoModel>();

                    for (int i = 0; i <list1.size() ; i++) {
                        PhotoModel photoModel = new PhotoModel();
                        photoModel.setOriginalPath(list.get(positon).getPic().get(i));
                        tempList.add(photoModel);
                    }

                    holder.iv_ngrid_layout.setImagesData(tempList, PreViewActivity.this);
                }
            }
            return convertView;
        }


        class ViewHolder{
            CustomImageView iv_oneimage;
            NineGridlayout iv_ngrid_layout;
        }
    }



    private boolean listIsEmpty(List list) {
        if (list != null && list.size() > 0)
            return false;
        return true;
    }

}
