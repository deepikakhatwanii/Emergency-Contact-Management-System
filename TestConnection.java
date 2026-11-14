public class TestConnection {
    public static void main(String[] args) {
        if (DBConnection.getConnection() != null) {
            System.out.println("Java and MySQL connected successfully!");
        } else {
            System.out.println("Connection failed. Check the error above.");
        }
    }
}