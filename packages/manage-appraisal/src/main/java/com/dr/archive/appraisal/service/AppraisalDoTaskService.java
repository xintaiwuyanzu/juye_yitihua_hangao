package com.dr.archive.appraisal.service;

public interface AppraisalDoTaskService {

    String getTaskType();


    void doGetArchive4WaitAppraisal(String taskId);

}
