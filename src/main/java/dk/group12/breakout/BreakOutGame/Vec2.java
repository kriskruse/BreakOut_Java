package dk.group12.breakout.BreakOutGame;

public class Vec2 {
    private double x, y;
    private final double scalar;

    public Vec2(double x, double y, double scalar) {
        set(x, y);
        this.scalar = scalar;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    public double getScalar() {
        return scalar;
    }

    public void setX(double x) {
        set(x, this.y);
    }

    public void setY(double y) {
        set(this.x, y);
    }


    public void set(double x, double y) {
        double length = Math.sqrt(x * x + y * y);
        if (length == 0) {
            throw new IllegalArgumentException("Cannot set vector to zero length");
        }
        this.x = x / length;
        this.y = y / length;
    }
}