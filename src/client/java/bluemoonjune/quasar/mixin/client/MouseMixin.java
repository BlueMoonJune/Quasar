package bluemoonjune.quasar.mixin.client;

import bluemoonjune.quasar.QuasarClient;
import bluemoonjune.quasar.pond.OrientableEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow
    @Final
    private MinecraftClient client;

    @WrapOperation(
            method = "updateMouse",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;changeLookDirection(DD)V")
    )
    private void changeOrientation(ClientPlayerEntity instance, double i, double j, Operation<Void> original) {
        if (QuasarClient.ROLL_KEYBIND.isPressed()) {
            ((OrientableEntity) this.client.player).getOrientation().rotateX((float) j * MathHelper.PI / -180).rotateZ((float) i * MathHelper.PI / -180);
        }
        original.call(instance, i, j);
    }
}
