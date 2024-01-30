/**
 * @author : Zachary Doll
 */

public class Vector2D {
    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Adds another vector to this one
    public void add(Vector2D v) {
        this.x += v.x;
        this.y += v.y;
    }

    // Subtracts another vector from this one
    public void subtract(Vector2D v) {
        this.x -= v.x;
        this.y -= v.y;
    }

    // Multiplies this vector by a scalar
    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    // Calculates the magnitude (length) of this vector
    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    // Returns a new vector that is the sum of this vector and another vector
    public Vector2D addNew(Vector2D v) {
        return new Vector2D(this.x + v.x, this.y + v.y);
    }

    // Returns a new vector that is the difference between this vector and another
    public Vector2D subtractNew(Vector2D v) {
        return new Vector2D(this.x - v.x, this.y - v.y);
    }

    // Returns a new vector that is this vector scaled by a scalar
    public Vector2D multiplyNew(double scalar) {
        return new Vector2D(this.x * scalar, this.y * scalar);
    }

    @Override
    public String toString() {
        return "Vector2D(" + x + ", " + y + ")";
    }

    public Vector2D normalizeNew() {
        double magnitude = magnitude();
        if (magnitude == 0) {
            return new Vector2D(0, 0); // Return a zero vector if the original vector is zero
        }
        return new Vector2D(x / magnitude, y / magnitude);
    }
}

