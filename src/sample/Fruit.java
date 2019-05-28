package sample;

import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

class Fruit extends Rectangle {

    private FruitSample fruitSample;
    private int id;
    private boolean collected = false;

    Fruit(FruitSample fruit, int id, int x, int y) {
        super(fruit.getWidth(), fruit.getHeight());
        fruitSample = fruit;
        this.id = id;
        this.setTranslateX(x);
        this.setTranslateY(y);
        this.setFill(new ImagePattern(fruit.getImage()));
    }

    void moveDown(int dropSpeed) {
        setTranslateY(getTranslateY() + dropSpeed);
    }

    FruitSample getFruitSample() {
        return fruitSample;
    }

    boolean isCollected() {
        return collected;
    }

    void setCollected() {
        this.collected = true;
    }

    public int getFruitId() {
        return id;
    }
}
