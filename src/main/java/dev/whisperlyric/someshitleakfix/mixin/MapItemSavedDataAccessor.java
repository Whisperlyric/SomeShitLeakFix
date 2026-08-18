package dev.whisperlyric.someshitleakfix.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.Map;

/**
 * HoldingPlayer is a private nested class; use Object to avoid referencing it.
 */
@Mixin(MapItemSavedData.class)
public interface MapItemSavedDataAccessor {

    @Accessor("carriedByPlayers")
    Map<Player, Object> getCarriedByPlayers();

    @Accessor("carriedBy")
    List<Object> getCarriedBy();

    @Invoker("removeDecoration")
    void callRemoveDecoration(String name);
}
