package com.rewindmc.upsilonfixes.vanilla;

import java.nio.ByteBuffer;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Slot;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.GuiContainer")
public class GuiContainerTransformer extends UpsilonMiniTransformer {
	
	@Patch.Method("keyTyped(CI)V")
	public void patchKeyTyped(PatchContext ctx) {
		ctx.search(
			INVOKEVIRTUAL("net/minecraft/src/GuiContainer", "checkHotbarKeys", "(I)Z"),
			POP()
		).jumpAfter();
		ctx.add(
			ALOAD(0),
			ALOAD(0),
			GETFIELD("net/minecraft/src/GuiContainer", "theSlot", "Lnet/minecraft/src/Slot;"),
			ILOAD(1),
			ILOAD(2),
			INVOKESTATIC(hooks(), "keyTyped", "(Lnet/minecraft/src/GuiContainer;Lnet/minecraft/src/Slot;CI)V")
		);
	}

	public static class Hooks {
		
		public static void keyTyped(GuiContainer gui, Slot slot, char c, int code) {
			if (Minecraft.getMinecraft().thePlayer.inventory.getItemStack() == null && slot != null) {
				if (code == Minecraft.getMinecraft().gameSettings.keyBindDrop.keyCode) {
					byte[] bys = new byte[5];
					ByteBuffer.wrap(bys)
						.putInt(slot.slotNumber)
						.put((byte)(GuiScreen.isCtrlKeyDown() ? 1 : 0));
					Minecraft.getMinecraft().getSendQueue().addToSendQueue(new Packet250CustomPayload("υinvthrw", bys));
				}
			}
		}
		
	}
}
