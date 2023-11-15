package useless.xiehunger.mixin.core;

import com.mojang.nbt.CompoundTag;
import net.minecraft.client.Minecraft;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.xiehunger.FoodLists;
import useless.xiehunger.IHunger;
import useless.xiehunger.XieHunger;

@Mixin(value = EntityPlayer.class, remap = false)
public class EntityPlayerMixin extends EntityLiving implements IHunger {
	public EntityPlayerMixin(World world) {
		super(world);
	}
	@Unique
	private int hungerTicks = 0;
	@Unique
	private int thirstTicks = 0;
	@Unique
	private int fatigueOverflow = 0;
	@Unique
	private int fatigueScaled = 0;
	@Unique
	private boolean dying = false;
	@Unique
	private int tickCounter = 0;
	@Unique
	private long timeOfLastTick = 0L;
	@Unique
	public int hungerState = 0;
	@Unique
	public int thirstState = 0;
	@Unique
	public int fatigueState = 0;
	@Unique
	public int hunger = 0; // Save
	@Unique
	public int thirst = 0; // Save
	@Unique
	public int fatigue = 0; // Save
	@Unique
	private EntityPlayer thisAs = (EntityPlayer)(Object)this;
	@Inject(method = "tick()V", at = @At("HEAD"))
	private void tick(CallbackInfo ci){
		if (++tickCounter >= XieHunger.hungerClockTickRate) {
			tickCounter = 0;
			hungerClockTick();
		}
	}
	@Unique
	public void hungerClockTick() {
		if (timeOfLastTick == 0L) {
			timeOfLastTick = world.getWorldTime();
		}
		XieHunger.LOGGER.info("Hunger" + hunger);
		XieHunger.LOGGER.info("Thirst" + thirst);
		XieHunger.LOGGER.info("Fatigue" + fatigue);

		long scalingFactor = 1L;
		long deltaTime = world.getWorldTime() - timeOfLastTick;
		if (deltaTime > (long)XieHunger.hungerClockTickRate) {
			scalingFactor = deltaTime / (long)XieHunger.hungerClockTickRate;
		}

		timeOfLastTick = world.getWorldTime();
		if (XieHunger.passiveRegen && inTheGreen()) {
			this.heal((int) scalingFactor);
		}

		if (XieHunger.hungerEnabled) {
			this.doHunger(scalingFactor);
		}

		if (XieHunger.thirstEnabled) {
			this.doThirst(scalingFactor);
		}

		if (XieHunger.fatigueEnabled) {
			this.doFatigue(scalingFactor, deltaTime);
		}

//		if (gryllsEnabled) {
//			doGrylls(game);
//		}

		stateUpdate();
	}
	@Unique
	private void doHunger(long scale) {
		hungerTicks = (int)((long)hungerTicks + scale);
		if (hungerTicks >= XieHunger.hungerRate) {
			int amt = hungerTicks / XieHunger.hungerRate;
			hungerTicks = 0;
			hunger += amt;
			if (hunger >= XieHunger.hungerMax) {
				dying = true;
				hurt(null, hunger - XieHunger.hungerMax, DamageType.GENERIC);
				hunger = XieHunger.hungerMax;
			}
		}

	}

	@Unique
	private void doThirst(long scale) {
		thirstTicks = (int)((long)thirstTicks + scale);
		if (thirstTicks >= XieHunger.thirstRate) {
			int amt = thirstTicks / XieHunger.thirstRate;
			thirstTicks = 0;
			thirst += amt;
			if (thirst >= XieHunger.thirstMax) {
				dying = true;
				hurt(null, thirst - XieHunger.thirstMax, DamageType.GENERIC);
				thirst = XieHunger.thirstMax;
			}
		}

		if (XieHunger.waterDrinkable && thisAs.isInWater() && thisAs.isSneaking() && moveStrafing == 0.0F && moveForward == 0.0F) {
			thirst -= XieHunger.hungerClockTickRate * XieHunger.thirstMax / 100;
		}

	}

	@Unique
	private void doFatigue(long scale, long deltaTime) {
		fatigue = (int)((long)fatigue + (long)XieHunger.fatigueRate[XieHunger.CONSTANT] * scale);
		boolean resting = true;
		if (deltaTime >= 1200L) { // Full sleep
			fatigue = (int)((long)fatigue + (long)XieHunger.fatigueRate[XieHunger.SLEEPING] * scale);
			if (fatigue < 0) {
				fatigue = 0;
			}

		} else {
			if (moveStrafing != 0.0F || moveForward != 0.0F) {
				if (thisAs.isInWater()) {
					fatigue = (int)((long)fatigue + (long)XieHunger.fatigueRate[XieHunger.SWIMMING] * scale);
				} else {
					fatigue = (int)((long)fatigue + (long)XieHunger.fatigueRate[XieHunger.WALKING] * scale);
				}

				resting = false;
			}

			if (isJumping) {
				resting = false;
				fatigue = (int)((long)fatigue + (long)XieHunger.fatigueRate[XieHunger.JUMPING] * scale);
			}

			if (thisAs.isSwinging) {
				resting = false;
				fatigue = (int)((long)fatigue + (long)XieHunger.fatigueRate[XieHunger.SWINGING] * scale);
			}

			if (thisAs.isSneaking()) {
				resting = false;
				fatigue = (int)((long)fatigue + (long)XieHunger.fatigueRate[XieHunger.SNEAKING] * scale);
			}

			if (resting) {
				fatigue = (int)((long)fatigue + (long)XieHunger.fatigueRate[XieHunger.RESTING] * scale);
			}
		}

		if (fatigue >= XieHunger.fatigueMax) {
			dying = true;
			this.fatigueOverflow += fatigue - XieHunger.fatigueMax;
			int amt = this.fatigueOverflow / XieHunger.fatigueHeartScaleFactor;
			if (amt >= 1) {
				if (hurt(null, amt, DamageType.GENERIC)) {
					fatigue = XieHunger.fatigueMax;
				}

				this.fatigueOverflow = 0;
			}
		}

	}
	@Unique
	public boolean inTheGreen() {
		return hungerState <= 1 && thirstState <= 1 && fatigueState <= 1;
	}
	@Unique
	private void stateUpdate() {
		if (hunger < 0) {
			hunger = 0;
		}

		if (hunger > XieHunger.hungerMax) {
			hunger = XieHunger.hungerMax;
		}

		if (thirst < 0) {
			thirst = 0;
		}

		if (thirst > XieHunger.thirstMax) {
			thirst = XieHunger.thirstMax;
		}

		if (fatigue < 0) {
			fatigue = 0;
		}

		if (fatigue > XieHunger.fatigueMax) {
			fatigue = XieHunger.fatigueMax;
		}

		dying = hunger >= XieHunger.hungerMax || thirst >= XieHunger.thirstMax || fatigue >= XieHunger.fatigueMax;

		hungerState = (int)Math.floor((double) hunger / XieHunger.hungerStateFactor);
		thirstState = (int)Math.floor((double) thirst / XieHunger.thirstStateFactor);
		fatigueScaled = fatigue * XieHunger.fatigueScaledMax / XieHunger.fatigueMax;
		fatigueState = (int)Math.floor((double)fatigue / XieHunger.fatigueStateFactor);
	}

	public void feed(int amount) {
		hunger -= amount;
		stateUpdate();
	}

	@Unique
	public int getHungerState() {
		return hungerState;
	}

	@Unique
	public int getThirstState() {
		return thirstState;
	}

	@Unique
	public int getFatigueState() {
		return fatigueState;
	}

	@Override
	public int getFatigueScaled() {
		return fatigueScaled;
	}

	@Override
	public int getHunger() {
		return hunger;
	}

	@Override
	public int getThirst() {
		return thirst;
	}

	@Override
	public int getFatigue() {
		return fatigue;
	}

	@Override
	public boolean isDying() {
		return dying;
	}

	public void feed(int amount, ItemFood food) {
		int hungerRelief = amount;
		int thirstRelief = 0;
		if (FoodLists.juicyList.contains(food)) {
			thirstRelief = amount / 2;
		} else if (FoodLists.juiceList.contains(food)) {
			hungerRelief = amount / 2;
			thirstRelief = amount;
		} else if (FoodLists.waterList.contains(food)) {
			hungerRelief = 0;
			thirstRelief = amount;
		}

		hunger -= hungerRelief;
		thirst -= thirstRelief;
		stateUpdate();
	}
	@Inject(method = "onDeath(Lnet/minecraft/core/entity/Entity;)V", at = @At("TAIL"))
	private void resetHungerOnDeath(Entity entity, CallbackInfo ci){
		resetState();
	}


	@Unique
	private void resetState() {
		hunger = 0;
		hungerState = 0;
		thirst = 0;
		thirstState = 0;
		fatigue = 0;
		fatigueState = 0;
		hungerTicks = 0;
		thirstTicks = 0;
		tickCounter = 0;
		timeOfLastTick = 0L;
	}
	@Inject(method = "addAdditionalSaveData(Lcom/mojang/nbt/CompoundTag;)V", at = @At("TAIL"))
	public void addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		tag.putInt("Hunger", this.hunger);
		tag.putInt("Thirst", this.thirst);
		tag.putInt("Fatigue", this.fatigue);
		tag.putInt("TickCounter", this.tickCounter);
		tag.putLong("LastTick", this.timeOfLastTick);
	}

	@Inject(method = "readAdditionalSaveData(Lcom/mojang/nbt/CompoundTag;)V", at = @At("TAIL"))
	public void readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
		this.hunger = tag.getInteger("Hunger");
		this.thirst = tag.getInteger("Thirst");
		this.fatigue = tag.getInteger("Fatigue");
		this.tickCounter = tag.getInteger("TickCounter");
		this.timeOfLastTick = tag.getLong("LastTick");
	}
}
