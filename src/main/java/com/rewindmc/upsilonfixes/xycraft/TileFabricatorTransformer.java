package com.rewindmc.upsilonfixes.xycraft;

import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.asm.Opcodes;
import nilloader.api.lib.asm.tree.ClassNode;
import nilloader.api.lib.asm.tree.MethodNode;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.annotation.Patch;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Patch.Class("soaryn.xycraft.machines.block.TileFabricator")
public class TileFabricatorTransformer extends UpsilonMiniTransformer {

	@Override
	protected boolean modifyClassStructure(ClassNode clazz) {
		try {
			clazz.interfaces.add("logisticspipes.proxy.interfaces.ICraftingRecipeProvider");
			Method mthd = MiniTransformer.class.getDeclaredMethod("remapMethodDesc", String.class);
			mthd.setAccessible(true);
			String desc = (String)mthd.invoke(this, "(Lnet/minecraft/src/TileEntity;)Z");
			MethodNode mn = new MethodNode(4, Opcodes.ACC_PUBLIC, "canOpenGui", desc, null, null);
			mn.instructions.add(ALOAD(1));
			mn.instructions.add(INVOKESTATIC("com/rewindmc/upsilonfixes/xycraft/FabricatorRecipeProvider", "canOpenGui", desc));
			mn.instructions.add(IRETURN());
			clazz.methods.add(mn);
			desc = (String)mthd.invoke(this, "(Lnet/minecraft/src/TileEntity;Llogisticspipes/utils/SimpleInventory;)Z");
			mn = new MethodNode(4, Opcodes.ACC_PUBLIC, "importRecipe", desc, null, null);
			mn.instructions.add(ALOAD(1));
			mn.instructions.add(ALOAD(2));
			mn.instructions.add(INVOKESTATIC("com/rewindmc/upsilonfixes/xycraft/FabricatorRecipeProvider", "importRecipe", desc));
			mn.instructions.add(IRETURN());
			clazz.methods.add(mn);

		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return false;
	}
	
}