package com.rewindmc.upsilonfixes.vanilla;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;;

@Patch.Class("net.minecraft.src.FontRenderer")
public class FontRendererTransformer extends UpsilonMiniTransformer {

	@Patch.Method("<init>(Lnet/minecraft/src/GameSettings;Ljava/lang/String;Lnet/minecraft/src/RenderEngine;Z)V")
	public void patchConstructor(PatchContext ctx) {
		ctx.search(
			ALOAD(2),
			INVOKEVIRTUAL("java/lang/Class", "getResourceAsStream", "(Ljava/lang/String;)Ljava/io/InputStream;")
		).erase();
		
		ctx.search(
			INVOKESTATIC("javax/imageio/ImageIO", "read", "(Ljava/io/InputStream;)Ljava/awt/image/BufferedImage;")
		).jumpBefore();
		
		ctx.add(
			INVOKESTATIC("net/minecraft/client/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;"),
			GETFIELD("net/minecraft/client/Minecraft", "texturePackList", "Lnet/minecraft/src/TexturePackList;"),
			INVOKEVIRTUAL("net/minecraft/src/TexturePackList", "getSelectedTexturePack", "()Lnet/minecraft/src/ITexturePack;"),
			ALOAD(2),
			INVOKEINTERFACE("net/minecraft/src/ITexturePack", "getResourceAsStream", "(Ljava/lang/String;)Ljava/io/InputStream;")
		);
	}
	
}
