package com.unascribed.upsilonfixes.vanilla;

import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.Packet250CustomPayload")
public class Packet250CustomPayloadTransformer extends MiniTransformer {

	@Patch.Method("processPacket(Lnet/minecraft/src/NetHandler;)V")
	@Patch.Method.AffectsControlFlow
	public void patchProcessPacket(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode Lexit = new LabelNode();
		ctx.add(
			ALOAD(0),
			GETFIELD("net/minecraft/src/Packet250CustomPayload", "channel", "Ljava/lang/String;"),
			LDC("atkyaw"),
			INVOKEVIRTUAL("java/lang/String", "equals", "(Ljava/lang/Object;)Z"),
			IFEQ(Lexit),
			INVOKESTATIC("net/minecraft/client/Minecraft", "getMinecraft", "()Lnet/minecraft/client/Minecraft;"),
			GETFIELD("net/minecraft/client/Minecraft", "thePlayer", "Lnet/minecraft/src/EntityClientPlayerMP;"),
			ALOAD(0),
			GETFIELD("net/minecraft/src/Packet250CustomPayload", "data", "[B"),
			LDC(0),
			BALOAD(),
			SIPUSH(255),
			IAND(),
			BIPUSH(24),
			ISHL(),
			ALOAD(0),
			GETFIELD("net/minecraft/src/Packet250CustomPayload", "data", "[B"),
			LDC(1),
			BALOAD(),
			SIPUSH(255),
			IAND(),
			BIPUSH(16),
			ISHL(),
			IOR(),
			ALOAD(0),
			GETFIELD("net/minecraft/src/Packet250CustomPayload", "data", "[B"),
			LDC(2),
			BALOAD(),
			SIPUSH(255),
			IAND(),
			BIPUSH(8),
			ISHL(),
			IOR(),
			ALOAD(0),
			GETFIELD("net/minecraft/src/Packet250CustomPayload", "data", "[B"),
			LDC(3),
			BALOAD(),
			SIPUSH(255),
			IAND(),
			IOR(),
			INVOKESTATIC("java/lang/Float", "intBitsToFloat", "(I)F"),
			PUTFIELD("net/minecraft/src/EntityClientPlayerMP", "attackedAtYaw", "F"),
			RETURN(),
			Lexit
		);
	}

}
