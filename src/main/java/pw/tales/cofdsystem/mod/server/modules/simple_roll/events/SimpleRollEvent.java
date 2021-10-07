package pw.tales.cofdsystem.mod.server.modules.simple_roll.events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;
import pw.tales.cofdsystem.dices.IRollRequest;
import pw.tales.cofdsystem.dices.RollResponse;

public class SimpleRollEvent extends Event {

  private final EntityPlayerMP sender;
  private final IRollRequest request;

  public SimpleRollEvent(EntityPlayerMP sender, IRollRequest request) {
    this.sender = sender;
    this.request = request;
  }

  public EntityPlayerMP getSender() {
    return sender;
  }

  public IRollRequest getRequest() {
    return request;
  }

  public static class RollResult extends SimpleRollEvent {

    private final RollResponse response;

    public RollResult(EntityPlayerMP sender, IRollRequest request, RollResponse response) {
      super(sender, request);
      this.response = response;
    }

    public RollResponse getResponse() {
      return response;
    }
  }
}
