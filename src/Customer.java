public class Customer implements Cloneable {
    private String name, email, mobile;
    private final String customerId;
    private Address address;
    private static long customerCounter = 100;

    public Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }
    private static String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    // static nested class
    public static class Address {
        private String line, city, pincode;
        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }
        public String getLine()    { return line; }
        public String getCity()    { return city; }
        public String getPincode() { return pincode; }
    }

    public void setAddress(Address address) { this.address = address; }
    public Address getAddress() { return address; }

    public Customer clone() throws CloneNotSupportedException {
        return (Customer) super.clone();
    }
    public String toString() { return customerId + " | " + name; }

    public String getCustomerId() { return customerId; }
    public String getName()  { return name; }
    public String getEmail() { return email; }
    public String getMobile(){ return mobile; }
}