package com.udjaya.kasirudjay.modul.printer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.udjaya.kasirudjay.R;
import com.udjaya.kasirudjay.model.Category;
import com.udjaya.kasirudjay.model.printer.Printer;

import java.util.ArrayList;
import java.util.List;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.CategoryViewHolder> {

    private List<Category> data;
    private boolean[] switchStates;

    public ListCategoryAdapter(List<Category> data){
        this.data = data;
        this.switchStates = new boolean[data.size()];
    }

    public void setData(List<Category> newData, Printer printer){
        this.data = newData;
        this.switchStates = new boolean[newData.size()];
        if(printer != null){
            List<Integer> listIdCategory = printer.getListCategory();

            // Loop setiap kategori baru dan cek apakah ID-nya ada di listIdCategory printer
            for (int i = 0; i < newData.size(); i++) {
                int categoryId = newData.get(i).getId();
                if (listIdCategory.contains(categoryId)) {
                    switchStates[i] = true;  // Set switch di index ini aktif (true)
                } else {
                    switchStates[i] = false; // Optional, default false tapi lebih eksplisit
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent, false);
        return new CategoryViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = data.get(position);
//        Log.d("onBindViewHolder", String.valueOf(switchStates.length));
        holder.txtCategory.setText(category.getName());
        if(switchStates.length > 0){
            holder.switchCategory.setChecked(switchStates[position]);
            // Update state switch saat di toggle user
            holder.switchCategory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchStates[position] = isChecked;
            });
        }else{
            // Update state switch saat di toggle user
            holder.switchCategory.setOnCheckedChangeListener((buttonView, isChecked) -> {
                switchStates[position] = isChecked;
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    // Mengambil list id kategori yang aktif (switch on)
    public List<Integer> getSelectedCategoryIds() {
        List<Integer> selectedIds = new ArrayList<>();
        for (int i = 0; i < switchStates.length; i++) {
            if (switchStates[i]) {
                selectedIds.add(data.get(i).getId());
            }
        }
        return selectedIds;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory;
        SwitchCompat switchCategory;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txt_category);
            switchCategory = itemView.findViewById(R.id.switch_category);
        }
    }
}
