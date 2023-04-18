package com.example.sino.db.entity.form;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.sino.utils.converters.DateConverter;
import com.example.sino.utils.converters.FormItemAnswerConverter;
import com.example.sino.utils.converters.SurveyFormQuestionConverter;
import com.example.sino.utils.converters.SurveyFormQuestionTempConverter;

import java.util.Date;
import java.util.List;

@Entity(tableName = "SurveyForm")
public class SurveyForm implements Parcelable {
    @PrimaryKey
    private Long id;
    private String name;
    private Integer minScore;
    private Integer maxScore;
    @TypeConverters({DateConverter.class})
    private java.util.Date startDate;
    @TypeConverters({DateConverter.class})
    private java.util.Date endDate;
    private Integer statusEn;
    private Integer formStatus;
    @TypeConverters({DateConverter.class})
    private java.util.Date statusDate;
    private Integer sendingStatusEn;
    @TypeConverters({DateConverter.class})
    private java.util.Date sendingStatusDate;
    //private String xLatitude;
    //private String yLongitude;
    private Long serverAnswerInfoId;
    private String inputValuesDefault;
    @TypeConverters({SurveyFormQuestionTempConverter.class})
    private List<SurveyFormQuestionTemp> surveyFormQuestionTempList;
    @TypeConverters({SurveyFormQuestionConverter.class})
    private List<SurveyFormQuestion> surveyFormQuestionList;

    public SurveyForm() {
    }

    protected SurveyForm(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        if (in.readByte() == 0) {
            minScore = null;
        } else {
            minScore = in.readInt();
        }
        if (in.readByte() == 0) {
            maxScore = null;
        } else {
            maxScore = in.readInt();
        }
        if (in.readByte() == 0) {
            statusEn = null;
        } else {
            statusEn = in.readInt();
        }
        if (in.readByte() == 0) {
            formStatus = null;
        } else {
            formStatus = in.readInt();
        }
        if (in.readByte() == 0) {
            sendingStatusEn = null;
        } else {
            sendingStatusEn = in.readInt();
        }
        if (in.readByte() == 0) {
            serverAnswerInfoId = null;
        } else {
            serverAnswerInfoId = in.readLong();
        }
        inputValuesDefault = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        if (minScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(minScore);
        }
        if (maxScore == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(maxScore);
        }
        if (statusEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(statusEn);
        }
        if (formStatus == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(formStatus);
        }
        if (sendingStatusEn == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(sendingStatusEn);
        }
        if (serverAnswerInfoId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(serverAnswerInfoId);
        }
        dest.writeString(inputValuesDefault);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SurveyForm> CREATOR = new Creator<SurveyForm>() {
        @Override
        public SurveyForm createFromParcel(Parcel in) {
            return new SurveyForm(in);
        }

        @Override
        public SurveyForm[] newArray(int size) {
            return new SurveyForm[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinScore() {
        return minScore;
    }

    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getStatusEn() {
        return statusEn;
    }

    public void setStatusEn(Integer statusEn) {
        this.statusEn = statusEn;
    }

    public Integer getFormStatus() {
        return formStatus;
    }

    public void setFormStatus(Integer formStatus) {
        this.formStatus = formStatus;
    }

    public Date getStatusDate() {
        return statusDate;
    }

    public void setStatusDate(Date statusDate) {
        this.statusDate = statusDate;
    }

    public Integer getSendingStatusEn() {
        return sendingStatusEn;
    }

    public void setSendingStatusEn(Integer sendingStatusEn) {
        this.sendingStatusEn = sendingStatusEn;
    }

    public Date getSendingStatusDate() {
        return sendingStatusDate;
    }

    public void setSendingStatusDate(Date sendingStatusDate) {
        this.sendingStatusDate = sendingStatusDate;
    }

  /*  public String getxLatitude() {
        return xLatitude;
    }

    public void setxLatitude(String xLatitude) {
        this.xLatitude = xLatitude;
    }*/

/*    public String getyLongitude() {
        return yLongitude;
    }

    public void setyLongitude(String yLongitude) {
        this.yLongitude = yLongitude;
    }*/

    public Long getServerAnswerInfoId() {
        return serverAnswerInfoId;
    }

    public void setServerAnswerInfoId(Long serverAnswerInfoId) {
        this.serverAnswerInfoId = serverAnswerInfoId;
    }

    public String getInputValuesDefault() {
        return inputValuesDefault;
    }

    public void setInputValuesDefault(String inputValuesDefault) {
        this.inputValuesDefault = inputValuesDefault;
    }

    public List<SurveyFormQuestionTemp> getSurveyFormQuestionTempList() {
        return surveyFormQuestionTempList;
    }

    public void setSurveyFormQuestionTempList(List<SurveyFormQuestionTemp> surveyFormQuestionTempList) {
        this.surveyFormQuestionTempList = surveyFormQuestionTempList;
    }

    public List<SurveyFormQuestion> getSurveyFormQuestionList() {
        return surveyFormQuestionList;
    }

    public void setSurveyFormQuestionList(List<SurveyFormQuestion> surveyFormQuestionList) {
        this.surveyFormQuestionList = surveyFormQuestionList;
    }
}
