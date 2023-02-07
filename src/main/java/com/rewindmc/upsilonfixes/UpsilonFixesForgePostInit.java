package com.rewindmc.upsilonfixes;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.rewindmc.upsilonfixes.xycraft.FabricatorCraftingRecipeProvider;

import cpw.mods.fml.common.registry.LanguageRegistry;
import gregtechmod.common.items.GT_Jackhammer_Item;
import gregtechmod.common.items.GT_MetaItem_Cell;
import gregtechmod.common.items.GT_MetaItem_Gas;
import gregtechmod.common.items.GT_MetaItem_Liquid;
import ic2.core.IC2;
import ic2.core.Ic2Items;
import ic2.core.util.KeyboardClient;
import info.jbcs.minecraft.chisel.Chisel;
import logisticspipes.proxy.SimpleServiceLocator;
import net.minecraft.network.packet.Packet3Chat;

public class UpsilonFixesForgePostInit implements Runnable {

	@Override
	public void run() {
		try {
			Class<?> relauncherClazz = Class.forName("cpw.mods.fml.relauncher.FMLRelauncher");
			Method instance = relauncherClazz.getDeclaredMethod("instance");
			instance.setAccessible(true);
			Object relauncher = instance.invoke(null);
			Field classLoader = relauncherClazz.getDeclaredField("classLoader");
			classLoader.setAccessible(true);
			ClassLoader loader = (ClassLoader)classLoader.get(relauncher);
			Class.forName("com.rewindmc.upsilonfixes.UpsilonFixesForgePostInit$InRelauncher", true, loader).getMethod("run").invoke(null);
			if (Boolean.getBoolean("upsilonfixes.loadAllTargets")) {
				for (String s : UpsilonFixesPremain.transformerTargets) {
					try {
						Class.forName(s.replace('/', '.'), true, loader);
						UpsilonFixesPremain.log.info("Loaded {} successfully!", s);
					} catch (Throwable e) {
						UpsilonFixesPremain.log.warn("Error while loading {}", s, e);
					}
				}
			}
		} catch (Exception e) {
			UpsilonFixesPremain.log.warn("Failed to invoke Forge postinit handler inside relaunch class loader", e);
		}
	}
	
	public static class InRelauncher {
		
		public static void run() {
			if (IC2.keyboard.getClass().getSimpleName().equals("KeyboardClient") && UpsilonFixesConfig.sprintKey) {
				Client.modifyKeyNames();
			}
			if (UpsilonFixesConfig.localizeRejuvenatingEffect) {
				LanguageRegistry.instance().addStringLocalization("thaumicbees.effectNodeCharge", "Aura Charge");
			}
			if (UpsilonFixesConfig.localizeTradeOMatStock) {
				LanguageRegistry.instance().addStringLocalization("container.personalTrader.stock", "Stock");
			}
			try {
				GT_Jackhammer_Item.mineableBlocks.add(Chisel.blockLimestone);
				GT_Jackhammer_Item.mineableBlocks.add(Chisel.blockMarble);
			} catch (Throwable t) {}
			if (UpsilonFixesConfig.fixGregTechCellRemainder) {
				try {
					GT_MetaItem_Gas.instance.setContainerItem(Ic2Items.cell.getItem());
					GT_MetaItem_Liquid.instance.setContainerItem(Ic2Items.cell.getItem());
					GT_MetaItem_Cell.instance.setContainerItem(Ic2Items.cell.getItem());
				} catch (Throwable t) {}
			}
			if (UpsilonFixesConfig.increaseChatLimit) {
				Packet3Chat.maxChatLength = 275;
			}
			if (UpsilonFixesConfig.logisticsPipesFabricatorImport) {
				try {
					SimpleServiceLocator.addCraftingRecipeProvider(new FabricatorCraftingRecipeProvider());
				} catch (Throwable t) {}
			}
		}
		
		public static class Client {
			
			public static void modifyKeyNames() {
				((KeyboardClient)IC2.keyboard).boostKey.keyDescription = "Sprint/Boost Key";
			}
			
		}
		
	}
	
}
