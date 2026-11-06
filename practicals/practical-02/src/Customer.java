public class Customer implements Cloneable
{
    private String name;
    private String email;
    private String mobile;
    private final String customerId;
    private static long customerCounter=100;
    private static String generateCustomerId()
    {
        customerCounter++;
        return "CUST" + customerCounter;
    }
    public Customer(String name,String email,String mobile)
    {
        this.name=name;
        this.email=email;
        this.mobile=mobile;
        this.customerId = generateCustomerId();
    }
    public static class Address {

    private String line;
    private String city;
    private String pincode;

    public Address(String line, String city, String pincode) {
        this.line = line;
        this.city = city;
        this.pincode = pincode;
    }

    public String getLine() {
        return line;
    }

    public String getCity() {
        return city;
    }

    public String getPincode() {
        return pincode;
    }
}

    public String getName()
    {
        return name;
    }
    public String getEmail()
    {
        return email;
    }
    public String getMobile()
    {
        return mobile;
    }
    public String getCustomerId()
    {
        return customerId;
    }
   
   private Address address;

public Address getAddress() {
    return address;
}

public void setAddress(Address address) {
    this.address = address;
}


public Customer clone() {
    try {
        return (Customer) super.clone();
    } catch (CloneNotSupportedException e) {
        throw new AssertionError();
    }
}
}