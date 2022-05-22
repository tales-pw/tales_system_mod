package pw.tales.cofdsystem.mod.client.modules.gui_system.gui;

import com.google.inject.Inject;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientPlayerEntity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import pw.tales.cofdsystem.mod.TalesSystem;
import pw.tales.cofdsystem.mod.client.gui.project.GuiButtonImage;
import pw.tales.cofdsystem.mod.client.gui.project.GuiButtonRoll;
import pw.tales.cofdsystem.mod.client.modules.gui_windows.gui.GuiTabContainer;
import pw.tales.cofdsystem.mod.client.modules.targets.TargetsList;
import pw.tales.cofdsystem.mod.client.modules.targets.gui.GuiTargets;
import pw.tales.cofdsystem.mod.common.modules.attack.network.mesages.AttackMessage;
import pw.tales.cofdsystem.mod.common.modules.scene.command.SceneMenuCommand;
import pw.tales.cofdsystem.mod.common.network.messages.SimpleRollMessage;

public class GuiSystem extends GuiChat {

  private static final int ROLL_BUTTON_ID = 1;
  private static final int ATTACK_BUTTON_ID = 2;
  private static final int COMBAT_TRACKER_ID = 3;
  private static final ResourceLocation resourceAttack = new ResourceLocation(
      TalesSystem.MOD_ID, "textures/gui/sword-brandish.png"
  );
  private static final ResourceLocation resourceScene = new ResourceLocation(
      TalesSystem.MOD_ID, "textures/gui/checklist.png"
  );
  private static final int PADDING = 5;

  private final GuiTargets guiTargets;
  private final TargetsList targets;
  private final GuiTabContainer windowContainer;

  @Inject
  public GuiSystem(
      String defaultInputFieldText,
      GuiTargets guiTargets,
      TargetsList targets,
      GuiTabContainer windowContainer
  ) {
    super(defaultInputFieldText);
    this.guiTargets = guiTargets;
    this.targets = targets;
    this.windowContainer = windowContainer;
  }

  @Override
  public void initGui() {
    super.initGui();

    this.buttonList.add(new GuiButtonImage(ATTACK_BUTTON_ID,
        this.width - GuiButtonImage.SIZE - PADDING,
        PADDING + GuiButtonImage.SIZE + PADDING,
        resourceAttack
    ));

    this.buttonList.add(new GuiButtonImage(COMBAT_TRACKER_ID,
        this.width - GuiButtonImage.SIZE - PADDING,
        PADDING + GuiButtonImage.SIZE + PADDING + GuiButtonImage.SIZE + PADDING,
        resourceScene
    ));

    this.buttonList.add(new GuiButtonRoll(
        request -> TalesSystem.network.sendToServer(new SimpleRollMessage(request)),
        ROLL_BUTTON_ID,
        this.width - GuiButtonImage.SIZE - PADDING,
        PADDING
    ));
  }

  @Override
  public void handleMouseInput() throws IOException {
    this.windowContainer.handleMouseInput();

    if (this.windowContainer.isMouseIntercepted()) {
      return;
    }

    super.handleMouseInput();
  }

  @Override
  public void setWorldAndResolution(Minecraft mc, int width, int height) {
    super.setWorldAndResolution(mc, width, height);
    this.windowContainer.setWorldAndResolution(mc, width, height);
  }

  @Override
  protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    super.mouseClicked(mouseX, mouseY, mouseButton);
    this.guiTargets.mouseClicked(mouseX, mouseY);
  }

  @Override
  public void drawScreen(int mouseX, int mouseY, float partialTicks) {
    this.windowContainer.drawScreen(mouseX, mouseY, partialTicks);
    super.drawScreen(mouseX, mouseY, partialTicks);
  }

  @Override
  protected void actionPerformed(GuiButton button) throws IOException {
    super.actionPerformed(button);

    if (button.id == ATTACK_BUTTON_ID) {
      Entity[] entities = this.targets.getLoadedEntities();
      TalesSystem.network.sendToServer(new AttackMessage(entities));
    }

    ClientPlayerEntity player = Minecraft.getMinecraft().player;
    if (button.id == COMBAT_TRACKER_ID) {
      player.sendChatMessage(SceneMenuCommand.generate());
    }
  }
}
