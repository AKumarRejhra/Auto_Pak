package com.project.autopak;

public class ordermodel {

    String BuyerEmail,BuyerName,CategoryId,CategoryName,DateTime,ItemId,ItemName,SellerEmail,companyName,imageUrl,price;

    public ordermodel(String buyerEmail, String buyerName, String categoryId, String categoryName, String dateTime, String itemId, String itemName, String sellerEmail, String companyName, String imageUrl, String price) {
        BuyerEmail = buyerEmail;
        BuyerName = buyerName;
        CategoryId = categoryId;
        CategoryName = categoryName;
        DateTime = dateTime;
        ItemId = itemId;
        ItemName = itemName;
        SellerEmail = sellerEmail;
        this.companyName = companyName;
        this.imageUrl = imageUrl;
        this.price = price;
    }


    public ordermodel() {
    }

    public String getBuyerEmail() {
        return BuyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        BuyerEmail = buyerEmail;
    }

    public String getBuyerName() {
        return BuyerName;
    }

    public void setBuyerName(String buyerName) {
        BuyerName = buyerName;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getSellerEmail() {
        return SellerEmail;
    }

    public void setSellerEmail(String sellerEmail) {
        SellerEmail = sellerEmail;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
