package com.example.acpro.quiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ItemModelAdapter extends BaseAdapter{

    private List<ItemModel> list;
    private LayoutInflater layoutInflater;

    public ItemModelAdapter(Context context, List<ItemModel> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View view1 = view;
        if (view1 == null){
            view1 = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
        }

        ItemModel itemModel = getItemModel(i);

        TextView textView = (TextView) view1.findViewById(R.id.textViewName);
        TextView textView1 = (TextView) view1.findViewById(R.id.textViewScore);
        textView.setText(itemModel.getName());
        textView1.setText(String.valueOf(itemModel.getScore()));

        return view1;
    }

    private ItemModel getItemModel(int pos){
        return (ItemModel) getItem(pos);
    }

    /*void add(ItemModel item){
        ItemModel itemModel = new ItemModel();

        *//*
        itemModel.setName(name);
        itemModel.setScore(score);*//*
    }*/
}
