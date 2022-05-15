package com.unascribed.upsilonfixes.vanilla;

import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.src.Packet250CustomPayload;
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
			INVOKESTATIC("com/unascribed/upsilonfixes/vanilla/Packet250CustomPayloadTransformer$Hooks", "interceptPacket", "(Lnet/minecraft/src/Packet250CustomPayload;)Z"),
			IFEQ(Lexit),
			RETURN(),
			Lexit
		);
	}
	
	public static class Hooks {
		
		public static boolean interceptPacket(Packet250CustomPayload pkt) {
			if (pkt.channel.equals("atkyaw")) {
				Minecraft.getMinecraft().thePlayer.attackedAtYaw = ByteBuffer.wrap(pkt.data).getFloat();
				return true;
			}
			return false;
		}
		
	}

}
