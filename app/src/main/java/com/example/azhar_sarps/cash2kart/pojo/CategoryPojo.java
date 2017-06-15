
package com.example.azhar_sarps.cash2kart.pojo;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryPojo {

    @SerializedName("message")
    @Expose
    private List<Message> message = null;

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

}
