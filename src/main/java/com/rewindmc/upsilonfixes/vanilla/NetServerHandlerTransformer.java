package com.rewindmc.upsilonfixes.vanilla;

import net.minecraft.src.Container;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Slot;
import nilloader.api.lib.asm.tree.LabelNode;

import java.nio.ByteBuffer;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.NetServerHandler")
public class NetServerHandlerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("handleCustomPayload(Lnet/minecraft/src/Packet250CustomPayload;)V")
	@Patch.Method.AffectsControlFlow
	public void patchHandleCustomPayload(PatchContext ctx) {
		ctx.jumpToStart();
		LabelNode Lexit = new LabelNode();
		ctx.add(
			ALOAD(0),
			ALOAD(1),
			INVOKESTATIC(hooks(), "interceptPacket", "(Lnet/minecraft/src/NetServerHandler;Lnet/minecraft/src/Packet250CustomPayload;)Z"),
			IFZ(Lexit),
			RETURN(),
			Lexit
		);
	}
	
	@Patch.Method("handleChat(Lnet/minecraft/src/Packet3Chat;)V")
	public void patchHandleChat(PatchContext ctx) {
		if (UpsilonFixesConfig.increaseChatLimit) {
			ctx.search(
				LDC(100)
			).jumpAfter();
			ctx.add(
				POP(),
				LDC(256)
			);
		}
	}
	
	public static class Hooks {
		
		public static boolean interceptPacket(NetServerHandler handler, Packet250CustomPayload pkt) {
			if (pkt.channel.equals("υinvthrw") && UpsilonFixesConfig.dropKeyInInventories) {
				EntityPlayerMP player = handler.playerEntity;
				Container container = player.openContainer;
				ByteBuffer buf = ByteBuffer.wrap(pkt.data);
				int sid = buf.getInt();
				boolean all = buf.get() != 0;
				if (container != null && sid >= 0 && sid < container.inventorySlots.size()) {
					Slot slot = container.getSlot(sid);
					ItemStack stack = slot.getStack();
					if (stack != null) {
						if (!all) {
							stack.stackSize--;
							if (stack.stackSize <= 0) {
								slot.putStack(null);
							}
							stack = stack.copy();
							stack.stackSize = 1;
						} else {
							slot.putStack(null);
						}
						player.dropPlayerItem(stack);
					}
				}
				return true;
			} else if (pkt.channel.equals("υcollect") && UpsilonFixesConfig.dropKeyInInventories) {
//				EntityPlayerMP player = handler.playerEntity;
//				Container container = player.openContainer;
//				ByteBuffer buf = ByteBuffer.wrap(pkt.data);
//				int sid = buf.getInt();
//				if (container != null && sid >= 0 && sid < container.inventorySlots.size() && player.inventory.getItemStack() == null) {
//					Slot slot = container.getSlot(sid);
//					ItemStack stack = slot.getStack();
//					if (stack != null) {
//						stack = stack.copy();
//						int amt = 0;
//						for (Slot s : (List<Slot>)container.inventorySlots) {
//							if (amt >= stack.getMaxStackSize()) break;
//							if (s.getHasStack() && SmearingCompanion.canStack(s.getStack(), stack)) {
//								ItemStack is = s.getStack();
//								int toTake = Math.min(stack.getMaxStackSize()-amt, is.stackSize);
//								if (toTake > 0) {
//									amt += toTake;
//									ItemStack decrd = s.getStack().copy();
//									decrd.stackSize -= toTake;
//									if (decrd.stackSize <= 0) {
//										s.putStack(null);
//									} else {
//										s.putStack(decrd);
//									}
//								}
//							}
//						}
//						if (amt > 0) {
//							stack.stackSize = amt;
//							player.inventory.setItemStack(stack);
//						}
//					}
//				}
				return true;
			}
						
			return false;
		}
		
	}

}
