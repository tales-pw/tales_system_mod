package pw.tales.cofdsystem.mod.server.modules.go_relation_item;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import pw.tales.cofdsystem.game_object.GameObject;
import pw.tales.cofdsystem.mod.common.IModule;
import pw.tales.cofdsystem.mod.server.modules.go_relation.GORelation;
import pw.tales.cofdsystem.mod.server.modules.go_relation.exceptions.NotBoundException;
import pw.tales.cofdsystem.mod.server.modules.go_relation_item.exceptions.ItemNotBoundException;
import pw.tales.cofdsystem.mod.server.modules.go_source.IGOSource;

/**
 * Module responsible for relating ItemStack to GameObject.
 */
@Singleton
public class GOItemRelation extends GORelation<ItemStack> implements IModule {

  private static final String NBT_GAME_OBJECT_DN_KEY = "game_object_dn";

  @Inject
  public GOItemRelation(IGOSource goSource) {
    super(goSource);
  }

  @Nonnull
  @Override
  protected NotBoundException createNotBound(ItemStack holder) {
    return new ItemNotBoundException(holder);
  }

  /**
   * Extract DN for GameObject from ItemStack object.
   *
   * @param itemStack ItemStack.
   * @return DN.
   */
  @Override
  @Nullable
  public String getBind(ItemStack itemStack) {
    NBTTagCompound tag = itemStack.getTagCompound();

    if (itemStack.isEmpty()) {
      return null;
    }

    if (tag == null) {
      return null;
    }

    if (!tag.hasKey(NBT_GAME_OBJECT_DN_KEY)) {
      return null;
    }

    return tag.getString(NBT_GAME_OBJECT_DN_KEY);
  }

  /**
   * Bind ItemStack to GameObject.
   *
   * @param itemStack  ItemStack.
   * @param gameObject GameObject.
   */
  @Override
  public void bind(ItemStack itemStack, @Nullable GameObject gameObject) {
    if (gameObject == null) {
      return;
    }

    NBTTagCompound tag = itemStack.getTagCompound();

    if (tag == null) {
      tag = new NBTTagCompound();
    }

    tag.setString(NBT_GAME_OBJECT_DN_KEY, gameObject.getDN());
    itemStack.setTagCompound(tag);
  }
}
