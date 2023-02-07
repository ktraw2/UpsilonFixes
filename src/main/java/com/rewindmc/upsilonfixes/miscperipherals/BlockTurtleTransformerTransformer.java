package com.rewindmc.upsilonfixes.miscperipherals;

import com.rewindmc.upsilonfixes.ConfigOptions;

import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("miscperipherals.asm.BlockTurtleTransformer")
@ConfigOptions("fixMiscPeripheralsASM")
public class BlockTurtleTransformerTransformer extends TurtleTransformerTransformer {}
