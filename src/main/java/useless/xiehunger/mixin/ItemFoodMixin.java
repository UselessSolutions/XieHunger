package useless.xiehunger.mixin;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import useless.xiehunger.IHunger;

@Mixin(value = ItemFood.class, remap = false)
public class ItemFoodMixin {
	@Shadow
	protected int healAmount;

	@Inject(method = "onItemRightClick(Lnet/minecraft/core/item/ItemStack;Lnet/minecraft/core/world/World;Lnet/minecraft/core/entity/player/EntityPlayer;)Lnet/minecraft/core/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/entity/player/EntityPlayer;heal(I)V"))
	private void healHunger(ItemStack itemstack, World world, EntityPlayer entityplayer, CallbackInfoReturnable<ItemStack> cir){
		((IHunger)entityplayer).feed(healAmount, (ItemFood)(Object)this);
	}
}
