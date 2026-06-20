package com.franquicias.dto;

public class ProductoConSucursal {

    private String productName;
    private Integer stock;
    private String branchName;

    public ProductoConSucursal() {}

    public ProductoConSucursal(String productName, Integer stock, String branchName) {
        this.productName = productName;
        this.stock = stock;
        this.branchName = branchName;
    }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
}
