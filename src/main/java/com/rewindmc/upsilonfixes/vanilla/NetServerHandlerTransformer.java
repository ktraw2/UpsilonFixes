package com.rewindmc.upsilonfixes.vanilla;

import net.minecraft.entity.player.EntityServerPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.inventory.ScreenHandler;
import net.minecraft.inventory.Slot;
import nilloader.api.lib.asm.tree.LabelNode;

import java.nio.ByteBuffer;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.network.NetServerHandler")
@ConfigOptions({"dropKeyInInventories", "smearing", "increaseChatLimit"})
public class NetServerHandlerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("handleCustomPayload(Lnet/minecraft/network/packet/Packet250CustomPayload;)V")
	@Patch.Method.AffectsControlFlow
	public void patchHandleCustomPayload(PatchContext ctx) {
		if (UpsilonFixesConfig.dropKeyInInventories || UpsilonFixesConfig.smearing) {
			ctx.jumpToStart();
			LabelNode Lexit = new LabelNode();
			ctx.add(
				ALOAD(0),
				ALOAD(1),
				INVOKESTATIC(hooks(), "interceptPacket", "(Lnet/minecraft/network/NetServerHandler;Lnet/minecraft/network/packet/Packet250CustomPayload;)Z"),
				IFZ(Lexit),
				RETURN(),
				Lexit
			);
		}
	}
	
	@Patch.Method("handleChat(Lnet/minecraft/network/packet/Packet3Chat;)V")
	public void patchHandleChat(PatchContext ctx) {
		if (UpsilonFixesConfig.increaseChatLimit) {
			ctx.search(
				BIPUSH(100)
			).jumpAfter();
			ctx.add(
				POP(),
				SIPUSH(256)
			);
		}
	}
	
	public static class Hooks {
		
		public static boolean interceptPacket(NetServerHandler handler, Packet250CustomPayload pkt) {
			if (pkt.channel.equals("υinvthrw") && UpsilonFixesConfig.dropKeyInInventories) {
				EntityServerPlayer player = handler.playerEntity;
				ScreenHandler container = player.openContainer;
				ByteBuffer buf = ByteBuffer.wrap(pkt.data);
				int sid = buf.getInt();
				boolean all = buf.get() != 0;
				if (container != null && sid >= 0 && sid < container.inventorySlots.size()) {
					Slot slot = container.getSlot(sid);
					ItemStack stack = slot.getStack();
					if (stack != null) {
						if (!all) {
							stack.count--;
							if (stack.count <= 0) {
								slot.putStack(null);
							}
							stack = stack.copy();
							stack.count = 1;
						} else {
							slot.putStack(null);
							stack = stack.copy(); // avoid taking ownership of crafting outputs
						}
						slot.onPickupFromSlot(player, stack);
						player.dropPlayerItem(stack);
					}
				}
				return true;
			} else if (pkt.channel.equals("υcollect") && UpsilonFixesConfig.smearing) {
				// TODO
				return true;
			}
						
			return false;
		}
		
	}

}
