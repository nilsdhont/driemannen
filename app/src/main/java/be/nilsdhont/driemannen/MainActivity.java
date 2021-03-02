package be.nilsdhont.driemannen;

import static com.squareup.seismic.ShakeDetector.SENSITIVITY_LIGHT;

import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.seismic.ShakeDetector;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener {

  private boolean driemanRolled;
  private boolean nietsmanRolled;
//  https://github.com/fragalcer/3DDice/blame/master/app/src/main/java/com/example/franciscogallardo/a3ddice/MyGLRenderer.java

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    //    Toolbar toolbar = findViewById(R.id.toolbar);
    //    setSupportActionBar(toolbar);

    FloatingActionButton fab = findViewById(R.id.fab);
    fab.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            clearAll();
          }
        });
    SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    ShakeDetector sd = new ShakeDetector(this);
    sd.setSensitivity(SENSITIVITY_LIGHT);
    sd.start(sensorManager);
  }

  private void clearAll() {
    driemanRolled = false;
    nietsmanRolled = false;
    TextView drinkCommand = findViewById(R.id.drinkCommand);
    drinkCommand.setText("");
    TextView nextActionCommand = findViewById(R.id.nextActionCommand);
    nextActionCommand.setText("");
    ImageView diceLeft = findViewById(R.id.diceLeft);
    diceLeft.setImageResource(R.drawable.dice1);
    ImageView diceRight = findViewById(R.id.diceRight);
    diceRight.setImageResource(R.drawable.dice1);
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
    Random random = new Random();
    int valueLeft = random.nextInt(6) + 1;
    int valueRight = random.nextInt(6) + 1;

    updateDice(R.id.diceLeft, valueLeft);
    updateDice(R.id.diceRight, valueRight);
    updateDrinkCommand(valueLeft, valueRight);
  }

  private void updateDrinkCommand(int valueLeft, int valueRight) {
    int totalValue = valueLeft + valueRight;
    boolean drink = false;

    StringBuilder message = new StringBuilder();
    if (driemanRolled && (valueLeft == 3 || valueRight == 3)) {
      message.append("Drieman drinken!");
      message.append("\n");
      drink = true;
    }

    if (totalValue == 3) {
      message.append("We hebben nen drieman bij!");
      message.append("\n");
      drink = true;
      driemanRolled = true;
    }
    if (totalValue == 2) {
      message.append("We hebben nen nietsman bij!");
      message.append("\n");
      drink = true;
      nietsmanRolled = true;
    }

    if (totalValue == 7) {
      message.append("Vorige drinken!");
      message.append("\n");
      drink = true;
    }

    if (totalValue == 9) {
      message.append("Zelf drinken!");
      message.append("\n");
      drink = true;
    }

    if (totalValue == 11) {
      message.append("Volgende drinken!");
      message.append("\n");
      drink = true;
    }

    if (valueLeft == valueRight) {
      if (valueLeft == 1) {
        message.append(valueLeft + " slok uitdelen!");
        message.append("\n");
      } else {
        message.append(valueLeft + " slokken uitdelen!");
        message.append("\n");
        drink = true;
      }
    }

    if (nietsmanRolled && !drink) {
      message.append("Nietsman drinken!");
    }

    TextView drinkCommand = findViewById(R.id.drinkCommand);
    drinkCommand.setText(message.toString());

    TextView nextActionCommand = findViewById(R.id.nextActionCommand);
    String nextAction = drink ? "Nog eens dobbelen!" : "Doorgeven!";
    nextActionCommand.setText(nextAction);
  }

  private void updateDice(int diceId, int diceValue) {
    ImageView img = findViewById(diceId);
    switch (diceValue) {
      case 1:
        img.setImageResource(R.drawable.dice1);
        break;
      case 2:
        img.setImageResource(R.drawable.dice2);
        break;
      case 3:
        img.setImageResource(R.drawable.dice3);
        break;
      case 4:
        img.setImageResource(R.drawable.dice4);
        break;
      case 5:
        img.setImageResource(R.drawable.dice5);
        break;
      case 6:
        img.setImageResource(R.drawable.dice6);
        break;
    }
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
}
