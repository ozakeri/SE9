package com.example.sino.repository;

import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.sino.db.SinoDao;
import com.example.sino.db.SinoDatabase;
import com.example.sino.db.entity.AttachFile;
import com.example.sino.db.entity.form.FormQuestionGroup;
import com.example.sino.db.entity.form.SurveyForm;
import com.example.sino.db.entity.form.SurveyFormQuestion;
import com.example.sino.db.entity.form.SurveyFormQuestionTemp;
import com.example.sino.model.chatgroup.ChatGroup;
import com.example.sino.model.chatgroupmember.ChatGroupMember;
import com.example.sino.model.complaintreport.ComplaintReport;
import com.example.sino.model.db.AppUser;
import com.example.sino.model.db.ChatMessage;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DatabaseRepository {

    private SinoDao sinoDao;
    private SinoDatabase database;

    @Inject
    public DatabaseRepository(SinoDao sinoDao, SinoDatabase database) {
        this.sinoDao = sinoDao;
        this.database = database;
    }

    public List<ChatMessage> getChatMessagesByServerMessageIdRepo(Long id) {
        return sinoDao.getChatMessagesByServerMessageId(id);
    }

    public ChatGroup getChatGroupListByParamRepo(Long id) {
        return sinoDao.getChatGroupListByParam(id);
    }

    public ChatGroup getChatGroupByServerGroupIdRepo(Long id) {
        return sinoDao.getChatGroupByServerGroupId(id);
    }

    public ChatGroupMember getChatGroupMemberByUserAndGroupRepo(Long userId, Long chatGroupId) {
        return sinoDao.getChatGroupMemberByUserAndGroup(userId, chatGroupId);
    }

    public ChatGroupMember getChatGroupMemberListByParamRepo(Long userId, Long chatGroupId) {
        return sinoDao.getChatGroupMemberListByParam(userId, chatGroupId);
    }

    public Long insertChatMessageRepo(ChatMessage chatMessage) {
        return sinoDao.insertChatMessage(chatMessage);
    }

    public ChatMessage getChatMessagesByIdRepo(Long id) {
        return sinoDao.getChatMessagesById(id);
    }

    public void updateChatMessageRepo(ChatMessage chatMessage) {
        sinoDao.updateChatMessage(chatMessage);
    }

    public void insertChatGroupRepo(ChatGroup chatGroup) {
        sinoDao.insertChatGroup(chatGroup);
    }

    public void insertAttachFileRepo(AttachFile attachFile) {
        sinoDao.insertAttachFile(attachFile);
    }
    public LiveData<AttachFile> getAttachFileByParam(Long entityId, Long serverAttachFileSettingId) {
        return sinoDao.getAttachFileByParam(entityId,serverAttachFileSettingId);
    }

    public void updateChatGroupRepo(ChatGroup chatGroup) {
        sinoDao.updateChatGroup(chatGroup);
    }

    public void insertAppUserRepo(AppUser appUser) {
        sinoDao.insertAppUser(appUser);
    }

    public void insertSurveyFormRepo(SurveyForm surveyForm) {
        sinoDao.insertSurveyForm(surveyForm);
    }

    public void insertFormQuestionGroupRepo(FormQuestionGroup formQuestionGroup) {
        sinoDao.insertFormQuestionGroup(formQuestionGroup);
    }

    public void insertSurveyFormQuestionTempRepo(SurveyFormQuestionTemp surveyFormQuestionTemp) {
        sinoDao.insertSurveyFormQuestionTemp(surveyFormQuestionTemp);
    }

    public void insertSurveyFormQuestionRepo(SurveyFormQuestion surveyFormQuestion) {
        sinoDao.insertSurveyFormQuestion(surveyFormQuestion);
    }

    public void updateAppUserRepo(AppUser appUser) {
        sinoDao.updateAppUser(appUser);
    }

    public void insertChatGroupMemberRepo(ChatGroupMember chatGroupMember) {
        sinoDao.insertChatGroupMember(chatGroupMember);
    }

    public void updateChatGroupMemberRepo(ChatGroupMember chatGroupMember) {
        sinoDao.updateChatGroupMember(chatGroupMember);
    }

    public List<ChatGroup> getChatGroupList() {
        return sinoDao.getChatGroupList();
    }

    public List<ChatGroup> getActiveChatGroupListRepo(int status) {
        return sinoDao.getActiveChatGroupList(status);
    }

    public void deleteAllData() {
        Completable.fromAction
                        (new Action() {
                            @Override
                            public void run() throws Exception {
                                database.clearAllTables();
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("deleteAllData", "onSubscribe: ok");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("deleteAllData", "onComplete: ok");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("deleteAllData", "onError: ok");
                    }
                });
    }

    public void deleteAllEmdadAttachFile() {
        Completable.fromAction(new Action() {
                    @Override
                    public void run() throws Throwable {
                        sinoDao.deleteAllEmdadAttachFile();
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e("deleteAllData", "onSubscribe: ok");
                    }

                    @Override
                    public void onComplete() {
                        Log.e("deleteAllData", "onComplete: ok");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("deleteAllData", "onError: ok");
                    }
                });
    }

    public void deleteReportAttachFileById(AttachFile attachFile){
        sinoDao.deleteReportAttachFileByParam(attachFile);
    }

    public void updateAttachFileByParam(AttachFile attachFile){
        sinoDao.updateAttachFileByParam(attachFile);
    }

    public void updateComplaintReportByParam(ComplaintReport complaintReport){
        sinoDao.updateComplaintReportByParam(complaintReport);
    }

    public void insertComplaintReportByParam(ComplaintReport complaintReport){
        sinoDao.insertComplaintReportByParam(complaintReport);
    }

    public List<AttachFile> getPendingAttachFileByEntityId(Long entityNameEn,Long entityId){
        return sinoDao.getPendingAttachFileByEntityId(entityNameEn,entityId);
    }

    public Observable<List<AttachFile>> getAllAttachFile() {
        return sinoDao.getAllAttachFile();
    }

    public Observable<List<AttachFile>> getUnSentAttachFileList(int status) {
        return sinoDao.getUnSentAttachFileList(status);
    }

    public LiveData<List<AttachFile>> getReportAttachFileList(Long id) {
        return sinoDao.getReportAttachFileList(id);
    }

    public LiveData<List<SurveyForm>> getAllSurveyForm() {
        return sinoDao.getAllSurveyForm();
    }
}
