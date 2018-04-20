package com.tdr.registration.data;

import android.util.Log;

import com.tdr.registration.ble.utils.HexUtil;
import com.tdr.registration.gprinter.Util;
import com.tdr.registration.model.BlackCarModel;
import com.tdr.registration.model.ElectricCarModel;
import com.tdr.registration.util.CRC16M;
import com.tdr.registration.util.SharedPreferencesUtils;
import com.tdr.registration.util.Utils;
import com.tdr.registration.util.mLog;

import java.io.UnsupportedEncodingException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 蓝牙数据包处理类
 * Created by Linus_Xie on 2016/10/25.
 */

public class PacketData {

    public PacketData() {
    }

    public byte[] frameFlag = {(byte) 0xAA};
    //命令字
    public byte[] queryKeyCommand = {(byte) 0x01};//查询设备配置key
    public byte[] sendKeyCommand = {(byte) 0x02};//下发配置key
    public byte[] deleteOnePlateNumberCommand = {(byte) 0x03};//删除一个车牌
    public byte[] deleteAll = {(byte) 0x04};//清空所有车牌
    public byte[] controlCommand = {(byte) 0x05};//控制报警
    public byte[] quitLocked = {(byte) 0x06};//退出锁定状态
    public byte[] queryKeyVersion = {(byte) 0x07};//查询设备版本号

    public byte[] getDeviceTime = {(byte) 0x3f};//打包开始下发
    public byte[] setDeviceTime = {(byte) 0x40};//打包开始下发
    public byte[] queryCar = {(byte) 0x41};//打包开始下发
    public byte[] getGatewayDate = {(byte) 0x42};//打包开始下发
    public byte[] setGatewayTime = {(byte) 0x43};//打包开始下发
    public byte[] GatewayDate = {(byte) 0x44};//打包开始下发
    public byte[] GatewayLowPower = {(byte) 0x45};//打包开始下发

    public byte[] packagingStart = {(byte) 0x51};//打包开始下发
    public byte[] packagingComplete = {(byte) 0x52};//打包下发完成
    public byte[] packagingSend = {(byte) 0x53};//打包下发内容

    public byte[] updateStart = {(byte) 0x61};//更新包开始下发
    public byte[] updateComplete = {(byte) 0x62};//更新包下发完成
    public byte[] updateSend = {(byte) 0x63};//更新包下发内容

    public byte[] aerialSignal = {(byte) 0x71};//空中信号开关

    public byte[] uploadState = {(byte) 0x80};//上传搜索枪状态
    public byte[] uploadHeartBeat = {(byte) 0x81};//透传标签心跳
    public byte[] uploadStateSecond = {(byte) 0x82};//上传搜索枪状态2

    public byte[] uploadaerialSignal = {(byte) 0x83};//上传空中信号值

    public byte[] noneData = {(byte) 0x00};

    public byte[] Update840PD2 = {(byte) 0x24};//更新频点



    //================================打包蓝牙数据===============================================

    /**
     * 查询Key
     * 0x01
     *
     * @return
     */
    public byte[] queryKey() {
        byte[] requestData = null;//全部内容
        requestData = Utils.ByteArrayCopy(requestData, frameFlag);
        requestData = Utils.ByteArrayCopy(requestData, queryKeyCommand);
        byte[] content = new byte[16];
        requestData = Utils.ByteArrayCopy(requestData, content);
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(requestData, 1, 17)));
        requestData = Utils.ByteArrayCopy(requestData, verifyCode);
        return requestData;
    }
    /**
     * 下发频点更新
     * 0x24
     *
     * @return
     */
    public byte[] send840PD2() {
        byte[] requestData = null;//全部内容
        requestData = Utils.ByteArrayCopy(requestData, Update840PD2);
        byte[] byteKey = HexUtil.hexStringToBytes("37090418");
        for (int i = byteKey.length; i < 16; i++) {
            byteKey = Utils.ByteArrayCopy(byteKey, noneData);
        }
        requestData = Utils.ByteArrayCopy(requestData, byteKey);
        return requestData;
    }

    /**
     * 下发key
     * 0x02
     *
     * @return
     */
    public byte[] sendKey() {
        byte[] requestData = null;//全部内容
        String key = (String) SharedPreferencesUtils.get("key", "");
        byte[] byteKey = HexUtil.hexStringToBytes(key);
        requestData = Utils.ByteArrayCopy(requestData, frameFlag);
        requestData = Utils.ByteArrayCopy(requestData, sendKeyCommand);
        for (int i = byteKey.length; i < 16; i++) {
            byteKey = Utils.ByteArrayCopy(byteKey, noneData);
        }
        requestData = Utils.ByteArrayCopy(requestData, byteKey);
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(requestData, 1, 17)));
        requestData = Utils.ByteArrayCopy(requestData, verifyCode);
        return requestData;
    }

    /**
     * 删除某一个车牌
     * 0x03
     *
     * @param plateNumber
     * @return
     */
    public byte[] delOnePlateNumber(String plateNumber) {
        byte[] requestData = null;//全部内容
        byte[] byteKey = HexUtil.hexStringToBytes(plateNumber);
        requestData = Utils.ByteArrayCopy(requestData, frameFlag);
        requestData = Utils.ByteArrayCopy(requestData, deleteOnePlateNumberCommand);
        byte[] content = new byte[16];
        content = byteKey;
        requestData = Utils.ByteArrayCopy(requestData, byteKey);
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(requestData, 1, 17)));
        requestData = Utils.ByteArrayCopy(requestData, verifyCode);
        return requestData;
    }

    /**
     * 删除某一个车牌
     * 0x04
     *
     * @return
     */
    public byte[] delAll() {
        byte[] requestData = null;//全部内容
        requestData = Utils.ByteArrayCopy(requestData, frameFlag);
        requestData = Utils.ByteArrayCopy(requestData, deleteAll);
        byte[] content = new byte[16];
        requestData = Utils.ByteArrayCopy(requestData, content);
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(requestData, 1, 17)));
        requestData = Utils.ByteArrayCopy(requestData, verifyCode);
        return requestData;
    }

    /**
     * 控制报警
     * 0x05
     *
     * @param plateNumber
     * @return
     */
    public byte[] controlCommand(String plateNumber) {
        byte[] requestData = null;//全部内容
        requestData = Utils.ByteArrayCopy(requestData, frameFlag);
        requestData = Utils.ByteArrayCopy(requestData, controlCommand);
        return requestData;
    }

    /*public byte[] packageSendContent() {
        byte[] requestData = null;//全部内容
        byte[] empty = {(byte) 0x00};//空
        byte[] SSCode = {(byte) 0x01};//省市区编码下发命令，省(1) +  省拼音(2) + 市(1) + 市拼音(2) + 区数量(1)+区编码(2*N)
        byte[] deviceType = {(byte) 0x02};//设备类型下发，设备类型1显示内容(10，显示字符如"laoren")+设备类型数量(1)+设备类型(2)*N
        byte[] voiceSetting = {(byte) 0x03};//声音设定，声音设定(1，0关，1开))
        byte[] thoroughly = {(byte) 0x04};//透传模式设定，透传模式开关(1，0关，1开)
        byte[] sendBlackCar = {(byte) 0x05};//黑车下发内容，省(1) + 市(1) +显示字符（10字节.ACSII编号字符)+设备类型1（2字节）+车牌id1(4，低位在前)+设备类型2（2字节）+车牌id2(4，低位在前)
        byte[] lockedBlackCar = {(byte) 0x06};//锁定黑车，省(1) + 市(1) +显示字符（10字节.ACSII编号字符)+设备类型1（2字节）+车牌id1(4，低位在前)+设备类型2（2字节）+车牌id2(4，低位在前)
        String pcCode = (String) SharedPreferencesUtils.get("pcCode", "");//省市编码，云南昆明：0701
        String provinceAbbr = (String) SharedPreferencesUtils.get("provinceAbbr", "");//YN
        String cityAbbr = (String) SharedPreferencesUtils.get("cityAbbr", "");//KM
        String device = (String) SharedPreferencesUtils.get("deviceType", "");//8001,8002
        String content = (String) SharedPreferencesUtils.get("content", "");
        String sheng = pcCode.substring(1, 2);
        String shi = pcCode.substring(3);
        Log.e("Pan", "pcCode:" + pcCode);
        Log.e("Pan", "provinceAbbr:" + provinceAbbr);
        Log.e("Pan", "cityAbbr:" + cityAbbr);
        Log.e("Pan", "device:" + device);
        Log.e("Pan", "content:" + content);
        Log.e("Pan", "sheng:" + sheng);
        Log.e("Pan", "shi:" + shi);
        byte[] province = null;
        byte[] city = null;
        byte[] show = null;
        try {
            province = provinceAbbr.getBytes("ASCII");
            Log.e("Pan", "province=" + Utils.bytesToHexString(province));
            city = cityAbbr.getBytes("ASCII");
            Log.e("Pan", "city=" + Utils.bytesToHexString(city));
            show = content.getBytes("ASCII");
            Log.e("Pan", "show=" + Utils.bytesToHexString(show));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] send01 = null;
        send01 = Utils.ByteArrayCopy(send01, Utils.hexStr2Bytes(sheng));
        Log.e("Pan", "省编号=" + Utils.bytesToHexString(send01));
        send01 = Utils.ByteArrayCopy(send01, province);
        Log.e("Pan", "省拼音=" + Utils.bytesToHexString(send01));
        send01 = Utils.ByteArrayCopy(send01, Utils.hexStr2Bytes(shi));
        Log.e("Pan", "市编号=" + Utils.bytesToHexString(send01));
        send01 = Utils.ByteArrayCopy(send01, city);
        send01 = Utils.ByteArrayCopy(send01, empty);
        send01 = Utils.ByteArrayCopy(send01, empty);
        Log.e("Pan", "市编号=" + Utils.bytesToHexString(send01));
        byte[] size01 = Utils.shortToByte((short) send01.length);
        Log.e("Pan", "省市编码=" + Utils.bytesToHexString(size01));


        send01 = Utils.ByteArrayCopy(size01, send01);
        send01 = Utils.ByteArrayCopy(SSCode, send01);
        Log.e("Pan", "send01=" + Utils.bytesToHexString(send01));
        byte[] send02 = null;
        for (int i = show.length; i < 10; i++) {
            show = Utils.ByteArrayCopy(show, noneData);
        }
        send02 = Utils.ByteArrayCopy(send02, show);
        int length = device.length();//设备类型长度
        send02 = Utils.ByteArrayCopy(send02, Utils.intTobyte(length));
        for (int j = 0; j < device.split(",").length; j++) {
            send02 = Utils.ByteArrayCopy(send02, Utils.hexStr2Bytes(device.split(",")[j]));
        }
        byte[] size02 = Utils.shortToByte((short) send02.length);
        send02 = Utils.ByteArrayCopy(size02, send02);
        send02 = Utils.ByteArrayCopy(deviceType, send02);
        Log.e("Pan", "send02=" + Utils.bytesToHexString(send02));
        byte[] send03 = {(0x01)};
        byte[] size03 = Utils.shortToByte((short) send03.length);
        send03 = Utils.ByteArrayCopy(size03, send03);
        send03 = Utils.ByteArrayCopy(voiceSetting, send03);
        Log.e("Pan", "send03=" + Utils.bytesToHexString(send03));

        byte[] send04 = {(0x01)};
        byte[] size04 = Utils.shortToByte((short) send04.length);
        send04 = Utils.ByteArrayCopy(size04, send04);
        send04 = Utils.ByteArrayCopy(thoroughly, send04);
        Log.e("Pan", "send04=" + Utils.bytesToHexString(send04));

        requestData = Utils.ByteArrayCopy(requestData, send01);
        Log.e("Pan", "SendContent=" + Utils.bytesToHexString(requestData));
        requestData = Utils.ByteArrayCopy(requestData, send02);
        Log.e("Pan", "SendContent=" + Utils.bytesToHexString(requestData));
        requestData = Utils.ByteArrayCopy(requestData, send03);
        Log.e("Pan", "SendContent=" + Utils.bytesToHexString(requestData));
        requestData = Utils.ByteArrayCopy(requestData, send04);
        Log.e("Pan", "SendContent=" + Utils.bytesToHexString(requestData));

        String aa = Utils.bytesToHexString(requestData);
        return requestData;

    }*/

    public byte[] packageSendContent() {
        byte[] requestData = null;//全部内容
        byte[] empty = {(byte) 0x00};//空
        byte[] SSCode = {(byte) 0x01};//省市区编码下发命令，省(1) +  省拼音(2) + 市(1) + 市拼音(2) + 区数量(1)+区编码(2*N)
        byte[] deviceType = {(byte) 0x02};//设备类型下发，设备类型1显示内容(10，显示字符如"laoren")+设备类型数量(1)+设备类型(2)*N
        byte[] voiceSetting = {(byte) 0x03};//声音设定，声音设定(1，0关，1开))
        byte[] thoroughly = {(byte) 0x04};//透传模式设定，透传模式开关(1，0关，1开)
        byte[] sendBlackCar = {(byte) 0x05};//黑车下发内容，省(1) + 市(1) +显示字符（10字节.ACSII编号字符)+设备类型1（2字节）+车牌id1(4，低位在前)+设备类型2（2字节）+车牌id2(4，低位在前)
        byte[] lockedBlackCar = {(byte) 0x06};//锁定黑车，省(1) + 市(1) +显示字符（10字节.ACSII编号字符)+设备类型1（2字节）+车牌id1(4，低位在前)+设备类型2（2字节）+车牌id2(4，低位在前)
        String pcCode = (String) SharedPreferencesUtils.get("pcCode", "");//省市编码，云南昆明：0701
//        pcCode="0701";
        String provinceAbbr = (String) SharedPreferencesUtils.get("provinceAbbr", "");//YN
//        provinceAbbr="YN";
        String cityAbbr = (String) SharedPreferencesUtils.get("cityAbbr", "");//KM
//        cityAbbr="KM";
        String device = (String) SharedPreferencesUtils.get("deviceType", "");//8001,8002
        String content = (String) SharedPreferencesUtils.get("content", "");
        String XQCode = (String) SharedPreferencesUtils.get("XQCode", "");//区数量
        String sheng = pcCode.substring(0, 2);
        String shi = pcCode.substring(2);
//        Log.e("Pan", "pcCode:" + pcCode);
//        Log.e("Pan", "provinceAbbr:" + provinceAbbr);
//        Log.e("Pan", "cityAbbr:" + cityAbbr);
//        Log.e("Pan", "device:" + device);
//        Log.e("Pan", "content:" + content);
//        Log.e("Pan", "sheng:" + sheng);
//        Log.e("Pan", "shi:" + shi);
        byte[] XQCodeSize;
        byte[] DevicesSize;
        byte[] Package1Size;
        byte[] Package2Size;
        byte[] Package3Size;
        byte[] Package4Size;
        byte[] Package5Size;
        String[] xqcode=null;
        String[] Devices=null;
        int Package1_Size = 7;//长度最低7位
        int Package2_Size = 11;//长度最低11位
        int Package3_Size = 1;//长度最低1位
        int Package4_Size = 1;//长度最低1位
        int Package5_Size = 4;//长度最低1位
        int XQsize=0;
        int DeviceSize=0;
        if(!XQCode.equals("")){
            xqcode = XQCode.split(",");//拆分区编码

            XQsize = xqcode.length;//区数量
            Package1_Size += (xqcode.length * 2);//增加总长度 区编码*2
        }
        if(!device.equals("")){
            Devices =device.split(",");
            DeviceSize=Devices.length;
            Package2_Size+=(Devices.length*2);
        }
        XQCodeSize= Utils.intTobyte2(XQsize);
        DevicesSize= Utils.intTobyte2(DeviceSize);
//        Log.e("Pan", Utils.bytesToHexString(XQCodeSize)+"   区数量=" + XQsize);
//        Log.e("Pan", Utils.bytesToHexString(DevicesSize)+"   设备数量=" + DeviceSize);

        Package1Size= Utils.intTobyte(Package1_Size);
        Package2Size= Utils.intTobyte(Package2_Size);
        Package3Size= Utils.intTobyte(Package3_Size);
        Package4Size= Utils.intTobyte(Package4_Size);
        Package5Size= Utils.intTobyte(Package5_Size);
//        Log.e("Pan", "Package1Size=" + Utils.bytesToHexString(Package1Size));
//        Log.e("Pan", "Package2Size=" + Utils.bytesToHexString(Package2Size));
        byte[] province = null;
        byte[] city = null;
        byte[] show = null;
        try {
            province = provinceAbbr.getBytes("ASCII");
//            Log.e("Pan", "province=" + Utils.bytesToHexString(province));
            city = cityAbbr.getBytes("ASCII");
//            Log.e("Pan", "city=" + Utils.bytesToHexString(city));
            show = content.getBytes("ASCII");
//            Log.e("Pan", "show=" + Utils.bytesToHexString(show));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        省(1) +  省拼音(2) + 市(1) + 市拼音(2) + 区数量(1)+区编码(2*N)
        //------------------------开始编码x01-----------------------
        byte[] send01 =  {(0x01)};
        send01 = Utils.ByteArrayCopy(send01, Package1Size);
//        Log.e("Pan", "长度=" + Utils.bytesToHexString(send01));
//        if(Package1Size.length<=2){
//            send01 = Utils.ByteArrayCopy(send01, empty);
//            Log.e("Pan", "长度补位=" + Utils.bytesToHexString(send01));
//        }
        send01 = Utils.ByteArrayCopy(send01, Utils.hexStr2Bytes(sheng));
//        Log.e("Pan", "省编号=" + Utils.bytesToHexString(send01));

        send01 = Utils.ByteArrayCopy(send01, province);
//        Log.e("Pan", "省拼音=" + Utils.bytesToHexString(send01));

        send01 = Utils.ByteArrayCopy(send01, Utils.hexStr2Bytes(shi));
//        Log.e("Pan", "市编号=" + Utils.bytesToHexString(send01));

        send01 = Utils.ByteArrayCopy(send01, city);
//        Log.e("Pan", "市拼音=" + Utils.bytesToHexString(send01));
//
        send01 = Utils.ByteArrayCopy(send01, XQCodeSize);
//        Log.e("Pan", "区数量=" + Utils.bytesToHexString(send01));
        if(xqcode!=null){
            for (int i = 0; i < xqcode.length; i++) {
//                send01 = Utils.ByteArrayCopy(send01, Utils.hexStr2Bytes(xqcode[i]));
                try {
                    send01 = Utils.ByteArrayCopy(send01, xqcode[i].getBytes("ASCII"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                Log.e("Pan", "添加区编码");
            }
        }
        Log.e("Pan", "x01编码完成=" + Utils.bytesToHexString(send01));
        //------------------------------------------------------ 0700 08 4758 02 4c5a 00

        //------------------------开始编码x02------------------------------
        byte[] send02 =  {(0x02)};
//        设备类型1显示内容(10，显示字符如"laoren")+设备类型数量(1)+设备类型(2)*N
        send02 = Utils.ByteArrayCopy(send02, Package2Size);
//        Log.e("Pan", "包2总长=" + Utils.bytesToHexString(send02));
        for (int i = show.length; i < 10; i++) {
            show = Utils.ByteArrayCopy(noneData,show);
        }
        send02 = Utils.ByteArrayCopy(send02, show);
//        Log.e("Pan", "补位显示内容=" + Utils.bytesToHexString(send02));
        send02 = Utils.ByteArrayCopy(send02, DevicesSize);
//        Log.e("Pan", "添加设备类型数量" + Utils.bytesToHexString(send02));
        if(Devices!=null){
            for (int i = 0; i < Devices.length; i++) {
                String d= Utils.HL_Transposition(Devices[i]);
                send02 = Utils.ByteArrayCopy(send02, Utils.hexStr2Bytes(d));
//                Log.e("Pan", "添加设备类型数量" + Utils.bytesToHexString(send02));
            }
        }
        Log.e("Pan", "x02编码完成=" + Utils.bytesToHexString(send02));
        //------------------------------------------------------ 0f00 0000000000454c444552 02 0180 0280

        //------------------------开始编码x03------------------------------
        byte[] send03 =  {(0x03)};
        send03 = Utils.ByteArrayCopy(send03, Package3Size);
        send03 = Utils.ByteArrayCopy(send03,Utils.intTobyte2(1));
//        Log.e("Pan", "send03=" + Utils.bytesToHexString(send03));

        byte[] send04 = {(0x04)};
        send04 = Utils.ByteArrayCopy(send04, Package4Size);
        send04 = Utils.ByteArrayCopy(send04, Utils.intTobyte2(1));
//        Log.e("Pan", "send04=" + Utils.bytesToHexString(send04));

        byte[] send24 = {(0x12)};
        send24 = Utils.ByteArrayCopy(send24, Package5Size);
        send24 = Utils.ByteArrayCopy(send24,  Utils.hexStr2Bytes("37090418"));

        requestData = Utils.ByteArrayCopy(requestData, send01);
//        Log.e("Pan", "SendContent=" + Utils.bytesToHexString(requestData));
        requestData = Utils.ByteArrayCopy(requestData, send02);
//        Log.e("Pan", "SendContent=" + Utils.bytesToHexString(requestData));
        requestData = Utils.ByteArrayCopy(requestData, send03);
//        Log.e("Pan", "SendContent=" + Utils.bytesToHexString(requestData));
        requestData = Utils.ByteArrayCopy(requestData, send04);
    if(((String)SharedPreferencesUtils.get("locCityName", "")).contains("贵港")){
        requestData = Utils.ByteArrayCopy(requestData, send24);
        Log.e("Pan", "SendContent=" + Utils.bytesToHexString(requestData));
        Log.e("Pan", "send24=" + Utils.bytesToHexString(send24));
    }
//        String aa = Utils.bytesToHexString(requestData);
//        Log.e("Pan", "数据打包：" + aa);
        return requestData;
    }
    /**
     * 获取现在时间
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yy:MM:dd:HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    public void AnalysisDate(byte[] Date){
//        aa44 40e20100 000101001327 34 b3 1b486d38 b921
        String date=Utils.bytesToHexString(Date);
        Log.e("Bluetooth","接收到的数据： "+date);

        String GatewayID = date.substring(4,13);//网关ID
        Log.e("Bluetooth","GatewayID： "+GatewayID);

        String GatewayTime = date.substring(13,25);//网关时间
        Log.e("Bluetooth","GatewayTime： "+GatewayTime);

        String GatewayRSSI = date.substring(25,27);//网关信号
        Log.e("Bluetooth","GatewayRSSI： "+GatewayRSSI);

        String GatewayElectricity = date.substring(27,29);//网关电量
        Log.e("Bluetooth","GatewayElectricity： "+GatewayElectricity);

        byte[] sendMSG = {(0x44)};

        sendMSG = Utils.ByteArrayCopy(frameFlag,sendMSG);
        sendMSG = Utils.ByteArrayCopy(sendMSG,Utils.intTobyte2(1));


    }
    public void AnalysisCarDate(byte[] Date){
//        aa44 40e20100 0001 01001327 34b31b486d38 b921
//        网关ID(4)+标签类型(2)+标签ID(4)+标签最后出现时间(6,年月日时分秒，全为ff表示查询超时，全为0表示标签不存在)

        String date=Utils.bytesToHexString(Date);
        Log.e("Bluetooth","接收到的数据： "+date);

        String GatewayID = date.substring(0,8);//网关ID
        Log.e("Bluetooth","GatewayID： "+GatewayID);

        String LableType = date.substring(8,12);//标签类型
        Log.e("Bluetooth","LableType： "+LableType);

        String LableID= date.substring(12,20);//标签ID
        Log.e("Bluetooth","LableID： "+LableID);

//        String LableLastTime = date.substring(24,36);//标签最后时间
        String LableLastTime=analysisDeviceTime(Date);
        Log.e("Bluetooth","LableLastTime： "+LableLastTime);

    }

    /**
     * 回复设备数据是否接收成功
     * @param num 0成功 1失败
     * @return
     */
    public byte[] AnalysisDateReturn(int num){
//        aa44 40e20100 000101001327 34 b3 1b486d38 b921
        byte[] sendMSG = {(0x44)};
        byte[] empty = {(byte) 0x00};//空
        sendMSG = Utils.ByteArrayCopy(frameFlag,sendMSG);
        sendMSG = Utils.ByteArrayCopy(sendMSG,Utils.intTobyte2(1));
        for (int i = 0; i <15 ; i++) {
            sendMSG = Utils.ByteArrayCopy(sendMSG,empty);
        }
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(sendMSG, 1, 17)));
        sendMSG = Utils.ByteArrayCopy(sendMSG, verifyCode);
        return sendMSG;
    }
    /**
     * 读取稽查抢时间
     * @return
     */
    public byte[] getDeviceTime() {
        byte[] empty = {(byte) 0x00};//空
        byte[] sendMSG = {(0x3f)};
        sendMSG = Utils.ByteArrayCopy(frameFlag,sendMSG);
        for (int i = 0; i <16 ; i++) {
            sendMSG = Utils.ByteArrayCopy(sendMSG,empty);
        }
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(sendMSG, 1, 17)));
        sendMSG = Utils.ByteArrayCopy(sendMSG, verifyCode);
        Log.e("Bluetooth", "3f下发打包开始命令: " + Utils.bytesToHexString(sendMSG));
        return sendMSG;
    }

    /**
     * 解析稽查枪时间
     * @param time
     * @return
     */
    public String analysisDeviceTime(byte[] time) {
//        aa44 11031c0f3237 00000000000000000000 b921
        String Time=Utils.bytesToHexString(time);
//        String Time="aa4411031c0f323700000000000000000000b921";
        String t=Time.substring(4,16);

        String Y=t.substring(0,2);
        int YY = Integer.parseInt(Y, 16);
        String M=t.substring(2,4);
        int MM = Integer.parseInt(M, 16);
        String D=t.substring(4,6);
        int DD = Integer.parseInt(D, 16);
        String h=t.substring(6,8);
        int hh = Integer.parseInt(h, 16);
        String m=t.substring(8,10);
        int mm = Integer.parseInt(m, 16);
        String s=t.substring(10);
        int ss = Integer.parseInt(s, 16);
        Log.e("Bluetooth","接收到的数据： "+YY+":"+MM+":"+DD+":"+hh+":"+mm+":"+ss);

        return Time;
    }

    /**
     * 设置稽查枪时间
     * @return
     */
    public byte[] setDeviceTime() {
        byte[] sendMSG = {(0x40)};
        byte[] empty = {(byte) 0x00};//空
        String GatewayTime = "";//时间
        sendMSG = Utils.ByteArrayCopy(frameFlag,sendMSG);
        GatewayTime = getStringDate();
        String time[]=GatewayTime.split(":");
        byte[] Time=null;
        for (int i = 0; i <time.length ; i++) {
            int t=Integer.parseInt(time[i]);
            Time = Utils.ByteArrayCopy(Time, Utils.intTobyte2(t));
        }
        sendMSG = Utils.ByteArrayCopy(sendMSG, Time);
        for (int i = 0; i <10 ; i++) {
            sendMSG = Utils.ByteArrayCopy(sendMSG,empty);
        }
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(sendMSG, 1, 17)));
        sendMSG = Utils.ByteArrayCopy(sendMSG, verifyCode);
        Log.e("Bluetooth", "40下发打包开始命令: " + Utils.bytesToHexString(sendMSG));
        return sendMSG;
    }

    /**
     *  查询黑车
     * @param GatewayID    网关ID
     * @param LableType 标签类型
     * @param LableID   标签ID
     * @return
     */
    public byte[] queryCar(int GatewayID,int LableType ,int LableID) {
//        网关ID(4)+标签类型(2)+标签ID(4)
        byte[] sendMSG = {(0x41)};
        byte[] empty = {(byte) 0x00};//空
//        aa41 40e20100 551f 00006100 00000000000000002cb0
        sendMSG = Utils.ByteArrayCopy(frameFlag,sendMSG);

        byte[] gatewayid = Utils.intToByte(GatewayID);
        int gatewayid_off=4-gatewayid.length;
        if(gatewayid_off>0){
            for (int i = 0; i <gatewayid_off ; i++) {
                gatewayid = Utils.ByteArrayCopy(empty, gatewayid);
            }
        }
        Log.e("Bluetooth", "gatewayid: " + Utils.bytesToHexString(gatewayid));
        sendMSG = Utils.ByteArrayCopy(sendMSG,gatewayid);

        byte[] labletype = Utils.intTobyte(LableType);
        int labletype_off=2-labletype.length;
        if(labletype_off>0){
            for (int i = 0; i <labletype_off ; i++) {
                labletype = Utils.ByteArrayCopy(empty, labletype);
            }
        }
        Log.e("Bluetooth", "labletype: " + Utils.bytesToHexString(labletype));
        sendMSG = Utils.ByteArrayCopy(sendMSG,labletype);

        byte[] lableid = Utils.intToByte(LableID);
        int lableid_off=4-lableid.length;
        if(lableid_off>0){
            for (int i = 0; i <lableid_off ; i++) {
                lableid = Utils.ByteArrayCopy(empty, lableid);
            }
        }
        Log.e("Bluetooth", "lableid: " + Utils.bytesToHexString(lableid));
        sendMSG = Utils.ByteArrayCopy(sendMSG,lableid);

        for (int i = 0; i <6 ; i++) {
            sendMSG = Utils.ByteArrayCopy(sendMSG,empty);
        }

        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(sendMSG, 1, 17)));
        sendMSG = Utils.ByteArrayCopy(sendMSG, verifyCode);
        Log.e("Bluetooth", "41下发打包开始命令: " + Utils.bytesToHexString(sendMSG));
        return sendMSG;
    }

    /**
     * 获取网关信息
     * @param GatewayID 网关ID
     * @return
     */
    public byte[] getGatewayDate(int GatewayID) {
        byte[] sendMSG = {(0x42)};
        byte[] empty = {(byte) 0x00};//空
//        aa42 00010001 0000000000000000000000002345
        sendMSG = Utils.ByteArrayCopy(frameFlag,sendMSG);

        byte[] gatewayid = Utils.intToByte(GatewayID);

        int gatewayid_off=4-gatewayid.length;
        if(gatewayid_off>0){
            for (int i = 0; i <gatewayid_off ; i++) {
                gatewayid = Utils.ByteArrayCopy(empty, gatewayid);
            }
        }
        sendMSG = Utils.ByteArrayCopy(sendMSG,gatewayid);

        for (int i = 0; i <12 ; i++) {
            sendMSG = Utils.ByteArrayCopy(sendMSG,empty);
        }
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(sendMSG, 1, 17)));
        sendMSG = Utils.ByteArrayCopy(sendMSG, verifyCode);
        Log.e("Bluetooth", "42下发打包开始命令: " + Utils.bytesToHexString(sendMSG));
        return sendMSG;
    }

    /**
     * 设置网关时间
     * @param GatewayID 网关ID
     * @return
     */
    public byte[] setGatewayTime(int GatewayID) {
        byte[] sendMSG = {(0x43)};
        byte[] empty = {(byte) 0x00};//空
        String GatewayTime = "";//时间
        GatewayTime = getStringDate();
        String time[]=GatewayTime.split(":");
        sendMSG = Utils.ByteArrayCopy(frameFlag,sendMSG);

        byte[] gatewayid = Utils.intToByte(GatewayID);
        int gatewayid_off=4-gatewayid.length;
        if(gatewayid_off>0){
            for (int i = 0; i <gatewayid_off ; i++) {
                gatewayid = Utils.ByteArrayCopy(empty, gatewayid);
            }
        }
        sendMSG = Utils.ByteArrayCopy(sendMSG,gatewayid);


        byte[] Time=null;
        for (int i = 0; i <time.length ; i++) {
            int t=Integer.parseInt(time[i]);
            Time = Utils.ByteArrayCopy(Time, Utils.intTobyte2(t));
        }
        sendMSG = Utils.ByteArrayCopy(sendMSG, Time);

        for (int i = 0; i <6 ; i++) {
            sendMSG = Utils.ByteArrayCopy(sendMSG,empty);
        }
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(sendMSG, 1, 17)));
        sendMSG = Utils.ByteArrayCopy(sendMSG, verifyCode);
        Log.e("Bluetooth", "43下发打包开始命令: " + Utils.bytesToHexString(sendMSG));
        return sendMSG;
    }

    public byte[] newDevicePackage(int flag) {
        byte[] sendMSG=null;
        byte[] send44 = {(0x44)};
        byte[] send45 = {(0x45)};
        byte[] empty = {(byte) 0x00};//空
//        0x44	网关ID(4)+网关时间(6)+网关RSSI(1)+网关电量(1,3V=150,  上传的电量除于50为实际电量（单位V))
        //44 00001002 170328151644 0a 96
        String GatewayID = "1002";//网关ID
        String GatewayTime = "";//网关时间
        int GatewayRSSI = 10;//网关信号
        int GatewayElectricity = 150;//网关电量

        GatewayTime = getStringDate();
        String time[]=GatewayTime.split(":");
        byte[] Time=null;
        for (int i = 0; i <time.length ; i++) {
            int t=Integer.parseInt(time[i]);
            Time = Utils.ByteArrayCopy(Time, Utils.intTobyte2(t));
        }
        Log.e("Bluetooth", "x44编码GatewayTime=" + Utils.bytesToHexString(Time));


        byte[] gatewayid = Utils.hexStr2Bytes(GatewayID);
        int off=4-gatewayid.length;
        if(off>0){
            for (int i = 0; i <off ; i++) {
                gatewayid = Utils.ByteArrayCopy(empty, gatewayid);
            }
        }


        if(flag==44){
            sendMSG=send44;
        }else{
            sendMSG=send45;
        }

        sendMSG = Utils.ByteArrayCopy(sendMSG, gatewayid);
        Log.e("Bluetooth", "x44编码GatewayID=" + Utils.bytesToHexString(sendMSG));

        sendMSG = Utils.ByteArrayCopy(sendMSG, Time);
        Log.e("Bluetooth", "x44编码GatewayTime=" + Utils.bytesToHexString(sendMSG));

        sendMSG = Utils.ByteArrayCopy(sendMSG, Utils.intTobyte2(GatewayRSSI));
        Log.e("Bluetooth", "x44编码GatewayRSSI=" + Utils.bytesToHexString(sendMSG));

        sendMSG = Utils.ByteArrayCopy(sendMSG, Utils.intTobyte2(GatewayElectricity));
        Log.e("Bluetooth", "x44编码GatewayElectricity=" + Utils.bytesToHexString(sendMSG));

        return sendMSG;
    }

    public byte[] lockedOnePlateNumber(BlackCarModel model) {
        byte[] requestData = null;
        String pcCode = (String) SharedPreferencesUtils.get("pcCode", "");//省市编码，云南昆明：0701
        byte[] childContent = null;
        byte[] content = null;
        byte[] plate = new byte[10];
        String plateNumber = model.getPLATENUMBER();
        String theftNo = Utils.initNullStr(model.getTHEFTNO());//840,920
        String theftNo2 = Utils.initNullStr(model.getTHEFTNO2());//2.4G
        while (plateNumber.length() < 10) {
            plateNumber = "" + plateNumber;
        }
        plate = plateNumber.getBytes();
        byte[] no1 = Utils.longToByte(Long.valueOf(theftNo));
        byte[] tag1 = Utils.GetByteArrayByLength(no1, 4, 2);
        byte[] tagId1 = Utils.GetByteArrayByLength(no1, 0, 4);
        byte[] no2 = Utils.longToByte(Long.valueOf(theftNo2));
        byte[] tag2 = Utils.GetByteArrayByLength(no2, 4, 2);
        byte[] tagId2 = Utils.GetByteArrayByLength(no2, 0, 4);
        childContent = Utils.ByteArrayCopy(childContent, Utils.hexStr2Bytes(pcCode));
        childContent = Utils.ByteArrayCopy(childContent, plate);
        childContent = Utils.ByteArrayCopy(childContent, tag1);
        childContent = Utils.ByteArrayCopy(childContent, tagId1);
        childContent = Utils.ByteArrayCopy(childContent, tag2);
        childContent = Utils.ByteArrayCopy(childContent, tagId2);
        byte[] size = Utils.shortToByte((short) childContent.length);
        content = Utils.ByteArrayCopy(content, quitLocked);
        content = Utils.ByteArrayCopy(content, size);
        content = Utils.ByteArrayCopy(content, childContent);
        requestData = Utils.ByteArrayCopy(requestData, content);
        Utils.bytesToHexString(requestData);
        return requestData;
    }

    /**
     * 打包开始下发
     * 0x51 ,0x52
     *
     * @return
     */
    public byte[] sendPackageBegin(int flag, byte[] data) {
        Log.e("Pan", "1sendPackageBegin 51_52:" + data.toString());
        byte[] requestData = null;//全部内容
        byte[] content = null;
        byte[] contentLength = null;
        while (data.length % 12 != 0) {
            data = Utils.ByteArrayCopy(data, noneData);
        }

        contentLength = Utils.intToByte(data.length);
        //byte[] totalPackage = Utils.intToByte((int) Math.ceil((short) data.length / 12));

        short length = (short) Math.ceil((short) data.length / 12);//总共需要发送的包数
        if (data.length % 12 != 0) {
            length += 1;
        }
        byte[] totalPackage = Utils.intToByte(length);
        byte[] allContentVerifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(data));
        content = Utils.ByteArrayCopy(content, contentLength);
        content = Utils.ByteArrayCopy(content, totalPackage);
        content = Utils.ByteArrayCopy(content, allContentVerifyCode);
        for (int i = content.length; i < 16; i++) {
            content = Utils.ByteArrayCopy(content, noneData);
        }
        requestData = Utils.ByteArrayCopy(requestData, frameFlag);
        if (flag == 51) {
            requestData = Utils.ByteArrayCopy(requestData, packagingStart);
        } else if (flag == 52) {
            requestData = Utils.ByteArrayCopy(requestData, packagingComplete);
        }
        requestData = Utils.ByteArrayCopy(requestData, content);
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(requestData, 1, 17)));
        requestData = Utils.ByteArrayCopy(requestData, verifyCode);
        String bb = Utils.bytesToHexString(requestData);
        Log.e("Pan", "2sendPackageBegin 51_52:" + bb);
        return requestData;
    }

    /**
     * 打包下发内容
     * 0x53
     *
     * @return
     */
    public byte[] sendPackaging(byte[] data) {
        Log.e("Pan", "53:data=" + Utils.bytesToHexString(data));
        byte[] allData = null;//返回的全部内容
        short length = (short) Math.ceil((short) data.length / 12);//总共需要发送的包数
        if (data.length % 12 != 0) {
            length += 1;
        }
        for (short i = 0; i < length; i++) {
            byte[] requestData = null;//全部内容
            byte[] packageNum = new byte[4];
            byte[] packageContent = new byte[12];
            byte[] childContent = null;
            packageNum = Utils.intToByte(i + 1);
//            Log.e("Pan", "53:packageNum=" + Utils.bytesToHexString(packageNum));

            packageContent = Utils.GetByteArrayByLength(data, 12 * i, 12);
//            Log.e("Pan", "53:packageContent=" + Utils.bytesToHexString(packageContent));

            childContent = Utils.ByteArrayCopy(childContent, packageNum);
//            Log.e("Pan", "53:childContent=" + Utils.bytesToHexString(childContent));

            childContent = Utils.ByteArrayCopy(childContent, packageContent);
//            Log.e("Pan", "53:childContent=" + Utils.bytesToHexString(childContent));

            requestData = Utils.ByteArrayCopy(requestData, frameFlag);
//            Log.e("Pan", "53:requestData=" + Utils.bytesToHexString(requestData));

            requestData = Utils.ByteArrayCopy(requestData, packagingSend);
//            Log.e("Pan", "53:requestData=" + Utils.bytesToHexString(requestData));

            requestData = Utils.ByteArrayCopy(requestData, childContent);
//            Log.e("Pan", "53:requestData=" + Utils.bytesToHexString(requestData));

            byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(requestData, 1, 17)));
            requestData = Utils.ByteArrayCopy(requestData, verifyCode);
//            Log.e("Pan", "53:requestData=" + Utils.bytesToHexString(requestData));

            allData = Utils.ByteArrayCopy(allData, requestData);
        }
        String as = Utils.bytesToHexString(allData);
        Log.e("Pan", "53:as=" + as);
        return allData;
    }


    //============================解析蓝牙数据==========================================

    /**
     * 解析蓝牙下位机发过来的key
     *
     * @param rev
     * @return
     */
    public static String parsingKey(byte[] rev) {
        byte[] content = Utils.GetByteArrayByLength(rev, 0, 6);
        return Utils.bytesToHexString(content);
    }
    /**
     * 上传搜索抢状态
     * 0x80
     *
     * @param rev
     * @return
     */
    public static List<String> searchState(byte[] rev) {
        List<String> back = new ArrayList<>();
        //截取所需的数据长度8位 标签1信号强度天线1(1) +搜索状态(1，0搜索中,1锁定中，2已锁定)
        mLog.e("数据总长："+Utils.bytesToHexString(rev));
        byte[] content1 = Utils.subBytes(rev, 0, 2);
        mLog.e("数据1："+Utils.bytesToHexString(content1));
        byte[] content2 = Utils.subBytes(rev, 2, 6);
        mLog.e("数据2："+Utils.bytesToHexString(content2));
        byte[] content3 = Utils.subBytes(rev, 8, 6);
        mLog.e("数据3："+Utils.bytesToHexString(content3));

        String data1=  Utils.bytesToHexString(content1);
        mLog.e("省市数据："+Utils.bytesToHexString(content1));
        String data2=  Utils.bytesToHexString(content2);
        mLog.e("标签1数据："+Utils.bytesToHexString(content2));
        String data3=  Utils.bytesToHexString(content3);
        mLog.e("标签2数据："+Utils.bytesToHexString(content3));

        //省(1)
        String sheng = data1.substring(0,2);
        back.add(Integer.parseInt(sheng,16)+"");
        mLog.e("省："+back.get(0));
        //市(1)
        String shi =data1.substring(2,4);
        back.add(Integer.parseInt(shi,16)+"");
        mLog.e("市："+back.get(1));


        //标签1ID(4)
        String THEFTNO = data2.substring(0,8);
        back.add(Integer.parseInt(THEFTNO,16)+"");
        mLog.e(THEFTNO+"标签1ID："+back.get(2));
        //标签1信号强度
        String signal = data2.substring(8,10);
        back.add(Integer.parseInt(signal,16)+"");
        mLog.e("标签1信号强度："+back.get(3));
        //标签1搜索状态(1，0搜索中,1锁定中，2已锁定)
        String state =data2.substring(10,12);
        back.add(Integer.parseInt(state,16)+"");
        mLog.e("标签1搜索状态："+back.get(4));


        //标签2ID(4)
        String THEFTNO2 = data3.substring(0,8);
        back.add(Integer.parseInt(THEFTNO2,16)+"");
        mLog.e(THEFTNO+"标签2ID："+back.get(5));
        //标签2信号强度
        String signal2 = data3.substring(8,10);
        back.add(Integer.parseInt(signal2,16)+"");
        mLog.e("标签2信号强度："+back.get(6));
        //标签2搜索状态(1，0搜索中,1锁定中，2已锁定)
        String state2 =data3.substring(10,12);
        back.add(Integer.parseInt(state2,16)+"");
        mLog.e("标签2搜索状态："+back.get(7));


        return back;
    }
    /**
     * 上传搜索抢状态
     * 0x80
     *
     * @param rev
     * @return
     */
    public static List<String> searchState2(byte[] rev) {
        List<String> back = new ArrayList<>();
        //截取所需的数据长度8位 标签1信号强度天线1(1) +搜索状态(1，0搜索中,1锁定中，2已锁定)
        byte[] content = Utils.subBytes(rev, 0, 8);
        String data=  Utils.bytesToHexString(content);
        mLog.e("省："+Utils.bytesToHexString(content));
        //省(1)
        byte[] sheng = Utils.subBytes(content, 0, 1);
        back.add(Integer.parseInt(Utils.bytesToHexString(sheng),16)+"");
        mLog.e("省："+back.get(0));
        //市(1)
        byte[] shi = Utils.subBytes(content, 1, 2);
        back.add(Integer.parseInt(Utils.bytesToHexString(shi),16)+"");
        mLog.e("市："+back.get(1));
        //标签1id(4)
        byte[] THEFTNO = Utils.subBytes(content, 2, 6);
        back.add(Integer.parseInt(Utils.bytesToHexString(THEFTNO),16)+"");
        mLog.e("标签1id："+back.get(2));
        //标签1信号强度天线1(1)
        byte[] signal = Utils.subBytes(content, 6, 7);
        back.add(Integer.parseInt(Utils.bytesToHexString(signal),16)+"");
        mLog.e("标签1信号强度："+back.get(3));
        //搜索状态(1，0搜索中,1锁定中，2已锁定)
        byte[] state = Utils.subBytes(content, 7, 8);
        back.add(Integer.parseInt(Utils.bytesToHexString(state),16)+"");
        mLog.e("搜索状态："+back.get(4));
        return back;
    }
    /**
     * 设备透传心跳
     * 0x81
     *
     * @param rev
     * @return
     */
    public static Long heartBeat(byte[] rev) {
        byte[] content = Utils.GetByteArrayByLength(rev, 1, 6);
        String aa = Utils.bytesToHexString(content);
        return Long.valueOf(aa, 16);
    }


    /**
     * 上传搜索抢状态
     * 0x82
     *
     * @param rev
     * @return
     */
    public static String searchStateSecond(byte[] rev) {
        byte[] content = Utils.GetByteArrayByLength(rev, 0, 13);
        byte[] tag1 = Utils.GetByteArrayByLength(content, 2, 4);
        byte[] intensity1 = Utils.GetByteArrayByLength(content, 5, 1);
        byte[] state1 = Utils.GetByteArrayByLength(content, 7, 1);
        byte[] tag2 = Utils.GetByteArrayByLength(content, 8, 4);
        byte[] intensity2 = Utils.GetByteArrayByLength(content, 12, 1);
        return Utils.bytesToHexString(tag1) + "," + Utils.bytesToHexString(intensity1) + "," + Utils.bytesToHexString(state1) +
                "," + Utils.bytesToHexString(tag2) + "," + Utils.bytesToHexString(intensity2);//标签1+标签2信号强度+搜索状态（0搜索中,1锁定中，2已锁定）
    }

    /**
     * 空中信号开关
     * 0x71
     *
     * @return
     */
    public byte[] AerialSignal(boolean off) {
        byte[] requestData = null;//全部内容
        requestData = Utils.ByteArrayCopy(requestData, frameFlag);
        requestData = Utils.ByteArrayCopy(requestData, aerialSignal);
        if(off){
            requestData = Utils.ByteArrayCopy(requestData,Utils.intTobyte2(1));
        }else{
            requestData = Utils.ByteArrayCopy(requestData,Utils.intTobyte2(0));
        }
        mLog.e("AerialSignal="+Utils.bytesToHexString(requestData));
        byte[] content = new byte[15];
        requestData = Utils.ByteArrayCopy(requestData, content);
        mLog.e("AerialSignal="+Utils.bytesToHexString(requestData));
        byte[] verifyCode = Utils.shortToBytes(CRC16M.CalculateCrc16(Utils.GetByteArrayByLength(requestData, 1, 17)));
        requestData = Utils.ByteArrayCopy(requestData, verifyCode);
        mLog.e("AerialSignal="+Utils.bytesToHexString(requestData));
        return requestData;
    }

    public int AerialSignal_Analysis(byte[] data) {
        String date = Utils.bytesToHexString(data);
        mLog.e( "接收到的数据： " + date);
        String AerialSignal= date.substring(4,6);
        int YY = Integer.parseInt(AerialSignal, 16);
        return YY ;
    }
}
