package set.util;

//TODO: should this be an unchecked exception?
public class VersionMismatchException extends RuntimeException
{
    private String invalidVersion;
    private String expectedVersion;
    
    public VersionMismatchException(String invalidVersion, String expectedVersion)
    {
        // TODO Auto-generated constructor stub
        this.invalidVersion = invalidVersion;
        this.expectedVersion = expectedVersion;
    }
    
    public String getInvalidVersion()
    {
        return invalidVersion;
    }
    
    public String getExpectedVersion()
    {
        return expectedVersion;
    }
}
