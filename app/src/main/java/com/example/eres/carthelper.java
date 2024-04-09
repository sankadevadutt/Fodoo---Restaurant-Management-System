package com.example.eres;

public class carthelper {
    private String product,quant,chefphone,dish,price;

    public carthelper() {
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dish) {
        this.dish = dish;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public carthelper(String product, String quant, String chefphone, String dish, String price) {
        this.product = product;
        this.quant = quant;
        this.chefphone = chefphone;
        this.dish = dish;
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuant() {
        return quant;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public String getChefphone() {
        return chefphone;
    }

    public void setChefphone(String chefphone) {
        this.chefphone = chefphone;
    }
}
