package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Main extends Application {

    private BorderPane root = new BorderPane();
    private Pane gamePane = new Pane();

    private final double CANVAS_WIDTH = 800;
    private final double CANVAS_HEIGHT = 450;

    private int score = 0;
    private double t = 0;
    private int fruitId = 0;

    private double cursorX;

    private Label scoreLabel = new Label(String.valueOf(score));
    private Label timeLabel = new Label("0:00");

    private Rectangle ago = new Rectangle(100, 100);

    private FruitSample[] fruits
            = {FruitSample.APPLE, FruitSample.BANANA, FruitSample.MELON, FruitSample.PEAR, FruitSample.KIWI};

    private Parent createContent() {
        final Image backgroundImage = new Image("assets/game-background.png");
        final ImageView screen_node = new ImageView();

        root.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        screen_node.setImage(backgroundImage);
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.getChildren().add(screen_node);

        ago.setFill(new ImagePattern(new Image("assets/ago.png")));
        ago.setTranslateX(CANVAS_WIDTH / 2 - ago.getWidth());
        ago.setTranslateY(CANVAS_HEIGHT - ago.getHeight());
        root.getChildren().add(ago);

        timeLabel.setFont(new Font("SegoeUI", 18));
        scoreLabel.setFont(new Font("SegoeUI", 18));

        HBox hbox = new HBox(scoreLabel);
        hbox.setPadding(new Insets(10));
        hbox.setSpacing(700);
        root.setTop(hbox);
        root.setCenter(gamePane);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };

        timer.start();

        return root;
    }

    private List<Fruit> getFruits() {
        return gamePane.getChildren().stream().map(f -> (Fruit) f).collect(Collectors.toList());
    }

    private void update() {
        t += 0.036;
        ago.setTranslateX(cursorX);
        if (t > 2) {
            if (Math.random() < 0.5) {
                dropFruit();
            }
        }
        getFruits().forEach(f -> {
            f.moveDown();
            if (f.getBoundsInParent().intersects(ago.getBoundsInParent())) {
                score += f.getFruitSample().getScore();
                scoreLabel.setText(String.valueOf(score));
                System.out.println(score);
                f.setCollected(true);
            } else if (f.getTranslateY() > CANVAS_HEIGHT - 2 * f.getHeight()) {
                System.out.println("missed!");
                f.setCollected(true);
            }
        });
        gamePane.getChildren().removeIf(f -> {
            Fruit fruit = (Fruit) f;
            return fruit.isCollected();
        });
        if (t > 2) {
            t = 0;
        }
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //timeLabel.setText(String.valueOf());
            }
        }, 0, 1000);
    }

    private void dropFruit() {
        int randomFruitNumber = new Random().nextInt(fruits.length);
        int randomNum = ThreadLocalRandom.current().nextInt(100, 701);
        fruitId++;
        Fruit f = new Fruit(fruits[randomFruitNumber], fruitId, randomNum, 0);
        gamePane.getChildren().add(f);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        /*scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
                case A:
                    ago.setTranslateX(ago.getTranslateX() - 10);
                    break;
                case D:
                    ago.setTranslateX(ago.getTranslateX() + 10);;
                    break;
            }
        });*/
        scene.setOnMouseMoved(e -> {
            cursorX = e.getX();
        });
        primaryStage.setTitle("Ago's Fruits");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
