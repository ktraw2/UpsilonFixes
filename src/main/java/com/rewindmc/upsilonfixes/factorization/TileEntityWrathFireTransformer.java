package com.rewindmc.upsilonfixes.factorization;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("factorization.common.TileEntityWrathFire")
@ConfigOptions("buffWrathForge")
public class TileEntityWrathFireTransformer extends UpsilonMiniTransformer {

    @Patch.Method("doUpdate()V")
    public void patchDoUpdate(PatchContext ctx) {
        ctx.jumpToStart();
        ctx.search(
                GETSTATIC("factorization/common/TileEntityWrathFire", "netherBrick", "Lfactorization/common/TileEntityWrathFire$BlockMatch;"),
                ALOAD(0),
                GETFIELD("factorization/common/TileEntityWrathFire", "host", "Lfactorization/common/TileEntityWrathFire$BlockMatch;"),
                INVOKEVIRTUAL("factorization/common/TileEntityWrathFire$BlockMatch", "equals", "(Ljava/lang/Object;)Z")
        ).jumpAfter();
        ctx.search(
                ALOAD(0),
                DUP(),
                GETFIELD("factorization/common/TileEntityWrathFire", "age", "I"),
                ICONST_1()
        ).jumpAfter();
        ctx.add(
                POP(),
                ICONST_0()
        );
    }
}
