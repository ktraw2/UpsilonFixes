package com.rewindmc.upsilonfixes.vanilla;

import ic2.core.IC2;
import net.minecraft.src.EntityPlayerSP;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.EntityPlayerSP")
public class EntityPlayerSPTransformer extends MiniTransformer {

	@Patch.Method("onLivingUpdate()V")
	public void patchOnLivingUpdate(PatchContext ctx) {
		ctx.search(
			ALOAD(0),
			INVOKEVIRTUAL("net/minecraft/src/EntityPlayerSP", "isSneaking", "()Z"),
			IFEQ(new LabelNode()),
			ALOAD(0),
			ICONST_0(),
			PUTFIELD("net/minecraft/src/EntityPlayerSP", "sprintToggleTimer", "I")
		).jumpBefore();
		
		ctx.add(
			ALOAD(0),
			INVOKESTATIC("com/rewindmc/upsilonfixes/vanilla/EntityPlayerSPTransformer$Hooks", "onLivingUpdate", "(Lnet/minecraft/src/EntityPlayerSP;)V")
		);
	}
	
	public static class Hooks {
		
		public static void onLivingUpdate(EntityPlayerSP player) {
			if (IC2.keyboard.isBoostKeyDown(player)) {
				player.setSprinting(true);
			}
		}
		
	}
	
}
