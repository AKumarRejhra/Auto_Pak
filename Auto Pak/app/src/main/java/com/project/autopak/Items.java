package com.project.autopak;

public class Items {
    String name,categoryid,imageUrl,companyName,price,email,address,contact,categoryName,id;

    public Items() {
    }

    public Items(String name, String categoryid, String imageUrl, String companyName, String price, String email,String address,String contact) {
        this.name = name;
        this.categoryid = categoryid;
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.price = price;
        this.email=email;
        this.address = address;
        this.contact = contact;
    }

    public Items(String name, String categoryid, String imageUrl, String companyName, String price, String email,String address,String contact,String categorName,String id) {
        this.name = name;
        this.categoryid = categoryid;
        this.imageUrl = imageUrl;
        this.companyName = companyName;
        this.price = price;
        this.email=email;
        this.address = address;
        this.contact = contact;
        this.categoryName = categorName;
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
