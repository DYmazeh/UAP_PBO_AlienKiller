package game;

import javafx.geometry.Rectangle2D;

public class Musuh extends Karakter {
    private int speed; 

    // Konstruktor untuk Musuh
    public Musuh(int x, int y, int speed) {
        super(x, y, 1); 
        this.speed = speed; 
    }

    // Metode untuk mendapatkan batasan (bounding box) musuh
    public Rectangle2D getBounds() {
        return new Rectangle2D(x, y, 50, 50); 
    }
}