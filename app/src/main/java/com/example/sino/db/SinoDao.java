package com.example.sino.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sino.db.entity.AttachFile;
import com.example.sino.db.entity.form.FormQuestionGroup;
import com.example.sino.db.entity.form.SurveyForm;
import com.example.sino.db.entity.form.SurveyFormQuestion;
import com.example.sino.db.entity.form.SurveyFormQuestionTemp;
import com.example.sino.enumtype.SendingStatusEn;
import com.example.sino.model.chatgroup.ChatGroup;
import com.example.sino.model.chatgroupmember.ChatGroupMember;
import com.example.sino.model.complaintreport.ComplaintReport;
import com.example.sino.model.db.AppUser;
import com.example.sino.model.db.ChatMessage;
import com.example.sino.model.db.User;
import com.example.sino.model.db.UserPermission;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface SinoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(User user);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPermission(UserPermission permission);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insertChatMessage(ChatMessage chatMessage);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateChatMessage(ChatMessage chatMessage);

    @Insert()
    void insertChatGroup(ChatGroup chatGroup);

    @Insert()
    void insertAttachFile(AttachFile attachFile);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateChatGroup(ChatGroup chatGroup);

    @Insert()
    void insertAppUser(AppUser appUser);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSurveyForm(SurveyForm surveyForm);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFormQuestionGroup(FormQuestionGroup formQuestionGroup);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSurveyFormQuestionTemp(SurveyFormQuestionTemp surveyFormQuestionTemp);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSurveyFormQuestion(SurveyFormQuestion surveyFormQuestion);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateAppUser(AppUser appUser);

    @Query("SELECT * FROM sino_table WHERE mobileNo=:mobileNo")
    User getUserByMobileNo(String mobileNo);

    @Query("SELECT * FROM sino_table")
    LiveData<List<User>> getAllUser();

    @Query("SELECT * FROM attachfile")
    Observable<List<AttachFile>> getAllAttachFile();

    @Query("SELECT * FROM attachfile WHERE sendingStatusEn=:sendingStatus")
    Observable<List<AttachFile>> getUnSentAttachFileList(int sendingStatus);

    @Query("SELECT * FROM attachfile WHERE entityId=:id AND sendingStatusEn != 2")
    LiveData<List<AttachFile>> getReportAttachFileList(Long id);

    @Query("SELECT * FROM surveyform")
    LiveData<List<SurveyForm>> getAllSurveyForm();

    @Query("SELECT * FROM sino_table")
    List<ChatGroup> getChatGroupList();

    @Query("SELECT * FROM permission WHERE userId=:userId")
    List<UserPermission> getUserPermissionListByUserId(Long userId);

    @Query("SELECT * FROM permission")
    Flowable<List<UserPermission>> getUserPermissionListByUserIdCopy();

    @Query("SELECT * FROM chat_group WHERE status=:status")
    List<ChatGroup> getActiveChatGroupList(int status);

    @Query("SELECT * FROM appuser WHERE id=:id")
    AppUser getAppUserById(Long id);

    @Query("SELECT * FROM surveyform WHERE id=:id")
    SurveyForm getSurveyFormById(Long id);

    @Query("SELECT * FROM surveyformquestion WHERE surveyFormId=:id")
    List<SurveyFormQuestion> getSurveyFormQuestionListByFormId(Long id);

    @Query("SELECT * FROM surveyformquestion WHERE id=:id")
    SurveyFormQuestion getSurveyFormQuestionById(Long id);

    @Query("SELECT * FROM surveyformquestiontemp WHERE id=:id")
    SurveyFormQuestionTemp getSurveyFormQuestionTempById(Long id);

    @Query("SELECT * FROM formquestiongroup WHERE id=:id")
    FormQuestionGroup getCheckListFormQuestionGroupById(Long id);

    @Query("SELECT * FROM chatMessage WHERE serverMessageId=:id")
    List<ChatMessage> getChatMessagesByServerMessageId(Long id);

    @Query("SELECT * FROM chatMessage WHERE id=:id")
    ChatMessage getChatMessagesById(Long id);

    @Query("SELECT * FROM chatgroupmember WHERE id=:id")
    ChatGroup getChatGroupListByParam(Long id);

    @Query("DELETE FROM permission WHERE userId=:userId & permissionName=:permissionName")
    void deletePermission(Long userId, String permissionName);

    @Query("DELETE FROM permission WHERE userId=:userId")
    void deletePermissionByUserId(Long userId);

    @Query("SELECT * FROM chat_group WHERE serverGroupId=:serverGroupId")
    ChatGroup getChatGroupByServerGroupId(Long serverGroupId);

    @Query("SELECT * FROM chatgroupmember WHERE userId=:userId AND chatGroupId=:chatGroupId")
    ChatGroupMember getChatGroupMemberByUserAndGroup(Long userId, Long chatGroupId);

    @Query("SELECT * FROM chatgroupmember WHERE userId=:userId AND chatGroupId=:chatGroupId")
    ChatGroupMember getChatGroupMemberListByParam(Long userId, Long chatGroupId);

    @Insert()
    void insertChatGroupMember(ChatGroupMember chatGroupMember);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void updateChatGroupMember(ChatGroupMember chatGroupMember);

    @Query("DELETE FROM attachfile")
    void deleteAllEmdadAttachFile();

    @Delete
    void deleteReportAttachFileByParam(AttachFile attachFile);

    @Update
    void updateAttachFileByParam(AttachFile attachFile);

    @Update
    void updateComplaintReportByParam(ComplaintReport complaintReport);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComplaintReportByParam(ComplaintReport complaintReport);

    @Query("SELECT * FROM attachfile WHERE entityNameEn=:entityNameEn AND entityId=:entityId")
    List<AttachFile> getPendingAttachFileByEntityId(Long entityNameEn, Long entityId);

    @Query("SELECT * FROM attachfile WHERE entityId=:entityId AND serverAttachFileSettingId=:serverAttachFileSettingId")
    Observable<AttachFile> getAttachFileByParam(Long entityId, Long serverAttachFileSettingId);

}
