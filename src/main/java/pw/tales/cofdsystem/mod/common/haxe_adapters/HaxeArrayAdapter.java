package pw.tales.cofdsystem.mod.common.haxe_adapters;

import haxe.root.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class HaxeArrayAdapter<T> extends ArrayList<T> {

  public HaxeArrayAdapter(Array<T> haxeArray) {
    super(Arrays.asList(haxeArray.__a).subList(0, haxeArray.length));
  }
}
