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
    
