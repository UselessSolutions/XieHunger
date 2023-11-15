package useless.xiehunger.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.xiehunger.XieHunger;

@Mixin(value = Minecraft.class, remap = false)
public class MinecraftMixin {
	@Inject(method = "runTick()V", at = @At("HEAD"))
	private void runTick(CallbackInfo ci){
		XieHunger.instance.gameTick((Minecraft)(Object)this);
	}
}
