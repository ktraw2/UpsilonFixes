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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions("com.unascribed.upsilonfixes")
public class UpsilonFixesCorePlugin implements IFMLLoadingPlugin {
	
	@Comment("Backports the 1.5 'water source blocks fill in above water' fix.")
	private static boolean cfg_enableWhirlpoolFix = true;
	@Comment("Prevents XyCraft's quartz crystals from generating. They're a huge performance hit and nobody likes them.")
	private static boolean cfg_disableXycraftQuartzCrystalWorldgen = true;
	@Comment("Fixes sounds and music for Portal Gun by replacing the asset index.")
	private static boolean cfg_fixPortalGunResources = true;
	@Comment("Fixes transmutation crafting recipes in EE3 only working once, and being broken by NEI.")
	private static boolean cfg_fixEE3TransmuteRecipes = true;
	
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
	public String[] getLibraryRequestClass() {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		List<String> li = new ArrayList<>();
		
		add(li, "appeng.VersionChecker", cfg_disableVersionCheckers);
		add(li, "buildcraft.Version", cfg_disableVersionCheckers);
		add(li, "cofh.VersionCheckThread", cfg_disableVersionCheckers);
		add(li, "mffs.Versioninfo", cfg_disableVersionCheckers);
		add(li, "neiplugins.VersionCheckThread", cfg_disableVersionCheckers);
		
		add(li, "ic2.PlatformClient", cfg_disableDeadCosmetics);
		add(li, "gregtech.GT_ClientAnon1", cfg_disableDeadCosmetics);
		add(li, "gregtech.GT_Renderer", cfg_disableDeadCosmetics);
		add(li, "stevescarts.CapeHandler", cfg_disableDeadCosmetics);

		add(li, "ee3.ItemMagicStone", cfg_fixEE3TransmuteRecipes);
		add(li, "portalgun.ThreadDownloadResources", cfg_fixPortalGunResources);
		add(li, "vanilla.BlockFlowing", cfg_enableWhirlpoolFix);
		add(li, "xycraft.WorldPopCrystal", cfg_disableXycraftQuartzCrystalWorldgen);
		
		return li.toArray(new String[li.size()]);
	}
	
	private void add(List<String> li, String str, boolean doIt) {
		if (doIt) {
			li.add("com.unascribed.upsilonfixes."+str+"Transformer");
		}
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> map) {
		
	}
	
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
			for (Field f : UpsilonFixesCorePlugin.class.getDeclaredFields()) {
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
	}
	
}
