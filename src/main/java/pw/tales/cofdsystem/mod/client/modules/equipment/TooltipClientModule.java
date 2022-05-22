package pw.tales.cofdsystem.mod.client.modules.equipment;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

import com.google.inject.Singleton;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.common.modules.equipment.network.messages.TooltipRequestMessage;

@Singleton
public class TooltipClientModule implements IModule {

  private UUID currentRequestId = null;
  private ItemStack currentItemStack = null;
  private ITextComponent currentTooltip = null;

  public void handleTooltipResponse(UUID requestId, ITextComponent tooltip) {
    if (!Objects.equals(requestId, this.currentRequestId)) {
      return;
    }
    this.currentTooltip = tooltip;
  }

  @SubscribeEvent
  public void onItemTooltip(ItemTooltipEvent event) {
    PlayerEntity player = event.getPlayerEntity();

    // Skip this handler when building search tree as it is too early.
    if (player == null) {
      return;
    }

    ItemStack itemStack = event.getItemStack();

    if (this.currentItemStack != itemStack) {
      this.requestTooltip(itemStack);
      return;
    }

    if (this.currentTooltip == null) {
      // Tooltip is not loaded yet.
      return;
    }

    List<String> lines = Arrays.asList(
        this.currentTooltip.getFormattedText().split("\n")
    );

    event.getToolTip().addAll(lines);
  }

  public void requestTooltip(ItemStack itemStack) {
    this.currentRequestId = UUID.randomUUID();
    this.currentItemStack = itemStack;
    this.currentTooltip = null;

    TalesSystem.network.sendToServer(new TooltipRequestMessage(
        this.currentRequestId,
        itemStack
    ));
  }

  @Override
  public void setUp() {
    EVENT_BUS.register(this);
  }
}