package com.example.sino.utils.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.sino.SinoApplication;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.enumtype.SendingStatusEn;
import com.example.sino.model.db.User;
import com.example.sino.utils.ImageUtil;
import com.example.sino.utils.common.Constant;
import com.example.sino.viewmodel.DatabaseViewModel;
import com.google.android.gms.common.util.IOUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ApiServiceAsync {

    public static Integer MAX_ATTACH_FILE_PACKET_SIZE = 8192;
    private int counter = 1;


    public void resumeAttachFileList(User user, Context context, List<AttachFile> attachFileList, DatabaseViewModel databaseViewModel) {
       /* counter = 1;
        if (!attachFileList.isEmpty()) {
            System.out.println("attachFileList====" + attachFileList.size());
            for (AttachFile attachFile : attachFileList) {
                resumeAttachFile(user, context,attachFile, databaseViewModel);
               // counter++;
            }
        }*/
    }

    public void resumeAttachFile(User user, Context context,AttachFile attachFile, DatabaseViewModel databaseViewModel) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", user.getUsername());
            jsonObject.put("tokenPass", user.getBisPassword());
            JSONObject attachFileJsonObject = new JSONObject();

            attachFileJsonObject.put("id", attachFile.getId());
            attachFileJsonObject.put("serverId", attachFile.getServerAttachFileId());
            attachFileJsonObject.put("entityNameEn", attachFile.getEntityNameEn());
            attachFileJsonObject.put("entityId", attachFile.getServerEntityId());
            attachFileJsonObject.put("attachFileSettingId", attachFile.getServerAttachFileSettingId());
            attachFileJsonObject.put("attachFileUserFileName", attachFile.getAttachFileUserFileName());

            if (attachFile.getAttachFileLocalPath() != null) {
                File attachedFile = new File(attachFile.getAttachFileLocalPath());
                if (attachedFile.exists()) {

                    if (attachFile.getAttachFileSentSize() == null) {
                        attachFile.setAttachFileSentSize(0);
                    }
                    FileInputStream inputStream = new FileInputStream(attachedFile);
                    Integer fileSize = inputStream.available();
                    attachFile.setAttachFileSize(fileSize);
                    byte[] fileBytes = new byte[MAX_ATTACH_FILE_PACKET_SIZE];
                    byte[] buffer = new byte[(int) attachedFile.length()];

                    //int res;
                    byte[] fixedFileBytes;
                    int total = attachFile.getAttachFileSize();
                    int start = attachFile.getAttachFileSentSize();
                    int mines = total - start;
                    if (mines < 0){
                        System.out.println("====total====" + total);
                        System.out.println("====start====" + start);
                        System.out.println("====mines====" + mines);
                        attachFile.setAttachFileSize(attachFile.getAttachFileSentSize());
                        attachFile.setSendingStatusEn(SendingStatusEn.Sent.ordinal());
                        return;
                    }
                    System.out.println("mines: " + mines);

                    buffer = IOUtils.toByteArray(inputStream);
                    if (mines >= 8192) {
                        fixedFileBytes = Arrays.copyOfRange(buffer, attachFile.getAttachFileSentSize(), attachFile.getAttachFileSentSize() + 8192);
                    } else {
                        fixedFileBytes = Arrays.copyOfRange(buffer, attachFile.getAttachFileSentSize(), attachFile.getAttachFileSentSize() + mines);
                    }

                    JSONArray attachmentByteJsonArray = new JSONArray();
                    System.out.println("resume: ");
                    for (int i = 0; i < fixedFileBytes.length; i++) {
                        byte fileByte = fixedFileBytes[i];
                        System.out.print(fileByte);
                        attachmentByteJsonArray.put(fileByte);
                    }

                    attachFileJsonObject.put("attachmentBytes", attachmentByteJsonArray);
                    attachFileJsonObject.put("attachmentChecksum", ImageUtil.getMD5Checksum(attachFile.getAttachFileLocalPath()));

                    System.out.println("attachmentBytes=====" + attachmentByteJsonArray);
                    System.out.println("attachmentChecksum=====" + ImageUtil.getMD5Checksum(attachFile.getAttachFileLocalPath()));

                    jsonObject.put("attachFile", attachFileJsonObject);
                    MyPostJsonService postJsonService = new MyPostJsonService(SinoApplication.getInstance().getApplicationContext());
                    String result = postJsonService.sendData("saveEntityAttachFileResumable", jsonObject, true);
                    if (result != null) {
                        JSONObject resultJson = new JSONObject(result);
                        if (!resultJson.isNull(Constant.SUCCESS_KEY)) {
                            if (!resultJson.isNull(Constant.RESULT_KEY)) {
                                JSONObject resultJsonObject = resultJson.getJSONObject(Constant.RESULT_KEY);
                                if (!resultJsonObject.isNull("savedAttachFile")) {
                                    JSONObject savedAttachFileJsonObject = resultJsonObject.getJSONObject("savedAttachFile");
                                    if (!savedAttachFileJsonObject.isNull("id")) {
                                        attachFile.setServerAttachFileId(savedAttachFileJsonObject.getLong("id"));
                                    }

                                    if (!savedAttachFileJsonObject.isNull("totalReceivedBytes")) {
                                        attachFile.setAttachFileSentSize(savedAttachFileJsonObject.getInt("totalReceivedBytes"));
                                    }
                                }

                                System.out.println("attachFileSize-----" + attachFile.getAttachFileSize());
                                System.out.println("attachFileSentSize-----" + attachFile.getAttachFileSentSize());

                                if (attachFile.getAttachFileSize() != null && attachFile.getAttachFileSentSize() != null && attachFile.getAttachFileSentSize().compareTo(attachFile.getAttachFileSize()) > 0) {
                                    attachFile.setSendingStatusEn(SendingStatusEn.Fail.ordinal());
                                    attachFile.setSendingStatusDate(new Date());
                                    databaseViewModel.updateAttachFileByParam(attachFile);
                                } else if (attachFile.getAttachFileSize() == null || attachFile.getAttachFileSentSize() == null || attachFile.getAttachFileSize().equals(attachFile.getAttachFileSentSize())) {
                                    attachFile.setSendingStatusEn(SendingStatusEn.Sent.ordinal());
                                    attachFile.setSendingStatusDate(new Date());
                                    databaseViewModel.updateAttachFileByParam(attachFile);

                                    /*if (counter == attachFileList.size()) {
                                        EventBus.getDefault().post(new EventBusModel(true, true, true));
                                    }*/

                                } else {
                                    attachFile.setSendingStatusEn(SendingStatusEn.AttachmentResuming.ordinal());
                                    attachFile.setSendingStatusDate(new Date());
                                    databaseViewModel.updateAttachFileByParam(attachFile);
                                    resumeAttachFile(user, context,attachFile, databaseViewModel);
                                }
                            }
                        } else {
                            attachFile.setSendingStatusEn(SendingStatusEn.Fail.ordinal());
                            attachFile.setSendingStatusDate(new Date());
                            databaseViewModel.updateAttachFileByParam(attachFile);
                        }
                    }

                } else {
                    attachFile.setSendingStatusEn(SendingStatusEn.Fail.ordinal());
                    attachFile.setSendingStatusDate(new Date());
                    databaseViewModel.updateAttachFileByParam(attachFile);
                }
            } else {
                attachFile.setSendingStatusEn(SendingStatusEn.Fail.ordinal());
                attachFile.setSendingStatusDate(new Date());
                databaseViewModel.updateAttachFileByParam(attachFile);
            }

        } catch (Exception e) {
            e.printStackTrace();
            String errorMsg = e.getMessage();
            if (errorMsg == null) {
                errorMsg = "AttachFileReceiver";
            }
            Log.d(errorMsg, errorMsg);
            attachFile.setSendingStatusEn(SendingStatusEn.Fail.ordinal());
            attachFile.setSendingStatusDate(new Date());
            databaseViewModel.updateAttachFileByParam(attachFile);
        }
    }
}
