package com.example.sino.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.sino.db.entity.AttachFile;
import com.example.sino.db.entity.form.FormItemAnswer;
import com.example.sino.db.entity.form.FormQuestion;
import com.example.sino.db.entity.form.FormQuestionGroup;
import com.example.sino.db.entity.form.FormQuestionGroupForm;
import com.example.sino.db.entity.form.SurveyForm;
import com.example.sino.db.entity.form.SurveyFormQuestion;
import com.example.sino.db.entity.form.SurveyFormQuestionTemp;
import com.example.sino.model.chatgroup.ChatGroup;
import com.example.sino.model.chatgroupmember.ChatGroupMember;
import com.example.sino.model.complaintreport.ComplaintReport;
import com.example.sino.model.db.AppUser;
import com.example.sino.model.db.ChatMessage;
import com.example.sino.model.db.User;
import com.example.sino.model.db.UserPermission;

@Database(entities = {User.class,
        UserPermission.class,
        ChatMessage.class,
        ChatGroup.class,
        ChatGroupMember.class,
        AppUser.class,
        AttachFile.class,
        SurveyForm.class,
        SurveyFormQuestion.class,
        SurveyFormQuestionTemp.class,
        FormItemAnswer.class,
        FormQuestion.class,
        FormQuestionGroup.class,
        FormQuestionGroupForm.class,
        ComplaintReport.class},
        version = 6)
public abstract class SinoDatabase extends RoomDatabase {
    public abstract SinoDao sinoDao();
}
