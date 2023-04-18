package com.example.sino.utils;

import static com.example.sino.utils.common.Constant.MAX_ATTACH_FILE_PACKET_SIZE;

import com.example.sino.db.entity.AttachFile;
import com.example.sino.db.entity.AttachFileCopy;
import com.example.sino.model.complaintreport.ComplaintReport;
import com.example.sino.model.db.ChatMessage;
import com.example.sino.model.db.TmpChatMessage;
import com.example.sino.utils.common.Util;
import com.example.sino.viewmodel.DatabaseViewModel;

import org.json.JSONArray;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GsonGenerator {

    public static String mobileNoConfirmationToGson(String mobileNo) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("mobileNo", mobileNo));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String getLastDocumentVersionToGson() {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String sendActivationCodeToGson(String mobileNo, String activationCode) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("mobileNo", mobileNo));
        wsParameters.add(new Util.WSParameter("activationCode", activationCode));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String getUserPermissionList(String username, String tokenPass) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String getDailyEventList(String username, String tokenPass,String companyCode) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("companyCode", companyCode));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String getDriverByParam(String username, String tokenPass, String driverCode) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("driverCode", driverCode));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String getUserInfoById(String username, String tokenPass, Long userId) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("id", userId));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String chatMessageDeliveredReport(String username, String tokenPass, List<Long> messageIdList) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("messageIdList", messageIdList));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String getUserSurveyFormList(String username, String tokenPass, int formType) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("formType", formType));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String getPersonByNationalCode(String username, String tokenPass, int personType,String nationalCode) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("nationalCode", nationalCode));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String upsertPersonByParam(String username, String tokenPass,String nationalCode,String name,String family,String mobile,String phone,String address,int type) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("nationalCode", nationalCode));
        wsParameters.add(new Util.WSParameter("name", name));
        wsParameters.add(new Util.WSParameter("family", family));
        wsParameters.add(new Util.WSParameter("telNo", phone));
        wsParameters.add(new Util.WSParameter("mobileNo", mobile));
        wsParameters.add(new Util.WSParameter("address", address));
        wsParameters.add(new Util.WSParameter("personTypeEn", type));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String getCarInfo(String username, String tokenPass, String plateText, String chassisNumber, int selectedType,int companyCode) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("companyCode", companyCode));

        if (selectedType == 1){
            wsParameters.add(new Util.WSParameter("chassisNumber", chassisNumber));
        }else if (selectedType == 2){
            wsParameters.add(new Util.WSParameter("plateText", plateText));
        }else if (selectedType == 3){
            wsParameters.add(new Util.WSParameter("plateNo", plateText));
        }

        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String getCompanyInfo(String username, String tokenPass, String companyCode) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("companyCode", companyCode));
        //json = URLEncoder.encode(json);
        return Util.createJson(wsParameters, null);
    }

    public static String sendComplaintReport(String username, String tokenPass, ComplaintReport complaintReport) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, complaintReport);
    }

    public static String saveProModel(String username, String tokenPass,int carId,int companyCode,String kmCar,String fuel,int serviceType,String x,String y) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("carId", carId));
        wsParameters.add(new Util.WSParameter("companyCode", companyCode));
        wsParameters.add(new Util.WSParameter("serviceType", serviceType));
        wsParameters.add(new Util.WSParameter("kmCar", kmCar));
        wsParameters.add(new Util.WSParameter("fuel", fuel));
        wsParameters.add(new Util.WSParameter("xLatitude", x));
        wsParameters.add(new Util.WSParameter("yLongitude", y));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }
    public static String editProModel(String username, String tokenPass,int id,String customerMessageStr,String expertMessageStr,String xLatitude,String yLongitude) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("id", id));
        wsParameters.add(new Util.WSParameter("customerMessageStr", customerMessageStr));
        wsParameters.add(new Util.WSParameter("expertMessageStr", expertMessageStr));
        wsParameters.add(new Util.WSParameter("xLatitude", xLatitude));
        wsParameters.add(new Util.WSParameter("yLongitude", yLongitude));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String getProSrvList(String username, String tokenPass,int companyCode) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
       // wsParameters.add(new Util.WSParameter("processName", processName));
        wsParameters.add(new Util.WSParameter("companyCode", companyCode));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String getProSrvById(String username, String tokenPass,long id) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("id", id));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String getProSrvByCarId(String username, String tokenPass,long id) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("id", id));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String getAttachFileSettingItemList(String username, String tokenPass,String ProcessStrucSettingId) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("ProcessStrucSettingId", ProcessStrucSettingId));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String getProSrvAttachFileList(String username, String tokenPass,String processBisDataVOId,String attachFileSettingId) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        if (processBisDataVOId == null){
            return null;
        }
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("id", processBisDataVOId));
        wsParameters.add(new Util.WSParameter("attachFileSettingId", attachFileSettingId));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String deleteAttachFile(String username, String tokenPass,String id) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("id", id));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String saveDailyEvent(String username, String tokenPass,int eventTypeEn,
                                        int carTypeEn,int typePrcEn,int typeGetCarEn,int ObjActionEn,
                                        int carId,int dailEvn_id,String plateNo,String description,int parentId,String companyCode) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("companyCode", companyCode));
        wsParameters.add(new Util.WSParameter("eventTypeEn", eventTypeEn));
        wsParameters.add(new Util.WSParameter("typePrcEn", typePrcEn));
        wsParameters.add(new Util.WSParameter("objActionEn", ObjActionEn));
        if (carTypeEn != -1){
            wsParameters.add(new Util.WSParameter("carTypeEn", carTypeEn));
        }
        if (typeGetCarEn != -1){
            wsParameters.add(new Util.WSParameter("typeGetCarEn", typeGetCarEn));
        }
        if (carId != 0){
            wsParameters.add(new Util.WSParameter("carId", carId));
        }
        if (parentId != 0){
            wsParameters.add(new Util.WSParameter("parentId", parentId));
        }
        if (dailEvn_id != 0){
            wsParameters.add(new Util.WSParameter("id", dailEvn_id));
        }

        wsParameters.add(new Util.WSParameter("plateNo", plateNo));
        wsParameters.add(new Util.WSParameter("advertDescription", description));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String saveDailyEventCopy(String username, String tokenPass,int id,String companyCode,int ObjActionEn) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("id", id));
        wsParameters.add(new Util.WSParameter("companyCode", companyCode));
        wsParameters.add(new Util.WSParameter("objActionEn", ObjActionEn));
        return Util.createJsonReport(wsParameters, null);
    }

    public static String saveOrEditPrcData(String username, String tokenPass,String settingId,String entityNameEn,String entityId,String prcData_desc,String prcData_id) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("ProcessStrucSettingId", settingId));
        wsParameters.add(new Util.WSParameter("entityNameEn", entityNameEn));
        wsParameters.add(new Util.WSParameter("entityId", entityId));
        wsParameters.add(new Util.WSParameter("advertDescription", prcData_desc));
        wsParameters.add(new Util.WSParameter("id", prcData_id));

        System.out.println("=====prcDataId====="  + prcData_id);
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String confirmPrcData(String username, String tokenPass,String prcData_desc,String prcData_id,boolean confirm) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("advertDescription", prcData_desc));
        wsParameters.add(new Util.WSParameter("id", prcData_id));
        wsParameters.add(new Util.WSParameter("confirm", confirm));

        System.out.println("=====prcDataId====="  + prcData_id);
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String saveOrEditPrcDataIsEdit(String username, String tokenPass,String prcData_id,String settingId,String entityNameEn,String entityId,String prcData_desc) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("id", prcData_id));
        wsParameters.add(new Util.WSParameter("ProcessStrucSettingId", settingId));
        wsParameters.add(new Util.WSParameter("entityNameEn", entityNameEn));
        wsParameters.add(new Util.WSParameter("entityId", entityId));
        wsParameters.add(new Util.WSParameter("advertDescription", prcData_desc));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String getProSrvByCarId(String username, String tokenPass,int id) {
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));
        wsParameters.add(new Util.WSParameter("id", id));
        //json = URLEncoder.encode(json);
        return Util.createJsonReport(wsParameters, null);
    }

    public static String sendComplaintAttachment(String username, String tokenPass, AttachFile attachFile) throws Exception {
        AttachFileCopy attachFileCopy = new AttachFileCopy();
        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        ArrayList<Util.WSParameter> wsParametersAttachment = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));

        attachFileCopy.setId(attachFile.getId());
        attachFileCopy.setServerId(attachFile.getServerAttachFileId());
        attachFileCopy.setEntityNameEn(attachFile.getEntityNameEn());
        attachFileCopy.setEntityId(attachFile.getServerEntityId());
        attachFileCopy.setAttachFileSettingId(attachFile.getServerAttachFileSettingId());
        attachFileCopy.setAttachFileUserFileName(attachFile.getAttachFileUserFileName());

        File attachedFile = new File(attachFile.getAttachFileLocalPath());
        FileInputStream inputStream = new FileInputStream(attachedFile);
        byte[] fileBytes = new byte[MAX_ATTACH_FILE_PACKET_SIZE];
        int res = inputStream.read(fileBytes);
        byte[] fixedFileBytes = Arrays.copyOf(fileBytes, res);
        JSONArray attachmentByteJsonArray = new JSONArray();
        for (int i = 0; i < fixedFileBytes.length; i++) {
            byte fileByte = fixedFileBytes[i];
            attachmentByteJsonArray.put(fileByte);
        }

        attachFileCopy.setAttachmentBytes(attachmentByteJsonArray);
        attachFileCopy.setAttachmentChecksum(ImageUtil.getMD5Checksum(attachFile.getAttachFileLocalPath()));

        //json = URLEncoder.encode(json);
        return Util.createJsonReportAttachment(wsParameters, attachFileCopy);
    }

    public static String saveChatMessage(String username, String tokenPass, Long id, DatabaseViewModel viewModel) {

        ChatMessage chatMessage = viewModel.getChatMessagesByIdVM(id);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        TmpChatMessage tmpChatMessage = new TmpChatMessage();


        ArrayList<Util.WSParameter> wsParameters = new ArrayList<>();
        wsParameters.add(new Util.WSParameter("username", username));
        wsParameters.add(new Util.WSParameter("tokenPass", tokenPass));


        System.out.println("id==" + id);
        System.out.println("saveChatMessage==" + chatMessage.getId());
        System.out.println("saveChatMessage==" + chatMessage.getSenderUserId());
        System.out.println("saveChatMessage==" + chatMessage.getMessage());
        System.out.println("saveChatMessage==" + chatMessage.getStatus());
        System.out.println("saveChatMessage==" + chatMessage.getSendDate());

        tmpChatMessage.setId(chatMessage.getId());
        tmpChatMessage.setSenderUserId(chatMessage.getSenderUserId());
        tmpChatMessage.setMessage(chatMessage.getMessage());
        tmpChatMessage.setAttachFileUserFileName(chatMessage.getAttachFileUserFileName());

        if (chatMessage.getAttachFileLocalPath() != null) {
            File attachedFile = new File(chatMessage.getAttachFileLocalPath());
            if (attachedFile.exists()) {
                try {
                    FileInputStream inputStream = new FileInputStream(attachedFile);
                    byte[] fileBytes = new byte[MAX_ATTACH_FILE_PACKET_SIZE];

                    int res = inputStream.read(fileBytes);
                    byte[] fixedFileBytes = Arrays.copyOf(fileBytes, res);

                    JSONArray attachmentByteJsonArray = new JSONArray();
                    for (int i = 0; i < fixedFileBytes.length; i++) {
                        byte fileByte = fixedFileBytes[i];
                        attachmentByteJsonArray.put(fileByte);
                    }

                    //chatMessageParameters.add(new Util.WSParameter("attachmentBytes", attachmentByteJsonArray));
                    //chatMessageParameters.add(new Util.WSParameter("attachmentChecksum", ImageUtil.getMD5Checksum(chatMessage.getAttachFileLocalPath())));

                    tmpChatMessage.setAttachmentBytes(attachmentByteJsonArray);
                    tmpChatMessage.setAttachmentChecksum(ImageUtil.getMD5Checksum(chatMessage.getAttachFileLocalPath()));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (chatMessage.getValidUntilDate() != null) {
            // chatMessageParameters.add(new Util.WSParameter("validUntilDate", simpleDateFormat.format(chatMessage.getValidUntilDate())));
            tmpChatMessage.setValidUntilDate(simpleDateFormat.format(chatMessage.getValidUntilDate()));
        }

        // chatMessageParameters.add(new Util.WSParameter("chatGroupId", chatMessage.getChatGroupId()));
        //chatMessageParameters.add(new Util.WSParameter("isCreateNewPvChatGroup", chatMessage.getCreateNewPvChatGroup()));

        tmpChatMessage.setChatGroupId(chatMessage.getChatGroupId());
        tmpChatMessage.setCreateNewPvChatGroup(chatMessage.getCreateNewPvChatGroup());

        return Util.createJson(wsParameters, tmpChatMessage);
    }
}
