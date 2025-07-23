package com.example.tranminhphat_2123110213;

public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String birthday;
    private String imageUrl;
    private String fullname;
    private String gender;
    private String address; // ✅ Thêm thuộc tính address

    // ✅ Constructor có thêm address
    public User(int id, String name, String email, String phone, String birthday, String imageUrl, String fullname, String gender, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
        this.fullname = fullname;
        this.gender = gender;
        this.address = address;
    }

    // Getter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFullname() {
        return fullname;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() { // ✅ Getter cho address
        return address;
    }

    // Setter methods
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) { // ✅ Setter cho address
        this.address = address;
    }
}
