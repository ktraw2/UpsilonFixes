package com.unascribed.upsilonfixes.branding;

import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiMainMenu")
public class GuiMainMenuTransformer extends MiniTransformer {

	private static final String[] newSplashes = {
		"Just like you misremember!",
		"From the future, to the past!",
		"Yesterday's tomorrow, today!",
		"Now comes in a can!",
		"Rose-tinted!",
		"Smearing!",
		"Now with oceanic consistency!",
		"Not one, not two, but THREE mod loaders!",
		"Now understands Java 8!",
		"Fits on a VHS!",
		"Trans rights!",
		
		"Try the clones!",
		"Also try Minetest!",
		"Also try Terasology!",
		"Also try Vintage Story!",
		"Also try ZZT!",
		
		"The work of many people!",
		"#minecraftfarms",
		
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

	private static final String[] badSplashes = {
		"Notch <3 ez!",
		"Made by Notch!",
		"The Work of Notch!",
		"110813!",
		"Woo, /v/!",
		"Привет Россия!",
		"Hobo humping slobo babe!",
		"Lewd with two dudes with food!",
		// "Switches and ores!" gets to stay because it's actually good
		"Don't bother with the clones!",
	};

	@Patch.Method("<init>()V")
	public void patchInit(PatchContext ctx) {
		ctx.search(
			INVOKESPECIAL("java/util/ArrayList", "<init>", "()V")
		).jumpAfter();
		for (String splash : newSplashes) {
			ctx.add(
				DUP(),
				LDC(splash),
				INVOKEINTERFACE("java/util/List", "add", "(Ljava/lang/Object;)Z"),
				POP()
			);
		}
		
		ctx.search(
				ALOAD(0),
			    ALOAD(2),
			    GETSTATIC("net/minecraft/src/GuiMainMenu", "rand", "Ljava/util/Random;")
		).jumpBefore();

		for (String splash : badSplashes) {
			ctx.add(
				ALOAD(2),
				LDC(splash),
				INVOKEINTERFACE("java/util/List", "remove", "(Ljava/lang/Object;)Z"),
				POP()
			);
		}
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

}
