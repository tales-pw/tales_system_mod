package pw.tales.cofdsystem.mod.common.haxe_adapters;

import haxe.ds.StringMap;
import haxe.ds._StringMap.StringMapKeyIterator;
import java.util.HashMap;

public class HaxeMapAdapter<T> extends HashMap<String, T> {

  public HaxeMapAdapter(StringMap<T> haxeMap) {
    this.convert(haxeMap);
  }

  public void convert(StringMap<T> haxeMap) {
    StringMapKeyIterator<T> iterator = new StringMapKeyIterator<>(haxeMap);

    while (iterator.hasNext()) {
      String key = iterator.next();
      T value = (T) haxeMap.get(key);
      this.put(key, value);
    }
  }

}
