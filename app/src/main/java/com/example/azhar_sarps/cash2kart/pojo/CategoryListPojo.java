
package com.example.azhar_sarps.cash2kart.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryListPojo {

    @SerializedName("cat_desc")
    @Expose
    private String catDesc;
    @SerializedName("display_names")
    @Expose
    private String displayNames;

    public String getCatDesc() {
        return catDesc;
    }

    public void setCatDesc(String catDesc) {
        this.catDesc = catDesc;
    }

    public String getDisplayNames() {
        return displayNames;
    }

    public void setDisplayNames(String displayNames) {
        this.displayNames = displayNames;
    }

}
