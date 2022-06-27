package com.rewindmc.upsilonfixes.layering;

import java.io.InputStream;

import com.rewindmc.upsilonfixes.UpsilonFixesPremain;

import net.minecraft.src.ITexturePack;
import nilloader.api.lib.asm.tree.LabelNode;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;

public abstract class TexturePackTransformer extends UpsilonMiniTransformer {

	protected void patchGetResourceAsStream(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode Lcontinue = new LabelNode();
		ctx.add(
			ALOAD(0),
			ALOAD(1),
			INVOKESTATIC("com/rewindmc/upsilonfixes/layering/TexturePackTransformer$Hooks", "interceptGetResourceAsStream", "(Lnet/minecraft/src/ITexturePack;Ljava/lang/String;)Ljava/io/InputStream;"),
			DUP(),
			IFNULL(Lcontinue),
			ARETURN(),
			Lcontinue,
			POP()
		);
	}
	
	public static class Hooks {
		private static boolean reentering;
		
		public static InputStream interceptGetResourceAsStream(ITexturePack pack, String path) {
			if (reentering) return null;
			if ("/upsilon-layerable".equals(path)) return null;
			if ("/pack.png".equals(path)) return null;
			if ("/pack.txt".equals(path)) return null;
			try {
				reentering = true;
				for (ITexturePack layer : Layering.enabledLayerPacks) {
					InputStream in = layer.getResourceAsStream(path);
					if (in != null) {
						UpsilonFixesPremain.log.debug("Found {} in layerpack {}", path, layer.getTexturePackFileName());
						return in;
					}
				}
			} finally {
				reentering = false;
			}
			return null;
		}
	}
	
}
