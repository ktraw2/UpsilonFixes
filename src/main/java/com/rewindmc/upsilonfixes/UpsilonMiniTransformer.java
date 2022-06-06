package com.rewindmc.upsilonfixes;

import nilloader.api.lib.asm.tree.JumpInsnNode;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.mini.MiniTransformer;

public abstract class UpsilonMiniTransformer extends MiniTransformer {

	protected final String hooks() {
		return getClass().getName().replace('.', '/')+"$Hooks";
	}
	
	protected final JumpInsnNode IFZ(LabelNode label) {
		return IFEQ(label);
	}
	
	protected final JumpInsnNode IFNZ(LabelNode label) {
		return IFNE(label);
	}
	
}
