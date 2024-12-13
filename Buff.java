package game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Buff {
    private String jenisBuff; 
    private Circle buffShape; 
    private boolean isActive; 

    // Konstruktor untuk Buff
    public Buff(String jenisBuff, int x, int y) {
        this.jenisBuff = jenisBuff; 
        this.isActive = true; 

        // Membuat bentuk visual buff (lingkaran)
        buffShape = new Circle(10); 
        buffShape.setTranslateX(x); 
        buffShape.setTranslateY(y); 

        // Warna sesuai jenis buff
        switch (jenisBuff) {
            case "Speed":
                buffShape.setFill(Color.BLUE); // Warna untuk buff Speed
                break;
            case "Nyawa":
                buffShape.setFill(Color.GOLD); // Warna untuk buff Nyawa
                break;
            case "DoubleShot":
                buffShape.setFill(Color.RED); // Warna untuk buff DoubleShot
                break;
            default:
                buffShape.setFill(Color.GRAY); // Warna default
        }
    }

    // Getter 
    public String getJenis() {
        return jenisBuff; 
    }
    public Circle getBuffShape() {
        return buffShape; 
    }

    // Cek apakah buff aktif
    public boolean isActive() {
        return isActive; 
    }

    // Aktifkan efek buff pada pahlawan
    public void activate(Pahlawan pahlawan) {
        if (!isActive) return; // Tidak melakukan apa-apa jika buff sudah tidak aktif

        // Aktifkan efek buff sesuai jenisnya
        switch (jenisBuff) {
            case "Speed":
                pahlawan.setSpeedMultiplier(2); // Kecepatan pahlawan jadi 2x
                break;
            case "Nyawa":
                pahlawan.tambahNyawa(1); // Tambah nyawa pahlawan
                break;
            case "DoubleShot":
                pahlawan.setDoubleShot(true); // Pahlawan bisa menembak 8 arah
                break;
        }

        // Reset efek buff setelah durasi selesai (misalnya 5 detik)
        new Thread(() -> {
            try {
                Thread.sleep(5000); 
                switch (jenisBuff) {
                    case "Speed":
                        pahlawan.setSpeedMultiplier(1); 
                        break;
                    case "DoubleShot":
                        pahlawan.setDoubleShot(false); 
                        break;
                }
                removeFromScene(); 
            } catch (InterruptedException e) {
            }
        }).start();
    }

    // Hapus buff dari layar
    public void removeFromScene() {
        buffShape.setVisible(false); 
        isActive = false; 
    }

    // Cek apakah bentuk buff menyentuh pahlawan
    public boolean intersects(Pahlawan pahlawan) {
        return buffShape.getBoundsInParent().intersects(pahlawan.getBounds()); 
    }

    // Timer otomatis untuk menghilangkan buff jika tidak diambil (15 detik)
    public void activateDespawnTimer() {
        new Thread(() -> {
            try {
                Thread.sleep(15000); 
                removeFromScene(); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
