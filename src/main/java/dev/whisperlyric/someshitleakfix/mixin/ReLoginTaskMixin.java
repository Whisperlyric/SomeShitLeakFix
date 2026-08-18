package dev.whisperlyric.someshitleakfix.mixin;

import boat.carpetorgaddition.periodic.task.schedule.ReLoginTask;
import carpet.patches.EntityPlayerMPFake;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ReLoginTask.class)
public abstract class ReLoginTaskMixin {

    @Redirect(
            method = "lambda$logoutPlayer$2",
            at = @At(value = "INVOKE", target = "Lcarpet/patches/EntityPlayerMPFake;isRemoved()Z")
    )
    private static boolean someshitleakfix$forceDisconnect(EntityPlayerMPFake fakePlayer) {
        return false;
    }
}
