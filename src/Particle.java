import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;

/**
 * @author : Zachary Doll
 */

public class Particle extends Circle {
    private Vector2D velocity;
    private Vector2D acceleration;
    private Vector2D position;
    private final Color PARTICLE_COLOR = Color.BLUE;

    public Particle(double radius, Vector2D initialPosition) {
        super(radius);
        this.position = initialPosition;
        this.velocity = new Vector2D(0, 0);
        this.acceleration = new Vector2D(0, 0);

        setFill(PARTICLE_COLOR);
        updatePosition();
    }

    //apply a force to the particle
    public void applyForce(Vector2D force) {
        acceleration.add(force);
    }

    // Update the visual position based on the particle's position vector
    private void updatePosition() {
        setCenterX(position.getX());
        setCenterY(position.getY());
    }

    // Getters and setters
    public Vector2D getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2D velocity) {
        this.velocity = velocity;
    }

    public Vector2D getPosition() {
        return position;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
        updatePosition();
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vector2D acceleration) {
        this.acceleration = acceleration;
    }
}
