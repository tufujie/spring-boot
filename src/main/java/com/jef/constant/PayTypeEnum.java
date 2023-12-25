package com.jef.constant;

public enum PayTypeEnum {

    WEIXIN("weixin", "微信支付"),
    ALI("ali", "支付宝支付"),
    ;

    private String type;

    private String typeDesc;

    PayTypeEnum(String type, String typeDesc) {
        this.type = type;
        this.typeDesc = typeDesc;
    }

    public String getType() {
        return type;
    }
}
