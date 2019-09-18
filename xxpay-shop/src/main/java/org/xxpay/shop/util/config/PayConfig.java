package org.xxpay.shop.util.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: dingzhiwei
 * @date: 17/8/21
 * @description:
 */
@Component
@ConfigurationProperties(prefix="config.pay")
public class PayConfig {

    //支付宝商户ID
    private String aliMchId;
    //支付宝通知地址
    private String aliNotifyUrl;

    //微信商户ID
    private String wxMchId;
    //微信付款地址
    private String wxPayUrl;
    //微信商户对应appid
    private String wxAppID;
    //微信商户对应Secret
    private String wxAppSecret;
    //微信商户对应appid的OpenId
    private String wxGetOpenIdURL;
    private String wxGetOpenIdUR2;
    //微信商户对应appid的OpenId
    private String openId;
    //微信通知地址
    private String wxNotifyUrl;

    //请求私钥
    private String reqKey;
    //响应私钥
    private String resKey;
    //响应基本地址
    private String baseUrl;

    public String getAliMchId() {
        return aliMchId;
    }

    public void setAliMchId(String aliMchId) {
        this.aliMchId = aliMchId;
    }

    public String getAliNotifyUrl() {
        return aliNotifyUrl;
    }

    public void setAliNotifyUrl(String aliNotifyUrl) {
        this.aliNotifyUrl = aliNotifyUrl;
    }

    public String getWxMchId() {
        return wxMchId;
    }

    public void setWxMchId(String wxMchId) {
        this.wxMchId = wxMchId;
    }

    public String getWxPayUrl() {
        return wxPayUrl;
    }

    public void setWxPayUrl(String wxPayUrl) {
        this.wxPayUrl = wxPayUrl;
    }

    public String getWxAppID() {
        return wxAppID;
    }

    public void setWxAppID(String wxAppID) {
        this.wxAppID = wxAppID;
    }

    public String getWxAppSecret() {
        return wxAppSecret;
    }

    public void setWxAppSecret(String wxAppSecret) {
        this.wxAppSecret = wxAppSecret;
    }

    public String getWxGetOpenIdURL() {
        return wxGetOpenIdURL;
    }

    public void setWxGetOpenIdURL(String wxGetOpenIdURL) {
        this.wxGetOpenIdURL = wxGetOpenIdURL;
    }

    public String getWxGetOpenIdUR2() {
        return wxGetOpenIdUR2;
    }

    public void setWxGetOpenIdUR2(String wxGetOpenIdUR2) {
        this.wxGetOpenIdUR2 = wxGetOpenIdUR2;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getWxNotifyUrl() {
        return wxNotifyUrl;
    }

    public void setWxNotifyUrl(String wxNotifyUrl) {
        this.wxNotifyUrl = wxNotifyUrl;
    }

    public String getReqKey() {
        return reqKey;
    }

    public void setReqKey(String reqKey) {
        this.reqKey = reqKey;
    }

    public String getResKey() {
        return resKey;
    }

    public void setResKey(String resKey) {
        this.resKey = resKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}

