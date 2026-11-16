package dev.codex.gtaliketeleport.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({Camera.class})
public interface CameraAccessor {
   @Invoker("setPosition")
   void gtalikeTeleport$setPosition(Vec3 var1);

   @Invoker("setRotation")
   void gtalikeTeleport$setRotation(float var1, float var2);

   @Invoker("createProjectionMatrixForCulling")
   Matrix4f gtalikeTeleport$createProjectionMatrixForCulling();

   @Invoker("prepareCullFrustum")
   void gtalikeTeleport$prepareCullFrustum(Matrix4fc var1, Matrix4f var2, Vec3 var3);
}
