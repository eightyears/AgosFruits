package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    private Stage stage;

    private BorderPane root = new BorderPane();
    private Pane gamePane = new Pane();

    private final double CANVAS_WIDTH = 800;
    private final double CANVAS_HEIGHT = 450;

    private final Image backgroundImage = new Image("assets/game-background.png");

    private int score = 0;
    private double t = 0;
    private int fruitId = 0;

    private double cursorX;

    private Label scoreLabel = new Label(String.valueOf(score));
    private Label timeLabel = new Label();

    private long startTime;

    private Rectangle ago = new Rectangle(100, 100);
    private int agosLives = 3;

    private FruitSample[] fruits
            = {FruitSample.APPLE, FruitSample.BANANA, FruitSample.MELON, FruitSample.PEAR, FruitSample.KIWI};

    private Pane createMenu(Stage stage) {
        BorderPane menuPane = new BorderPane();
        menuPane.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        final ImageView screen_node = new ImageView();
        screen_node.setImage(backgroundImage);
        menuPane.getChildren().add(screen_node);

        Label header = new Label("Ago's Fruits");
        header.setFont(new Font("Arial", 80));
        header.setTextFill(Color.GREEN);

        Button start = new Button("New Game");
        Button quit = new Button("Quit");
        start.setOnAction(e -> {
            Scene scene = new Scene(createContent());
            stage.setScene(scene);
            scene.setOnMouseMoved(event -> cursorX = event.getX());
        });
        quit.setOnAction(f -> stage.close());
        VBox vbox = new VBox(header, start, quit);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        menuPane.setCenter(vbox);
        return menuPane;
    }

    private Parent createContent() {
        final ImageView screen_node = new ImageView();

        root.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        screen_node.setImage(backgroundImage);
        root.getChildren().add(screen_node);

        startTime = System.currentTimeMillis();

        ago.setFill(new ImagePattern(new Image("assets/ago.png")));
        ago.setTranslateX(CANVAS_WIDTH / 2 - ago.getWidth());
        ago.setTranslateY(CANVAS_HEIGHT - ago.getHeight());
        root.getChildren().add(ago);

        timeLabel.setFont(new Font("SegoeUI", 18));
        timeLabel.setMinWidth(50);
        scoreLabel.setFont(new Font("SegoeUI", 18));
        scoreLabel.setMinWidth(100);

        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        HBox hbox = new HBox(scoreLabel, space, timeLabel);
        hbox.setPadding(new Insets(10));
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
                agosLives--;
                f.setCollected(true);
                if (agosLives == 0) {
                    stage.close();
                }
            }
        });
        gamePane.getChildren().removeIf(f -> {
            Fruit fruit = (Fruit) f;
            return fruit.isCollected();
        });
        if (t > 2) {
            t = 0;
        }
        updateTimer();
    }

    private void updateTimer() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        long elapsedSeconds = elapsedTime / 1000;
        long secondsDisplay = elapsedSeconds % 60;
        long elapsedMinutes = elapsedSeconds / 60;
        String timeString = String.format("%02d:%02d", elapsedMinutes, secondsDisplay);
        timeLabel.setText(timeString);
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
        stage = primaryStage;
        Scene menu = new Scene(createMenu(stage));
        stage.setTitle("Ago's Fruits");
        stage.setScene(menu);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
