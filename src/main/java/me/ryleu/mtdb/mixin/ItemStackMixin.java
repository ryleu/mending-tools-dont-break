package me.ryleu.mtdb.mixin;

import me.ryleu.mtdb.MTDB;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow public abstract boolean isEmpty();

    @Shadow public abstract NbtList getEnchantments();

    @Shadow public abstract int getMaxDamage();

    @Shadow public abstract int getDamage();

    @Inject(at = @At("HEAD"), cancellable = true, method = "use(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/TypedActionResult;")
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (cancelMissingMending(cir)) {
            cir.setReturnValue(TypedActionResult.fail(ItemStack.EMPTY));
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "useOnEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;")
    public void useOnEntity(PlayerEntity user, LivingEntity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (cancelMissingMending(cir)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    @Inject(at = @At("HEAD"), cancellable = true, method = "useOnBlock(Lnet/minecraft/item/ItemUsageContext;)Lnet/minecraft/util/ActionResult;")
    public void useOnBlock(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (cancelMissingMending(cir)) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

    private boolean cancelMissingMending(CallbackInfoReturnable cir) {
        if (hasMendingHack() && !isEmpty() && MTDB.isDamagedEnough(getMaxDamage(), getDamage())) {
            cir.cancel();
            return true;
        }

        return false;
    }

    private boolean hasMendingHack() {
        for (NbtElement enchantment : this.getEnchantments()) {
            if (enchantment.asString().contains("minecraft:mending")) {
                return true;
            }
        }

        return false;
    }
}
