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
		
		register("branding.GuiButtonMainMenu", UpsilonFixesConfig.enableUpsilonBranding);
		register("branding.GuiMainMenuVoxelBox", UpsilonFixesConfig.enableUpsilonBranding);
		
		register("layering.GuiMainMenuVoxelBox", UpsilonFixesConfig.enableLayeredTexturePacks);
		register("layering.GuiMainMenu", UpsilonFixesConfig.enableLayeredTexturePacks);
		register("layering.TexturePackCustom", UpsilonFixesConfig.enableLayeredTexturePacks);
		register("layering.TexturePackFolder", UpsilonFixesConfig.enableLayeredTexturePacks);
		register("layering.TexturePackImplementation", UpsilonFixesConfig.enableLayeredTexturePacks);
		register("layering.TexturePackList", UpsilonFixesConfig.enableLayeredTexturePacks);
		
		register("appeng.VersionChecker", UpsilonFixesConfig.disableVersionCheckers);
		register("cofh.VersionCheckThread", UpsilonFixesConfig.disableVersionCheckers);
		register("mffs.Versioninfo", UpsilonFixesConfig.disableVersionCheckers);
		register("neiplugins.VersionCheckThread", UpsilonFixesConfig.disableVersionCheckers);
		
		register("ic2.PlatformClient", UpsilonFixesConfig.disableDeadCosmetics);
		register("gregtech.GT_ClientAnon1", UpsilonFixesConfig.disableDeadCosmetics);
		register("gregtech.GT_Renderer", UpsilonFixesConfig.disableDeadCosmetics);
		register("stevescarts.CapeHandler", UpsilonFixesConfig.disableDeadCosmetics);
		
		register("miscperipherals.BlockTurtleTransformer", UpsilonFixesConfig.enableMiscPeripheralsAsmFix);
		register("miscperipherals.TileEntityTurtleTransformer", UpsilonFixesConfig.enableMiscPeripheralsAsmFix);

		register("ee3.ItemMiniumStone", UpsilonFixesConfig.enableEE3TransmuteRecipesFix);
		register("ee3.ItemPhilosopherStone", UpsilonFixesConfig.enableEE3TransmuteRecipesFix);

		register("vanilla.EntityLiving", UpsilonFixesConfig.enableAttackerYawSyncing);
		register("vanilla.Packet250CustomPayload", UpsilonFixesConfig.enableAttackerYawSyncing);

		register("vanilla.FontRenderer", UpsilonFixesConfig.enableFontTexturePackFix);
		register("vanilla.RenderEngine", UpsilonFixesConfig.enableFontTexturePackFix);
		
		register("voxelmenu.GuiMainMenuVoxelBox", UpsilonFixesConfig.enableNilmodsInVoxelMenu);
		register("voxelmenu.GuiModInfo", UpsilonFixesConfig.enableNilmodsInVoxelMenu);
		
		register("rp2.BlockShapedLamp", UpsilonFixesConfig.enableRp2HitboxFix);
		
		register("thaumicbees.AlleleEffectAuraNodeFlux", UpsilonFixesConfig.enableAerFromFluxBee);
		register("codechicken.ClassHeirachyManager", UpsilonFixesConfig.enableCodeChickenCoreHierarchyFix);
		register("portalgun.ThreadDownloadResources", UpsilonFixesConfig.enablePortalGunResourcesFix);
		register("vanilla.BlockFlowing", UpsilonFixesConfig.enableWhirlpoolFix);
		register("vanilla.EntityPlayerSP", UpsilonFixesConfig.enableSprintKey);
		register("xycraft.WorldPopCrystal", UpsilonFixesConfig.disableXycraftQuartzCrystalWorldgen);
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
