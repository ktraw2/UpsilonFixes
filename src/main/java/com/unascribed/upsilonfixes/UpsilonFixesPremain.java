package com.unascribed.upsilonfixes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Properties;

import nilloader.api.ClassTransformer;

public class UpsilonFixesPremain implements Runnable {
	
	@Comment("Backports the 1.5 'water source blocks fill in above water' fix.")
	private static boolean cfg_enableWhirlpoolFix = true;
	@Comment("Prevents XyCraft's quartz crystals from generating. They're a huge performance hit and nobody likes them.")
	private static boolean cfg_disableXycraftQuartzCrystalWorldgen = true;
	@Comment("Fixes sounds and music for Portal Gun by replacing the asset index.")
	private static boolean cfg_enablePortalGunResourcesFix = true;
	@Comment("Fixes transmutation crafting recipes in EE3 only working once, and being broken by NEI.")
	private static boolean cfg_enableEE3TransmuteRecipesFix = true;
	@Comment("Fixes misuse of the ASM API by MiscPeripherals breaking with newer ASM libraries.")
	private static boolean cfg_enableMiscPeripheralsAsmFix = true;
	
	@Comment("1.4.7 mods don't really get updates anymore, and many version checkers try to contact dead servers.")
	private static boolean cfg_disableVersionCheckers = true;
	@Comment("Some mods add cosmetics (usually capes) that try to contact dead servers.")
	private static boolean cfg_disableDeadCosmetics = true;
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private @interface Comment {
		String value();
	}
	
	@Override
	public void run() {
		Properties props = new Properties();
		File cfg = new File("config/upsilonfixes.ini");
		try (FileInputStream fis = new FileInputStream(cfg)) {
			props.load(fis);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to load UpsilonFixes config");
		}
		StringBuilder out = new StringBuilder("# UpsilonFixes configuration file\r\n# Any unrecognized keys or comments you add will be lost!\r\n\r\n\r\n");
		try {
			for (Field f : UpsilonFixesPremain.class.getDeclaredFields()) {
				if (f.getName().startsWith("cfg_")) {
					String k = f.getName().substring(4);
					if (!props.containsKey(k)) {
						props.setProperty(k, Boolean.toString(f.getBoolean(null)));
					} else {
						f.set(null, Boolean.parseBoolean(props.getProperty(k)));
					}
					Comment comment = f.getAnnotation(Comment.class);
					if (comment != null) out.append("# "+comment.value()+"\r\n");
					out.append(k+"="+Boolean.toString(f.getBoolean(null))+"\r\n\r\n");
				}
			}
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		}
		try (FileOutputStream fos = new FileOutputStream(cfg)) {
			cfg.getParentFile().mkdirs();
			fos.write(out.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to save UpsilonFixes config");
		}
		
		register("appeng.VersionChecker", cfg_disableVersionCheckers);
		register("buildcraft.Version", cfg_disableVersionCheckers);
		register("cofh.VersionCheckThread", cfg_disableVersionCheckers);
		register("mffs.Versioninfo", cfg_disableVersionCheckers);
		register("neiplugins.VersionCheckThread", cfg_disableVersionCheckers);
		
		register("ic2.PlatformClient", cfg_disableDeadCosmetics);
		register("gregtech.GT_ClientAnon1", cfg_disableDeadCosmetics);
		register("gregtech.GT_Renderer", cfg_disableDeadCosmetics);
		register("stevescarts.CapeHandler", cfg_disableDeadCosmetics);
		
		register("miscperipherals.BlockTurtleTransformer", cfg_enableMiscPeripheralsAsmFix);
		register("miscperipherals.TileEntityTurtleTransformer", cfg_enableMiscPeripheralsAsmFix);

		register("ee3.ItemMiniumStone", cfg_enableEE3TransmuteRecipesFix);
		register("ee3.ItemPhilosopherStone", cfg_enableEE3TransmuteRecipesFix);
		
		register("portalgun.ThreadDownloadResources", cfg_enablePortalGunResourcesFix);
		register("vanilla.BlockFlowing", cfg_enableWhirlpoolFix);
		register("xycraft.WorldPopCrystal", cfg_disableXycraftQuartzCrystalWorldgen);
	}
	
	private void register(String str, boolean doIt) {
		if (doIt) {
			try {
				ClassTransformer.register((ClassTransformer)Class.forName("com.unascribed.upsilonfixes."+str+"Transformer").newInstance());
			} catch (Exception e) {
				throw new AssertionError(e);
			}
		}
	}
	
}
