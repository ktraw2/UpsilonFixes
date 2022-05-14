package com.unascribed.upsilonfixes.branding;

import java.util.List;

import com.unascribed.upsilonfixes.UpsilonFixesConfig;

import net.minecraft.src.GuiMainMenu;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiMainMenu")
public class GuiMainMenuTransformer extends MiniTransformer {

	private static final String[] addSplashes = {
		"Trans rights!",
		"The work of many people!",
		
		// I'm gonna include these from the new versions just to piss off Those Kind Of People
		"Black lives matter!",
		"Be anti-racist!",
		"Learn about allyship!",
		"Speak OUT against injustice and UP for equality!",
		"Amplify and listen to BIPOC voices!",
		"Educate your friends on anti-racism!",
		"Support the BIPOC community and creators!",
		"Stand up for equality in your community!",
	};

	private static final String[] addSplashesBrand = {
		"Just like you misremember!",
		"From the future, to the past!",
		"Yesterday's tomorrow, today!",
		"Comes in a can!",
		"Rose-tinted!",
		"Smearing!",
		"Now with oceanic consistency!",
		"Not one, not two, but THREE mod loaders!",
		"Now understands Java 8!",
		"Fits on a VHS!",
		"§bCo§dlo§frm§dat§bic",
		"Vertical!",
		"You hear about video games?",
		"Powered by complementary colors!",
		"Automatically updating!",
		"Perfect is the enemy of good!",
		"Composed of blobs!",
		"Optionally difficult!",
		"Could have been interesting!",
		"Treatment for your burn!",
		"1.18?",
		"1.19?",
		"Order-dependent transparency!",
		"Now with additional biomes!",
		"Contains a variety of nuts!",
		"Sleepypack didn't go far enough",
		"This is not an Error, it's just an Information.",
		"Ain't nothin' like a funky beat!",
		"Take us back!",
		"Keep steppin' at your own pace!",

		"Try the clones!",
		"Also try Minetest!",
		"Also try Terasology!",
		"Also try Vintage Story!",
		"Also try ZZT!",
		"Also try Aloe!",

		"#minecraftfarms",
	};

	private static final String[] rmSplashes = {
		"Notch <3 ez!",
		"Made by Notch!",
		"The Work of Notch!",
		"110813!",
		"Woo, /v/!",
		"Привет Россия!",
		"Hobo humping slobo babe!",
		"Lewd with two dudes with food!",
		// "Switches and ores!" gets to stay because it's an actually funny subversion, and on its own it's thematic
	};

	private static final String[] rmSplashesBrand = {
		"Don't bother with the clones!",
		"May contain nuts!", // Forestry adds a variety of nuts
	};

	@Patch.Method("<init>()V")
	public void patchInit(PatchContext ctx) {
		ctx.search(
			ALOAD(0),
		    ALOAD(2),
		    GETSTATIC("net/minecraft/src/GuiMainMenu", "rand", "Ljava/util/Random;")
		).jumpBefore();
		
		ctx.add(
			ALOAD(0),
			ALOAD(2),
			INVOKESTATIC("com/unascribed/upsilonfixes/branding/GuiMainMenuTransformer$Hooks", "contributeSplashes", "(Lnet/minecraft/src/GuiMainMenu;Ljava/util/List;)V")
		);
	}

	@Patch.Method("initGui()V")
	public void patchInitGui(PatchContext ctx) {
		ctx.search(
			LDC("Happy birthday, ez!")
		).jumpAfter();
		ctx.add(
			POP(),
			ALOAD(0),
			GETFIELD("net/minecraft/src/GuiMainMenu", "splashText", "Ljava/lang/String;")
		);

		ctx.search(
			LDC("Happy birthday, Notch!")
		).jumpAfter();
		ctx.add(
			POP(),
			ALOAD(0),
			GETFIELD("net/minecraft/src/GuiMainMenu", "splashText", "Ljava/lang/String;")
		);
	}
	
	@Patch.Method("drawScreen(IIF)V")
	@Patch.Method.AffectsControlFlow
	public void patchDrawScreen(PatchContext ctx) {
		ctx.search(
			LDC(-20f)
		).jumpAfter();
		
		LabelNode L1 = new LabelNode();
		
		ctx.add(
			LDC("Vertical!"),
			ALOAD(0),
			GETFIELD("net/minecraft/src/GuiMainMenu", "splashText", "Ljava/lang/String;"),
			INVOKEVIRTUAL("java/lang/String", "equals", "(Ljava/lang/Object;)Z"),
			IFEQ(L1),
			POP(),
			LDC(70f),
			L1
		);
	}
	
	public static final class Hooks {
		private Hooks() {}
		
		public static void contributeSplashes(GuiMainMenu gui, List<String> splashes) {
			for (String s : addSplashes) {
				splashes.add(s);
			}
			for (String s : rmSplashes) {
				splashes.remove(s);
			}
			
			if (UpsilonFixesConfig.enableUpsilonBranding) {
				for (String s : addSplashesBrand) {
					splashes.add(s);
				}
				for (String s : rmSplashesBrand) {
					splashes.remove(s);
				}
			}
			
			if (Boolean.getBoolean("upsilonfixes.debugSplashes")) {
				for (String s : splashes) {
					System.out.println(s);
				}
			}
		}
	}

}
