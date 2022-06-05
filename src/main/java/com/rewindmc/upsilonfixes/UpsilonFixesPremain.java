package com.rewindmc.upsilonfixes;

import nilloader.api.ClassTransformer;
import nilloader.api.NilLogger;

public class UpsilonFixesPremain implements Runnable {
	
	public static final NilLogger log = NilLogger.get("UpsilonFixes");
	
	@Override
	public void run() {
		register("entrypoints.MinecraftForge", true);
		register("entrypoints.LiteLoader", true);
		register("entrypoints.FMLPostInitializationEvent", true);
		register("branding.GuiMainMenu", true);
		
		register("forge.FMLRelauncher", true);
		
		register("branding.GuiButtonMainMenu", UpsilonFixesConfig.upsilonBranding);
		register("branding.GuiMainMenuVoxelBox", UpsilonFixesConfig.upsilonBranding);
		
		register("layering.GuiMainMenuVoxelBox", UpsilonFixesConfig.layeredTexturePacks);
		register("layering.GuiMainMenu", UpsilonFixesConfig.layeredTexturePacks);
		register("layering.TexturePackCustom", UpsilonFixesConfig.layeredTexturePacks);
		register("layering.TexturePackFolder", UpsilonFixesConfig.layeredTexturePacks);
		register("layering.TexturePackImplementation", UpsilonFixesConfig.layeredTexturePacks);
		register("layering.TexturePackList", UpsilonFixesConfig.layeredTexturePacks);
		
		register("appeng.VersionChecker", UpsilonFixesConfig.removeVersionCheckers);
		register("cofh.VersionCheckThread", UpsilonFixesConfig.removeVersionCheckers);
		register("mffs.Versioninfo", UpsilonFixesConfig.removeVersionCheckers);
		register("neiplugins.VersionCheckThread", UpsilonFixesConfig.removeVersionCheckers);
		
		register("ic2.PlatformClient", UpsilonFixesConfig.removeDeadCosmetics);
		register("gregtech.GT_ClientAnon1", UpsilonFixesConfig.removeDeadCosmetics);
		register("gregtech.GT_Renderer", UpsilonFixesConfig.removeDeadCosmetics);
		register("stevescarts.CapeHandler", UpsilonFixesConfig.removeDeadCosmetics);
		
		register("miscperipherals.BlockTurtleTransformer", UpsilonFixesConfig.fixMiscPeripheralsASM);
		register("miscperipherals.TileEntityTurtleTransformer", UpsilonFixesConfig.fixMiscPeripheralsASM);

		register("ee3.ItemMiniumStone", UpsilonFixesConfig.fixEETransmuteRecipes);
		register("ee3.ItemPhilosopherStone", UpsilonFixesConfig.fixEETransmuteRecipes);

		register("vanilla.EntityLiving", UpsilonFixesConfig.attackerYawSyncing);

		register("vanilla.FontRenderer", UpsilonFixesConfig.fixFontsInTexturePacks);
		register("vanilla.RenderEngine", UpsilonFixesConfig.fixFontsInTexturePacks);
		
		register("voxelmenu.GuiMainMenuVoxelBox", UpsilonFixesConfig.nilmodsInVoxelMenu);
		register("voxelmenu.GuiModInfo", UpsilonFixesConfig.nilmodsInVoxelMenu);
		
		register("rp2.BlockShapedLamp", UpsilonFixesConfig.fixRedPowerHitboxes);
		
		register("thaumicbees.AlleleEffectAuraNodeFlux", UpsilonFixesConfig.aerFromFluxBee);
		register("codechicken.ClassHeirachyManager", UpsilonFixesConfig.fixCodeChickenCoreHierarchyCheck);
		register("portalgun.ThreadDownloadResources", UpsilonFixesConfig.fixPortalGunResources);
		register("vanilla.BlockFlowing", UpsilonFixesConfig.whirlpoolFix);
		register("vanilla.EntityPlayerSP", UpsilonFixesConfig.sprintKey);
		register("xycraft.WorldPopCrystal", UpsilonFixesConfig.disableXycraftQuartzCrystalWorldgen);

		register("vanilla.GuiContainer", UpsilonFixesConfig.dropKeyInInventories || UpsilonFixesConfig.smearing);
		
		register("vanilla.GameSettings", UpsilonFixesConfig.smearing);
		register("vanilla.GuiOptions", UpsilonFixesConfig.smearing || UpsilonFixesConfig.removeSnooper);
		
		register("vanilla.NetClientHandler", UpsilonFixesConfig.attackerYawSyncing);
		register("vanilla.NetServerHandler", UpsilonFixesConfig.dropKeyInInventories || UpsilonFixesConfig.smearing);
	}
	
	private void register(String str, boolean doIt) {
		if (doIt) {
			try {
				ClassTransformer.register((ClassTransformer)Class.forName("com.rewindmc.upsilonfixes."+str+"Transformer").newInstance());
			} catch (Exception e) {
				log.error("Failed to register class transformer {}", str, e);
			}
		}
	}
	
}
