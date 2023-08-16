package com.project.autopak;

public class User {
    String name;
    String Email;
    String type;
    String address;
    String contact;
    String token;

    public User (){};
    public User(String name,String email,String type,String address,String contact,String token){
        this.name=name;
       this.Email=email;
        this.type=type;
        this.address=address;
        this.contact=contact;
        this.token=token;
    }


    public String getDisplayname() {
        return name;
    }

    public String getEmail() {
        return Email;
    }

    public String getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }


}