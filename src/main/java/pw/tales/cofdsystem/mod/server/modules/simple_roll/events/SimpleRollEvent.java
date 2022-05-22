package pw.tales.cofdsystem.mod.server.modules.simple_roll.events;

import net.minecraft.entity.player.ServerPlayerEntity;
import import net.minecraftforge.eventbus.api.Event;
import pw.tales.cofdsystem.dices.IRollRequest;
import pw.tales.cofdsystem.dices.RollResponse;

public class SimpleRollEvent extends Event {

  private final ServerPlayerEntity sender;
  private final IRollRequest request;

  public SimpleRollEvent(ServerPlayerEntity sender, IRollRequest request) {
    this.sender = sender;
    this.request = request;
  }

  public ServerPlayerEntity getSender() {
    return sender;
  }

  public IRollRequest getRequest() {
    return request;
  }

  public static class RollResult extends SimpleRollEvent {

    private final RollResponse response;

    public RollResult(ServerPlayerEntity sender, IRollRequest request, RollResponse response) {
      super(sender, request);
      this.response = response;
    }

    public RollResponse getResponse() {
      return response;
    }
  }
}
