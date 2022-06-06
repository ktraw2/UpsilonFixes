package com.rewindmc.upsilonfixes.vanilla;

import java.nio.ByteBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.Packet250CustomPayload;
import nilloader.api.lib.asm.tree.LabelNode;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.NetClientHandler")
public class NetClientHandlerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("handleCustomPayload(Lnet/minecraft/src/Packet250CustomPayload;)V")
	@Patch.Method.AffectsControlFlow
	public void patchHandleCustomPayload(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode Lcontinue = new LabelNode();
		ctx.add(
			ALOAD(0),
			ALOAD(1),
			INVOKESTATIC(hooks(), "interceptPacket", "(Lnet/minecraft/src/NetClientHandler;Lnet/minecraft/src/Packet250CustomPayload;)Z"),
			IFZ(Lcontinue),
			RETURN(),
			Lcontinue
		);
	}
	
	public static class Hooks {
		
		public static boolean interceptPacket(NetClientHandler handler, Packet250CustomPayload pkt) {
			if (pkt.channel.equals("υatkyaw")) {
				Minecraft.getMinecraft().thePlayer.attackedAtYaw = ByteBuffer.wrap(pkt.data).getFloat();
				return true;
			}
			return false;
		}
		
	}

}
