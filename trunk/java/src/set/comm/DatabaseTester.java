package set.comm;


/**
 * Tests the database connection.
 */
public class DatabaseTester
{
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        DatabaseConnection dbCon = new DatabaseConnection();
        
        // should print out "valid user: testuser"
        String s = dbCon.validateUser("default", "password");
        if (s != null)
        {
            System.out.println("valid user: " + s);
        }
        else
        {
            System.out.println("validation failed");
        }
        
        // should print out "validation failed"
        s = dbCon.validateUser("default", "wrong_password");
        if (s != null)
        {
            System.out.println("valid user: " + s);
        }
        else
        {
            System.out.println("validation failed");
        }
        
        // should print out "validation failed"
        s = dbCon.validateUser("wrong_username", "password");
        if (s != null)
        {
            System.out.println("valid user: " + s);
        }
        else
        {
            System.out.println("validation failed");
        }
    }

}
