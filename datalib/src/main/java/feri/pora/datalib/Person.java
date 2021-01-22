package feri.pora.datalib;

public class Person {
    protected String id;
    protected String fullName;
    protected String phone;
    protected String location;

    public Person() {
        location = "";
    }

    public Person(String id, String fullName, String phone, String location) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.location = location;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
