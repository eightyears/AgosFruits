package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Main extends Application {

    private BorderPane root = new BorderPane();
    private Pane gamePane = new Pane();

    private int score = 0;
    private double t = 0;

    private Label scoreLabel = new Label(String.valueOf(score));

    private Fruit[] fruits = {Fruit.APPLE, Fruit.BANANA, Fruit.MELON, Fruit.PEAR, Fruit.KIWI};

    private Parent createContent() {
        final Image backgroundImage = new Image("assets/game-background.png");
        final ImageView screen_node = new ImageView();
        final double CANVAS_WIDTH = 800;
        final double CANVAS_HEIGHT = 450;

        root.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        screen_node.setImage(backgroundImage);
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        root.getChildren().add(screen_node);

        //TODO add Ago here
        //root.getChildren().add(player);

        Label timeLabel = new Label("00:00");
        timeLabel.setFont(new Font("SegoeUI", 18));

        scoreLabel.setFont(new Font("SegoeUI", 18));

        HBox hbox = new HBox(timeLabel, scoreLabel);
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
        t += 0.016;
        if (t > 2) {
            if (Math.random() < 0.5) {
                dropFruit();
            }
            t = 0;
        }
        getFruits().forEach(f -> {
            f.moveDown();
            if (f.getTranslateY() == 400) {
                score += f.getScore();
                scoreLabel.setText(String.valueOf(score));
                System.out.println(score);
                f.setCollected(true);
            }
        });
        gamePane.getChildren().removeIf(f -> {
            Fruit fruit = (Fruit) f;
            return fruit.isCollected();
        });
    }

    private void dropFruit() {
        int randomFruitNumber = new Random().nextInt(fruits.length);
        int randomNum = ThreadLocalRandom.current().nextInt(100, 701);
        Fruit f = Fruit.createFruit(fruits[randomFruitNumber], randomNum, 100);
        //ImageView imageView = new ImageView(f.getImage());
        gamePane.getChildren().add(f);
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());
        primaryStage.setTitle("Ago's Fruits");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
