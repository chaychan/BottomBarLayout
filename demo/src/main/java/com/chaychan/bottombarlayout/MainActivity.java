package com.chaychan.bottombarlayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

public class MainActivity extends AppCompatActivity {

    private DemoBean[] mDatas= {
            new DemoBean("UseWithViewPager2", ViewPager2DemoActivity.class),
            new DemoBean("UseWithViewPager", ViewPagerDemoActivity.class),
            new DemoBean("UseWithoutViewPager",FragmentManagerActivity.class),
            new DemoBean("DynamicAddItem",DynamicAddItemActivity.class),
            new DemoBean("UseLottieDemo",LottieDemoActivity.class),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setHasFixedSize(true);
        rvList.setAdapter(new MyRvAdapter());
        rvList.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    private class MyRvAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setLayoutParams(params);
            textView.setPadding(30,30,30,30);
            textView.setTextSize(18);
            textView.setGravity(Gravity.CENTER);
            textView.setBackground(getResources().getDrawable(R.drawable.selector_bg));
            return new MyViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final DemoBean data = mDatas[position];
            ((TextView)holder.itemView).setText(data.name);
            holder.itemView.setOnClickListener(v -> v.getContext().startActivity(new Intent(v.getContext(), data.clazz)));
        }

        @Override
        public int getItemCount() {
            return mDatas.length;
        }
    }

    private class MyViewHolder extends ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

