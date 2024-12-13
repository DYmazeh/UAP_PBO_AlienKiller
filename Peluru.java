package game;

public class Peluru {
    private int x, y; // Posisi peluru
    private int dx, dy; // Arah peluru

    // Konstruktor untuk Peluru
    public Peluru(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
    
    // Memperbarui posisi peluru berdasarkan arah
    public void update() {
        x += dx;
        y += dy;
    }
    
    // Memeriksa apakah peluru keluar dari batas arena
    public boolean isOutOfBound(int width, int height) {
        return x < 0 || x > width || y < 0 || y > height;
    }

    // Getter posisi Peluru
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
