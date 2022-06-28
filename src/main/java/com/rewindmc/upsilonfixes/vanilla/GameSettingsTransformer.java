package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.src.GameSettings;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GameSettings")
public class GameSettingsTransformer extends UpsilonMiniTransformer {

	@Patch.Method("loadOptions()V")
	public void patchLoadOptions(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "postLoad", "(Lnet/minecraft/src/GameSettings;)V")
		);
	}
	
	@Patch.Method("setOptionValue(Lnet/minecraft/src/EnumOptions;I)V")
	public void patchSetOptionValue(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.search(PUTFIELD("net/minecraft/src/GameSettings",  "touchscreen", "Z")).jumpBefore();
			ctx.add(
				POP(),
				ICONST_0()
			);
		}
		if (UpsilonFixesConfig.removeSnooper) {
			ctx.jumpToStart();
			ctx.search(PUTFIELD("net/minecraft/src/GameSettings",  "snooperEnabled", "Z")).jumpBefore();
			ctx.add(
				POP(),
				ICONST_0()
			);
		}
	}
	
	public static class Hooks {
		
		public static void postLoad(GameSettings settings) {
			if (UpsilonFixesConfig.smearing) {
				settings.touchscreen = false;
			}
			if (UpsilonFixesConfig.removeSnooper) {
				settings.snooperEnabled = false;
			}
			if (UpsilonFixesConfig.fpsSlider) {
				if (settings.limitFramerate < 10) {
					settings.limitFramerate = 60;
				}
			}
		}
		
	}
	
}
