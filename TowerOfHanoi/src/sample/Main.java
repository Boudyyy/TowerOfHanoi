package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.Optional;
import java.util.Random;

public class Main extends Application {

    private static final int NUM_CIRCLES = 5;
    private Optional<Circle> selectedCircle = Optional.empty();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Tower of Hanoi");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    Random r = new Random();
    int colorRed = r.nextInt(255);
    int colorGreen = r.nextInt(255);
    int colorBlue = r.nextInt(255);

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(400*3, 400);

        for (int i = 0; i < 3; i++) {
            Tower tower = new Tower(i*400, 0);
            if(i == 0) {
                for (int j = NUM_CIRCLES; j > 0; j--) {
                    Circle circle = new Circle(30 + j*20, null);
                    circle.setStroke(Color.rgb(colorRed, colorGreen, colorBlue));
                    circle.setStrokeWidth(circle.getRadius() / 30.0);

                    tower.addCircle(circle);
                }
            }

            root.getChildren().add(tower);
        }
        return root;
    }

    private class Tower extends StackPane {
        Tower(int x, int y) {
            setTranslateX(x);
            setTranslateY(y);
            setPrefSize(400, 400);

            Rectangle bg = new Rectangle(25, 25);
            bg.setOnMouseClicked(e -> {
                if (selectedCircle.isPresent()) {
                    addCircle(selectedCircle.get());
                    selectedCircle = Optional.empty();
                }else{
                    selectedCircle = Optional.ofNullable(getTopMost());

                    Circle topMost = getTopMost();
                }
            });

            getChildren().add(bg);
        }

        private Circle getTopMost() {
            return getChildren().stream()
                    .filter(n -> n instanceof Circle)
                    .map(n -> (Circle) n)
                    .min(Comparator.comparingDouble(Circle::getRadius))
                    .orElse(null);
        }

        void addCircle(Circle circle) {
            Circle topMost = getTopMost();
            if (topMost == null) {
                getChildren().add(circle);
            } else {
                if(circle.getRadius() < topMost.getRadius()) {
                    getChildren().add(circle);
                }
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
