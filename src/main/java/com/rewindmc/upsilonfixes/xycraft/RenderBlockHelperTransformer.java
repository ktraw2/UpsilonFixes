package com.rewindmc.upsilonfixes.xycraft;

import org.lwjgl.opengl.GL11;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("soaryn.xycraft.world.render.RenderBlockHelper")
@ConfigOptions("fixXycraftItemLighting")
public class RenderBlockHelperTransformer extends UpsilonMiniTransformer {

	@Patch.Method("DrawAnimation(Lnet/minecraft/client/renderer/RenderBlocks;Lnet/minecraft/block/Block;IFFFFFF)V")
	public void patchDrawAnimation(PatchContext ctx) {
		// This method tries to restore lighting to normal afterward by calling enableStandardItemLighting,
		// but that's not the right method for in-GUI rendering. Unfortunately, we can't just replace
		// the call -- this code is called for GUI and world rendering! However, the only reason
		// XyCraft needs to restore the lighting state is because it mutates it to something that
		// *looks* fully lit instead of just... disabling lighting.
		ctx.jumpToStart();
		ctx.add(
			SIPUSH(GL11.GL_LIGHTING),
			INVOKESTATIC("org/lwjgl/opengl/GL11", "glDisable", "(I)V")
		);
		
		ctx.search(INVOKESTATIC("net/minecraft/client/renderer/RenderHelper", "enableStandardItemLighting", "()V")).erase();
		
		ctx.jumpToLastReturn();
		ctx.add(
			SIPUSH(GL11.GL_LIGHTING),
			INVOKESTATIC("org/lwjgl/opengl/GL11", "glEnable", "(I)V")
		);
	}
	
	@Patch.Method("setColorWithBrightness(Lcodechicken/xycraftcopy/core/colour/Colour;I)V")
	@Patch.Method.AffectsControlFlow
	public void patchSetColorWithBrightness(PatchContext ctx) {
		// This is the method that mutates light state. Add an early return before it does that.
		ctx.search(SIPUSH(GL11.GL_LIGHTING)).jumpBefore();
		ctx.add(RETURN());
	}
	
}
