package set.core;

public class Card
{
    public static final int VALUE_1 = 0;
    public static final int VALUE_2 = 1;
    public static final int VALUE_3 = 2;
    public static final int SHAPE_SQUARE = 0;
    public static final int SHAPE_CIRCLE = 1;
    public static final int SHAPE_TRIANGLE = 2;
    public static final int SHADE_NONE = 0;
    public static final int SHADE_SHADED = 1;
    public static final int SHADE_SOLID = 2;
    public static final int COLOR_GREEN = 0;
    public static final int COLOR_RED = 1;
    public static final int COLOR_BLUE = 2;
    
    
    private int value;
    private int shape;
    private int shading;
    private int color;

    public Card(int inputvalue, int inputshape, int inputshading, int inputcolor)
    {
        value = inputvalue;
        shape = inputshape;
        shading = inputshading;
        color = inputcolor;
    }

    public int id()
    {
        return (value * 27 + shape * 9 + shading * 3 + color);
    }

    public int getvalue()
    {
        return value;
    }

    public int getshape()
    {
        return shape;
    }

    public int getshading()
    {
        return shading;
    }

    public int getcolor()
    {
        return color;
    }

    @Override
    public String toString()
    {
        String s = "";
        s += value;
        s += shape;
        s += shading;
        s += color;
        return s;
    }

    @Override
    public int hashCode()
    {
        return id();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Card)
            return this.id() == ((Card) obj).id();
        else
            return false;
    }
    
    
}
