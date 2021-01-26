package com.example.carwash;

public class Order {
    private String id;
    private String name;
    private String location;
    private String packageName;
    private String provider;
    private String customerID;
    private String providerID;

    public String getId() {
        return id;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getProviderID() {
        return providerID;
    }

    public void setProviderID(String providerID) {
        this.providerID = providerID;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String cost;

    public Order(){

    }

    public Order(String  id, String name, String location, String packageName, String provider, String cost) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.packageName = packageName;
        this.provider = provider;
        this.cost = cost;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Order" +"\n"+
                "Name: " + name + '\n' +
                "Location: " + location + '\n' +
                "PackageName: " + packageName + '\n' +
                "Provider: " + provider + '\n' +
                "Cost: " + cost ;
    }
}
