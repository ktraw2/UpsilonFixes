package com.rewindmc.upsilonfixes.ee3;

import com.rewindmc.upsilonfixes.ConfigOptions;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("com.pahimar.ee3.item.ItemPhilosopherStone")
@ConfigOptions("fixEETransmuteRecipes")
public class ItemPhilosopherStoneTransformer extends ItemMagicStoneTransformer {}