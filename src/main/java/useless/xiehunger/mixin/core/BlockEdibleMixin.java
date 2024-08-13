package useless.xiehunger.mixin.core;

import net.minecraft.core.block.BlockEdible;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.xiehunger.interfaces.IHunger;

@Mixin(value = BlockEdible.class, remap = false)
public abstract class BlockEdibleMixin {
	@Shadow
	public int healAmount;

	@Shadow
	public int maxBites;

	@Inject(method = "eatSlice(Lnet/minecraft/core/world/World;IIILnet/minecraft/core/entity/player/EntityPlayer;)V", at = @At(value = "HEAD"), cancellable = true)
	private void xie_healHungerWhenHungry(World world, int x, int y, int z, EntityPlayer player, CallbackInfo ci){
		IHunger hPlayer = (IHunger)player;
		if (hPlayer.xieHunger$getHunger() > 0) {
			player.heal(healAmount);
			hPlayer.xieHunger$feed(healAmount);
			int data = world.getBlockMetadata(x, y, z) + 1;
			if (data >= maxBites) {
				world.setBlockWithNotify(x, y, z, 0);
			} else {
				world.setBlockMetadataWithNotify(x, y, z, data);
				world.markBlockDirty(x, y, z);
			}
			ci.cancel();
		}
	}
	@Inject(method = "eatSlice(Lnet/minecraft/core/world/World;IIILnet/minecraft/core/entity/player/EntityPlayer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/EntityPlayer;heal(I)V"))
	private void xie_healHunger(World world, int i, int j, int k, EntityPlayer entityplayer, CallbackInfo ci){
		((IHunger)entityplayer).xieHunger$feed(healAmount);
	}
}
