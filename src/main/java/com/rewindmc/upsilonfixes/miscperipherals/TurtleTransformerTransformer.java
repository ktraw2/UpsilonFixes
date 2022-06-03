package com.rewindmc.upsilonfixes.miscperipherals;

import nilloader.api.lib.asm.Opcodes;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.PatchContext.SearchResult;
import nilloader.api.lib.mini.annotation.Patch;

public abstract class TurtleTransformerTransformer extends UpsilonMiniTransformer {

	@Patch.Method("transform([B)[B")
	public void patchTransform(PatchContext ctx) {
		// In ASM 5, they added verification of the "api" parameter to various ASM classes, to ensure
		// people were properly using the constants in Opcodes (e.g. Opcodes.ASM4) which encoded the
		// version using bit shifts, so that a minor version could be differentiated if ever needed.
		// MiscPeripherals did not get this memo, and just uses the literal integer "4" to mean
		// ASM 4. Womp womp womp.
		while (true) {
			SearchResult res = ctx.search(
				NEW("org/objectweb/asm/tree/MethodNode"),
				DUP(),
				ICONST_4()
			);
			if (!res.isSuccessful()) break;
			res.jumpAfter();
			ctx.add(
				POP(),
				LDC(Opcodes.ASM4)
			);
		}
	}
	
}
