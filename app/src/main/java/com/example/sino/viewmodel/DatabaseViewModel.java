package com.example.sino.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
import com.example.sino.repository.DatabaseRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.rxjava3.core.Observable;

@HiltViewModel
public class DatabaseViewModel extends AndroidViewModel {

    @Inject
    DatabaseRepository repository;

    @Inject
    public DatabaseViewModel(@NonNull Application application) {
        super(application);
    }

    public List<ChatMessage> getChatMessagesByServerMessageIdVM(Long id) {
        return repository.getChatMessagesByServerMessageIdRepo(id);
    }

    public Long insertChatMessageVM(ChatMessage chatMessage) {
         return repository.insertChatMessageRepo(chatMessage);
    }

    public ChatMessage getChatMessagesByIdVM(Long id) {
         return repository.getChatMessagesByIdRepo(id);
    }

    public void updateChatMessageVM(ChatMessage chatMessage) {
         repository.updateChatMessageRepo(chatMessage);
    }

    public void insertChatGroupVM(ChatGroup chatGroup) {
        repository.insertChatGroupRepo(chatGroup);
    }

    public void updateChatGroupVM(ChatGroup chatGroup) {
        repository.updateChatGroupRepo(chatGroup);
    }

    public void insertAppUserVM(AppUser appUser) {
        repository.insertAppUserRepo(appUser);
    }

    public void insertSurveyFormVM(SurveyForm surveyForm) {
        repository.insertSurveyFormRepo(surveyForm);
    }

    public void insertFormQuestionGroupVM(FormQuestionGroup formQuestionGroup) {
        repository.insertFormQuestionGroupRepo(formQuestionGroup);
    }

    public void insertSurveyFormQuestionTempVM(SurveyFormQuestionTemp surveyFormQuestionTemp) {
        repository.insertSurveyFormQuestionTempRepo(surveyFormQuestionTemp);
    }

    public void insertSurveyFormQuestionVM(SurveyFormQuestion surveyFormQuestion) {
        repository.insertSurveyFormQuestionRepo(surveyFormQuestion);
    }

    public void updateAppUserVM(AppUser appUser) {
        repository.updateAppUserRepo(appUser);
    }

    public void insertChatGroupMemberVM(ChatGroupMember chatGroupMember) {
        repository.insertChatGroupMemberRepo(chatGroupMember);
    }

    public void updateChatGroupMemberVM(ChatGroupMember chatGroupMember) {
        repository.updateChatGroupMemberRepo(chatGroupMember);
    }

    public List<ChatGroup> getChatGroupListVM() {
        return repository.getChatGroupList();
    }

    public List<ChatGroup> getActiveChatGroupListVM(int status) {
        return repository.getActiveChatGroupListRepo(status);
    }

    public ChatGroup getChatGroupByServerGroupIdVM(Long id) {
        return repository.getChatGroupByServerGroupIdRepo(id);
    }

    public ChatGroupMember getChatGroupMemberByUserAndGroupVM(Long userId, Long chatGroupId) {
        return repository.getChatGroupMemberByUserAndGroupRepo(userId, chatGroupId);
    }

    public ChatGroup getChatGroupListByParamVM(Long id) {
        return repository.getChatGroupListByParamRepo(id);
    }

    public void deleteAllData() {
        repository.deleteAllData();
    }

    public void deleteAllEmdadAttachFile() {
        repository.deleteAllEmdadAttachFile();
    }

    public void deleteReportAttachFileById(AttachFile attachFile){
        repository.deleteReportAttachFileById(attachFile);
    }

    public void updateAttachFileByParam(AttachFile attachFile){
        repository.updateAttachFileByParam(attachFile);
    }

    public void insertAttachFileRepoVM(AttachFile attachFile) {
        repository.insertAttachFileRepo(attachFile);
    }
    public Observable<AttachFile> getAttachFileByParamRepoVM(Long entityId, Long serverAttachFileSettingId) {
        return repository.getAttachFileByParam(entityId,serverAttachFileSettingId);
    }

    public void updateComplaintReportByParam(ComplaintReport complaintReport){
        repository.updateComplaintReportByParam(complaintReport);
    }

    public void insertComplaintReportByParam(ComplaintReport complaintReport){
        repository.insertComplaintReportByParam(complaintReport);
    }

    public List<AttachFile> getPendingAttachFileByEntityId(Long entityNameEn,Long entityId){
        return repository.getPendingAttachFileByEntityId(entityNameEn,entityId);
    }

    public Observable<List<AttachFile>> getAllAttachFile() {
        return repository.getAllAttachFile();
    }

    public Observable<List<AttachFile>> getUnSentAttachFileList(int status) {
        return repository.getUnSentAttachFileList(status);
    }

    public LiveData<List<AttachFile>> getReportAttachFileList(Long id) {
        return repository.getReportAttachFileList(id);
    }

    public LiveData<List<SurveyForm>> getAllSurveyForm() {
        return repository.getAllSurveyForm();
    }

}
