package com.example.sino.api;


import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.sino.db.SinoDao;
import com.example.sino.db.entity.form.FormQuestionGroup;
import com.example.sino.db.entity.form.SurveyForm;
import com.example.sino.db.entity.form.SurveyFormQuestion;
import com.example.sino.db.entity.form.SurveyFormQuestionTemp;
import com.example.sino.model.PersonRequentBean;
import com.example.sino.model.SuccessActivationBean;
import com.example.sino.model.SuccessChatReceiveBean;
import com.example.sino.model.SuccessPermissionBean;
import com.example.sino.model.SuccessRegisterBean;
import com.example.sino.model.chatgroup.SuccessChatGroupBean;
import com.example.sino.model.chatgroupmember.SuccessChatGroupMemberBean;
import com.example.sino.model.dailyEvent.DailyEventRespons;
import com.example.sino.model.db.AppUser;
import com.example.sino.model.db.User;
import com.example.sino.model.db.UserPermission;
import com.example.sino.model.carinfo.SuccessCarInfoBean;
import com.example.sino.model.documentversion.DocumentVersion;
import com.example.sino.model.driverlicence.DriverLicenceRequentBean;
import com.example.sino.model.form.FormRequentBean;
import com.example.sino.model.neshan.NeshanRequentBean;
import com.example.sino.model.reception.ProServiceResponse;
import com.example.sino.model.userInfobyid.SuccessUserInfoByIdBean;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class NetworkRepository {

    private NetworkApi networkApi;
    private NeshanApi neshanApi;
    private SinoDao sinoDao;

    @Inject
    public NetworkRepository(NeshanApi neshanApi,NetworkApi networkApi,SinoDao sinoDao) {
        this.neshanApi = neshanApi;
        this.networkApi = networkApi;
        this.sinoDao = sinoDao;
    }

    public Observable<SuccessRegisterBean> mobileNoConfirmation(String INPUT_PARAM) {
        return networkApi.mobileNoConfirmation(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessActivationBean> activeCodeConfirmation(String INPUT_PARAM) {
        return networkApi.activeCodeConfirmation(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessPermissionBean> getUserPermissionListRepo(String INPUT_PARAM) {
        return networkApi.getUserPermissionListApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<DocumentVersion> getLastDocumentVersionRepo(String INPUT_PARAM) {
        return networkApi.getLastDocumentVersionApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessChatReceiveBean> getUserChatMessageListRepo(String INPUT_PARAM) {
        return networkApi.getUserChatMessageListApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessChatReceiveBean> chatMessageDeliveredReportRepo(String INPUT_PARAM) {
        return networkApi.chatMessageDeliveredReportApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessCarInfoBean> getCarInfoRepo(String INPUT_PARAM) {
        return networkApi.getCarInfoApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessCarInfoBean> getCarInfoCopyRepo(String INPUT_PARAM) {
        return networkApi.getCarInfoCopyApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessCarInfoBean> getCarInfo_ProSrv(String INPUT_PARAM) {
        return networkApi.getCarInfo_ProSrv(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<PersonRequentBean> getPersonByNationalCodeRepo(String INPUT_PARAM) {
        return networkApi.getPersonByNationalCodeApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<PersonRequentBean> upsertPersonByParamRepo(String INPUT_PARAM) {
        return networkApi.upsertPersonByParamApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessCarInfoBean> getCompanyInfoRepo(String INPUT_PARAM) {
        return networkApi.getCompanyInfoApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessChatGroupBean> getUserChatGroupListRepo(String INPUT_PARAM) {
        return networkApi.getUserChatGroupListApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessChatGroupMemberBean> getUserChatGroupMemberListRepo(String INPUT_PARAM) {
        return networkApi.getUserChatGroupMemberListApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessUserInfoByIdBean> getUserInfoByIdRepo(String INPUT_PARAM) {
        return networkApi.getUserInfoByIdApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<SuccessUserInfoByIdBean> saveChatMessageRepo(String INPUT_PARAM) {
        return networkApi.saveChatMessageApi(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<DriverLicenceRequentBean> getDriverJobListRepo(String INPUT_PARAM) {
        return networkApi.getDriverJobList(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<FormRequentBean> getUserSurveyFormListRepo(String INPUT_PARAM) {
        return networkApi.getUserSurveyFormList(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<FormRequentBean> sendComplaintReport(String INPUT_PARAM) {
        return networkApi.sendComplaintReport(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<FormRequentBean> saveProService(String INPUT_PARAM) {
        return networkApi.saveProService(INPUT_PARAM + "&IS_ENCRYPED=false");
    }


    public Observable<FormRequentBean> editProService(String INPUT_PARAM) {
        return networkApi.editProService(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<FormRequentBean> getProService(String INPUT_PARAM) {
        return networkApi.getProService(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<ProServiceResponse> getProSrvList(String INPUT_PARAM) {
        return networkApi.getProSrvList(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<ProServiceResponse> getProSrvById(String INPUT_PARAM) {
        return networkApi.getProSrvById(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<ProServiceResponse> saveOrEditPrcData(String INPUT_PARAM) {
        return networkApi.saveOrEditPrcData(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<ProServiceResponse> confirmPrcData(String INPUT_PARAM) {
        return networkApi.confirmPrcData(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<ProServiceResponse> getAttachFileSettingItemList(String INPUT_PARAM) {
        return networkApi.getAttachFileSettingItemList(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<ProServiceResponse> getProSrvAttachFileList(String INPUT_PARAM) {
        return networkApi.getProSrvAttachFileList(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<ProServiceResponse> deleteAttachFile(String INPUT_PARAM) {
        return networkApi.deleteAttachFile(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<DailyEventRespons> dailyEvent_MngAction(String INPUT_PARAM) {
        return networkApi.dailyEvent_MngAction(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

    public Observable<DailyEventRespons> dailyEvent_GetList_CarEnter_PS0(String INPUT_PARAM) {
        return networkApi.dailyEvent_GetList_CarEnter_PS0(INPUT_PARAM + "&IS_ENCRYPED=false");
    }

   /* public Observable<FormRequentBean> sendComplaintAttachment(String INPUT_PARAM) {
        return networkApi.sendComplaintAttachment(INPUT_PARAM + "&IS_ENCRYPED=false");
    }*/

    public void insertUser(User user) {
        sinoDao.insertUser(user);
    }

    public void insertPermission(UserPermission permission) {
        //sinoDao.insertPermission(permission);

        Completable.fromAction(()-> sinoDao.insertPermission(permission))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("insertPermission", "onSubscribe: ok");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("insertPermission", "onComplete: ok");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("insertPermission", "onError: ok");
                    }
                });
    }

    public void deletePermission(Long userId, String permissionName) {
        sinoDao.deletePermission(userId,permissionName);
    }

    public void deletePermissionByUserId(Long userId) {
        sinoDao.deletePermissionByUserId(userId);
    }

    public User getUserByMobileNo(String mobileNo) {
        return sinoDao.getUserByMobileNo(mobileNo);
    }

    public Observable<List<User>> getAllUser() {
        return sinoDao.getAllUser();
    }

    public List<UserPermission> getUserPermissionList(Long userId) {
        return sinoDao.getUserPermissionListByUserId(userId);
    }

    public Flowable<List<UserPermission>> getUserPermissionListCopy() {
        return sinoDao.getUserPermissionListByUserIdCopy();
    }

    public AppUser getAppUserByIdRepo(Long id) {
        return sinoDao.getAppUserById(id);
    }

    public SurveyForm getSurveyFormByIdRepo(Long id) {
        return sinoDao.getSurveyFormById(id);
    }

    public List<SurveyFormQuestion> getSurveyFormQuestionListByFormIdRepo(Long id) {
        return sinoDao.getSurveyFormQuestionListByFormId(id);
    }

    public SurveyFormQuestion getSurveyFormQuestionByIdRepo(Long id) {
        return sinoDao.getSurveyFormQuestionById(id);
    }

    public SurveyFormQuestionTemp getSurveyFormQuestionTempByIdRepo(Long id) {
        return sinoDao.getSurveyFormQuestionTempById(id);
    }

    public FormQuestionGroup getCheckListFormQuestionGroupByIdRepo(Long id) {
        return sinoDao.getCheckListFormQuestionGroupById(id);
    }


    public Observable<NeshanRequentBean> getDetailLocation(String type, String latLngList, String destinations) {
        return neshanApi.getDetailLocation(type,latLngList,destinations);
    }

    public Observable<NeshanRequentBean> getReverseGeocoding(String lat, String lng) {
        return neshanApi.getReverseGeocoding(lat,lng);
    }

}
