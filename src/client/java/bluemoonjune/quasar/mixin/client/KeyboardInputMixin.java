package bluemoonjune.quasar.mixin.client;

import bluemoonjune.quasar.Quasar;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {

    @Inject(method = "getMovementMultiplier", at = @At("HEAD"), cancellable = true)
    private static void disallowMovement(CallbackInfoReturnable<Float> ci) {
        if (MinecraftClient.getInstance().world.getRegistryKey() == Quasar.INSTANCE.getSPACE()) {
            ci.setReturnValue(0f);
        }
    }
}
