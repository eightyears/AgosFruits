package sample;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Fruit extends Rectangle {

    private FruitSample fruitSample;
    private int id;
    private boolean collected = false;

    public Fruit(FruitSample fruit, int id, int x, int y) {
        super(fruit.getWidth(), fruit.getHeight());
        fruitSample = fruit;
        this.id = id;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setFill(new ImagePattern(fruit.getImage()));
    }

    void moveDown() {
        setTranslateY(getTranslateY() + 5);
    }

    public FruitSample getFruitSample() {
        return fruitSample;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
