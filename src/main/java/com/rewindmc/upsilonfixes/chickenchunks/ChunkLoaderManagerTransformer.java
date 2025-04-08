package com.rewindmc.upsilonfixes.chickenchunks;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.asm.tree.LabelNode;
import nilloader.api.lib.asm.tree.MethodInsnNode;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("codechicken.chunkloader.ChunkLoaderManager")
@ConfigOptions("fixChickenChunksAwayTimeout")
public class ChunkLoaderManagerTransformer extends UpsilonMiniTransformer {
    @Patch.Method("load(Lnet/minecraft/world/WorldServer;)V")
    public void patchLoad(PatchContext ctx) {
        // Calls in the load method are in the wrong order.
        // Call to loadLoginTimes needs to be called before trying load the chunk loaders, because the eligibility
        // can't be checked properly otherwise.

        MethodInsnNode loadLoginTimesCall = INVOKESTATIC("codechicken/chunkloader/ChunkLoaderManager", "loadLoginTimes", "()V");
        LabelNode Lskip = new LabelNode();

        ctx.jumpToStart();
        ctx.search(
                INVOKESTATIC("codechicken/chunkloader/ChunkLoaderManager", "loadPlayerChunks", "()V")
        ).jumpBefore();
        ctx.add(loadLoginTimesCall);

        // Skip the original call
        ctx.search(loadLoginTimesCall).jumpBefore();
        ctx.add(GOTO(Lskip));
        ctx.search(loadLoginTimesCall).jumpAfter();
        ctx.add(Lskip);
    }
}
