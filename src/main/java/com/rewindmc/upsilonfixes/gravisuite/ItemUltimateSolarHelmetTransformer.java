package com.rewindmc.upsilonfixes.gravisuite;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.item.ElectricItem;
import ic2.core.item.ItemTinCan;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("gravisuite.ItemUltimateSolarHelmet")
@ConfigOptions("quantumSolarHelmCans")
public class ItemUltimateSolarHelmetTransformer extends UpsilonMiniTransformer {

	@Patch.Method("onTick(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)Z")
	public void patchOnTick(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.add(
				ALOAD(0),
				ALOAD(1),
				INVOKESTATIC(hooks(), "feedPlayer", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemStack;)V")
		);
	}

	public static class Hooks {

		public static void feedPlayer(EntityPlayer player, ItemStack itemStack) {
			if (ElectricItem.canUse(itemStack, 1000) && player.getFoodStats().needFood()) {
				int slot = -1;

				for(int i = 0; i < player.inventory.mainInventory.length; ++i) {
					if (player.inventory.mainInventory[i] != null && player.inventory.mainInventory[i].itemID == Ic2Items.filledTinCan.itemID) {
						slot = i;
						break;
					}
				}

				if (slot > -1) {
					ItemTinCan can = (ItemTinCan)player.inventory.mainInventory[slot].getItem();
					player.getFoodStats().addStats(can.getHealAmount(), can.getSaturationModifier());
					can.c(player.inventory.mainInventory[slot], player.worldObj, player);
					can.onEaten(player);
					if (--player.inventory.mainInventory[slot].count <= 0) {
						player.inventory.mainInventory[slot] = null;
					}

					ElectricItem.use(itemStack, 1000, null);
				}
			} else if (player.getFoodStats().getFoodLevel() <= 0) {
				IC2.achievements.issueAchievement(player, "starveWithQHelmet");
			}
		}
		
	}
	
}
