package com.example.eres;

public class UserHelperClass {
    String phonenumber;
    String name;

    public UserHelperClass(String phonenumber, String name) {
        this.phonenumber = phonenumber;
        this.name = name;
    }

    public UserHelperClass(Class<UserHelperClass> userHelperClassClass) {
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
