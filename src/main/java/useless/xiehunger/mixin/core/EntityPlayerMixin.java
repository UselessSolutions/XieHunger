package useless.xiehunger.mixin.core;

import com.mojang.nbt.CompoundTag;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.EntityLiving;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.xiehunger.FoodLists;
import useless.xiehunger.HungerConfig;
import useless.xiehunger.PacketUpdateHunger;
import useless.xiehunger.XieHunger;
import useless.xiehunger.interfaces.IHunger;

@Mixin(value = EntityPlayer.class, remap = false)
public abstract class EntityPlayerMixin extends EntityLiving implements IHunger {
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
	private int prevHunger = 0;
	@Unique
	private int prevThirst = 0;
	@Unique
	private int prevFatigue = 0;
	@Unique
	private final EntityPlayer thisAs = (EntityPlayer)(Object)this;
	@Inject(method = "onLivingUpdate()V", at = @At("HEAD"))
	private void xie_tick(CallbackInfo ci){
		if (!world.isClientSide && ++tickCounter >= HungerConfig.hungerClockTickRate) {
			tickCounter = 0;
			xie_hungerClockTick();
		}
	}
	@Unique
	public void xie_hungerClockTick() {
		prevHunger = hunger;
		prevThirst = thirst;
		prevFatigue = fatigue;
		if (timeOfLastTick == 0L) {
			timeOfLastTick = world.getWorldTime();
		}

		if (HungerConfig.logBars) {
			XieHunger.LOGGER.info("Hunger" + hunger);
			XieHunger.LOGGER.info("Thirst" + thirst);
			XieHunger.LOGGER.info("Fatigue" + fatigue);
		}

		long scalingFactor = 1L;
		long deltaTime = world.getWorldTime() - timeOfLastTick;
		if (deltaTime > (long)HungerConfig.hungerClockTickRate) {
			scalingFactor = deltaTime / (long)HungerConfig.hungerClockTickRate;
		}

		timeOfLastTick = world.getWorldTime();
		if (HungerConfig.passiveRegen && xieHunger$inTheGreen()) {
			heal((int) scalingFactor);
		}

		if (HungerConfig.hungerEnabled) {
			this.xie_doHunger(scalingFactor);
		}

		if (HungerConfig.thirstEnabled) {
			this.xie_doThirst(scalingFactor);
		}

		if (HungerConfig.fatigueEnabled) {
			this.xie_doFatigue(scalingFactor, deltaTime);
		}

		xie_stateUpdate();
	}
	@Unique
	private void xie_doHunger(long scale) {
		hungerTicks = (int)((long)hungerTicks + scale);
		if (hungerTicks >= HungerConfig.hungerRate) {
			int amt = hungerTicks / HungerConfig.hungerRate;
			hungerTicks = 0;
			hunger += amt;
			if (hunger >= HungerConfig.hungerMax) {
				dying = true;
				hurt(null, hunger - HungerConfig.hungerMax, DamageType.GENERIC);
				hunger = HungerConfig.hungerMax;
			}
		}

	}

	@Unique
	private void xie_doThirst(long scale) {
		thirstTicks = (int)((long)thirstTicks + scale);
		if (thirstTicks >= HungerConfig.thirstRate) {
			int amt = thirstTicks / HungerConfig.thirstRate;
			thirstTicks = 0;
			thirst += amt;
			if (thirst >= HungerConfig.thirstMax) {
				dying = true;
				hurt(null, thirst - HungerConfig.thirstMax, DamageType.GENERIC);
				thirst = HungerConfig.thirstMax;
			}
		}

		if (HungerConfig.waterDrinkable && thisAs.isInWater() && thisAs.isSneaking() && moveStrafing == 0.0F && moveForward == 0.0F) {
			thirst -= HungerConfig.hungerClockTickRate * HungerConfig.thirstMax / 100;
		}

	}

	@Unique
	private void xie_doFatigue(long scale, long deltaTime) {
		fatigue = (int)((long)fatigue + (long)HungerConfig.fatigueRate[HungerConfig.CONSTANT] * scale);
		boolean resting = true;
		if (deltaTime >= 1200L) { // Full sleep
			fatigue = (int)((long)fatigue + (long)HungerConfig.fatigueRate[HungerConfig.SLEEPING] * scale);
			if (fatigue < 0) {
				fatigue = 0;
			}

		} else {
			if (moveStrafing != 0.0F || moveForward != 0.0F) {
				if (thisAs.isInWater()) {
					fatigue = (int)((long)fatigue + (long)HungerConfig.fatigueRate[HungerConfig.SWIMMING] * scale);
				} else {
					fatigue = (int)((long)fatigue + (long)HungerConfig.fatigueRate[HungerConfig.WALKING] * scale);
				}

				resting = false;
			}

			if (isJumping) {
				resting = false;
				fatigue = (int)((long)fatigue + (long)HungerConfig.fatigueRate[HungerConfig.JUMPING] * scale);
			}

			if (thisAs.isSwinging) {
				resting = false;
				fatigue = (int)((long)fatigue + (long)HungerConfig.fatigueRate[HungerConfig.SWINGING] * scale);
			}

			if (thisAs.isSneaking()) {
				resting = false;
				fatigue = (int)((long)fatigue + (long)HungerConfig.fatigueRate[HungerConfig.SNEAKING] * scale);
			}

			if (resting) {
				fatigue = (int)((long)fatigue + (long)HungerConfig.fatigueRate[HungerConfig.RESTING] * scale);
			}
		}

		if (fatigue >= HungerConfig.fatigueMax) {
			dying = true;
			this.fatigueOverflow += fatigue - HungerConfig.fatigueMax;
			int amt = this.fatigueOverflow / HungerConfig.fatigueHeartScaleFactor;
			if (amt >= 1) {
				if (hurt(null, amt, DamageType.GENERIC)) {
					fatigue = HungerConfig.fatigueMax;
				}

				this.fatigueOverflow = 0;
			}
		}

	}
	@Unique
	public boolean xieHunger$inTheGreen() {
		return hungerState <= 1 && thirstState <= 1 && fatigueState <= 1;
	}
	@Unique
	private void xie_stateUpdate() {
		if (hunger < 0) {
			hunger = 0;
		}

		if (hunger > HungerConfig.hungerMax) {
			hunger = HungerConfig.hungerMax;
		}

		if (thirst < 0) {
			thirst = 0;
		}

		if (thirst > HungerConfig.thirstMax) {
			thirst = HungerConfig.thirstMax;
		}

		if (fatigue < 0) {
			fatigue = 0;
		}

		if (fatigue > HungerConfig.fatigueMax) {
			fatigue = HungerConfig.fatigueMax;
		}

		dying = hunger >= HungerConfig.hungerMax || thirst >= HungerConfig.thirstMax || fatigue >= HungerConfig.fatigueMax;

		hungerState = (int)Math.floor((double) hunger / HungerConfig.hungerStateFactor);
		thirstState = (int)Math.floor((double) thirst / HungerConfig.thirstStateFactor);
		fatigueScaled = fatigue * HungerConfig.fatigueScaledMax / HungerConfig.fatigueMax;
		fatigueState = (int)Math.floor((double)fatigue / HungerConfig.fatigueStateFactor);

		if (thisAs instanceof EntityPlayerMP && (hunger != prevHunger || thirst != prevThirst || fatigue != prevFatigue)){
			((EntityPlayerMP)thisAs).playerNetServerHandler.sendPacket(new PacketUpdateHunger(hunger, thirst, fatigue));
		}
	}

	public void xieHunger$feed(int amount) {
		hunger -= amount;
		xie_stateUpdate();
	}

	@Unique
	public int xieHunger$getHungerState() {
		return hungerState;
	}

	@Unique
	public int xieHunger$getThirstState() {
		return thirstState;
	}

	@Unique
	public int xieHunger$getFatigueState() {
		return fatigueState;
	}

	@Override
	public int xieHunger$getFatigueScaled() {
		return fatigueScaled;
	}

	@Override
	public int xieHunger$getHunger() {
		return hunger;
	}

	@Override
	public int xieHunger$getThirst() {
		return thirst;
	}

	@Override
	public int xieHunger$getFatigue() {
		return fatigue;
	}

	@Override
	public boolean xieHunger$isDying() {
		return dying;
	}

	@Override
	public void xieHunger$updateHunger(int hunger, int thirst, int fatigue) {
		this.hunger = hunger;
		this.thirst = thirst;
		this.fatigue = fatigue;
		xie_stateUpdate();
	}

	public void xieHunger$feed(int amount, ItemFood food) {
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
		xie_stateUpdate();
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
