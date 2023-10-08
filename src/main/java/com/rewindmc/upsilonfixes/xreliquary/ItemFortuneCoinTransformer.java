package com.rewindmc.upsilonfixes.xreliquary;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;
@Patch.Class("xeno.reliquary.items.ItemFortuneCoin")
@ConfigOptions("improveXReliquaryCoinPickup")
public class ItemFortuneCoinTransformer extends UpsilonMiniTransformer {
    @Patch.Method("scanForEntitiesInRange(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;D)V")
    public void patchScanForEntitiesInRange(PatchContext ctx) {
        // Force pickup through onCollideWithPlayer after item has been teleported to player
        ctx.search(
                ALOAD(0),
                ALOAD(7),
                ALOAD(2),
                INVOKESPECIAL("xeno/reliquary/items/ItemFortuneCoin", "teleportEntityToPlayer", "(Llq;Lnet/minecraft/entity/player/EntityPlayer;)V")
        ).jumpAfter();
        ctx.add(
                ALOAD(7),
                ALOAD(2),
                INVOKEVIRTUAL("net/minecraft/entity/item/EntityItem", "onCollideWithPlayer", "(Lnet/minecraft/entity/player/EntityPlayer;)V")
        );
    }
}
