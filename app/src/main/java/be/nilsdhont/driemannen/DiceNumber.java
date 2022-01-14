package be.nilsdhont.driemannen;

import java.security.InvalidParameterException;
import java.util.Arrays;

public enum DiceNumber {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6);

  int intValue;

  DiceNumber(int intValue) {
    this.intValue = intValue;
  }

  public static DiceNumber fromInt(int number) {
    return Arrays.stream(values())
        .filter(diceNumber -> diceNumber.intValue == number)
        .findFirst()
        .orElseThrow(InvalidParameterException::new);
  }
}
