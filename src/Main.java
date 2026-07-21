public class Main {
    public static void main(String[] args) {
        Account a1 = new Account("Riya", 4000);
        Account a2 = new Account("Aman", 4000);          // different account number

        System.out.println(a1);                                  // toString
        System.out.println("a1 equals a2 -> " + a1.equals(a2));  // false (numbers differ)
        System.out.println("a1 equals a1 -> " + a1.equals(a1));  // true

        Object obj = a1;
        System.out.println("obj instanceof Account -> " + (obj instanceof Account));

        Customer c = new Customer("Riya", "riya@example.com", "9876543210");
        c.setAddress(new Customer.Address("12 MG Road", "Nadiad", "387001"));
        System.out.println(c + " lives in " + c.getAddress().getCity());
    }
}