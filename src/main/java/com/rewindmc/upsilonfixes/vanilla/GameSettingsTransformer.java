package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GameSettings")
public class GameSettingsTransformer extends UpsilonMiniTransformer {

	@Patch.Method("loadOptions()V")
	public void patchLoadOptions(PatchContext ctx) {
		ctx.jumpToLastReturn();
		
		if (UpsilonFixesConfig.smearing) {
			ctx.add(
				ALOAD(0),
				ICONST_0(),
				PUTFIELD("net/minecraft/src/GameSettings", "touchscreen", "Z")
			);
		}
		
		if (UpsilonFixesConfig.removeSnooper) {
			ctx.add(
				ALOAD(0),
				ICONST_0(),
				PUTFIELD("net/minecraft/src/GameSettings", "snooperEnabled", "Z")
			);
		}
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
	
}
