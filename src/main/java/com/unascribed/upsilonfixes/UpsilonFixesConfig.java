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

public class UpsilonFixesConfig {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private @interface Comment {
		String value();
	}
	
	@Comment("Enables the Rewind Upsilon modpack branding.")
	public static boolean enableUpsilonBranding = true;
	@Comment("Backports the 1.5 'water source blocks fill in above water' fix.")
	public static boolean enableWhirlpoolFix = true;
	@Comment("Fixes the attacker yaw not syncing from server to client, preventing the camera tilt animation from working when damaged.")
	public static boolean enableAttackerYawSyncing = true;
	@Comment("Prevents XyCraft's quartz crystals from generating. They're a huge performance hit and nobody likes them.")
	public static boolean disableXycraftQuartzCrystalWorldgen = true;
	@Comment("Fixes sounds and music for Portal Gun by replacing the asset index.")
	public static boolean enablePortalGunResourcesFix = true;
	@Comment("Fixes transmutation crafting recipes in EE3 only working once, and being broken by NEI.")
	public static boolean enableEE3TransmuteRecipesFix = true;
	@Comment("Fixes misuse of the ASM API by MiscPeripherals breaking with newer ASM libraries.")
	public static boolean enableMiscPeripheralsAsmFix = true;
	@Comment("Attaches the LiteLoader logger to the FML logger, making it look less ugly.")
	public static boolean enableLiteLoaderLogFix = true;
	@Comment("1.4.7 mods don't really get updates anymore, and many version checkers try to contact dead servers.")
	public static boolean disableVersionCheckers = true;
	@Comment("Some mods add cosmetics (usually capes) that try to contact dead servers.")
	public static boolean disableDeadCosmetics = true;
	
	static {
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
			for (Field f : UpsilonFixesConfig.class.getDeclaredFields()) {
				String k = f.getName();
				if (!props.containsKey(k)) {
					props.setProperty(k, Boolean.toString(f.getBoolean(null)));
				} else {
					f.set(null, Boolean.parseBoolean(props.getProperty(k)));
				}
				Comment comment = f.getAnnotation(Comment.class);
				if (comment != null) out.append("# "+comment.value()+"\r\n");
				out.append(k+"="+Boolean.toString(f.getBoolean(null))+"\r\n\r\n");
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
	}

}
