package useless.xiehunger;

import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;

public class ItemContainedFood extends ItemFood {
	public int container;

	public ItemContainedFood(String name, int id, int healAmount) {
		super(name,id, healAmount, false);
		this.container = Item.bowl.id;
	}

	public ItemContainedFood(String name, int id, int healAmount, int it) {
		super(name, id, healAmount, false);
		this.container = it;
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (itemstack.stackSize > 1) {
			return itemstack;
		} else {
			super.onItemRightClick(itemstack, world, entityplayer);
			return new ItemStack(this.container, 1, 0);
		}
	}
}
