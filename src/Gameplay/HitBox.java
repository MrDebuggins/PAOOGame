package Gameplay;

public class HitBox
{
    public double posx, posy;
    public int width, height;

    HitBox(){}
    HitBox(double x, double y, int w, int h)
    {
        posx = x; posy = y; width = w; height = h;
    }
}
