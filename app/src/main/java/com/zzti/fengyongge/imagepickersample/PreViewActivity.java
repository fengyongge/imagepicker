package com.zzti.fengyongge.imagepickersample;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zzti.fengongge.imagepickersample.R;
import com.zzti.fengyongge.imagepicker.ImagePickerInstance;
import com.zzti.fengyongge.imagepicker.PhotoPreviewActivity;
import com.zzti.fengyongge.imagepicker.model.PhotoModel;
import com.zzti.fengyongge.imagepicker.util.CommonUtils;
import com.zzti.fengyongge.imagepickersample.model.TestBean;
import com.zzti.fengyongge.imagepickersample.view.CustomImageView;
import com.zzti.fengyongge.imagepickersample.view.NineGridlayout;

import java.util.ArrayList;
import java.util.List;

public class PreViewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter myAdapter;
    List<TestBean> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_view);
        recyclerView = findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(this);
        recyclerView.setAdapter(myAdapter);
        loadMore();
    }

    public void loadMore() {
        TestBean testBean = new TestBean();
        List<String> list1 = new ArrayList<>();
        list1.add("http://gank.io/images/f0c192e3e335400db8a709a07a891b2e");
        testBean.setPic(list1);
        list.add(testBean);

        TestBean testBean1 = new TestBean();
        List<String> list2 = new ArrayList<>();
        list2.add("http://gank.io/images/f4f6d68bf30147e1bdd4ddbc6ad7c2a2");
        list2.add("http://gank.io/images/dc75cbde1d98448183e2f9514b4d1320");
        list2.add("http://gank.io/images/bdb35e4b3c0045c799cc7a494a3db3e0");
        testBean1.setPic(list2);
        list.add(testBean1);
        myAdapter.setData(list);
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerViewViewHolder> {
        private Context mContext;
        private List<TestBean> dataList = new ArrayList<>();

        public MyAdapter(Context context) {
            this.mContext = context;
        }

        public void setData(List<TestBean> dataList) {
            if (null != dataList) {
                this.dataList.clear();
                this.dataList.addAll(dataList);
                notifyDataSetChanged();
            }
        }

        @NonNull
        @Override
        public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PreViewActivity.this).inflate(R.layout.activity_pre_view_item, parent, false);
            return new RecyclerViewViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, final int position) {

            if (dataList.get(position).getPic().size() == 1) {
                holder.iv_oneimage.setVisibility(View.VISIBLE);
                holder.iv_ngrid_layout.setVisibility(View.GONE);
                holder.iv_oneimage.setImageUrl(dataList.get(position).getPic().get(0));

                holder.iv_oneimage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        List<PhotoModel> tempList = new ArrayList<PhotoModel>();
                        for (int i = 0; i < dataList.get(position).getPic().size(); i++) {
                            PhotoModel photoModel = new PhotoModel();
                            photoModel.setOriginalPath(dataList.get(position).getPic().get(i));
                            tempList.add(photoModel);
                        }
                        ImagePickerInstance.getInstance().photoPreview(PreViewActivity.this,tempList,position,true);
                    }
                });
            } else {
                holder.iv_oneimage.setVisibility(View.GONE);
                holder.iv_ngrid_layout.setVisibility(View.VISIBLE);
                final ArrayList<String> list1 = (ArrayList<String>) dataList.get(position).getPic();

                List<PhotoModel> tempList = new ArrayList<PhotoModel>();

                for (int i = 0; i < list1.size(); i++) {
                    PhotoModel photoModel = new PhotoModel();
                    photoModel.setOriginalPath(dataList.get(position).getPic().get(i));
                    tempList.add(photoModel);
                }

                holder.iv_ngrid_layout.setImagesData(tempList, PreViewActivity.this);
            }

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }


    class RecyclerViewViewHolder extends RecyclerView.ViewHolder {

        CustomImageView iv_oneimage;
        NineGridlayout iv_ngrid_layout;

        public RecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_oneimage = itemView.findViewById(R.id.iv_oneimage);
            iv_ngrid_layout = itemView.findViewById(R.id.iv_ngrid_layout);
        }
    }





}
