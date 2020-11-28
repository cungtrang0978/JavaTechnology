package com.example.tikicloneapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tikicloneapp.R;

import java.util.List;

public class AddressDialogAdapter extends ArrayAdapter<String> {
    final Context context;
    final int resource;
    final List<String> addressArrayList;


    public AddressDialogAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.addressArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new ViewHolder();

            viewHolder.tvAddress = convertView.findViewById(R.id.textView_address);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String address = addressArrayList.get(position);
        viewHolder.tvAddress.setText(address);

        return convertView;
    }

    public static class ViewHolder{
        private TextView tvAddress;
    }

}
