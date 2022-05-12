package com.unascribed.upsilonfixes.vanilla;

import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.EntityLiving")
public class EntityLivingTransformer extends MiniTransformer {

	@Patch.Method("attackEntityFrom(Lnet/minecraft/src/DamageSource;I)Z")
	public void patchAttackEntityFrom(PatchContext ctx) {
		ctx.jumpToLastReturn();

		LabelNode Lexit = new LabelNode();

		ctx.add(
				ALOAD(0),
				INSTANCEOF("net/minecraft/src/EntityPlayerMP"),
				IFEQ(Lexit),
				ALOAD(0),
				GETFIELD("net/minecraft/src/EntityLiving", "attackedAtYaw", "F"),
				INVOKESTATIC("java/lang/Float", "floatToIntBits", "(F)I"),
				ISTORE(2),
				ALOAD(0),
				CHECKCAST("net/minecraft/src/EntityPlayerMP"),
				GETFIELD("net/minecraft/src/EntityPlayerMP", "playerNetServerHandler", "Lnet/minecraft/src/NetServerHandler;"),
				NEW("net/minecraft/src/Packet250CustomPayload"),
				DUP(),
				LDC("atkyaw"),
				LDC(4),
				NEWARRAY(T_BYTE),
				DUP(),
				LDC(0),
				ILOAD(2),
				BIPUSH(24),
				ISHR(),
				SIPUSH(255),
				IAND(),
				I2B(),
				BASTORE(),
				DUP(),
				LDC(1),
				ILOAD(2),
				BIPUSH(16),
				ISHR(),
				SIPUSH(255),
				IAND(),
				I2B(),
				BASTORE(),
				DUP(),
				LDC(2),
				ILOAD(2),
				BIPUSH(8),
				ISHR(),
				SIPUSH(255),
				IAND(),
				I2B(),
				BASTORE(),
				DUP(),
				LDC(3),
				ILOAD(2),
				SIPUSH(255),
				IAND(),
				I2B(),
				BASTORE(),
				INVOKESPECIAL("net/minecraft/src/Packet250CustomPayload", "<init>", "(Ljava/lang/String;[B)V"),
				INVOKEVIRTUAL("net/minecraft/src/NetServerHandler", "sendPacket", "(Lnet/minecraft/src/Packet;)V"),
				Lexit
			);
	}

}
