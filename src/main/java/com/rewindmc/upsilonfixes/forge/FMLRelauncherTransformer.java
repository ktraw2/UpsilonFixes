package com.rewindmc.upsilonfixes.forge;

import com.rewindmc.upsilonfixes.UpsilonFixesPremain;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("cpw.mods.fml.relauncher.FMLRelauncher")
public class FMLRelauncherTransformer extends UpsilonMiniTransformer {

	@Patch.Method("setupHome(Ljava/io/File;)V")
	@Patch.Method.AffectsControlFlow
	public void patchSetupHome(PatchContext ctx) {
		ctx.jumpToStart();
		
		LabelNode Lcontinue = new LabelNode();
		
		ctx.add(
			INVOKESTATIC(hooks(), "shouldIgnore", "()Z"),
			IFZ(Lcontinue),
			RETURN(),
			Lcontinue
		);
	}
	
	public static class Hooks {

		private static boolean alreadyLaunched;
		
		public static boolean shouldIgnore() {
			if (alreadyLaunched) {
				UpsilonFixesPremain.log.warn("Ignoring second launch caused by old buggy PolyMC!");
				return true;
			}
			alreadyLaunched = true;
			return false;
		}
	
	}
	
}
