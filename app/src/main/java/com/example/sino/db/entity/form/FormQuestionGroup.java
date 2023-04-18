package com.example.sino.db.entity.form;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sino.utils.converters.FormItemAnswerConverter;
import com.example.sino.utils.converters.FormQuestionConverter;
import com.example.sino.utils.converters.FormQuestionGroupFormConverter;
import com.example.sino.utils.converters.FormTempConverter;

import java.util.List;


@Entity(tableName = "FormQuestionGroup")
public class FormQuestionGroup {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private Long groupId;
    private String groupName;
    private Long formId;
    @TypeConverters({FormQuestionConverter.class})
    private List<FormQuestion> formQuestionList;
    @TypeConverters({FormItemAnswerConverter.class})
    private List<FormItemAnswer> formItemAnswerList;
    @TypeConverters({FormTempConverter.class})
    private List<FormTemp> formTempList;
    @TypeConverters({FormQuestionGroupFormConverter.class})
    private List<FormQuestionGroupForm> formQuestionGroupFormId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public List<FormQuestion> getFormQuestionList() {
        return formQuestionList;
    }

    public void setFormQuestionList(List<FormQuestion> formQuestionList) {
        this.formQuestionList = formQuestionList;
    }

    public List<FormItemAnswer> getFormItemAnswerList() {
        return formItemAnswerList;
    }

    public void setFormItemAnswerList(List<FormItemAnswer> formItemAnswerList) {
        this.formItemAnswerList = formItemAnswerList;
    }

    public List<FormTemp> getFormTempList() {
        return formTempList;
    }

    public void setFormTempList(List<FormTemp> formTempList) {
        this.formTempList = formTempList;
    }

    public List<FormQuestionGroupForm> getFormQuestionGroupFormId() {
        return formQuestionGroupFormId;
    }

    public void setFormQuestionGroupFormId(List<FormQuestionGroupForm> formQuestionGroupFormId) {
        this.formQuestionGroupFormId = formQuestionGroupFormId;
    }
}
