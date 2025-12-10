package bluemoonjune.quasar.mixin;

import bluemoonjune.quasar.Quasar;
import bluemoonjune.quasar.Utils;
import bluemoonjune.quasar.pond.OrientableEntity;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Entity.class)
public abstract class EntityMixin implements OrientableEntity {

    @Shadow
    public abstract World getWorld();

    @WrapMethod(method = "hasNoGravity")
    private boolean modifyGravity(Operation<Boolean> original) {
        if (Utils.INSTANCE.hasGravity((Entity)(Object) this)) {
            return true;
        }
        return original.call();
    }
}
