package pw.tales.cofdsystem.mod;

import net.minecraftforge.common.config.Config;

@SuppressWarnings({"java:S1444", "java:S1118", "java:S1104", "java:S3008"})
@Config(modid = TalesSystem.MOD_ID)
public class ModConfig {

  public static String systemApiUrl = "https://api.tales.pw";
  public static String systemApiToken = "server_token";

  public static String accountsApiUrl = "http://auth.tales.pw";
  public static String accountsApiToken = "apiKey";
}
