package sample;

import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public final class Fruit extends Rectangle {

    public static final Fruit APPLE = new Fruit("Apple", new Image("assets/apple.png"), 20, 20, 20);
    public static final Fruit BANANA = new Fruit("Banana", new Image("assets/banana.png"), 15, 25, 15);
    public static final Fruit MELON = new Fruit("Melon", new Image("assets/melon.png"), 25, 25, 30);
    public static final Fruit PEAR = new Fruit("Pear", new Image("assets/pear.png"), 15, 20, 10);
    public static final Fruit KIWI = new Fruit("Kiwi", new Image("assets/kiwi.png"), 20, 20, 25);

    private String name;
    private Image image;
    private int score;
    private boolean collected = false;

    private Fruit(String name, Image image, int w, int h, int score) {
        super(w, h);
        this.name = name;
        this.image = image;
        this.score = score;
    }

    public static Fruit createFruit(Fruit f, int x, int y) {
        f.setTranslateX(x);
        f.setTranslateY(y);
        return f;
    }

    void moveDown() {
        setTranslateY(getTranslateY() + 2);
    }

    public Image getImage() {
        return image;
    }

    public int getScore() {
        return score;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
