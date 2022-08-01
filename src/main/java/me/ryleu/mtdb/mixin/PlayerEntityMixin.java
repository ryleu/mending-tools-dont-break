package me.ryleu.mtdb.mixin;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {

	@Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Inject(at = @At("RETURN"), cancellable = true, method = "isBlockBreakingRestricted(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/GameMode;)Z")
	private void isBlockBreakingRestricted(World world, BlockPos pos, GameMode gameMode, CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemStack = this.getEquippedStack(EquipmentSlot.MAINHAND);
		NbtList enchantments = itemStack.getEnchantments();

		if (!itemStack.isEmpty() && itemStack.getMaxDamage() - itemStack.getDamage() <= 1) {
			for (NbtElement enchantment : enchantments) {
				if (enchantment.asString().contains("minecraft:mending")) {
					cir.setReturnValue(true);
				}
			}
		}
	}
}
