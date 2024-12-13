package game;

import javafx.scene.shape.Rectangle;

public class Pahlawan extends Karakter {
    private Rectangle pahlawanShape; 
    private int speedMultiplier = 1; 
    private boolean doubleShot = false; 
    
    
    public Pahlawan(int x, int y) {
        super(x, y, 1);
        
        
        pahlawanShape = new Rectangle(30, 30);
        pahlawanShape.setTranslateX(x);
        pahlawanShape.setTranslateY(y);
        pahlawanShape.setStyle("-fx-fill: yellow;");
    }

    public Rectangle getPahlawanShape() {
        return pahlawanShape;
    }

    public javafx.geometry.Bounds getBounds() {
        return pahlawanShape.getBoundsInParent(); 
    }
    
    @Override
    public void gerak(int dx, int dy) {
        x = Math.max(0, Math.min(Constants.ARENA_WIDTH - 30, x + dx * speedMultiplier));
        y = Math.max(0, Math.min(Constants.ARENA_HEIGHT - 30, y + dy * speedMultiplier));
        pahlawanShape.setTranslateX(x);
        pahlawanShape.setTranslateY(y);
    }
    
    public void tambahNyawa(int jumlah) {
    nyawa += jumlah; 
    if (nyawa > 5) {
        nyawa = 5; 
    }
}

    public int getNyawa() {
        return nyawa;
    }

    public void setSpeedMultiplier(int multiplier) {
        this.speedMultiplier = multiplier;
    }

    public void setDoubleShot(boolean doubleShot) {
        this.doubleShot = doubleShot;
    }

    public boolean isDoubleShot() {
        return doubleShot;
    }
}

