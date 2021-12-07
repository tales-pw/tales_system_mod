package pw.tales.cofdsystem.mod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.mod.common.CommonProxy;
import pw.tales.cofdsystem.mod.logger.HaxeLogger;
import pw.tales.cofdsystem.utils.logger.LoggerManager;

@Mod(
    modid = TalesSystem.MOD_ID,
    name = TalesSystem.MOD_NAME,
    version = TalesSystem.VERSION,
    dependencies = "required-after:roleplaychat"
)
public class TalesSystem {

  public static final String MOD_ID = "@MODID@";
  public static final String MOD_NAME = "@MODID@";
  public static final String VERSION = "@VERSION@";

  public static final SimpleNetworkWrapper network = NetworkRegistry.INSTANCE
      .newSimpleChannel(MOD_ID);
  public static final Logger logger = LogManager.getLogger(MOD_ID);
  public static final HaxeLogger haxeLogger = new HaxeLogger(logger);

  @SuppressWarnings("java:S1444")
  @SidedProxy(
      serverSide = "pw.tales.cofdsystem.mod.server.ServerProxy",
      clientSide = "pw.tales.cofdsystem.mod.client.ClientProxy"
  )
  public static CommonProxy proxy;

  public TalesSystem() {
    CofDSystem.VERSION_CHECK = false;
    LoggerManager.setLogger(haxeLogger);
  }

  @Mod.EventHandler
  public void init(FMLPreInitializationEvent event) {
    proxy.setUp(event);
  }

  @Mod.EventHandler
  public void startServer(FMLServerStartingEvent event) {
    proxy.onServerStarted(event);
  }
}
