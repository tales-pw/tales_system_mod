package pw.tales.cofdsystem.mod.server.modules.operators.commands;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import jdk.nashorn.api.scripting.NashornScriptEngineFactory;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.io.output.StringBuilderWriter;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;


/**
 * Command that allows someone with special permission to directly access system objects.
 */
@Singleton
public class SystemCommand extends CommandBase {

  private static final String NAME = "s";
  private static final NashornScriptEngineFactory factory = new NashornScriptEngineFactory();
  private static final ITextComponent CONSOLE_PREFIX;

  static {
    CONSOLE_PREFIX = new TextComponentString("[System]").appendText(" ");
    CONSOLE_PREFIX.getStyle().setColor(TextFormatting.GOLD);
  }

  //this one simply forbids use of any java classes, including reflection
  private final ScriptEngine engine = factory.getScriptEngine(
      new String[]{"--no-java"},
      null,
      string -> false
  );
  private final OperatorsModule operatorsModule;

  @Inject
  public SystemCommand(CofDSystem system, OperatorsModule operatorsModule) {
    this.operatorsModule = operatorsModule;
    this.engine.getContext().setAttribute("system", system, ScriptContext.ENGINE_SCOPE);
    this.engine.getContext().setAttribute("quit", null, ScriptContext.ENGINE_SCOPE);
    this.engine.getContext().setAttribute("readFully", null, ScriptContext.ENGINE_SCOPE);
    this.engine.getContext().setAttribute("readLine", null, ScriptContext.ENGINE_SCOPE);
    this.engine.getContext().setAttribute("load", null, ScriptContext.ENGINE_SCOPE);
    this.engine.getContext().setAttribute("loadWithNewGlobal", null, ScriptContext.ENGINE_SCOPE);
    this.engine.getContext().setAttribute("Object", null, ScriptContext.ENGINE_SCOPE);
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    return this.operatorsModule.isConsoleOperator(sender);
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "command.system_console.usage";
  }

  @Override
  public void execute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {

    // Set up console response forwarding.
    engine.getContext().setWriter(new StringBuilderWriter() {
      @Override
      public void flush() {
        String o = this.getBuilder().toString();
        if (o.endsWith("\n")) {
          o = o.substring(0, o.length() - 1);
        }
        SystemCommand.this.onJSResponse(sender, o);
      }
    });

    try {
      Object result = engine.eval(String.join(" ", args));
      this.onJSResponse(sender, String.valueOf(result));
    } catch (ScriptException e) {
      throw new CommandException(String.valueOf(e));
    }
  }

  /**
   * Forwards sent message to chat.
   *
   * @param recipient Recipient.
   * @param value     Message.
   */
  public void onJSResponse(ICommandSender recipient, String value) {
    TextComponentString text = new TextComponentString((value));
    text.getStyle().setColor(TextFormatting.WHITE);
    recipient.sendMessage(CONSOLE_PREFIX.createCopy().appendSibling(text));
  }
}
