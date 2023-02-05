package com.rewindmc.upsilonfixes.vanilla;

import java.awt.Color;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.RenderItem")
public class RenderItemTransformer extends UpsilonMiniTransformer {

	@Patch.Method("renderItemOverlayIntoGUI(Lnet/minecraft/src/FontRenderer;Lnet/minecraft/src/RenderEngine;Lnet/minecraft/src/ItemStack;II)V")
	public void patch(PatchContext ctx) {
		// int var10 = (255 - var7) / 4 << 16 | 16128;
		ctx.search(
			SIPUSH(255),
			ILOAD(7),
			ISUB(),
			ICONST_4(),
			IDIV(),
			BIPUSH(16),
			ISHL(),
			SIPUSH(16128),
			IOR(),
			ISTORE(10)
		).jumpAfter();
		
		// 10 is the bg color, 9 is the fg
		// 7 is the durability (0 to 255), 11 is the durability (0 to 13)
		ctx.add(
			ILOAD(7),
			INVOKESTATIC(hooks(), "modifyBg", "(I)I"),
			ISTORE(10),
			
			ILOAD(7),
			INVOKESTATIC(hooks(), "modifyFg", "(I)I"),
			ISTORE(9)
		);
	}
	
	public static class Hooks {
		
		private static int getItemBarColor(float d) {
			return Color.HSBtoRGB(d / 3, 1, 1);
		}
		
		public static int modifyBg(int dura) {
			return 0xFF000000;
		}
		
		public static int modifyFg(int dura) {
			return getItemBarColor(dura/255f);
		}
		
	}
	
}
