package pw.tales.cofdsystem.mod.server.modules.attack.network.handlers;

import com.google.inject.Inject;
import java.util.Arrays;
import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import pw.tales.cofdsystem.mod.common.modules.attack.network.mesages.AttackMessage;
import pw.tales.cofdsystem.mod.common.network.TalesMessageHandler;
import pw.tales.cofdsystem.mod.server.modules.attack.AttackModule;

public class AttackMessageHandler extends TalesMessageHandler<AttackMessage> {

  @SuppressWarnings({"java:S1444", "java:S1104"})
  @Inject
  public static AttackModule attackModule;

  @Override
  public void process(ServerPlayerEntity player, AttackMessage message) {
    Optional<Entity> target = Arrays.stream(message.getEntities()).findFirst();
    target.ifPresent(entity -> attackModule.attack(player, entity));
  }
}
