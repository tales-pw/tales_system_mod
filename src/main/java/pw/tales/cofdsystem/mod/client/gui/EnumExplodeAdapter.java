package pw.tales.cofdsystem.mod.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import pw.tales.cofdsystem.dices.EnumExplode;
import pw.tales.cofdsystem.mod.client.gui.project.GuiButtonEnum.IOrderedValue;

public enum EnumExplodeAdapter implements IOrderedValue<EnumExplodeAdapter> {
  NONE(EnumExplode.NONE),
  DEFAULT(EnumExplode.DEFAULT),
  NINE_AGAIN(EnumExplode.NINE_AGAIN),
  EIGHT_AGAIN(EnumExplode.EIGHT_AGAIN),
  ROTE_ACTION(EnumExplode.ROTE_ACTION);

  private static final List<EnumExplodeAdapter> VALUES = new ArrayList<>();

  static {
    Collections.addAll(VALUES, values());
  }

  private final EnumExplode explode;

  EnumExplodeAdapter(EnumExplode explode) {
    this.explode = explode;
  }

  @Override
  public EnumExplodeAdapter getNext() {
    int i = VALUES.indexOf(this) + 1;
    i = i >= VALUES.size() ? 0 : i;
    return VALUES.get(i);
  }

  @Override
  public String getEnumName() {
    return "explode";
  }

  @Override
  public String getValueName() {
    return this.explode.getName();
  }

  public EnumExplode getExplode() {
    return explode;
  }
}
