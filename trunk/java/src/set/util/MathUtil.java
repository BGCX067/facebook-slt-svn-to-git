package set.util;


public class MathUtil
{
    public static int powersOf2[] = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024};
    
    public static int decToBase3(int x)
    {
        String s = Integer.toString(x, 3);
        return Integer.parseInt(s);
    }
    
    public static int[] decToBaseBArray(int x, int b, int outputLength)
    {
        int[] output = new int[outputLength];
        int q = x;
        
        for (int i = output.length - 1; i >= 0; i--)
        {
            if (q == 0)
            {
                output[i] = 0;
            }
            else
            {
                output[i] = q % b;
                q = q / b;
            }
        }
        
        return output;
    }
    
    public static int[] decToBase3Array(int x)
    {
        return decToBaseBArray(x, 3, 4);
    }
}
