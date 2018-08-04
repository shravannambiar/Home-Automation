package com.example.shravan.techhome;

import java.io.Serializable;

/**
 * Created by shravan on 11-02-2018.
 */

public class UserInfo implements Serializable {
public String name,pass;


    public UserInfo() {
    }

    public UserInfo(String name,String pass) {
        this.name=name;
        this.pass=pass;
        }

    public String getname() {
        return name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setname(String name) {
        this.name = name;
    }
}
