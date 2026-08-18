package dev.whisperlyric.someshitleakfix.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {

    /**
     * Frame maps register every player in the level as an update recipient every 10 ticks.
     * Fake players have no real client and org relogin leaves old avatars unremoved,
     * so their registrations leak forever. Skip fake players.
     */
    @Redirect(
            method = "sendChanges",
            at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/world/level/saveddata/maps/MapItemSavedData;tickCarriedBy(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/decoration/ItemFrame;)V")
    )
    private void someshitleakfix$skipFakePlayerRegistration(
            MapItemSavedData data, Player player, ItemStack stack, ItemFrame frame) {
        if (!(player instanceof EntityPlayerMPFake)) {
            data.tickCarriedBy(player, stack, frame);
        }
    }
}
