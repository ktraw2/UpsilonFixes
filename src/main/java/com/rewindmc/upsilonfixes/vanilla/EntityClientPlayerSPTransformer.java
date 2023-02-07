package com.rewindmc.upsilonfixes.vanilla;

import ic2.core.IC2;
import net.minecraft.client.entity.EntityClientPlayerSP;
import nilloader.api.lib.asm.tree.LabelNode;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.client.entity.EntityClientPlayerSP")
@ConfigOptions({"sprintKey", "guisInPortals"})
public class EntityClientPlayerSPTransformer extends UpsilonMiniTransformer {

	@Patch.Method("onLivingUpdate()V")
	public void patchOnLivingUpdate(PatchContext ctx) {
		if (UpsilonFixesConfig.sprintKey) {
			ctx.search(
				ALOAD(0),
				INVOKEVIRTUAL("net/minecraft/client/entity/EntityClientPlayerSP", "isSneaking", "()Z"),
				IFEQ(new LabelNode()),
				ALOAD(0),
				ICONST_0(),
				PUTFIELD("net/minecraft/client/entity/EntityClientPlayerSP", "sprintToggleTimer", "I")
			).jumpBefore();
			
			ctx.add(
				ALOAD(0),
				INVOKESTATIC(hooks(), "onLivingUpdate", "(Lnet/minecraft/client/entity/EntityClientPlayerSP;)V")
			);
		}
		
		if (UpsilonFixesConfig.guisInPortals) {
			ctx.jumpToStart();
			
			ctx.search(
				ALOAD(0),
				GETFIELD("net/minecraft/client/entity/EntityClientPlayerSP", "mc", "Lnet/minecraft/client/Minecraft;"),
				ACONST_NULL(),
				INVOKEVIRTUAL("net/minecraft/client/Minecraft", "displayScreen", "(Lnet/minecraft/client/gui/Screen;)V")
			).erase();
		}
	}
	
	
	public static class Hooks {
		
		public static void onLivingUpdate(EntityClientPlayerSP player) {
			if (IC2.keyboard.isBoostKeyDown(player)) {
				player.setSprinting(true);
			}
		}
		
	}
	
}
