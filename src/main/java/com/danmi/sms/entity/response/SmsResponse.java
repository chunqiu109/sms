package com.danmi.sms.entity.response;

import com.danmi.sms.entity.FailPhone;
import lombok.Data;

import java.util.List;

@Data
public class SmsResponse {
//
//"respDesc":"请求成功。",
//"smsId":"ed4bb01827334ccaa769203db69c3240",
//        "failList":[
//    {
//        "phone":"152XXXXXXXX",
//            "respCode":"0098"
//    }
//],
//        "respCode":"0000"
//}
    private String respDesc;
    private String smsId;
    private List<FailPhone> failList;
    private String respCode;
}
