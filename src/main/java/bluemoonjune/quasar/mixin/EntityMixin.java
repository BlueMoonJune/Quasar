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

    @Unique
    public Quaternionf quasar$orientation = new Quaternionf();

    @WrapMethod(method = "hasNoGravity")
    private boolean modifyGravity(Operation<Boolean> original) {
        if (Utils.INSTANCE.hasGravity((Entity)(Object) this)) {
            return true;
        }
        return original.call();
    }

    @WrapMethod(method = "getBoundingBox")
    private Box modifyBoundingBox(Operation<Box> original) {
        if (Utils.INSTANCE.hasGravity((Entity)(Object) this)) {
            return Quasar.INSTANCE.hitboxTest(original.call());
        }
        return original.call();
    }

    @WrapMethod(method = "getEyePos")
    private Vec3d modifyEyePos(Operation<Vec3d> original) {
        var v = original.call();
        var c = ((Entity)(Object) this).getBoundingBox().getCenter();
        return new Vec3d(v.subtract(c).toVector3f().rotate(quasar$orientation)).add(c);
    }

    @Override
    public @NotNull Quaternionf getOrientation() {
        return this.quasar$orientation;
    }

    @Override
    public void setOrientation(@NotNull Quaternionf quaternionf) {
        this.quasar$orientation = quaternionf;
    }
}
