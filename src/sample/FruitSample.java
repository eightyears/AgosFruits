package sample;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public final class FruitSample extends Rectangle {

    public static final FruitSample APPLE = new FruitSample("Apple", new Image("assets/apple.png"), 40, 40, 20);
    public static final FruitSample BANANA = new FruitSample("Banana", new Image("assets/banana.png"), 35, 45, 15);
    public static final FruitSample MELON = new FruitSample("Melon", new Image("assets/melon.png"), 35, 35, 30);
    public static final FruitSample PEAR = new FruitSample("Pear", new Image("assets/pear.png"), 30, 40, 10);
    public static final FruitSample KIWI = new FruitSample("Kiwi", new Image("assets/kiwi.png"), 40, 40, 25);

    public static final FruitSample BOMB = new FruitSample("Bomb", new Image("assets/bomb.png"), 40, 40, 0);

    private String name;
    private Image image;
    private int score;

    private FruitSample(String name, Image image, int w, int h, int score) {
        super(w, h);
        this.name = name;
        this.image = image;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    public int getScore() {
        return score;
    }
}
