package com.danmi.sms.entity;

import lombok.Data;

@Data
public class Phone {
//    "area":"1","areaName":"北京","provider":0,"providerName":"联通"
    private String area;
    private String areaName;
    private String provider;
    private String providerName;
}
