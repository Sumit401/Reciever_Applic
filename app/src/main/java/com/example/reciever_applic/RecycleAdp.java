package com.example.reciever_applic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecycleAdp extends RecyclerView.Adapter<RecycleAdp.MyViewHolder> {

    private ArrayList<String> Title;
    private ArrayList<String> Dataurl;
    private ArrayList<String> Dataid;
    RecycleAdp(Context context, ArrayList<String> titleArray, ArrayList<String> urlArray, ArrayList<String> idArray) {
        Title=titleArray;
        Dataid=idArray;
        Dataurl=urlArray;
    }

    @NonNull
    @Override
    public RecycleAdp.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cusomrec,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecycleAdp.MyViewHolder myViewHolder, int i) {
        myViewHolder.textView.setText(Title.get(i));
        myViewHolder.tv1.setText(Dataid.get(i));
        myViewHolder.tv2.setText(Dataurl.get(i));

       /* myViewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return Title.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView,tv1,tv2;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.titledata);
            tv1=itemView.findViewById(R.id.dataid);
            tv2=itemView.findViewById(R.id.dataurl);
            linearLayout=itemView.findViewById(R.id.ll);
        }
    }
}
