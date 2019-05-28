package sample;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

final class FruitSample extends Rectangle {

    static final FruitSample APPLE = new FruitSample("Apple", new Image("assets/images/apple.png"), 40, 40, 20);
    static final FruitSample BANANA = new FruitSample("Banana", new Image("assets/images/banana.png"), 35, 45, 15);
    static final FruitSample MELON = new FruitSample("Melon", new Image("assets/images/melon.png"), 35, 35, 30);
    static final FruitSample PEAR = new FruitSample("Pear", new Image("assets/images/pear.png"), 30, 40, 10);
    static final FruitSample KIWI = new FruitSample("Kiwi", new Image("assets/images/kiwi.png"), 40, 40, 25);

    static final FruitSample BOMB = new FruitSample("Bomb", new Image("assets/images/bomb.png"), 40, 40, 0);

    private String name;
    private Image image;
    private int score;

    private FruitSample(String name, Image image, int w, int h, int score) {
        super(w, h);
        this.name = name;
        this.image = image;
        this.score = score;
    }

    String getName() {
        return name;
    }

    Image getImage() {
        return image;
    }

    int getScore() {
        return score;
    }
}
