
package com.example.azhar_sarps.cash2kart.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryCouponPojo {

    @SerializedName("message")
    @Expose
    private List<CouponMessage> message = null;

    public List<CouponMessage> getMessage() {
        return message;
    }

    public void setMessage(List<CouponMessage> message) {
        this.message = message;
    }

}
