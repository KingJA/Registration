package com.tdr.registration.data;

import com.tdr.registration.util.Base64;
import com.tdr.registration.util.CRC16M;
import com.tdr.registration.util.TendencyEncrypt;
import com.tdr.registration.util.Utils;

/**
 * 二维码解析类
 * Created by Linus_Xie on 2016/10/27.
 */

public class ParsingQR {
    public ParsingQR() {
    }

    /**
     * 带字母的二维码
     *
     * @param scanResult
     * @return
     */
    public String plateNumber(String scanResult) {
        int i = scanResult.indexOf("?ba");
        String re = scanResult.substring(i + 3);
        String decryptResult = TendencyEncrypt.decrypt(re);
        int len = decryptResult.length();
        String code = decryptResult.substring(0, len - 4);
        String verifyCode = Base64.encode(Utils.shortToBytes(CRC16M.CalculateCrc16(code.getBytes())));
        if ((code + verifyCode).equals(decryptResult)) {
            String content = new String(Base64.decode(code));
            String plateNumber = content.substring(4);
            return plateNumber;
        } else {
            return "-1";
        }
    }

    /**
     * 带设备类型的便签
     * @param scanRsult
     * @return
     */
    public int labelNumber(String scanRsult){
        return 0;
    }
}
