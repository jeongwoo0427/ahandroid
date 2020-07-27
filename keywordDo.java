package com.example.yournote;

public class keywordDo {


    private String iNo;
    private String iImportance;
    private String sCategory;
    private String sKeyword;
    private String sSummary;

    public void setNo(String no){
        this.iNo = no;
    }
    public void setImportance(String importance){
        this.iImportance = importance;
    }
    public void setCategory(String category){
        this.sCategory = category;
    }
    public void setKeyword(String keyword){
        this.sKeyword = keyword;
    }
    public void setSummary(String summary){
        this.sSummary = summary;
    }

    public String getNo(){
        return this.iNo;
    }
    public String getImportance(){
        return this.iImportance;
    }
    public String getCategory(){
        return this.sCategory;
    }
    public String getKeyword(){
        return this.sKeyword;
    }
    public String getSummary(){
        return this.sSummary;
    }




}
