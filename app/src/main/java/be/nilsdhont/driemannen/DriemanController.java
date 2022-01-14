package be.nilsdhont.driemannen;

public class DriemanController {

  private boolean driemanRolled = false;
  private boolean nietsmanRolled = false;
  private StringBuilder message = new StringBuilder();
  private String nextAction = "";

  public void roll(int dice1, int dice2) {
    message = new StringBuilder();
    boolean drink = false;
    int totalValue = dice1 + dice2;

    //    message.append("(" + dice1 + "&" +dice2+")");

    if (driemanRolled && (dice1 == 3 || dice2 == 3)) {
      message.append("Drieman drinken!");
      message.append("\n");
      drink = true;
    }

    switch (totalValue) {
      case 2:
        message.append("We hebben nen nietsman bij!");
        message.append("\n");
        drink = false;
        nietsmanRolled = true;
        break;
      case 3:
        message.append("We hebben nen drieman bij!");
        message.append("\n");
        drink = true;
        driemanRolled = true;
        break;
      case 7:
        message.append("Vorige drinken!");
        message.append("\n");
        drink = true;
        break;
      case 9:
        message.append("Zelf drinken!");
        message.append("\n");
        drink = true;
        break;
      case 11:
        message.append("Volgende drinken!");
        message.append("\n");
        drink = true;
        break;
      default:
        break;
    }

    if (dice1 == dice2) {
      if (dice1 == 1) {
        message.append(dice1 + " slok uitdelen!");
        message.append("\n");
      } else {
        message.append(dice1 + " slokken uitdelen!");
        message.append("\n");
        drink = true;
      }
    }

    if (nietsmanRolled && !drink && totalValue != 2) {
      message.append("Nietsman drinken!");
    }

    nextAction = drink ? "Nog eens dobbelen!" : "Doorgeven!";
  }

  public String getDrinkMessage() {
    return message.toString();
  }

  public String getNextAction() {
    return nextAction;
  }

  public void reset() {
    driemanRolled = false;
    nietsmanRolled = false;
    message = new StringBuilder();
  }
}
