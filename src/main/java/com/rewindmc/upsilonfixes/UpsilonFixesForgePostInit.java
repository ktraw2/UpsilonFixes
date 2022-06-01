package com.rewindmc.upsilonfixes;

import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.FMLRelauncher;
import ic2.core.IC2;
import ic2.core.util.KeyboardClient;

public class UpsilonFixesForgePostInit implements Runnable {

	@Override
	public void run() {
		try {
			Class.forName("com.rewindmc.upsilonfixes.UpsilonFixesForgePostInit$InRelauncher", true, FMLRelauncher.instance().classLoader).getMethod("run").invoke(null);
		} catch (Exception e) {
			UpsilonFixesPremain.log.warn("Failed to invoke Forge postinit handler inside relaunch class loader", e);
		}
	}
	
	public static class InRelauncher {
		
		public static void run() {
			if (IC2.keyboard.getClass().getSimpleName().equals("KeyboardClient")) {
				Client.modifyKeyNames();
			}
			if (UpsilonFixesConfig.enableRejuvenatingEffectLocalization) {
				LanguageRegistry.instance().addStringLocalization("thaumicbees.effectNodeCharge", "Aura Charge");
			}
		}
		
		public static class Client {
			
			public static void modifyKeyNames() {
				((KeyboardClient)IC2.keyboard).boostKey.keyDescription = "Sprint/Boost Key";
			}
			
		}
		
	}
	
}
