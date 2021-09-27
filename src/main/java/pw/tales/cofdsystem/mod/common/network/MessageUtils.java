package pw.tales.cofdsystem.mod.common.network;

import io.netty.buffer.ByteBuf;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageUtils {

  private static final Charset charset = StandardCharsets.UTF_8;

  private MessageUtils() {
  }

  public static void writeIntegerMap(ByteBuf buf, Map<String, Integer> map) {
    buf.writeInt(map.size());

    for (Map.Entry<String, Integer> entry : map.entrySet()) {
      writeString(buf, entry.getKey());
      buf.writeInt(entry.getValue());
    }
  }

  public static void writeString(ByteBuf buf, String string) {
    buf.writeInt(string.getBytes(charset).length);
    buf.writeBytes(string.getBytes(charset));
  }

  public static Map<String, Integer> readIntegerMap(ByteBuf buf) {
    Map<String, Integer> map = new HashMap<>();

    int amount = buf.readInt();

    for (int i = 0; i < amount; i++) {
      String key = readString(buf);
      int value = buf.readInt();

      map.put(key, value);
    }

    return map;
  }

  public static String readString(ByteBuf buf) {
    int length = buf.readInt();

    byte[] bytes = new byte[length];
    buf.readBytes(bytes);

    return new String(bytes, charset);
  }

  public static void writeStringList(ByteBuf buf, List<String> list) {
    buf.writeInt(list.size());
    for (String item : list) {
      writeString(buf, item);
    }
  }

  public static List<String> readStringList(ByteBuf buf) {
    List<String> list = new ArrayList<>();

    int amount = buf.readInt();

    for (int i = 0; i < amount; i++) {
      list.add(readString(buf));
    }

    return list;
  }
}
