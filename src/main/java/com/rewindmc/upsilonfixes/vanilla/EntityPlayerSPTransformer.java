package com.rewindmc.upsilonfixes.vanilla;

import ic2.core.IC2;
import net.minecraft.src.EntityPlayerSP;
import nilloader.api.lib.asm.tree.LabelNode;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.EntityPlayerSP")
public class EntityPlayerSPTransformer extends UpsilonMiniTransformer {

	@Patch.Method("onLivingUpdate()V")
	public void patchOnLivingUpdate(PatchContext ctx) {
		if (UpsilonFixesConfig.sprintKey) {
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
				INVOKESTATIC(hooks(), "onLivingUpdate", "(Lnet/minecraft/src/EntityPlayerSP;)V")
			);
		}
		
		if (UpsilonFixesConfig.guisInPortals) {
			ctx.jumpToStart();
			
			ctx.search(
				ALOAD(0),
				GETFIELD("net/minecraft/src/EntityPlayerSP", "mc", "Lnet/minecraft/client/Minecraft;"),
				ACONST_NULL(),
				INVOKEVIRTUAL("net/minecraft/client/Minecraft", "displayGuiScreen", "(Lnet/minecraft/src/GuiScreen;)V")
			).erase();
		}
	}
	
	
	public static class Hooks {
		
		public static void onLivingUpdate(EntityPlayerSP player) {
			if (IC2.keyboard.isBoostKeyDown(player)) {
				player.setSprinting(true);
			}
		}
		
	}
	
}
