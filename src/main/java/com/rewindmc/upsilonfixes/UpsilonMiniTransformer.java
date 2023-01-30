package com.rewindmc.upsilonfixes;

import nilloader.api.ClassRetransformer;
import nilloader.api.lib.asm.tree.AbstractInsnNode;
import nilloader.api.lib.asm.tree.InsnList;
import nilloader.api.lib.asm.tree.JumpInsnNode;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.MiniTransformer;

public abstract class UpsilonMiniTransformer extends MiniTransformer implements ClassRetransformer {

	protected final String hooks() {
		return getClass().getName().replace('.', '/')+"$Hooks";
	}
	
	protected final JumpInsnNode IFZ(LabelNode label) {
		return IFEQ(label);
	}
	
	protected final JumpInsnNode IFNZ(LabelNode label) {
		return IFNE(label);
	}

	protected final InsnList build(AbstractInsnNode... insns) {
		InsnList li = new InsnList();
		for (AbstractInsnNode ain : insns) {
			li.add(ain);
		}
		return li;
	}
	
}
