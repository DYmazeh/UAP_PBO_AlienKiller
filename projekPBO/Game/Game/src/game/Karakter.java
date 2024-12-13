package game;

public abstract class Karakter {
    protected int x; // Posisi horizontal
    protected int y; // Posisi vertikal
    public int nyawa; // Jumlah nyawa karakter
    
    // Konstruktor untuk Karakter
    public Karakter(int x, int y, int nyawa) {
        this.x = x;
        this.y = y;
        this.nyawa = nyawa; 
    }

    // Getter 
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getNyawa() {
        return nyawa;
    }

    // Metode untuk mengubah posisi
    public void gerak(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    // Metode untuk mengurangi nyawa
    public void kurangiNyawa(int jumlah) {
        nyawa -= jumlah;
        if (nyawa < 0) {
            nyawa = 0; //Non-Negatif
        }
    }

    // Metode untuk memeriksa apakah karakter masih hidup
    public boolean isAlive() {
        return nyawa > 0;
    }
}

