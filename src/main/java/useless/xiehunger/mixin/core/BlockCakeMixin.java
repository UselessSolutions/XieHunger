package useless.xiehunger.mixin.core;

import net.minecraft.core.block.BlockCake;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.xiehunger.IHunger;

@Mixin(value = BlockCake.class, remap = false)
public class BlockCakeMixin {
	@Inject(method = "eatCakeSlice(Lnet/minecraft/core/world/World;IIILnet/minecraft/core/entity/player/EntityPlayer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/EntityPlayer;heal(I)V"))
	private void healHunger(World world, int i, int j, int k, EntityPlayer entityplayer, CallbackInfo ci){
		((IHunger)entityplayer).feed(3);
	}
}
