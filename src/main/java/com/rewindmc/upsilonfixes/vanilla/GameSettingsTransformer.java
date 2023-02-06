package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.EnumOptions;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.StringTranslate;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GameSettings")
public class GameSettingsTransformer extends UpsilonMiniTransformer {

	@Patch.Method("loadOptions()V")
	public void patchLoadOptions(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "postLoad", "(Lnet/minecraft/client/settings/GameSettings;)V")
		);
	}
	
	@Patch.Method("setOptionValue(Lnet/minecraft/client/settings/EnumOptions;I)V")
	public void patchSetOptionValue(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.search(PUTFIELD("net/minecraft/client/settings/GameSettings", "touchscreen", "Z")).jumpBefore();
			ctx.add(
				POP(),
				ICONST_0()
			);
		}
		if (UpsilonFixesConfig.removeSnooper) {
			ctx.jumpToStart();
			ctx.search(PUTFIELD("net/minecraft/client/settings/GameSettings",  "snooperEnabled", "Z")).jumpBefore();
			ctx.add(
				POP(),
				ICONST_0()
			);
		}
		if (UpsilonFixesConfig.modernGuiScale) {
			ctx.jumpToStart();
			ctx.search(
				PUTFIELD("net/minecraft/client/settings/GameSettings", "guiScale", "I")
			).jumpBefore();
			ctx.searchBackward(
				ICONST_3(),
				IAND()
			).erase();

			ctx.jumpToStart();
			ctx.search(
				PUTFIELD("net/minecraft/client/settings/GameSettings", "guiScale", "I")
			).jumpBefore();
			ctx.add(
				INVOKESTATIC(hooks(), "getMaxGuiScale", "()I"),
				IREM()
			);
		}
	}
	
	@Patch.Method("getKeyBinding(Lnet/minecraft/client/settings/EnumOptions;)Ljava/lang/String;")
	@Patch.Method.AffectsControlFlow
	public void patchGetKeyBinding(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode Lcontinue = new LabelNode();
		ctx.add(
			ALOAD(0),
			ALOAD(1),
			INVOKESTATIC(hooks(), "getText", "(Lnet/minecraft/client/settings/GameSettings;Lnet/minecraft/client/settings/EnumOptions;)Ljava/lang/String;"),
			DUP(),
			IFNULL(Lcontinue),
			ARETURN(),
			Lcontinue,
			POP()
		);
	}
	
	public static class Hooks {
		
		public static void postLoad(GameSettings settings) {
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
			Minecraft mc = Minecraft.getMinecraft();
			int oldScale = mc.gameSettings.guiScale;
			try {
				mc.gameSettings.guiScale = 0;
				return new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight).getScaleFactor()+1;
			} finally {
				mc.gameSettings.guiScale = oldScale;
			}
		}
		
		public static String getText(GameSettings inst, EnumOptions option) {
			if (UpsilonFixesConfig.modernGuiScale && option == EnumOptions.GUI_SCALE && inst.guiScale != 0) {
				return StringTranslate.getInstance().translateKey(option.getEnumString())+": "+inst.guiScale;
			}
			return null;
		}
		
	}
	
}
