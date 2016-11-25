package com.example.admin.listview_example_new;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    protected ListView listView;
    private ArrayList<MyModel> myModels;

    class MyModel {
        public String title;
        public boolean state;
        private long id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = ListView.class.cast(findViewById(R.id.listView));

        myModels = new ArrayList<>();
        for (int i = 0; i < 80; i++) {
            MyModel nModel = new MyModel();
            nModel.title = "Item number " + i;
            nModel.id = i;
            myModels.add(nModel);
        }

        TwoAdapter adapter = new TwoAdapter(myModels);
        listView.setAdapter(adapter);
    }

    class TwoAdapter extends BaseAdapter implements View.OnLongClickListener{
        private ViewHolder viewHolder;
        private ArrayList<MyModel> myModels;
        private SparseArrayCompat<ViewHolder> sparseArrayCompat;

        public TwoAdapter(ArrayList<MyModel> myModels) {
            this.myModels = myModels;
            sparseArrayCompat = new SparseArrayCompat<>(); //коллекция Map
        }

        @Override
        public int getCount() {
            return myModels.size();
        }

        @Override
        public Object getItem(int i) {
            return myModels.get(i);
        }

        @Override
        public long getItemId(int i) {
            return myModels.get(i).id;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if (view == null){
                LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item, viewGroup, false);
                viewHolder = new ViewHolder(view);
                sparseArrayCompat.put(view.hashCode(), viewHolder);
//                view.setTag(viewHolder);
            } else {
//               viewHolder = (ViewHolder) view.getTag();
                viewHolder = sparseArrayCompat.get(view.hashCode());
            }
            viewHolder.placeIt(myModels.get(i), this);
            Log.d("V", "Size: " + sparseArrayCompat.size());
            return view;
        }



        @Override
        public boolean onLongClick(View view) {
            notifyDataSetChanged();
            return true;
        }
    }

    static class ViewHolder implements View.OnLongClickListener{
        private static final String TAG = "ViewHolder";
        public LinearLayout llContainer;
        public TextView tvTitle;
        public TextView tvId;
        private View.OnLongClickListener onLongClickListener;

        MyModel mModel;

        public ViewHolder(View v) {
            tvId = (TextView) v.findViewById(R.id.tvId);
            tvTitle = (TextView) v.findViewById(R.id.tvTitle);
            llContainer = (LinearLayout) v.findViewById(R.id.llContainer);
        }

        public void placeIt(MyModel mModel, View.OnLongClickListener onLongClickListener){
            tvId.setText("" + mModel.id);
            tvTitle.setText(mModel.title);
            this.mModel = mModel;
            llContainer.setOnLongClickListener(this);
            this.onLongClickListener = onLongClickListener;
            if (mModel.state) {
                llContainer.setBackgroundResource(R.color.colorPrimaryDark);
                tvTitle.setTextColor(Color.WHITE);
                tvId.setTextColor(Color.WHITE);
            } else {
                llContainer.setBackgroundResource(android.R.color.transparent);
                tvTitle.setTextColor(Color.BLACK);
                tvId.setTextColor(Color.BLACK);
            }
        }

        @Override
        public boolean onLongClick(View view) {
            Log.d(TAG, "onLongClick: " + mModel.id);
            mModel.state = !mModel.state;
            if (onLongClickListener!=null) {
                onLongClickListener.onLongClick(view);
            }
            return true;
        }
    }
}