package com.rewindmc.upsilonfixes.layering;

import nilloader.api.lib.asm.tree.LabelNode;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.TexturePackImplementation")
public class TexturePackImplementationTransformer extends UpsilonMiniTransformer {

	@Patch.Method("getResourceAsStream(Ljava/lang/String;)Ljava/io/InputStream;")
	@Patch.Method.AffectsControlFlow
	public void patchGetResourceAsStream(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode Lnotdefault = new LabelNode();
		LabelNode Lcontinuefromintercept = new LabelNode();
		LabelNode Lcontinue = new LabelNode();
		ctx.add(
			// call interceptor for default pack (can't patch TexturePackDefault as it doesn't define this method)
			ALOAD(0),
			INSTANCEOF("net/minecraft/src/TexturePackDefault"),
			IFZ(Lnotdefault),
			ALOAD(0),
			ALOAD(1),
			INVOKESTATIC("com/rewindmc/upsilonfixes/layering/TexturePackTransformer$Hooks", "interceptGetResourceAsStream", "(Lnet/minecraft/src/ITexturePack;Ljava/lang/String;)Ljava/io/InputStream;"),
			DUP(),
			IFNULL(Lcontinuefromintercept),
			ARETURN(),
			Lcontinuefromintercept,
			POP(),
			
			Lnotdefault,
			// don't delegate to classloader for layer packs
			LDC("/pack.png"),
			ALOAD(1),
			INVOKEVIRTUAL("java/lang/String", "equals", "(Ljava/lang/Object;)Z"),
			IFNZ(Lcontinue),
			ALOAD(0),
			INVOKESTATIC("com/rewindmc/upsilonfixes/layering/Layering", "isLayerPack", "(Lnet/minecraft/src/ITexturePack;)Z"),
			IFZ(Lcontinue),
			ACONST_NULL(),
			ARETURN(),
			Lcontinue
		);
	}
	
}
