package com.tdr.registration.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tdr.registration.R;
import com.tdr.registration.model.WithdrawalsModel;
import com.tdr.registration.util.Utils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 钱包提现详情
 */
@ContentView(R.layout.activity_withdrawals_details)
public class WithdrawalsDetails extends Activity {
    @ViewInject(R.id.IV_Back)
    private ImageView IV_Back;

    @ViewInject(R.id.TV_Title_State)
    private TextView TV_Title_State;

    @ViewInject(R.id.TV_Title_Money)
    private TextView TV_Title_Money;

    @ViewInject(R.id.TV_PayeeName)
    private TextView TV_PayeeName;

    @ViewInject(R.id.TV_PayeePayID)
    private TextView TV_PayeePayID;

    @ViewInject(R.id.TV_PayeePhone)
    private TextView TV_PayeePhone;

    @ViewInject(R.id.TV_PaymentName)
    private TextView TV_PaymentName;

    @ViewInject(R.id.TV_PaymentTime)
    private TextView TV_PaymentTime;

    @ViewInject(R.id.TV_TransactionTime)
    private TextView TV_TransactionTime;

    @ViewInject(R.id.TV_TransactionType)
    private TextView TV_TransactionType;

    @ViewInject(R.id.TV_TransactionAmount)
    private TextView TV_TransactionAmount;

    @ViewInject(R.id.TV_TransactionState)
    private TextView TV_TransactionState;

    @ViewInject(R.id.TV_TransactionReject)
    private TextView TV_TransactionReject;

    @ViewInject(R.id.LL_Reject)
    private LinearLayout LL_Reject;


    private WithdrawalsModel WM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        setdata();
    }

    private void setdata() {
        WM = (WithdrawalsModel) getIntent().getExtras().getSerializable("WithdrawalsModel");
        IV_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        switch (WM.getSTATE()) {
            case "0":
                TV_Title_State.setTextColor(getResources().getColor(R.color.colorTitle));
                TV_Title_State.setText("未发放");
                LL_Reject.setVisibility(View.GONE);
                break;
            case "1":
                TV_Title_State.setTextColor(getResources().getColor(R.color.green));
                TV_Title_State.setText("已发放");
                LL_Reject.setVisibility(View.GONE);
                break;
            case "2":
                TV_Title_State.setTextColor(getResources().getColor(R.color.red));
                TV_Title_State.setText("已驳回");
                LL_Reject.setVisibility(View.VISIBLE);
                TV_TransactionReject.setText("驳回原因");
                break;
        }

        TV_Title_Money.setText(Utils.fmtMicrometer(WM.getAPPLYAMOUNT()));
        TV_PayeeName.setText(WM.getPAYEENAME());
        TV_PayeePayID.setText(WM.getPAYEEALIPAY());
        TV_PayeePhone.setText(WM.getPAYEEPHONE());
        TV_PaymentName.setText(WM.getFROM_USERNAME());
        TV_PaymentTime.setText(WM.getBILLTIME());
        TV_TransactionTime.setText(WM.getAPPLYTIME());
        TV_TransactionType.setText(WM.getACTTYPE_NAME());
        TV_TransactionAmount.setText(Utils.fmtMicrometer(WM.getAPPLYAMOUNT()) + "元");
        TV_TransactionState.setText(TV_Title_State.getText().toString().trim());
    }
}
