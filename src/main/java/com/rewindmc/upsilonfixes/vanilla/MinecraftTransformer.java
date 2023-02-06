package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.Minecraft")
public class MinecraftTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("func_90020_K()I")
	@Patch.Method.AffectsControlFlow
	public void patchGetPerformance(PatchContext ctx) {
		if (UpsilonFixesConfig.modernFpsSlider) {
			ctx.jumpToStart();
			ctx.add(
				INVOKESTATIC(hooks(), "replaceGetPerformance", "()I"),
				IRETURN()
			);
		}
	}
	
	public static class Hooks {
		
		public static int replaceGetPerformance() {
			int limit = Minecraft.getMinecraft().gameSettings.limitFramerate;
			return limit >= 250 ? 0 : limit;
		}
		
	}

}
