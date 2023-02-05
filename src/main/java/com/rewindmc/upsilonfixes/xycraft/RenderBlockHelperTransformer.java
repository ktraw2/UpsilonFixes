package com.rewindmc.upsilonfixes.xycraft;

import org.lwjgl.opengl.GL11;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("soaryn.xycraft.world.render.RenderBlockHelper")
public class RenderBlockHelperTransformer extends UpsilonMiniTransformer {

	@Patch.Method("DrawAnimation(Lnet/minecraft/src/RenderBlocks;Lnet/minecraft/src/Block;IFFFFFF)V")
	public void patchDrawAnimation(PatchContext ctx) {
		ctx.jumpToStart();
		ctx.add(
			SIPUSH(GL11.GL_LIGHTING),
			INVOKESTATIC("org/lwjgl/opengl/GL11", "glDisable", "(I)V")
		);
		
		ctx.search(INVOKESTATIC("net/minecraft/src/RenderHelper", "enableStandardItemLighting", "()V")).erase();
		
		ctx.jumpToLastReturn();
		ctx.add(
			SIPUSH(GL11.GL_LIGHTING),
			INVOKESTATIC("org/lwjgl/opengl/GL11", "glEnable", "(I)V")
		);
	}
	
	@Patch.Method("setColorWithBrightness(Lcodechicken/xycraftcopy/core/colour/Colour;I)V")
	@Patch.Method.AffectsControlFlow
	public void patchSetColorWithBrightness(PatchContext ctx) {
		ctx.search(SIPUSH(2896)).jumpBefore();
		ctx.add(RETURN());
	}
	
}
