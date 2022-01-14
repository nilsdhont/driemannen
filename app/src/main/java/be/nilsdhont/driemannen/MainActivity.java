package be.nilsdhont.driemannen;

import static com.squareup.seismic.ShakeDetector.SENSITIVITY_LIGHT;

import android.graphics.PixelFormat;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.seismic.ShakeDetector;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener {

  private GLSurfaceView glViewDice1;
  private MyGLRenderer myGLRendererDice1;

  private GLSurfaceView glViewDice2;
  private MyGLRenderer myGLRendererDice2;

  private TextView drinkCommandView;
  private TextView nextActionCommand;

  private final DriemanController driemanController = new DriemanController();

  private boolean rolling = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    drinkCommandView = findViewById(R.id.drinkCommand);

    glViewDice1 = findViewById(R.id.dice1);
    myGLRendererDice1 = createDice3DArea(glViewDice1);
    glViewDice2 = findViewById(R.id.dice2);
    myGLRendererDice2 = createDice3DArea(glViewDice2);

    nextActionCommand = findViewById(R.id.nextActionCommand);

    createResetButton();
    addShakeSensor();
  }

  private MyGLRenderer createDice3DArea(GLSurfaceView view) {
    MyGLRenderer myGLRenderer = new MyGLRenderer(getApplicationContext());
    view.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
    view.getHolder().setFormat(PixelFormat.RGBA_8888);
    view.setZOrderOnTop(true);
    view.setRenderer(myGLRenderer);
    return myGLRenderer;
  }

  private void createResetButton() {
    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(v -> clearAll());
  }

  private void addShakeSensor() {
    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    ShakeDetector sd = new ShakeDetector(this);
    sd.setSensitivity(SENSITIVITY_LIGHT);
    sd.start(sensorManager);
  }

  private void clearAll() {
    driemanController.reset();
    clearDrinkCommand();
    clearNextAction();
  }

  @Override
  public void hearShake() {
    rollDices();
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (event.getAction() == MotionEvent.ACTION_UP) {
      rollDices();
    }
    return super.onTouchEvent(event);
  }

  private void rollDices() {
    if (!rolling) {
      rolling = true;
      clearDrinkCommand();
      clearNextAction();

      Random random = new Random();
      int diceThrow1 = random.nextInt(6) + 1;
      int diceThrow2 = random.nextInt(6) + 1;

      animateDice(myGLRendererDice1, diceThrow1);
      animateDice(myGLRendererDice2, diceThrow2);

      driemanController.roll(diceThrow1, diceThrow2);

      (new Handler()).postDelayed(this::updateDrinkCommand, 5 * 1000);
      (new Handler()).postDelayed(this::updateNextAction, 5 * 1000);
      (new Handler()).postDelayed(() -> rolling = false, 6 * 1000);
    }
  }

  private void animateDice(MyGLRenderer myGLRenderer, int diceThrow) {
    myGLRenderer.angleDice = 1080;
    myGLRenderer.speedDice = 5;
    myGLRenderer.fixX = 15;
    myGLRenderer.fixY = 100;
    myGLRenderer.fixZ = 30;

    myGLRenderer.diceNumberSetter(DiceNumber.fromInt(diceThrow));
  }

  private void updateDrinkCommand() {
    drinkCommandView.setText(driemanController.getDrinkMessage());
  }

  private void clearDrinkCommand() {
    drinkCommandView.setText("");
  }

  private void updateNextAction() {
    nextActionCommand.setText(driemanController.getNextAction());
  }

  private void clearNextAction() {
    nextActionCommand.setText("");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onPause() {
    super.onPause();
    glViewDice1.onPause();
    glViewDice2.onPause();
  }

  @Override
  protected void onResume() {
    super.onResume();
    glViewDice1.onResume();
    glViewDice2.onResume();
  }
}
