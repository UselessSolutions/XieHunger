package useless.xiehunger.mixin.core;

import net.minecraft.core.block.BlockCake;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.xiehunger.interfaces.IHunger;

@Mixin(value = BlockCake.class, remap = false)
public class BlockCakeMixin {
	@Inject(method = "eatCakeSlice(Lnet/minecraft/core/world/World;IIILnet/minecraft/core/entity/player/EntityPlayer;)V", at = @At(value = "HEAD"))
	private void healHungerWhenHungry(World world, int i, int j, int k, EntityPlayer entityplayer, CallbackInfo ci){
		IHunger hPlayer = (IHunger)entityplayer;
		if (hPlayer.getHunger() > 0){
			entityplayer.heal(3);
			hPlayer.feed(3);
			int l = world.getBlockMetadata(i, j, k) + 1;
			if (l >= 6) {
				world.setBlockWithNotify(i, j, k, 0);
			} else {
				world.setBlockMetadataWithNotify(i, j, k, l);
				world.markBlockDirty(i, j, k);
			};
		}
	}
	@Inject(method = "eatCakeSlice(Lnet/minecraft/core/world/World;IIILnet/minecraft/core/entity/player/EntityPlayer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/EntityPlayer;heal(I)V"))
	private void healHunger(World world, int i, int j, int k, EntityPlayer entityplayer, CallbackInfo ci){
		((IHunger)entityplayer).feed(3);
	}
}
