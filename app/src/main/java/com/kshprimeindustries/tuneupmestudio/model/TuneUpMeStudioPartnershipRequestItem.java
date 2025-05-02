package com.kshprimeindustries.tuneupmestudio.model;

public class TuneUpMeStudioPartnershipRequestItem {

    private String itemId;
    private String name;
    private String email;
    private String mobile;


    public TuneUpMeStudioPartnershipRequestItem() {
    }

    public TuneUpMeStudioPartnershipRequestItem(String itemId, String name, String email, String mobile) {
        this.itemId = itemId;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}
