package com.jinjunhang.contract.model;

import java.io.Serializable;

/**
 * Created by lzn on 16/4/8.
 */
public class Product implements Serializable {
    private String mChineseName;
    private String mEnglishName;
    private String mHuohao;
    private String mXinghao;
    private double mChengbenPrice;
    private double mSellPrice;
    private String mChineseDesc;
    private String mEnglishDesc;

    public String getChineseName() {
        return mChineseName;
    }

    public String getEnglishName() {
        return mEnglishName;
    }

    public String getHuohao() {
        return mHuohao;
    }

    public String getXinghao() {
        return mXinghao;
    }

    public double getChengbenPrice() {
        return mChengbenPrice;
    }

    public double getSellPrice() {
        return mSellPrice;
    }

    public String getChineseDesc() {
        return mChineseDesc;
    }

    public String getEnglishDesc() {
        return mEnglishDesc;
    }

    public void setChineseName(String chineseName) {
        mChineseName = chineseName;
    }

    public void setEnglishName(String englishName) {
        mEnglishName = englishName;
    }

    public void setHuohao(String huohao) {
        mHuohao = huohao;
    }

    public void setXinghao(String xinghao) {
        mXinghao = xinghao;
    }

    public void setChengbenPrice(double chengbenPrice) {
        mChengbenPrice = chengbenPrice;
    }

    public void setSellPrice(double sellPrice) {
        mSellPrice = sellPrice;
    }

    public void setChineseDesc(String chineseDesc) {
        mChineseDesc = chineseDesc;
    }

    public void setEnglishDesc(String englishDesc) {
        mEnglishDesc = englishDesc;
    }
}
