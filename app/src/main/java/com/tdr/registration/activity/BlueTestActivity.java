package com.tdr.registration.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.tdr.registration.R;
import com.tdr.registration.base.BaseActivity;
import com.tdr.registration.util.Constants;
import com.tdr.registration.util.mLog;

import org.apache.commons.lang.ArrayUtils;

import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Linus_Xie on 2016/11/4.
 */

public class BlueTestActivity extends BaseActivity {

    private static final String TAG = "BlueTestActivity";
    @BindView(R.id.open)
    Button open;
    @BindView(R.id.send)
    Button send;

    //=========================蓝牙打印机============================
    private GpService mGpService = null;
    private PrinterServiceConnection conn = null;
    private int mPrinterIndex = 0;
    private int mTotalCopies1 = 0, mTotalCopies2 = 0, mTotalCopies3 = 0;

    class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLog.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetest);
        ButterKnife.bind(this);
        connection();
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent("com.gprinter.aidl.GpPrintService");
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        mLog.e(TAG, "onResume");
    }

    @OnClick({R.id.open, R.id.send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open:
                mLog.d(TAG, "openPortConfigurationDialog ");
                Intent intent = new Intent(this,BlueListActivity.class);
                boolean[] state = getConnectState();
                intent.putExtra(Constants.CONNECT_STATUS, state);
                this.startActivity(intent);
                break;
            case R.id.send:
                sendMsg();
                break;
        }
    }

    private boolean[] getConnectState() {
        boolean[] state = new boolean[GpPrintService.MAX_PRINTER_CNT];
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            state[i] = false;
        }
        for (int i = 0; i < GpPrintService.MAX_PRINTER_CNT; i++) {
            try {
                if (mGpService.getPrinterConnectStatus(i) == GpDevice.STATE_CONNECTED) {
                    state[i] = true;
                }
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return state;
    }

    private void sendMsg() {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte) 3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("车辆防盗备案凭单\n"); // 打印文字
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText("身份证号：\n"); // 打印文字
        esc.addText("330302198602010305\n"); // 打印文字

        esc.addText("车主姓名：\n");
        esc.addText("张三\n");

        esc.addText("联系方式：\n");
        esc.addText("18757707988\n");

        esc.addText("车辆品牌：");
        esc.addText("爱玛\n");

        esc.addText("车牌：");
        esc.addText("0123456\n");

        esc.addText("车架号：");
        esc.addText("0123456\n");

        esc.addText("电机号：");
        esc.addText("0123456\n");

        esc.addText("----------------------------\n");
        esc.addText("车主签名：\n");
        esc.addText("\n");
        esc.addText("\n");
        esc.addText("----------------------------\n");

        //esc.addText("Print bitmap!\n"); // 打印文字
       // Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.abs);
        //esc.addRastBitImage(b, b.getWidth(), 0); // 打印图片

        // /* 打印繁体中文 需要打印机支持繁体字库 */
        // String message = Util.SimToTra("佳博票据打印机\n");
        // esc.addText(message, "BIG5");
        // esc.addPrintAndLineFeed();
        //
        // /* 打印图片 */
        // esc.addText("Print bitmap!\n"); // 打印文字
        // Bitmap b = BitmapFactory.decodeResource(getResources(),
        // R.drawable.gprinter);
        // esc.addRastBitImage(b, b.getWidth(), 0); // 打印图片
        //
        // /* 打印一维条码 */
        // esc.addText("Print code128\n"); // 打印文字
        // esc.addSelectPrintingPositionForHRICharacters(HRI_POSITION.BELOW);//
        // 设置条码可识别字符位置在条码下方
        // esc.addSetBarcodeHeight((byte) 60); // 设置条码高度为60点
        // esc.addCODE128("Gprinter"); // 打印Code128码
        // esc.addPrintAndLineFeed();
        //
        // /*
        // * QRCode命令打印 此命令只在支持QRCode命令打印的机型才能使用。 在不支持二维码指令打印的机型上，则需要发送二维条码图片
        // */
        // // esc.addText("Print QRcode\n"); // 打印文字
        // // esc.addSelectErrorCorrectionLevelForQRCode((byte)0x31); //设置纠错等级
        // // esc.addSelectSizeOfModuleForQRCode((byte)3);//设置qrcode模块大小
        // // esc.addStoreQRCodeData("www.gprinter.com.cn");//设置qrcode内容
        // // esc.addPrintQRCode();//打印QRCode
        // // esc.addPrintAndLineFeed();
        //
        // /* 打印文字 */
        // esc.addSelectJustification(JUSTIFICATION.CENTER);// 设置打印左对齐
        // esc.addText("Completed!\r\n"); // 打印结束
        esc.addPrintAndFeedLines((byte) 8);

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendEscCommand(mPrinterIndex, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        mLog.e(TAG, "onDestroy");
        super.onDestroy();
        if (conn != null) {
            unbindService(conn); // unBindService
        }
    }
}
