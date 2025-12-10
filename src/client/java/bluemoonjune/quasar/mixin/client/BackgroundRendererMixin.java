package bluemoonjune.quasar.mixin.client;

import bluemoonjune.quasar.Quasar;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.FogShape;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {
    @Shadow
    private static float red;

    @Shadow
    private static float green;

    @Shadow
    private static float blue;

    @Inject(method = "applyFog", at = @At("HEAD"), cancellable = true)
    private static void removeFog(Camera camera, BackgroundRenderer.FogType fogType, float viewDistance, boolean thickFog, float tickDelta, CallbackInfo ci) {
        if (red == 0 && green == 0 && blue == 0) {
            RenderSystem.setShaderFogStart(100000000);
            RenderSystem.setShaderFogEnd(100000000);
            RenderSystem.setShaderFogShape(FogShape.SPHERE);
            ci.cancel();
        }
    }
}