import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * @author : Zachary Doll
 */

public class FluidSimApp extends Application {

    private FluidDynamicsEngine engine;
    private Vector2D mousePressPosition = new Vector2D(0,0);
    private boolean mouseLeftClicked = false;
    private final double TRANSLATION_MULTIPLIER = 0.05;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, 800, 600);

        FluidDynamicsEngine engine = new FluidDynamicsEngine(800, 600);
        //adding particles to the root
        for (Particle p : engine.getParticles()) {
            root.getChildren().add(p);
        }

        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Update the simulation width in your engine
            engine.setWidth(newVal.doubleValue());

            // Optionally, reposition particles if needed
            engine.repositionParticles();
        });

        scene.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Update the simulation height in your engine
            engine.setHeight(newVal.doubleValue());

            // Optionally, reposition particles if needed
            engine.repositionParticles();
        });

        scene.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                mouseLeftClicked = true;
                mousePressPosition = new Vector2D(event.getX(), event.getY());
            }
        });

        scene.setOnMouseReleased(event -> mouseLeftClicked = false);
        scene.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                mousePressPosition = new Vector2D(event.getX(), event.getY());
            }
        });

        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.G) {
                engine.toggleGravity();
            }
        });

        primaryStage.setTitle("Fluid Simulation Sandbox");
        primaryStage.setScene(scene);
        primaryStage.show();

        ChangeListener<Number> windowPositionListener = new ChangeListener<Number>() {
            private double lastX = primaryStage.getX();
            private double lastY = primaryStage.getY();

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double deltaX = primaryStage.getX() - lastX;
                double deltaY = primaryStage.getY() - lastY;

                // Create a Vector2D representing the movement, you may need to scale this vector
                Vector2D movementForce = new Vector2D(deltaX, deltaY);
                movementForce.multiply(TRANSLATION_MULTIPLIER);
                engine.applyGlobalForce(movementForce); // Apply this force to all particles in the engine

                lastX = primaryStage.getX();
                lastY = primaryStage.getY();
            }
        };

        primaryStage.xProperty().addListener(windowPositionListener);
        primaryStage.yProperty().addListener(windowPositionListener);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (mouseLeftClicked) {
                    engine.applyAttraction(mousePressPosition); // Apply continuous attraction
                }
                engine.update();
            }
        }.start();
    }

}
