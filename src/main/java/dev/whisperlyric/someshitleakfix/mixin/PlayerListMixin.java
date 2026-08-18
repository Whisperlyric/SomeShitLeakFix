package dev.whisperlyric.someshitleakfix.mixin;

import carpet.patches.EntityPlayerMPFake;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Inject(method = "save", at = @At("HEAD"), cancellable = true)
    private void someshitleakfix$skipSavingRemovedFakePlayer(ServerPlayer player, CallbackInfo ci) {
        if (player instanceof EntityPlayerMPFake && player.isRemoved()) {
            ci.cancel();
        }
    }
}
