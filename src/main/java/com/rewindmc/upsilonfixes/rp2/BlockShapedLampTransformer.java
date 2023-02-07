package com.rewindmc.upsilonfixes.rp2;

import nilloader.api.lib.asm.tree.ClassNode;
import nilloader.api.lib.asm.tree.MethodNode;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.annotation.Patch;

import static nilloader.api.lib.asm.Opcodes.*;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonFixesPremain;

@Patch.Class("com.eloraam.redpower.lighting.BlockShapedLamp")
@ConfigOptions("fixRedPowerHitboxes")
public class BlockShapedLampTransformer extends UpsilonMiniTransformer {

	@Override
	protected boolean modifyClassStructure(ClassNode clazz) {
		UpsilonFixesPremain.log.debug("[{}] Applying structure changes to {}", getClass().getName(), clazz.name);
		// AxisAlignedBB getCollisionBoundingBoxFromPool(World, int, int, int)
		MethodNode selected = new MethodNode(ASM9, ACC_PUBLIC, "e", "(Lyc;III)Laoe;", null, null);
		selected.instructions = build(
			ALOAD(0),
			ALOAD(1),
			ILOAD(2),
			ILOAD(3),
			ILOAD(4),
			// void setBlockBoundsBasedOnState(IBlockAccess, int, int, int)
			INVOKEVIRTUAL("com/eloraam/redpower/lighting/BlockShapedLamp", "a", "(Lym;III)V"),
			ALOAD(0),
			ALOAD(1),
			ILOAD(2),
			ILOAD(3),
			ILOAD(4),
			// AxisAlignedBB getCollisionBoundingBoxFromPool(World, int, int, int)
			INVOKESPECIAL("com/eloraam/redpower/core/BlockExtended", "e", "(Lyc;III)Laoe;"),
			ARETURN()
		);
		clazz.methods.add(selected);
		return true;
	}
	
}
