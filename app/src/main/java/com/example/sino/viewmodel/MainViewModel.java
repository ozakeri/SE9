package com.example.sino.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sino.api.NetworkRepository;
import com.example.sino.db.entity.form.FormQuestionGroup;
import com.example.sino.db.entity.form.SurveyForm;
import com.example.sino.db.entity.form.SurveyFormQuestion;
import com.example.sino.db.entity.form.SurveyFormQuestionTemp;
import com.example.sino.model.PersonRequentBean;
import com.example.sino.model.SuccessChatReceiveBean;
import com.example.sino.model.SuccessPermissionBean;
import com.example.sino.model.carinfo.SuccessCarInfoBean;
import com.example.sino.model.carinfo.VoiceRecordItem;
import com.example.sino.model.chatgroup.SuccessChatGroupBean;
import com.example.sino.model.chatgroupmember.SuccessChatGroupMemberBean;
import com.example.sino.model.dailyEvent.DailyEventRespons;
import com.example.sino.model.db.AppUser;
import com.example.sino.model.db.User;
import com.example.sino.model.db.UserPermission;
import com.example.sino.model.documentversion.DocumentVersion;
import com.example.sino.model.driverlicence.DriverLicenceRequentBean;
import com.example.sino.model.form.FormRequentBean;
import com.example.sino.model.neshan.NeshanRequentBean;
import com.example.sino.model.reception.ProServiceResponse;
import com.example.sino.model.userInfobyid.SuccessUserInfoByIdBean;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;


@HiltViewModel
public class MainViewModel extends AndroidViewModel {

    @Inject
    NetworkRepository repository;

    @Inject
    public MainViewModel(@NonNull Application application) {
        super(application);
    }


    MutableLiveData<SuccessPermissionBean> userPermissionMutableLiveData = new MutableLiveData<>();
    MutableLiveData<SuccessCarInfoBean> carInfoMutableLiveData = new MutableLiveData<>();
    MutableLiveData<VoiceRecordItem> voiceRecordItemMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<SuccessPermissionBean> getUserPermissionListResult() {
        return userPermissionMutableLiveData;
    }

    public Observable<List<User>> getAllUser() {
        return repository.getAllUser();
    }

    public List<UserPermission> getUserPermission(Long userId) {
        return repository.getUserPermissionList(userId);
    }

    public Flowable<List<UserPermission>> getUserPermissionCopy() {
        return repository.getUserPermissionListCopy();
    }

    public AppUser getAppUserByIdVM(Long id) {
        return repository.getAppUserByIdRepo(id);
    }

    public SurveyForm getSurveyFormByIdVM(Long id) {
        return repository.getSurveyFormByIdRepo(id);
    }

    public List<SurveyFormQuestion> getSurveyFormQuestionListByFormIdVM(Long id) {
        return repository.getSurveyFormQuestionListByFormIdRepo(id);
    }

    public SurveyFormQuestion getSurveyFormQuestionByIdVM(Long id) {
        return repository.getSurveyFormQuestionByIdRepo(id);
    }

    public SurveyFormQuestionTemp getSurveyFormQuestionTempByIdVM(Long id) {
        return repository.getSurveyFormQuestionTempByIdRepo(id);
    }

    public FormQuestionGroup getCheckListFormQuestionGroupByIdVM(Long id) {
        return repository.getCheckListFormQuestionGroupByIdRepo(id);
    }

    public void insertUser(User user) {
        repository.insertUser(user);
    }

    public void insertPermission(UserPermission permission) {
        repository.insertPermission(permission);
    }

    public void deleteUserPermission(Long userId, String permissionName) {
        repository.deletePermission(userId, permissionName);
    }

    public void deletePermissionByUserId(Long userId) {
        repository.deletePermissionByUserId(userId);
    }

    public Observable<SuccessPermissionBean> getUserPermissionListVM(String INPUT_PARAM) {
        return repository.getUserPermissionListRepo(INPUT_PARAM);
    }

    public Observable<DocumentVersion> getLastDocumentVersionVM(String INPUT_PARAM) {
        return repository.getLastDocumentVersionRepo(INPUT_PARAM);
    }

    public Observable<SuccessChatReceiveBean> getUserChatMessageListVM(String INPUT_PARAM) {
        return repository.getUserChatMessageListRepo(INPUT_PARAM);
    }

    public Observable<SuccessChatReceiveBean> chatMessageDeliveredReportVM(String INPUT_PARAM) {
        return repository.chatMessageDeliveredReportRepo(INPUT_PARAM);
    }

    public Observable<SuccessChatGroupBean> getUserChatGroupList(String INPUT_PARAM) {
        return repository.getUserChatGroupListRepo(INPUT_PARAM);
    }

    public Observable<SuccessChatGroupMemberBean> getChatGroupMemberList(String INPUT_PARAM) {
        return repository.getUserChatGroupMemberListRepo(INPUT_PARAM);
    }

    public Observable<SuccessUserInfoByIdBean> getUserInfoById(String INPUT_PARAM) {
        return repository.getUserInfoByIdRepo(INPUT_PARAM);
    }

    public Observable<SuccessCarInfoBean> getCompanyInfo(String INPUT_PARAM) {
        return repository.getCompanyInfoRepo(INPUT_PARAM);
    }

    public Observable<SuccessCarInfoBean> getCarInfo_ProSrv(String INPUT_PARAM) {
        return repository.getCarInfo_ProSrv(INPUT_PARAM);
    }

    public Observable<PersonRequentBean> getPersonByNationalCodeRepo(String INPUT_PARAM) {
        return repository.getPersonByNationalCodeRepo(INPUT_PARAM);
    }

    public Observable<PersonRequentBean> upsertPersonByParam(String INPUT_PARAM) {
        return repository.upsertPersonByParamRepo(INPUT_PARAM);
    }

    public Observable<SuccessUserInfoByIdBean> saveChatMessageRV(String INPUT_PARAM) {
        return repository.saveChatMessageRepo(INPUT_PARAM);
    }

    public Observable<FormRequentBean> getUserSurveyFormListVM(String INPUT_PARAM) {
        System.out.println("INPUT_PARAM====" + INPUT_PARAM);
        return repository.getUserSurveyFormListRepo(INPUT_PARAM);
    }

    public Observable<FormRequentBean> sendComplaintReport(String INPUT_PARAM) {
        return repository.sendComplaintReport(INPUT_PARAM);
    }

    public Observable<FormRequentBean> saveProService(String INPUT_PARAM) {
        return repository.saveProService(INPUT_PARAM);
    }

    public Observable<ProServiceResponse> getProSrvList(String INPUT_PARAM) {
        return repository.getProSrvList(INPUT_PARAM);
    }

    public Observable<ProServiceResponse> getProSrvById(String INPUT_PARAM) {
        return repository.getProSrvById(INPUT_PARAM);
    }

    public Observable<ProServiceResponse> saveOrEditPrcData(String INPUT_PARAM) {
        return repository.saveOrEditPrcData(INPUT_PARAM);
    }
    public Observable<ProServiceResponse> confirmPrcData(String INPUT_PARAM) {
        return repository.confirmPrcData(INPUT_PARAM);
    }

    public Observable<ProServiceResponse> getAttachFileSettingItemList(String INPUT_PARAM) {
        return repository.getAttachFileSettingItemList(INPUT_PARAM);
    }

    public Observable<ProServiceResponse> getProSrvAttachFileList(String INPUT_PARAM) {
        return repository.getProSrvAttachFileList(INPUT_PARAM);
    }

    public Observable<ProServiceResponse> deleteAttachFile(String INPUT_PARAM) {
        return repository.deleteAttachFile(INPUT_PARAM);
    }

    public Observable<DailyEventRespons> dailyEvent_MngAction(String INPUT_PARAM) {
        return repository.dailyEvent_MngAction(INPUT_PARAM);
    }

    public Observable<DailyEventRespons> dailyEvent_GetList_CarEnter_PS0(String INPUT_PARAM) {
        return repository.dailyEvent_GetList_CarEnter_PS0(INPUT_PARAM);
    }

   /* public Observable<FormRequentBean> sendComplaintAttachment(String INPUT_PARAM) {
        return repository.sendComplaintAttachment(INPUT_PARAM);
    }*/


    public Observable<NeshanRequentBean> getDetailLocation(String type, String latLngList, String destinations) {
        return repository.getDetailLocation(type, latLngList, destinations);
    }

    public Observable<NeshanRequentBean> getReverseGeocoding(String lat, String lng) {
        return repository.getReverseGeocoding(lat, lng);
    }

    public LiveData<VoiceRecordItem> getVoiceRecordItemResult() {
        if(voiceRecordItemMutableLiveData == null){
            voiceRecordItemMutableLiveData = new MutableLiveData<>();
        }
        return voiceRecordItemMutableLiveData;
    }

    public void getVoiceRecordItemData(String path,String name){
        VoiceRecordItem voiceRecordItem = new VoiceRecordItem();
        voiceRecordItem.setPath(path);
        voiceRecordItem.setName(name);

        System.out.println("path====" + path);
        System.out.println("name====" + name);

        if(voiceRecordItemMutableLiveData == null){
            voiceRecordItemMutableLiveData = new MutableLiveData<>();
        }

        voiceRecordItemMutableLiveData.setValue(voiceRecordItem);
    }

}
