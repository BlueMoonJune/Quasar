package bluemoonjune.quasar.mixin.client;

import bluemoonjune.quasar.mixin.EntityMixin;
import bluemoonjune.quasar.pond.OrientableEntity;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import net.minecraft.client.render.Camera;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public class CameraMixin {
    @ModifyExpressionValue(
            method = "setRotation",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/joml/Quaternionf;rotationYXZ(FFF)Lorg/joml/Quaternionf;",
                    remap = false
            )
    )
    private Quaternionf quasar$setRoll(Quaternionf original) {
        var cam = (Camera)(Object)this;
        original.premul(((OrientableEntity)cam.getFocusedEntity()).getOrientation());
        return original;
    }
}
