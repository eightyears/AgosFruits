package sample;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import leaderboard.LeaderboardManager;
import leaderboard.Score;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Main extends Application {
    private Stage stage;

    private Pane gamePane;

    private final double CANVAS_WIDTH = 800;
    private final double CANVAS_HEIGHT = 450;

    private final Image backgroundImage = new Image("assets/game-background.png");
    private final Image tutorialImage = new Image("assets/Tutorial.png");

    private int score = 0;
    private int currentLevel = 1;
    private double t = 0;
    private int fruitId = 0;

    private LeaderboardManager lm = new LeaderboardManager();

    private double cursorX;

    private Label scoreLabel;
    private Label levelLabel;
    private Label timeLabel = new Label();

    private long startTime;

    private Rectangle ago = new Rectangle(75, 100);
    private int agosLives = 3;
    private VBox healthBox;

    private String playerName = "";
    private static final int NAME_LIMIT = 14;

    private AnimationTimer timer;
    private String playTime;

    private FruitSample[] fruits
            = {FruitSample.APPLE, FruitSample.BANANA, FruitSample.MELON, FruitSample.PEAR,
            FruitSample.KIWI, FruitSample.BOMB};

    private double fruitDropRate = 0.5;
    private int dropSpeed = 5;

    private boolean levelUp = true;

    private Pane createMenu(Stage stage) {
        BorderPane menuPane = new BorderPane();
        menuPane.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        final ImageView screen_node = new ImageView();
        screen_node.setImage(backgroundImage);
        menuPane.getChildren().add(screen_node);

        Label header = new Label("Ago's Fruits");
        header.setFont(new Font("Arial", 80));
        header.setTextFill(Color.GREEN);

        Button start = new Button("Play");
        Button highscores = new Button("High Scores");
        Button tutorial = new Button("Tutorial");
        Button quit = new Button("Quit");
        start.setOnAction(e -> {
            Scene scene = new Scene(askPlayerName());
            stage.setScene(scene);
        });
        highscores.setOnAction(h -> {
            Scene scene = new Scene(showLeaderboard());
            stage.setScene(scene);
        });
        tutorial.setOnAction(e -> {
            Scene scene = new Scene(createTutorial());
            stage.setScene(scene);
        });
        quit.setOnAction(f -> stage.close());
        VBox vbox = new VBox(header, start, highscores, tutorial, quit);
        vbox.setPadding(new Insets(10));
        menuPane.setCenter(vbox);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        return menuPane;
    }

    private Parent askPlayerName() {
        BorderPane pane = new BorderPane();
        pane.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        final ImageView screen_node = new ImageView();
        screen_node.setImage(backgroundImage);
        pane.getChildren().add(screen_node);

        Label askName = new Label("Enter your name!");
        TextField textField = new TextField(playerName);
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                if (textField.getText().length() >= NAME_LIMIT) {
                    textField.setText(textField.getText().substring(0, NAME_LIMIT));
                }
            }
        });
        textField.setAlignment(Pos.CENTER);
        textField.setPrefWidth(120);
        HBox hb = new HBox(textField);
        hb.setAlignment(Pos.CENTER);
        Button start = new Button("Start");
        Button back = new Button("Back");
        start.setOnAction(e -> {
            if (textField.getText().equals("")) playerName = "Player";
            else playerName = textField.getText();
            if (agosLives != 3) {
                agosLives = 3;
                score = 0;
                currentLevel = 1;
                fruitDropRate = 0.5;
                dropSpeed = 5;
            }
            Scene scene = new Scene(createContent());
            stage.setScene(scene);
            scene.setOnMouseMoved(event -> cursorX = event.getX());
        });
        back.setOnAction(event -> {
            Scene scene = new Scene(createMenu(stage));
            stage.setScene(scene);
        });
        VBox vb = new VBox(askName, hb, start, back);
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);
        pane.setCenter(vb);
        return pane;
    }

    private Parent showLeaderboard() {
        BorderPane bPane = new BorderPane();
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(0, 0, 0, 120));
        bPane.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        pane.setVgap(10);
        pane.setHgap(50);

        final ImageView screen_node = new ImageView();
        screen_node.setImage(backgroundImage);
        bPane.getChildren().add(screen_node);

        Label leaderboard = new Label("Leaderboard");
        leaderboard.setFont(new Font("SegoeUI", 28));

        pane.add(new Label("Place"), 0, 0);
        pane.add(new Label("Player Name"), 1, 0);
        pane.add(new Label("Score"), 2, 0);
        pane.add(new Label("Maximum Level"), 3, 0);
        pane.add(new Label("Played Time"), 4, 0);

        List<Score> highscores = lm.getHighscores();
        int row = 1;
        for (Score highscore : highscores) {
            pane.add(new Label(row + "."), 0, row);
            pane.add(new Label(highscore.getName()), 1, row);
            pane.add(new Label(String.valueOf(highscore.getScore())), 2, row);
            pane.add(new Label(String.valueOf(highscore.getLevel())), 3, row);
            pane.add(new Label(highscore.getPlayTime()), 4, row);
            row++;
        }
        Button backToMenu = new Button("Back");
        backToMenu.setOnAction(event -> {
            Scene scene = new Scene(createMenu(stage));
            stage.setScene(scene);
        });
        pane.getStylesheets().add("sample/styles.css");
        VBox vBox = new VBox(leaderboard, pane, backToMenu);
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);
        bPane.setCenter(vBox);
        return bPane;
    }

    private Parent createTutorial() {
        BorderPane root = new BorderPane();
        final ImageView screen_node = new ImageView();

        screen_node.setImage(tutorialImage);
        root.getChildren().add(screen_node);

        root.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);
        Button quitTutorial = new Button("Exit");
        quitTutorial.setOnAction(e-> {
            Scene scene = new Scene (createMenu(stage));
            stage.setScene(scene);

        });
        VBox vbox = new VBox(quitTutorial);
        vbox.setPadding(new Insets(1));
        root.setCenter(vbox);
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.BOTTOM_RIGHT);

        return root;
    }

    private Parent createContent() {
        BorderPane root = new BorderPane();
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
        scoreLabel = new Label(String.valueOf(score));
        scoreLabel.setFont(new Font("SegoeUI", 18));
        scoreLabel.setMinWidth(100);
        levelLabel = new Label("Level " + currentLevel);
        levelLabel.setFont(new Font("SegoeUI", 18));

        HBox hbox = new HBox(scoreLabel, levelLabel, timeLabel);
        hbox.setSpacing(250);
        healthBox = new VBox();
        for (int i = 0; i < agosLives; i++) {
            ImageView image = new ImageView(new Image("assets/lifes.png"));
            healthBox.getChildren().add(image);
        }
        healthBox.setSpacing(5);
        hbox.setPadding(new Insets(10));
        root.setTop(hbox);
        root.setLeft(healthBox);
        gamePane = new Pane();
        root.setCenter(gamePane);

        timer = new AnimationTimer() {
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
        t += 0.056;
        ago.setTranslateX(cursorX - ago.getWidth() / 2);
        if (t > 2) {
            if (Math.random() < fruitDropRate) {
                dropFruit();
            }
        }
        getFruits().forEach(f -> {
            f.moveDown(dropSpeed);
            if (f.getBoundsInParent().intersects(ago.getBoundsInParent())) {
                if (f.getFruitSample().getName().equals("Bomb")) {
                    f.setCollected(true);
                    System.out.println("Kabooom!");
                    endGame();
                }else {
                    score += f.getFruitSample().getScore();
                    scoreLabel.setText(String.valueOf(score));
                    System.out.println(score);
                    f.setCollected(true);
                }
            } else if (f.getTranslateY() > CANVAS_HEIGHT - 2 * f.getHeight()
                    && !f.getFruitSample().getName().equals("Bomb")) {
                System.out.println("missed!");
                agosLives--;
                if (agosLives > 0) {
                    healthBox.getChildren().remove(agosLives - 1);
                }
                f.setCollected(true);
                if (agosLives == 0) {
                    endGame();
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
        playTime = String.format("%02d:%02d", elapsedMinutes, secondsDisplay);
        timeLabel.setText(playTime);
        if (!levelUp && elapsedSeconds % 21 == 0) {
            levelUp = true;
        }
        if (secondsDisplay % 20 == 0 && elapsedSeconds > 0 && levelUp) {
            currentLevel++;
            levelLabel.setText("Level " + currentLevel);
            fruitDropRate += 0.2;
            dropSpeed += 2;
            levelUp = false;
        }
    }

    private void dropFruit() {
        int randomFruitNumber = new Random().nextInt(fruits.length);
        int randomNum = ThreadLocalRandom.current().nextInt(100, (int) (CANVAS_WIDTH - 100));
        fruitId++;
        Fruit f = new Fruit(fruits[randomFruitNumber], fruitId, randomNum, 0);
        gamePane.getChildren().add(f);
    }

    private void endGame() {
        timer.stop();
        lm.addScore(playerName, score, currentLevel, playTime);
        Scene scene = new Scene(gameOver());
        stage.setScene(scene);
    }

    private Parent gameOver() {
        BorderPane gameOverPane = new BorderPane();
        gameOverPane.setPrefSize(CANVAS_WIDTH, CANVAS_HEIGHT);

        Label header = new Label("Game Over");
        header.setFont(new Font("Arial", 80));
        header.setTextFill(Color.RED);

        final ImageView screen_node = new ImageView();
        screen_node.setImage(backgroundImage);
        gameOverPane.getChildren().add(screen_node);

        Label finalScore = new Label("Your score: " + score);
        Label maximumLevel = new Label("Maximum level: " + currentLevel);
        Label playedTime = new Label("Total time played: " + playTime);
        finalScore.setFont(new Font("SegoeUI", 16));
        maximumLevel.setFont(new Font("SegoeUI", 16));
        playedTime.setFont(new Font("SegoeUI", 16));

        HBox hBox = new HBox(finalScore, maximumLevel, playedTime);
        hBox.setSpacing(25);
        hBox.setPadding(new Insets(10));
        hBox.setAlignment(Pos.CENTER);

        Button start = new Button("Play Again");
        Button mainMenu = new Button("Main Menu");
        Button quit = new Button("Quit");
        start.setOnAction(e -> {
            Scene scene = new Scene(askPlayerName());
            stage.setScene(scene);
        });
        mainMenu.setOnAction(event -> {
            Scene scene = new Scene(createMenu(stage));
            stage.setScene(scene);
        });
        quit.setOnAction(f -> stage.close());
        VBox vbox = new VBox(header, hBox, start, mainMenu, quit);
        vbox.setPadding(new Insets(10));
        vbox.setSpacing(5);
        vbox.setAlignment(Pos.CENTER);
        gameOverPane.setCenter(vbox);
        return gameOverPane;
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
