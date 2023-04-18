package com.example.sino.view.adapter.emdadadapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sino.R;
import com.example.sino.databinding.EmdadAttachmentItemBinding;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.viewmodel.DatabaseViewModel;

import java.util.List;

public class EmdadAttachFileAdapter extends RecyclerView.Adapter<EmdadAttachFileAdapter.CustomView> {


    List<AttachFile> attachFiles;
    private DatabaseViewModel databaseViewModel;

    public EmdadAttachFileAdapter(List<AttachFile> attachFiles, DatabaseViewModel databaseViewModel) {
        this.attachFiles = attachFiles;
        this.databaseViewModel = databaseViewModel;
    }

    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EmdadAttachmentItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.emdad_attachment_item, parent, false);
        return new CustomView(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
        System.out.println("onBindViewHolder====" + attachFiles.size());
        System.out.println(attachFiles.get(position).getAttachFileLocalPath());
        holder.bind(attachFiles.get(position));
    }

    @Override
    public int getItemCount() {
        return attachFiles.size();
    }

    class CustomView extends RecyclerView.ViewHolder {
        EmdadAttachmentItemBinding binding;

        public CustomView(EmdadAttachmentItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(AttachFile attachFile) {
            Bitmap bitmap = resizeBitmap(attachFile.getAttachFileLocalPath(), 120, 120);
            binding.imgAttachment.setImageBitmap(bitmap);
            binding.imgAttachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    databaseViewModel.deleteReportAttachFileById(attachFile);
                    notifyDataSetChanged();
                }
            });
        }
    }

    private Bitmap resizeBitmap(String photoPath, int targetW, int targetH) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((targetW > 0) || (targetH > 0)) {
            scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        }

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true; //Deprecated API 21

        return BitmapFactory.decodeFile(photoPath, bmOptions);
    }

}
