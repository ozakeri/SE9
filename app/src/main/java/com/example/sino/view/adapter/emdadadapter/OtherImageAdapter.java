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
import com.example.sino.model.ImageItem;
import com.example.sino.viewmodel.DatabaseViewModel;

import java.util.List;

public class OtherImageAdapter extends RecyclerView.Adapter<OtherImageAdapter.CustomView> {


    List<ImageItem> attachFiles;
    private DatabaseViewModel databaseViewModel;
    private Bitmap bitmap;

    public OtherImageAdapter(List<ImageItem> attachFiles) {
        this.attachFiles = attachFiles;
    }


    @NonNull
    @Override
    public CustomView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EmdadAttachmentItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.emdad_attachment_item, parent, false);
        return new CustomView(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomView holder, int position) {
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

        public void bind(ImageItem attachFile) {

            if (attachFile.getPath() != null){
                Bitmap bitmap = resizeBitmap(attachFile.getPath(), 80, 80);
                binding.imgAttachment.setImageBitmap(bitmap);
            }

            if (attachFile.getAttachFileJsonArray() != null){
                byte[] bytes = new byte[0];
                bytes = new byte[attachFile.getAttachFileJsonArray().size()];
                for (int j = 0; j < attachFile.getAttachFileJsonArray().size(); j++) {
                    bytes[j] = attachFile.getAttachFileJsonArray().get(j).byteValue();
                }
                bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                binding.imgAttachment.setImageBitmap(bitmap);
            }

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
