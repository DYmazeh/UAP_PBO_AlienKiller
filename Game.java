import game.Buff;
import game.Musuh;
import game.Pahlawan;
import game.Peluru;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.image.ImageView;

public class Game extends Application {
    private Pahlawan pahlawan;
    private ArrayList<Musuh> musuhList;
    private ArrayList<Peluru> peluruList;
    private int skor = 0;
    private int level = 1;
    private boolean up, down, left, right, shootUp, shootDown, shootLeft, shootRight;
    private boolean canShoot = true;
    private boolean isGameOver = false;
    private boolean levelComplete = false;
    private boolean isImmune = false; 
    private long immunityStartTime;
    private Image background;
    private Image pahlawanImage;
    private Image musuhImage;
    private Text gameOverText;
    private Text scoreText;

    @Override
public void start(Stage primaryStage) {
    Pane root = new Pane();
    Canvas canvas = new Canvas(800, 600);
    GraphicsContext gc = canvas.getGraphicsContext2D();   
    
    background = new Image("game/2kmdlrc0av041.png");
    pahlawanImage = new Image("game/8652737_nftrocket_digital_rocket_system_security_icon.png");
    musuhImage = new Image("game/fa082ab5716b887.png");
    Image backgroundImage = new Image("game/background1.png");
    ImageView backgroundView = new ImageView(backgroundImage);
    backgroundView.setFitWidth(800);
    backgroundView.setFitHeight(600);
    root.getChildren().add(canvas);
    root.getChildren().add(backgroundView);

    Button playButton = new Button("START");
    playButton.setLayoutX(350);
    playButton.setLayoutY(450);
    playButton.setStyle(
        "-fx-font-size: 20px; " +
        "-fx-padding: 10px 20px; " +
        "-fx-background-color: linear-gradient(to bottom, #ff0000, #0000ff); " +
        "-fx-text-fill: white;"
    );
    root.getChildren().add(playButton);
    
    gameOverText = new Text("GAME OVER!");
    gameOverText.setFont(new Font(50));
    gameOverText.setFill(Color.RED);
    gameOverText.setLayoutX(250);
    gameOverText.setLayoutY(250);
    gameOverText.setVisible(false);

    scoreText = new Text();
    scoreText.setFont(new Font(30));
    scoreText.setFill(Color.RED);
    scoreText.setLayoutX(300);
    scoreText.setLayoutY(300);
    scoreText.setVisible(false);
    root.getChildren().addAll(gameOverText, scoreText);

    playButton.setOnAction(e -> {
        root.getChildren().removeAll(playButton, backgroundView);
        startGame(root, gc);                                      
    });

    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.setTitle("ALIEN KILLER");
    primaryStage.show();
}
    private void startGame(Pane root, GraphicsContext gc) {
    pahlawan = new Pahlawan(400, 300);
    musuhList = new ArrayList<>();
    peluruList = new ArrayList<>();
    skor = 0;
    level = 1;
    isGameOver = false;
    levelComplete = false;
    spawnMusuh(level);

    Scene scene = root.getScene();
    scene.setOnKeyPressed(e -> {
        if (e.getCode() == KeyCode.W) up = true;
        if (e.getCode() == KeyCode.S) down = true;
        if (e.getCode() == KeyCode.A) left = true;
        if (e.getCode() == KeyCode.D) right = true;
        if (e.getCode() == KeyCode.UP) shootUp = true;
        if (e.getCode() == KeyCode.DOWN) shootDown = true;
        if (e.getCode() == KeyCode.LEFT) shootLeft = true;
        if (e.getCode() == KeyCode.RIGHT) shootRight = true;
    });
    scene.setOnKeyReleased(e -> {
        if (e.getCode() == KeyCode.W) up = false;
        if (e.getCode() == KeyCode.S) down = false;
        if (e.getCode() == KeyCode.A) left = false;
        if (e.getCode() == KeyCode.D) right = false;
        if (e.getCode() == KeyCode.UP) shootUp = false;
        if (e.getCode() == KeyCode.DOWN) shootDown = false;
        if (e.getCode() == KeyCode.LEFT) shootLeft = false;
        if (e.getCode() == KeyCode.RIGHT) shootRight = false;
    });

    new AnimationTimer() {
        @Override
        public void handle(long now) {
            if (!isGameOver) {
                update();
                render(gc); 
            }
        }
    }.start();
}

    private void spawnMusuh(int jumlah) {
    Random rand = new Random();
    for (int i = 0; i < jumlah; i++) {
        int x = rand.nextInt(800);
        int y = 0;      
        musuhList.add(new Musuh(x, y, 1));
    }
      
}

// Memperbarui status permainan    
private void update() {
    
    // Gerakan Pahlawan dengan batas arena
    if (up && pahlawan.getY() > 0) pahlawan.gerak(0, -5);
    if (down && pahlawan.getY() < 600 - 50) pahlawan.gerak(0, 5);
    if (left && pahlawan.getX() > 0) pahlawan.gerak(-5, 0);
    if (right && pahlawan.getX() < 800 - 50) pahlawan.gerak(5, 0); 
    
    // Imunitas Pahlawan
    if (isImmune && (System.currentTimeMillis() - immunityStartTime >= 2000)) {
        isImmune = false; 
    }
    checkBuffCollision(); // Memeriksa tabrakan dengan buff
    
    // Menembak
    if (canShoot) {
    int dx = 0, dy = 0;
    if (shootUp) dy = -1;
    if (shootDown) dy = 1;
    if (shootLeft) dx = -1;
    if (shootRight) dx = 1;

        if (dx != 0 || dy != 0) {
        double magnitude = Math.sqrt(dx * dx + dy * dy);
        dx = (int) Math.round(dx / magnitude * 5);
        dy = (int) Math.round(dy / magnitude * 5);
            
            // Jika mengambil buff DoubleShot
            if (pahlawan.isDoubleShot()) {
            double[] arah = {
            0, Math.PI / 4, Math.PI / 2, 3 * Math.PI / 4,
            Math.PI, 5 * Math.PI / 4, 3 * Math.PI / 2, 7 * Math.PI / 4
            };
                
                // Menembak 8 arah
                for (double sudut : arah) {
                double dxTembak = Math.cos(sudut) * 5;
                double dyTembak = Math.sin(sudut) * 5;
                peluruList.add(new Peluru(pahlawan.getX() + 15, pahlawan.getY() + 15, (int) dxTembak, (int) dyTembak));
                }
            } 
            else { // Peluru menjadi normal
            peluruList.add(new Peluru(pahlawan.getX() + 15, pahlawan.getY() + 15, dx, dy));
            }
        canShoot = false;
        new Timeline(new KeyFrame(Duration.millis(200), e -> canShoot = true)).play();
        }
    }
    
    // Menghapus peluru dan musuh yang sudah tidak terlihat
    ArrayList<Peluru> peluruHapus = new ArrayList<>();
    ArrayList<Musuh> musuhHapus = new ArrayList<>();
    for (Peluru peluru : peluruList) {
        peluru.update();
        if (peluru.isOutOfBound(800, 600)) peluruHapus.add(peluru); // Jika peluru keluar dari batas, tandai untuk dihapus
        
        // Memeriksa tabrakan peluru dengan musuh
        for (Musuh musuh : musuhList) {
            if (peluru.getX() < musuh.getX() + 30 &&
                    peluru.getX() + 10 > musuh.getX() &&
                    peluru.getY() < musuh.getY() + 30 &&
                    peluru.getY() + 10 > musuh.getY()) {
                peluruHapus.add(peluru); // Tandai peluru untuk dihapus
                musuhHapus.add(musuh); // Tandai musuh untuk dihapus
                spawnBuff(musuh.getX(), musuh.getY()); // Tambahkan buff di posisi musuh yang mati
                skor += 10;
                break;
            }
        }
    }
    
    // Menghapus peluru dan musuh yang sudah ditandai
    peluruList.removeAll(peluruHapus);
    musuhList.removeAll(musuhHapus);
    
    //Menambahkan Musuh per level dengan jeda 2 detik
    if (musuhList.isEmpty() && !levelComplete) {
        levelComplete = true;
        new Timeline(new KeyFrame(Duration.seconds(2), e -> nextLevel())).play();
    }
    
    //Gerakan Musuh menuju pahlawan
    for (Musuh musuh : musuhList) {
        int dx = (pahlawan.getX() - musuh.getX()) > 0 ? 1 : -1;
        int dy = (pahlawan.getY() - musuh.getY()) > 0 ? 1 : -1;
        musuh.gerak(dx, dy);

    // Fungsi tabrakan dengan musuh dengan musuh
    for (Musuh musuhLain : musuhList) {
        if (musuh != musuhLain) {
            int jarakX = Math.abs(musuh.getX() - musuhLain.getX());
            int jarakY = Math.abs(musuh.getY() - musuhLain.getY());
            
            // Geser musuh sedikit menjauh dari musuh lainnya
            if (jarakX < 50 && jarakY < 50) {
                if (jarakX < jarakY) {
                    if (musuh.getX() < musuhLain.getX()) {
                        musuh.gerak(-1, 0); // Geser ke kiri
                    } else {
                        musuh.gerak(1, 0); // Geser ke kanan
                    }
                }
                else {
                    if (musuh.getY() < musuhLain.getY()) {
                        musuh.gerak(0, -1); // Geser ke atas
                    } else {
                        musuh.gerak(0, 1); // Geser ke bawah
                    }
                }
            }
        }
    }
    
        // Fungsi tabrakan antara pahlawan dan musuh
        if (Math.abs(musuh.getX() - pahlawan.getX()) < 30 && Math.abs(musuh.getY() - pahlawan.getY()) < 30) {
            if (!isImmune) {
                pahlawan.kurangiNyawa(1);
                isImmune = true;
                immunityStartTime = System.currentTimeMillis(); 
                if (pahlawan.getNyawa() == 0) {
                    gameOver();
                }
            }
            return;
        }
    }
}

private final List<Buff> buffList = new ArrayList<>(); // Daftar buff yang ada di arena

// Memunculkan buff di posisi ketika Musuh mati
private void spawnBuff(int x, int y) {
    Random rand = new Random();
    if (rand.nextInt(100) < 20) { // 20% kemungkinan muncul
        String[] buffTypes = {"Speed", "Nyawa", "DoubleShot"}; // Array jenis buff
        String jenisBuff = buffTypes[rand.nextInt(buffTypes.length)];  
        Buff buff = new Buff(jenisBuff, x + rand.nextInt(20) - 10, y + rand.nextInt(20) - 10); 
        buffList.add(buff); // Tambahkan ke Arena
        Pane root = (Pane) gameOverText.getParent();
        root.getChildren().add(buff.getBuffShape());

        // Timer untuk menghilangkan buff jika tidak diambil
        new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            root.getChildren().remove(buff.getBuffShape());
            buffList.remove(buff); 
        })).play();
    }
}

// Fungsi pahlawan menyentuh buff
private void checkBuffCollision() { 
    ArrayList<Buff> buffHapus = new ArrayList<>();
    for (Buff buff : buffList) {
        if (pahlawan.getBounds().intersects(buff.getBuffShape().getBoundsInParent())) {
            buff.activate(pahlawan); // Aktifkan efek buff
            Pane root = (Pane) gameOverText.getParent();
            root.getChildren().remove(buff.getBuffShape()); // Hapus dari scene
            buffHapus.add(buff); // Tandai untuk dihapus dari Arena
        }
    }
    buffList.removeAll(buffHapus); // Hapus buff yang telah dimakan
}

// Hapus semua buff ketika pahlawan mati
private void removeAllBuffs() {
    Pane root = (Pane) gameOverText.getParent();
    for (Buff buff : buffList) {
        root.getChildren().remove(buff.getBuffShape());
    }
    buffList.clear();
}

//Fungsi Musuh menabrak Pahlawan
private void checkCollisionWithMusuh() {
    for (Musuh musuh : musuhList) {
        
        // Menggerakkan musuh menuju pahlawan
        int dx = (pahlawan.getX() - musuh.getX()) > 0 ? 1 : -1;
        int dy = (pahlawan.getY() - musuh.getY()) > 0 ? 1 : -1;
        musuh.gerak(dx, dy);

        // Memeriksa tabrakan
        if (Math.abs(musuh.getX() - pahlawan.getX()) < 30 && Math.abs(musuh.getY() - pahlawan.getY()) < 30) {
            if (pahlawan.getNyawa() > 0) {
                pahlawan.kurangiNyawa(1);
                if (pahlawan.getNyawa() == 0) {
                    gameOver();
                }
            }
            return; 
        }
    }
}

//Melanjutkan ke level berikutnya
private void nextLevel() {
        level++;
        spawnMusuh(level);
        levelComplete = false;
    }

// Kondisi  ketika game over
private void gameOver() {
    isGameOver = true;
    removeAllBuffs();
    gameOverText.setVisible(true);
    scoreText.setVisible(true);
    scoreText.setText("Skor Akhir: " + skor);
    scoreText.setVisible(true); 
     
    // Tambahkan tombol restart
    Button restartButton = new Button("RESTART");
    restartButton.setLayoutX(330);
    restartButton.setLayoutY(350);
    restartButton.setStyle(
        "-fx-font-size: 20px; " +
        "-fx-padding: 10px 20px; " +
        "-fx-background-color: linear-gradient(to bottom, #ff0000, #0000ff); " +
        "-fx-text-fill: white;"
    );
    restartButton.setOnAction(e -> restartGame()); 
    Pane root = (Pane) gameOverText.getParent();
    root.getChildren().add(restartButton);
}
    
// Reset semua variabel untuk memulai ulang permainan
private void restartGame() {
    pahlawan = new Pahlawan(400, 300);
    musuhList.clear();
    peluruList.clear();
    skor = 0;
    level = 1;
    isGameOver = false;
    levelComplete = false;
    gameOverText.setVisible(false);
    scoreText.setVisible(false);
    Pane root = (Pane) gameOverText.getParent();
    root.getChildren().removeIf(node -> node instanceof Button);
    spawnMusuh(level);
}
    
// Render Gambar
private void render(GraphicsContext gc) {
    if (background != null) {
        gc.drawImage(background, 0, 0, 800, 600);
    }
    if (pahlawan != null && pahlawanImage != null) {
        gc.drawImage(pahlawanImage, pahlawan.getX(), pahlawan.getY(), 50, 50);
    }
    for (Musuh musuh : musuhList) {
        if (musuhImage != null) {
            gc.drawImage(musuhImage, musuh.getX(), musuh.getY(), 50, 50);
        }
    }
    gc.setFill(Color.YELLOW);
    for (Peluru peluru : peluruList) {
        gc.fillRect(peluru.getX(), peluru.getY(), 10, 10);
    }

    // Teks skor,level,dan nyawa
    gc.setFill(Color.WHITE);
    gc.setFont(new Font(20));
    gc.fillText("Score: " + skor, 10, 20);
    gc.fillText("Level: " + level, 10, 40);
    gc.fillText("Nyawa: " + pahlawan.nyawa, 10, 60);
    
    // Jika game over, tampilkan pesan "Game Over"
    if (isGameOver) {
    gc.setFill(Color.RED);
    gc.setFont(new Font(50));
    gc.fillText(" ", 240, 250);
    gc.setFont(new Font(30));
    gc.fillText(" ", 290, 300);
    }
}

    public static void main(String[] args) {
        launch(args);
    }
}
