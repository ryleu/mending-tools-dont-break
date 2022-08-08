package me.ryleu.mtdb;

import net.fabricmc.api.ModInitializer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MTDB implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("mtdb");

	@Override
	public void onInitialize() {
		LOGGER.warn("Mending tools shouldn't break now.");
	}

	public static boolean shouldBlock(ItemStack itemStack) {
		return hasMending(itemStack) && !itemStack.isEmpty() && isDamagedEnough(itemStack.getMaxDamage(), itemStack.getDamage());
	}

	public static boolean isDamagedEnough(int maxDamage, int damage) {
		return maxDamage - damage <= Math.max(2F, maxDamage / 100F);
	}

	public static boolean hasMending(ItemStack itemStack) {
		return EnchantmentHelper.getLevel(Enchantments.MENDING, itemStack) > 0;
	}
}
