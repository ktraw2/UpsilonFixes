package com.rewindmc.upsilonfixes.vanilla;

import java.nio.ByteBuffer;

import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Packet250CustomPayload;
import net.minecraft.src.Slot;
import nilloader.api.lib.asm.tree.ClassNode;
import nilloader.api.lib.asm.tree.FieldNode;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

import static nilloader.api.lib.asm.Opcodes.*;

@Patch.Class("net.minecraft.src.GuiContainer")
public class GuiContainerTransformer extends UpsilonMiniTransformer {
	
	@Override
	protected boolean modifyClassStructure(ClassNode clazz) {
		if (UpsilonFixesConfig.smearing) {
			clazz.fields.add(new FieldNode(ASM9, ACC_PRIVATE|ACC_FINAL, "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;", null, null));
		}
		return false;
	}
	
	@Patch.Method("<init>(Lnet/minecraft/src/Container;)V")
	public void patchConstructor(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.jumpToLastReturn();
			
			ctx.add(
				ALOAD(0),
				NEW("com/rewindmc/upsilonfixes/SmearingCompanion"),
					DUP(),
					ALOAD(0),
					INVOKESPECIAL("com/rewindmc/upsilonfixes/SmearingCompanion", "<init>", "(Lnet/minecraft/src/GuiContainer;)V"),
				PUTFIELD("net/minecraft/src/GuiContainer", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;")
			);
		}
	}
	
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
	
	@Patch.Method("mouseClicked(III)V")
	public void patchMouseClicked(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.jumpToStart();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/src/GuiContainer", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ILOAD(1),
				ILOAD(2),
				ILOAD(3),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "mouseDown", "(III)V"),
				
				// change mouse button to an invalid value to prevent most of the method from running
				ICONST_M1(),
				ISTORE(3)
			);
		}
	}
	
	@Patch.Method("drawScreen(IIF)V")
	public void patchDrawScreen(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.search(
				ALOAD(0),
				FLOAD(3),
				ILOAD(1),
				ILOAD(2),
				INVOKEVIRTUAL("net/minecraft/src/GuiContainer", "drawGuiContainerBackgroundLayer", "(FII)V")
			).jumpBefore();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/src/GuiContainer", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ILOAD(1),
				ILOAD(2),
				FLOAD(3),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "drawScreenBg", "(IIF)V")
			);
			
			ctx.jumpToStart();
			ctx.search(
				ALOAD(11),
				IFNULL(new LabelNode())
			).jumpBefore();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/src/GuiContainer", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ALOAD(11),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "modifyCursorStack", "(Lnet/minecraft/src/ItemStack;)Lnet/minecraft/src/ItemStack;"),
				ASTORE(11)
			);
			
			ctx.jumpToStart();
			ctx.search(
				ALOAD(0),
				ILOAD(1),
				ILOAD(2),
				INVOKEVIRTUAL("net/minecraft/src/GuiContainer", "drawGuiContainerForegroundLayer", "(II)V")
			).jumpBefore();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/src/GuiContainer", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ILOAD(1),
				ILOAD(2),
				FLOAD(3),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "drawScreenFg", "(IIF)V")
			);
		}
	}
	
	@Patch.Method("mouseMovedOrUp(III)V")
	public void patchMouseMovedOrUp(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.jumpToStart();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/src/GuiContainer", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ILOAD(1),
				ILOAD(2),
				ILOAD(3),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "mouseUp", "(III)V")
			);
		}
	}
	
	@Patch.Method("drawSlotInventory(Lnet/minecraft/src/Slot;)V")
	public void patchDrawSlotInventory(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.search(
				INVOKEVIRTUAL("net/minecraft/src/Slot", "getStack", "()Lnet/minecraft/src/ItemStack;"),
				ASTORE(4)
			).jumpAfter();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/src/GuiContainer", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ALOAD(1),
				ALOAD(4),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "modifySlotStack", "(Lnet/minecraft/src/Slot;Lnet/minecraft/src/ItemStack;)Lnet/minecraft/src/ItemStack;"),
				ASTORE(4)
			);
		}
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
