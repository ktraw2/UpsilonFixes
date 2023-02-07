package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.Option;
import net.minecraft.client.settings.OptionsManager;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.Translate;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.settings.OptionsManager")
@ConfigOptions({"smearing", "removeSnooper", "modernFpsSlider", "modernGuiScale"})
public class OptionsManagerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("load()V")
	public void patchLoad(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "postLoad", "(Lnet/minecraft/client/settings/OptionsManager;)V")
		);
	}
	
	@Patch.Method("setInt(Lnet/minecraft/client/settings/Option;I)V")
	public void patchSetInt(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.search(PUTFIELD("net/minecraft/client/settings/OptionsManager", "touchscreen", "Z")).jumpBefore();
			ctx.add(
				POP(),
				ICONST_0()
			);
		}
		if (UpsilonFixesConfig.removeSnooper) {
			ctx.jumpToStart();
			ctx.search(PUTFIELD("net/minecraft/client/settings/OptionsManager",  "snooperEnabled", "Z")).jumpBefore();
			ctx.add(
				POP(),
				ICONST_0()
			);
		}
		if (UpsilonFixesConfig.modernGuiScale) {
			ctx.jumpToStart();
			ctx.search(
				PUTFIELD("net/minecraft/client/settings/OptionsManager", "guiScale", "I")
			).jumpBefore();
			ctx.searchBackward(
				ICONST_3(),
				IAND()
			).erase();

			ctx.jumpToStart();
			ctx.search(
				PUTFIELD("net/minecraft/client/settings/OptionsManager", "guiScale", "I")
			).jumpBefore();
			ctx.add(
				INVOKESTATIC(hooks(), "getMaxGuiScale", "()I"),
				IREM()
			);
		}
	}
	
	@Patch.Method("translate(Lnet/minecraft/client/settings/Option;)Ljava/lang/String;")
	@Patch.Method.AffectsControlFlow
	public void patchTranslate(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode Lcontinue = new LabelNode();
		ctx.add(
			ALOAD(0),
			ALOAD(1),
			INVOKESTATIC(hooks(), "getText", "(Lnet/minecraft/client/settings/OptionsManager;Lnet/minecraft/client/settings/Option;)Ljava/lang/String;"),
			DUP(),
			IFNULL(Lcontinue),
			ARETURN(),
			Lcontinue,
			POP()
		);
	}
	
	public static class Hooks {
		
		public static void postLoad(OptionsManager settings) {
			if (UpsilonFixesConfig.smearing) {
				settings.touchscreen = false;
			}
			if (UpsilonFixesConfig.removeSnooper) {
				settings.snooperEnabled = false;
			}
			if (UpsilonFixesConfig.modernFpsSlider) {
				if (settings.limitFramerate < 10) {
					settings.limitFramerate = 60;
				}
			}
		}
		
		public static int getMaxGuiScale() {
			Minecraft mc = Minecraft.instance();
			int oldScale = mc.options.guiScale;
			try {
				mc.options.guiScale = 0;
				return new ScaledResolution(mc.options, mc.displayWidth, mc.displayHeight).getScaleFactor()+1;
			} finally {
				mc.options.guiScale = oldScale;
			}
		}
		
		public static String getText(OptionsManager inst, Option option) {
			if (UpsilonFixesConfig.modernGuiScale && option == Option.GUI_SCALE && inst.guiScale != 0) {
				return Translate.format(option.getTranslationKey())+": "+inst.guiScale;
			}
			return null;
		}
		
	}
	
}
