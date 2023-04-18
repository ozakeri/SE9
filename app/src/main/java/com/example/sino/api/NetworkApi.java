package com.example.sino.api;

import com.example.sino.model.PersonRequentBean;
import com.example.sino.model.SuccessActivationBean;
import com.example.sino.model.SuccessChatReceiveBean;
import com.example.sino.model.SuccessPermissionBean;
import com.example.sino.model.SuccessRegisterBean;
import com.example.sino.model.carinfo.SuccessCarInfoBean;
import com.example.sino.model.chatgroup.SuccessChatGroupBean;
import com.example.sino.model.chatgroupmember.SuccessChatGroupMemberBean;
import com.example.sino.model.dailyEvent.DailyEventRespons;
import com.example.sino.model.documentversion.DocumentVersion;
import com.example.sino.model.driverlicence.DriverLicenceRequentBean;
import com.example.sino.model.form.FormRequentBean;
import com.example.sino.model.reception.ProServiceResponse;
import com.example.sino.model.userInfobyid.SuccessUserInfoByIdBean;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetworkApi {

    @GET("mobileNoConfirmation?")
    Observable<SuccessRegisterBean> mobileNoConfirmation(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("activationCodeValidation?")
    Observable<SuccessActivationBean> activeCodeConfirmation(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getUserPermissionList?")
    Observable<SuccessPermissionBean> getUserPermissionListApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getLastDocumentVersion?")
    Observable<DocumentVersion> getLastDocumentVersionApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getUserChatMessageList?")
    Observable<SuccessChatReceiveBean> getUserChatMessageListApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("chatMessageDeliveredReport?")
    Observable<SuccessChatReceiveBean> chatMessageDeliveredReportApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getCarInfo?")
    Observable<SuccessCarInfoBean> getCarInfoApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getCarInfo?")
    Observable<SuccessCarInfoBean> getCarInfoCopyApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getCarInfo_ProSrv?")
    Observable<SuccessCarInfoBean> getCarInfo_ProSrv(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getPersonByNationalCode?")
    Observable<PersonRequentBean> getPersonByNationalCodeApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("insertPersonByParam?")
    Observable<PersonRequentBean> upsertPersonByParamApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getCompanyInfo?")
    Observable<SuccessCarInfoBean> getCompanyInfoApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getUserChatGroupList?")
    Observable<SuccessChatGroupBean> getUserChatGroupListApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getUserChatGroupMemberList?")
    Observable<SuccessChatGroupMemberBean> getUserChatGroupMemberListApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getUserInfoById?")
    Observable<SuccessUserInfoByIdBean> getUserInfoByIdApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("saveChatMessage?")
    Observable<SuccessUserInfoByIdBean> saveChatMessageApi(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getDriverJobList?")
    Observable<DriverLicenceRequentBean> getDriverJobList(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getUserSurveyFormList?")
    Observable<FormRequentBean> getUserSurveyFormList(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("saveComplaintReport?")
    Observable<FormRequentBean> sendComplaintReport(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("saveProService?")
    Observable<FormRequentBean> saveProService(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("editProService?")
    Observable<FormRequentBean> editProService(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getProService?")
    Observable<FormRequentBean> getProService(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getProSrvList_Rcp?")
    Observable<ProServiceResponse> getProSrvList(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getProSrvFullById?")
    Observable<ProServiceResponse> getProSrvById(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("saveOrEditPrcData?")
    Observable<ProServiceResponse> saveOrEditPrcData(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("confirmPrcData?")
    Observable<ProServiceResponse> confirmPrcData(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getAttachFileSettingItemList?")
    Observable<ProServiceResponse> getAttachFileSettingItemList(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("getProSrvAttachFileList?")
    Observable<ProServiceResponse> getProSrvAttachFileList(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("deleteAttachFile?")
    Observable<ProServiceResponse> deleteAttachFile(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("dailyEvent_MngAction?")
    Observable<DailyEventRespons> dailyEvent_MngAction(@Query("INPUT_PARAM") String INPUT_PARAM);

    @GET("dailyEvent_GetList_CarEnter_PS0?")
    Observable<DailyEventRespons> dailyEvent_GetList_CarEnter_PS0(@Query("INPUT_PARAM") String INPUT_PARAM);

}
