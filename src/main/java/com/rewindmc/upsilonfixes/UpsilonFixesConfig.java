package com.rewindmc.upsilonfixes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Optional;

import nilloader.api.lib.qdcss.QDCSS;

public class UpsilonFixesConfig {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private @interface Comment {
		String value();
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	private @interface Key {
		String value();
	}
	
	@Key("upsilon-branding")
	@Comment("Enables the Rewind Upsilon modpack branding.")
	public static boolean enableUpsilonBranding = true;
	
	@Key("whirlpool-fix")
	@Comment("Backports the 1.5 'water source blocks fill in above water' fix.")
	public static boolean enableWhirlpoolFix = true;
	
	@Key("attacker-yaw-syncing")
	@Comment("Fixes the attacker yaw not syncing from server to client, preventing the camera tilt\nanimation from working when damaged.")
	public static boolean enableAttackerYawSyncing = true;
	
	@Key("disable-xycraft-quartz-crystal-worldgen")
	@Comment("Prevents XyCraft's quartz crystals from generating. They're a huge performance hit and\nnobody likes them.")
	public static boolean disableXycraftQuartzCrystalWorldgen = true;
	
	@Key("fix-portal-gun-resources")
	@Comment("Fixes sounds and music for Portal Gun by replacing the asset index.")
	public static boolean enablePortalGunResourcesFix = true;
	
	@Key("fix-ee3-transmute-recipes")
	@Comment("Fixes transmutation crafting recipes in EE3 only working once, and being broken by\nNEI.")
	public static boolean enableEE3TransmuteRecipesFix = true;
	
	@Key("fix-miscperipherals-asm")
	@Comment("Fixes misuse of the ASM API by MiscPeripherals breaking with newer ASM libraries.")
	public static boolean enableMiscPeripheralsAsmFix = true;
	
	@Key("fix-liteloader-log")
	@Comment("Attaches the LiteLoader logger to the FML logger, making it look less ugly.")
	public static boolean enableLiteLoaderLogFix = true;
	
	@Key("remove-version-checkers")
	@Comment("1.4.7 mods don't really get updates anymore, and many version checkers try to contact\ndead servers.")
	public static boolean disableVersionCheckers = true;
	
	@Key("remove-dead-cosmetics")
	@Comment("Some mods add cosmetics (usually capes) that try to contact dead servers.")
	public static boolean disableDeadCosmetics = true;
	
	static {
		File cfg = new File("config/upsilonfixes.css");
		QDCSS css = QDCSS.load("", "");
		try {
			css = QDCSS.load(cfg);
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			UpsilonFixesPremain.log.error("Failed to load upsilonfixes.css", e);
		}
		System.out.println(css);
		StringBuilder out = new StringBuilder("/*\r\n * UpsilonFixes configuration file\r\n * Any unrecognized keys or comments you add will be lost!\r\n */\r\n\r\nfeatures {\r\n");
		try {
			for (Field f : UpsilonFixesConfig.class.getDeclaredFields()) {
				String k = f.getAnnotation(Key.class).value();
				Optional<Boolean> opt = css.getBoolean("features."+k);
				boolean v;
				if (opt.isPresent()) {
					v = opt.get();
					f.set(null, v);
				} else {
					v = f.getBoolean(null);
				}
				Comment comment = f.getAnnotation(Comment.class);
				if (comment != null) out.append("\t/*\r\n\t * "+comment.value().replace("\n", "\r\n\t * ")+"\r\n\t */\r\n");
				out.append("\t"+k+": "+(v ? "on" : "off")+";\r\n\r\n");
			}
		} catch (IllegalAccessException e) {
			throw new AssertionError(e);
		}
		out.append("}\r\n");
		try (FileOutputStream fos = new FileOutputStream(cfg)) {
			cfg.getParentFile().mkdirs();
			fos.write(out.toString().getBytes("UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Failed to save UpsilonFixes config");
		}
	}

}
