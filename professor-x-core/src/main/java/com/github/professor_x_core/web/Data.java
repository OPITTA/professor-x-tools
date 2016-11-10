package com.github.professor_x_core.web;

import java.util.Date;

public class Data {

    private Integer id;

    private Integer reportId;

    private Integer averageRt;

    private Integer minRt;

    private Integer maxRt;

    private Integer tps;

    private Integer concurrency;

    private Integer errorRate;

    private Date createTime;

    private String startTime;

    private String endTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public Integer getAverageRt() {
        return averageRt;
    }

    public void setAverageRt(Integer averageRt) {
        this.averageRt = averageRt;
    }

    public Integer getMinRt() {
        return minRt;
    }

    public void setMinRt(Integer minRt) {
        this.minRt = minRt;
    }

    public Integer getMaxRt() {
        return maxRt;
    }

    public void setMaxRt(Integer maxRt) {
        this.maxRt = maxRt;
    }

    public Integer getTps() {
        return tps;
    }

    public void setTps(Integer tps) {
        this.tps = tps;
    }

    public Integer getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(Integer concurrency) {
        this.concurrency = concurrency;
    }

    public Integer getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(Integer errorRate) {
        this.errorRate = errorRate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

}