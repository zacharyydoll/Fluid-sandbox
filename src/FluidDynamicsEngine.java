import java.util.ArrayList;
import java.util.List;

/**
 * @author : Zachary Doll
 */

import javafx.scene.paint.Color;

public class FluidDynamicsEngine {
    private List<Particle> particles;
    private double width;
    private double height;
    private final int REPULSE_RADIUS = 30;
    private final int PARTICLE_COUNT = 3500;
    private final double REPULSION_FORCE = 50;
    private final double ATTRACTION_FORCE = 500;
    private final double DAMPING = 0.98;
    private final double PARTICLE_RADIUS = 5;
    private final double FORCE_RADIUS = 50;
    private final double RESTITUTION_COEFFICIENT = 0.08;
    private final double MAX_FORCE = 0.06; // The maximum force applied to any particle
    private final double SOFTENING_FACTOR = 100; // Prevents singularity by avoiding division by a value near zero
    private final double GRAVITY_STRENGTH = 0.1;
    private Vector2D gravity = new Vector2D(0, GRAVITY_STRENGTH);
    private boolean isGravityEnabled = false;

    public FluidDynamicsEngine(double width, double height) {
        this.width = width;
        this.height = height;
        particles = new ArrayList<>();
        initializeParticles();
    }

    private void initializeParticles() {
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            double x = Math.random() * width;
            double y = Math.random() * height;
            Particle particle = new Particle(PARTICLE_RADIUS, new Vector2D(x, y)); // Radius set to 2, adjust as needed
            particle.setFill(Color.BLUE); // Color for water, adjust as desired
            particles.add(particle);
        }
    }

    public void update() {
        for (Particle particle : particles) {

            if (isGravityEnabled) particle.applyForce(gravity);

            // Basic physics update (Euler integration)
            particle.getVelocity().add(particle.getAcceleration());
            particle.getPosition().add(particle.getVelocity());
            particle.setAcceleration(new Vector2D(0, 0)); // Reset acceleration

            // Boundary conditions - make particles bounce off walls
            if (particle.getPosition().getX() - PARTICLE_RADIUS < 0 || particle.getPosition().getX() > width) {
                particle.getVelocity().setX(particle.getVelocity().getX() * -RESTITUTION_COEFFICIENT);
            }
            if (particle.getPosition().getY() - PARTICLE_RADIUS < 0 || particle.getPosition().getY() > height) {
                particle.getVelocity().setY(particle.getVelocity().getY() * -RESTITUTION_COEFFICIENT);
            }

            // Update the graphical representation of the particle
            particle.setCenterX(particle.getPosition().getX());
            particle.setCenterY(particle.getPosition().getY());
        }

        // Apply repulsion forces between particles for fluid-like behavior
        applyRepulsionForces();

        for (Particle particle : particles) {
            particle.getVelocity().multiply(DAMPING);
        }
    }

    // Apply repulsion forces between particles for fluid-like behavior, preventing formation of singularities
    private void applyRepulsionForces() {
        for (int i = 0; i < particles.size(); i++) {
            Particle pi = particles.get(i);
            for (int j = i + 1; j < particles.size(); j++) {
                Particle pj = particles.get(j);
                Vector2D distanceVector = pi.getPosition().subtractNew(pj.getPosition());
                double distance = distanceVector.magnitude();
                if (distance < REPULSE_RADIUS) { // Only apply force if particles are close enough
                    Vector2D forceDirection = distanceVector.normalizeNew();
                    double forceMagnitude = REPULSION_FORCE / (distance * distance); // Inverse square law
                    Vector2D force = forceDirection.multiplyNew(forceMagnitude);

                    pi.getVelocity().add(force);
                    pj.getVelocity().subtract(force); // Newton's third law
                }
            }
        }
    }

    public void applyAttraction(Vector2D point) {

        for (Particle particle : particles) {
            Vector2D direction = point.subtractNew(particle.getPosition());
            double distance = direction.magnitude();
            double softenedDistance = distance + SOFTENING_FACTOR; // Softening factor to prevent singularity
            if (distance > FORCE_RADIUS) {  // Avoid applying force when the mouse is too close to the particle
                // Normalize direction and scale by force strength, limit the maximum force
                Vector2D force = direction.normalizeNew()
                        .multiplyNew(Math.min(ATTRACTION_FORCE / softenedDistance, MAX_FORCE));
                particle.applyForce(force);
            }
        }
    }

    public void applyGlobalForce(Vector2D force) {
        for (Particle particle : particles) {
            particle.applyForce(force);
        }
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public void repositionParticles() {
        for (Particle particle : particles) {
            // Reposition particles if they are outside the new boundaries
            double x = Math.min(particle.getPosition().getX(), width - PARTICLE_RADIUS);
            double y = Math.min(particle.getPosition().getY(), height - PARTICLE_RADIUS);

            // Ensure particles are not repositioned into the non-visible area
            x = Math.max(x, PARTICLE_RADIUS);
            y = Math.max(y, PARTICLE_RADIUS);

            particle.setPosition(new Vector2D(x, y));
        }
    }

    public void toggleGravity() {
        isGravityEnabled = !isGravityEnabled;
    }

    public List<Particle> getParticles() {
        return particles;
    }
}

