package pw.tales.cofdsystem.mod.server.modules.equipment.command;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import haxe.root.Array;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.server.permission.PermissionAPI;
import pw.tales.cofdsystem.CofDSystem;
import pw.tales.cofdsystem.armor.prefabs.ArmorPrefab;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.server.modules.go_relation_item.GOItemRelation;
import pw.tales.cofdsystem.mod.server.modules.go_source_local.LocalGOModule;
import pw.tales.cofdsystem.mod.server.modules.operators.OperatorsModule;

@Singleton
public class BindArmorCommand extends BindCommand {

  private static final String NAME = "s.go.item.armor.bind";
  private static final String SHORT_NAME = "s.armor.bind";
  private final CofDSystem system;
  private final GOItemRelation goItemRelation;
  private final LocalGOModule localGOModule;

  @Inject
  public BindArmorCommand(
      CofDSystem system,
      GOItemRelation goItemRelation,
      LocalGOModule localGOModule
  ) {
    super(NAME);
    this.system = system;
    this.goItemRelation = goItemRelation;
    this.localGOModule = localGOModule;
  }

  public static String generate(String dn) {
    return String.format("/%s %s", NAME, dn);
  }

  @Override
  public List<String> getAliases() {
    return Collections.singletonList(SHORT_NAME);
  }

  @Override
  public String getUsage(ICommandSender sender) {
    return "command.wiki_armor.usage";
  }

  @Override
  public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
    try {
      return PermissionAPI.hasPermission(
          getCommandSenderAsPlayer(sender),
          OperatorsModule.SYSTEM_OPERATOR_PERMISSION
      );
    } catch (PlayerNotFoundException e) {
      return false;
    }
  }

  @Override
  public List<String> getTabCompletions(
      MinecraftServer server,
      ICommandSender sender,
      String[] args,
      @Nullable BlockPos targetPos
  ) {
    Array<ArmorPrefab> armors = system.armors.items();
    String[] dns = new String[armors.length];
    for (int i = 0; i < armors.length; i++) {
      dns[i] = this.normalize(armors.__get(i).getDN());
    }
    return getListOfStringsMatchingLastWord(args, dns);
  }

  public String normalize(String dn) {
    return dn.replace(" ", "_").toLowerCase();
  }

  @Override
  public void wrappedExecute(
      MinecraftServer server,
      ICommandSender sender,
      String[] args
  ) throws CommandException {
    if (args.length < 1) {
      throw new CommandException("commands.wiki_type_item.not_enough_arguments");
    }

    String wikitype = args[0];

    ArmorPrefab prefab = system.armors.getRecord(wikitype);

    if (prefab == null) {
      throw new CommandException("commands.wiki_type_item.unknown_wikitype", wikitype);
    }

    PlayerEntity player = (PlayerEntity) sender;
    ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);

    if (heldItem == ItemStack.EMPTY) {
      throw new CommandException("commands.wiki_type_item.no_item");
    }

    GameObject gameObject = prefab.createGameObject(system);

    this.localGOModule.save(gameObject);
    this.goItemRelation.bind(heldItem, gameObject);

    disableDefaultTooltip(heldItem);

    notifyCommandListener(
        sender,
        this,
        "commands.wiki_type_item.successful",
        wikitype
    );
  }
}