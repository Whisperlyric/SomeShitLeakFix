package dev.whisperlyric.someshitleakfix.mixin;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.SavedDataStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Optional;

/**
 * Purge a disconnecting player from every loaded map's holder registries.
 * Vanilla only cleans lazily inside tickCarriedBy, which never runs again for a
 * departed player, so entries pin ServerPlayer instances forever (Paper #14088).
 * PlayerList.remove only fires on real disconnects, not dimension changes.
 */
@Mixin(PlayerList.class)
public abstract class PlayerListMapPurgeMixin {

    @Inject(method = "remove", at = @At("HEAD"))
    private void someshitleakfix$purgeMapRegistrations(ServerPlayer player, CallbackInfo ci) {
        MinecraftServer server = player.level().getServer();
        String playerName = player.getPlainTextName();
        for (ServerLevel level : server.getAllLevels()) {
            SavedDataStorage storage = level.getDataStorage();
            for (Optional<SavedData> optional : ((SavedDataStorageAccessor) (Object) storage).getCache().values()) {
                if (optional.isPresent() && optional.get() instanceof MapItemSavedData data) {
                    Map<Player, Object> carriedByPlayers = ((MapItemSavedDataAccessor) (Object) data).getCarriedByPlayers();
                    Object holder = carriedByPlayers.get(player);
                    if (holder != null) {
                        carriedByPlayers.remove(player);
                        ((MapItemSavedDataAccessor) (Object) data).getCarriedBy().remove(holder);
                        ((MapItemSavedDataAccessor) (Object) data).callRemoveDecoration(playerName);
                    }
                }
            }
        }
    }
}
