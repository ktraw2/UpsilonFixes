package com.unascribed.upsilonfixes.vanilla;

import java.nio.ByteBuffer;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Packet250CustomPayload;
import nilloader.api.lib.mini.MiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.EntityLiving")
public class EntityLivingTransformer extends MiniTransformer {

	@Patch.Method("attackEntityFrom(Lnet/minecraft/src/DamageSource;I)Z")
	public void patchAttackEntityFrom(PatchContext ctx) {
		ctx.jumpToLastReturn();

		ctx.add(
			ALOAD(0),
			INVOKESTATIC("com/unascribed/upsilonfixes/vanilla/EntityLivingTransformer$Hooks", "sendPacket", "(Lnet/minecraft/src/EntityLiving;)V")
		);
	}
	
	public static class Hooks {
		
		public static void sendPacket(EntityLiving entity) {
			if (entity instanceof EntityPlayerMP) {
				EntityPlayerMP p = (EntityPlayerMP)entity;
				byte[] bys = new byte[4];
				ByteBuffer.wrap(bys).putFloat(entity.attackedAtYaw);
				p.playerNetServerHandler.sendPacket(new Packet250CustomPayload("atkyaw", bys));
			}
		}
		
	}

}
