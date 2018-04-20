package com.tdr.registration.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/2.
 */

public class InsuranceRenewModel implements Serializable {
    String Title;
    String IsBuy;
    String Life;
    String Expiration;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIsBuy() {
        return IsBuy;
    }

    public void setIsBuy(String isBuy) {
        IsBuy = isBuy;
    }

    public String getLife() {
        return Life;
    }

    public void setLife(String life) {
        Life = life;
    }

    public String getExpiration() {
        return Expiration;
    }

    public void setExpiration(String expiration) {
        Expiration = expiration;
    }
}
