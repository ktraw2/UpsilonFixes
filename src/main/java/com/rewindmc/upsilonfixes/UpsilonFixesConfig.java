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
import java.lang.reflect.Modifier;
import java.util.Locale;
import java.util.Optional;

import nilloader.api.NilLogger;
import nilloader.api.lib.qdcss.BadValueException;
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
	
	public enum Trilean {
		AUTO,
		OFF,
		ON,
		;
		
		public boolean resolve(boolean def) {
			if (this == AUTO) return def;
			return this == ON;
		}
	}
	
	@Key("aer-from-flux-bee")
	@Comment("Allows the Flux bee from Thaumic Bees to provide Aer-type flux. The Aer\neffect is often thought to be unused, but there are some obscure ways to\nget it in Thaumcraft, so the Flux bee should be able to provide it.")
	public static boolean aerFromFluxBee = true;
	
	@Key("attacker-yaw-syncing")
	@Comment("Fixes the attacker yaw not syncing from server to client, to restore the\nproper camera tilt animation when damaged.")
	public static boolean attackerYawSyncing = true;
	
	@Key("buff-gregtech-jackhammers")
	@Comment("Makes GregTech Jack Hammers a lot better.")
	public static boolean buffGregTechJackHammers = true;
	
	@Key("disable-codechicken-stencil")
	@Comment("Prevents CodeChickenCore from allocating a stencil buffer. May\ncause rendering issues. Fixes the game not rendering at all on macOS.")
	public static Trilean disableCodeChickenStencil = Trilean.AUTO;

	@Key("disable-xycraft-quartz-crystal-worldgen")
	@Comment("Prevents XyCraft's quartz crystals from generating. They're a huge\nperformance hit and nobody likes them.")
	public static boolean disableXycraftQuartzCrystalWorldgen = true;

	@Key("drop-key-in-inventories")
	@Comment("Allows pressing the drop key over a slot in an inventory to drop the\ncontents of the slot on the ground. 1.5 backport.")
	public static boolean dropKeyInInventories = true;
	
	@Key("fix-codechickencore-hierarchy-check")
	@Comment("Prevents CodeChickenCore from exploding when prematurely reading classes.")
	public static boolean fixCodeChickenCoreHierarchyCheck = true;
	
	@Key("fix-ee-transmute-recipes")
	@Comment("Fixes transmutation crafting recipes in EE3 only working once, and being\nbroken by NEI.")
	public static boolean fixEETransmuteRecipes = true;
	
	@Key("fix-fonts-in-texture-packs")
	@Comment("Allow texture packs to override the font.")
	public static boolean fixFontsInTexturePacks = true;
	
	@Key("fix-fz-mixer-infinite-loop")
	@Comment("Fixes an infinite loop with the Factorization mixer caused\nby a minor oversight.")
	public static boolean fixFzMixerInfiniteLoop = true;
	
	@Key("fix-gregtech-cell-remainder")
	@Comment("Configures remainder items for GregTech cells.")
	public static boolean fixGregTechCellRemainder = true;
	
	@Key("fix-liteloader-log")
	@Comment("Attaches the LiteLoader logger to the FML logger, making it look less\nugly.")
	public static boolean fixLiteLoaderLog = true;
	
	@Key("fix-miscperipherals-asm")
	@Comment("Fixes misuse of the ASM API by MiscPeripherals breaking with newer ASM\nlibraries.")
	public static boolean fixMiscPeripheralsASM = true;
	
	@Key("fix-portal-gun-resources")
	@Comment("Fixes sounds and music for Portal Gun by replacing the asset index.")
	public static boolean fixPortalGunResources = true;
	
	@Key("fix-redpower-hitboxes")
	@Comment("Fixes RedPower2 non-full-block hitboxes.")
	public static boolean fixRedPowerHitboxes = true;
	
	@Key("fix-xycraft-ore-textures")
	@Comment("Fixes XyCraft ore textures to actually use the correct parts\nof the block atlas, instead of just recoloring the white one.")
	public static boolean fixXycraftOreTextures = true;
	
	@Key("fps-slider")
	@Comment("Replace the \"Performance\" option with an FPS slider like modern\nversions.")
	public static boolean fpsSlider = true;
	
	@Key("guis-in-portals")
	@Comment("Allows opening GUIs in Nether portals. This was a fix for a\ndupe bug in Beta that is long gone.")
	public static boolean guisInPortals = true;
	
	@Key("increase-chat-limit")
	@Comment("Increase the chat length limit from 100 to 256 to match modern versions.")
	public static boolean increaseChatLimit = true;
	
	@Key("keep-tesseract-frequency-on-dismantle")
	@Comment("Stores the frequency of a dismantled Tesseract in the resulting\nitem, and restores it on place.")
	public static boolean keepTesseractFrequencyOnDismantle = true;
	
	@Key("layered-texture-packs")
	@Comment("Allows using multiple texture packs at once if they're marked as\nlayerable.")
	public static boolean layeredTexturePacks = true;
	
	@Key("localize-rejuvenating-effect")
	@Comment("Adds a missing lang entry for the Rejuvenating bee's effect.")
	public static boolean localizeRejuvenatingEffect = true;
	
	@Key("localize-tradeomat-stock")
	@Comment("Adds a missing lang entry for the Trade-O-Mat's stock.")
	public static boolean localizeTradeOMatStock = true;
	
	@Key("logistics-pipes-fabricator-import")
	@Comment("Adds XyCraft's Fabricator as an importable tile entity in crafting logistics pipes.")
	public static boolean logisticsPipesFabricatorImport = true;

	@Key("nilmods-in-voxelmenu")
	@Comment("Show nilmods in Voxel Menu's Mod Information screen.")
	public static boolean nilmodsInVoxelMenu = true;
	
	@Key("remove-dead-cosmetics")
	@Comment("Some mods add cosmetics (usually capes) that try to contact dead servers.")
	public static boolean removeDeadCosmetics = true;
	
	@Key("remove-snooper")
	@Comment("Removes the vanilla snooper. The server is gone anyway.")
	public static boolean removeSnooper = true;
	
	@Key("remove-version-checkers")
	@Comment("1.4.7 mods don't really get updates anymore, and many version checkers\ntry to contact dead servers.")
	public static boolean removeVersionCheckers = true;
	
	@Key("smearing")
	@Comment("Allows holding left/right click and dragging with an item on your cursor\nto spread it between all the passed slots. 1.5 backport.")
	public static boolean smearing = true;
	
	@Key("sprint-key")
	@Comment("Enables holding the IC2 boost key causing you to sprint.")
	public static boolean sprintKey = true;
	
	@Key("swap-red-blue")
	@Comment("Swap the red and blue channels in the main framebuffer.\nFixes the game rendering with incorrect colors on M1 Macs.")
	public static Trilean swapRedBlue = Trilean.AUTO;
	
	@Key("upsilon-branding")
	@Comment("Enables the Rewind Upsilon modpack branding.")
	public static boolean upsilonBranding = false;
	
	@Key("whirlpool-fix")
	@Comment("Backports the 1.5 'water source blocks fill in above water' fix.")
	public static boolean whirlpoolFix = true;
	
	static {
		Class<?> me = UpsilonFixesConfig.class;
		if (me.getClassLoader() != ClassLoader.getSystemClassLoader()) {
			NilLogger.get("UpsilonFixes").debug("Delegating UpsilonFixesConfig within relaunch class loader");
			try {
				Class<?> real = Class.forName(UpsilonFixesConfig.class.getName(), true, ClassLoader.getSystemClassLoader());
				for (Field f : real.getFields()) {
					if (Modifier.isStatic(f.getModifiers())) {
						Field mine = me.getField(f.getName());
						if (f.getType() == boolean.class) {
							mine.set(null, f.get(null));
						} else if (f.getType().isEnum()) {
							mine.set(null, Enum.valueOf((Class)mine.getType(), ((Enum<?>)f.get(null)).name()));
						}
					}
				}
			} catch (Throwable t) {
				throw new AssertionError(t);
			}
		} else {
			File cfg = new File("config/upsilonfixes.css");
			QDCSS css = QDCSS.load("", "");
			try {
				css = QDCSS.load(cfg);
			} catch (FileNotFoundException e) {
			} catch (IOException e) {
				UpsilonFixesPremain.log.error("Failed to load upsilonfixes.css", e);
			}
			StringBuilder out = new StringBuilder("/*\r\n * UpsilonFixes configuration file\r\n * Any unrecognized keys or comments you add will be lost!\r\n */\r\n\r\nfeatures {\r\n");
			try {
				for (Field f : UpsilonFixesConfig.class.getDeclaredFields()) {
					String k = f.getAnnotation(Key.class).value();
					String valueStr;
					if (f.getType() == boolean.class) {
						Optional<Boolean> opt;
						try {
							opt = css.getBoolean("features."+k);
						} catch (BadValueException e) {
							UpsilonFixesPremain.log.warn("Config file is malformed", e);
							opt = Optional.empty();
						}
						boolean v;
						if (opt.isPresent()) {
							v = opt.get();
							f.set(null, v);
						} else {
							v = f.getBoolean(null);
						}
						valueStr = v ? "on" : "off";
					} else if (f.getType() == Trilean.class) {
						Optional<Trilean> opt;
						try {
							opt = css.getEnum("features."+k, Trilean.class);
						} catch (BadValueException e) {
							UpsilonFixesPremain.log.warn("Config file is malformed", e);
							opt = Optional.empty();
						}
						Trilean v;
						if (opt.isPresent()) {
							v = opt.get();
							f.set(null, v);
						} else {
							v = (Trilean)f.get(null);
						}
						valueStr = v.name().toLowerCase(Locale.ROOT);
					} else {
						UpsilonFixesPremain.log.warn("Unknown field type {}", f.getType());
						continue;
					}
					Comment comment = f.getAnnotation(Comment.class);
					if (comment != null) out.append("\t/*\r\n\t * "+comment.value().replace("\n", "\r\n\t * ")+"\r\n\t */\r\n");
					out.append("\t"+k+": "+valueStr+";\r\n\r\n");
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

}
