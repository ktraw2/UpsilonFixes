package com.unascribed.upsilonfixes.branding;

import java.util.Collections;

import com.unascribed.upsilonfixes.UpsilonFixesPremain;

import nilloader.api.lib.asm.tree.AbstractInsnNode;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiMainMenu")
public class GuiMainMenuTransformer extends MiniTransformer {

	private static final String[] newSplashes = {
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

	private static final String[] newSplashesBrand = {
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

		"Try the clones!",
		"Also try Minetest!",
		"Also try Terasology!",
		"Also try Vintage Story!",
		"Also try ZZT!",

		"#minecraftfarms",
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
	};

	private static final String[] badSplashesBrand = {
		"Don't bother with the clones!",
	};

	@Patch.Method("<init>()V")
	public void patchInit(PatchContext ctx) {
		ctx.search(
			INVOKESPECIAL("java/util/ArrayList", "<init>", "()V")
		).jumpAfter();
		injectCalls(ctx, DUP(), newSplashes, "add");
		if (UpsilonFixesPremain.cfg_enableUpsilonBranding)
			injectCalls(ctx, DUP(), newSplashesBrand, "add");
		
		ctx.search(
			ALOAD(0),
		    ALOAD(2),
		    GETSTATIC("net/minecraft/src/GuiMainMenu", "rand", "Ljava/util/Random;")
		).jumpBefore();

		injectCalls(ctx, ALOAD(2), badSplashes, "remove");
		if (UpsilonFixesPremain.cfg_enableUpsilonBranding)
			injectCalls(ctx, ALOAD(2), badSplashesBrand, "remove");
		
		if (Boolean.getBoolean("upsilonfixes.debugSplashes")) {
			ctx.add(
				GETSTATIC("java/lang/System", "out", "Ljava/io/PrintStream;"),
				ALOAD(2),
				INVOKEVIRTUAL("java/io/PrintStream", "println", "(Ljava/lang/Object;)V")
			);
		}
	}
	
	private void injectCalls(PatchContext ctx, AbstractInsnNode load, String[] arr, String method) {
		for (String splash : arr) {
			ctx.add(
				load.clone(Collections.emptyMap()),
				LDC(splash),
				INVOKEINTERFACE("java/util/List", method, "(Ljava/lang/Object;)Z"),
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
