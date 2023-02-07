package com.rewindmc.upsilonfixes.vanilla;

import java.nio.ByteBuffer;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesConfig;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Screen;
import net.minecraft.client.gui.handled.HandledScreen;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.inventory.Slot;
import nilloader.api.lib.asm.tree.ClassNode;
import nilloader.api.lib.asm.tree.FieldNode;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.PatchContext.SearchResult;
import nilloader.api.lib.mini.annotation.Patch;

import static nilloader.api.lib.asm.Opcodes.*;

@Patch.Class("net.minecraft.client.gui.handled.HandledScreen")
@ConfigOptions({"smearing", "dropKeyInInventories"})
public class HandledScreenTransformer extends UpsilonMiniTransformer {
	
	@Override
	protected boolean modifyClassStructure(ClassNode clazz) {
		if (UpsilonFixesConfig.smearing) {
			clazz.fields.add(new FieldNode(ASM9, ACC_PRIVATE|ACC_FINAL, "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;", null, null));
		}
		return false;
	}
	
	@Patch.Method("<init>(Lnet/minecraft/inventory/ScreenHandler;)V")
	public void patchConstructor(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.jumpToLastReturn();
			
			ctx.add(
				ALOAD(0),
				NEW("com/rewindmc/upsilonfixes/SmearingCompanion"),
					DUP(),
					ALOAD(0),
					INVOKESPECIAL("com/rewindmc/upsilonfixes/SmearingCompanion", "<init>", "(Lnet/minecraft/client/gui/handled/HandledScreen;)V"),
				PUTFIELD("net/minecraft/client/gui/handled/HandledScreen", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;")
			);
		}
	}
	
	@Patch.Method("keyTyped(CI)V")
	public void patchKeyTyped(PatchContext ctx) {
		ctx.search(
			INVOKEVIRTUAL("net/minecraft/client/gui/handled/HandledScreen", "checkHotbarKeys", "(I)Z"),
			POP()
		).jumpAfter();
		
		ctx.add(
			ALOAD(0),
			ALOAD(0),
			GETFIELD("net/minecraft/client/gui/handled/HandledScreen", "theSlot", "Lnet/minecraft/inventory/Slot;"),
			ILOAD(1),
			ILOAD(2),
			INVOKESTATIC(hooks(), "keyTyped", "(Lnet/minecraft/client/gui/handled/HandledScreen;Lnet/minecraft/inventory/Slot;CI)V")
		);
	}
	
	@Patch.Method("mouseClicked(III)V")
	public void patchMouseClicked(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.jumpToStart();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/client/gui/handled/HandledScreen", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ILOAD(1),
				ILOAD(2),
				ILOAD(3),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "mouseDown", "(III)V")
			);
			
			// prevent vanilla logic from running
			
			ctx.search(
				GETFIELD("net/minecraft/inventory/Slot", "slotNumber", "I")
			).jumpAfter();
			
			ctx.add(
				POP(),
				ICONST_M1()
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
				INVOKEVIRTUAL("net/minecraft/client/gui/handled/HandledScreen", "drawGuiContainerBackgroundLayer", "(FII)V")
			).jumpAfter();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/client/gui/handled/HandledScreen", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ILOAD(1),
				ILOAD(2),
				FLOAD(3),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "drawScreenBg", "(IIF)V")
			);
			
			ctx.jumpToStart();
			SearchResult checkCursorStack = ctx.search(
				ALOAD(11),
				IFNULL(new LabelNode())
			);
			
			// for some reason, this class loads twice, and the first time it's not yet patched so the var indices are wrong
			if (checkCursorStack.isSuccessful()) {
				checkCursorStack.jumpBefore();
				ctx.add(
					ALOAD(0),
					GETFIELD("net/minecraft/client/gui/handled/HandledScreen", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
					ALOAD(11),
					INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "modifyCursorStack", "(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"),
					ASTORE(11)
				);
			}
			
			ctx.jumpToStart();
			ctx.search(
				ALOAD(0),
				ILOAD(1),
				ILOAD(2),
				INVOKEVIRTUAL("net/minecraft/client/gui/handled/HandledScreen", "drawGuiContainerForegroundLayer", "(II)V")
			).jumpBefore();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/client/gui/handled/HandledScreen", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
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
				GETFIELD("net/minecraft/client/gui/handled/HandledScreen", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ILOAD(1),
				ILOAD(2),
				ILOAD(3),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "mouseUp", "(III)V")
			);
		}
	}
	
	@Patch.Method("drawSlotInventory(Lnet/minecraft/inventory/Slot;)V")
	public void patchDrawSlotInventory(PatchContext ctx) {
		if (UpsilonFixesConfig.smearing) {
			ctx.search(
				INVOKEVIRTUAL("net/minecraft/inventory/Slot", "getStack", "()Lnet/minecraft/item/ItemStack;"),
				ASTORE(4)
			).jumpAfter();
			
			ctx.add(
				ALOAD(0),
				GETFIELD("net/minecraft/client/gui/handled/HandledScreen", "smearingCompanion", "Lcom/rewindmc/upsilonfixes/SmearingCompanion;"),
				ALOAD(1),
				ALOAD(4),
				INVOKEVIRTUAL("com/rewindmc/upsilonfixes/SmearingCompanion", "modifySlotStack", "(Lnet/minecraft/inventory/Slot;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/item/ItemStack;"),
				ASTORE(4)
			);
		}
	}

	public static class Hooks {
		
		public static void keyTyped(HandledScreen gui, Slot slot, char c, int code) {
			if (UpsilonFixesConfig.dropKeyInInventories && Minecraft.instance().player.inventory.getItemStack() == null && slot != null) {
				if (code == Minecraft.instance().options.keyBindDrop.keyCode) {
					byte[] bys = new byte[5];
					ByteBuffer.wrap(bys)
						.putInt(slot.slotNumber)
						.put((byte)(Screen.isCtrlKeyDown() ? 1 : 0));
					Minecraft.instance().getSendQueue().addToSendQueue(new Packet250CustomPayload("υinvthrw", bys));
				}
			}
		}
		
	}
}
