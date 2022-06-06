package com.rewindmc.upsilonfixes.vanilla;

import java.nio.ByteBuffer;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Packet250CustomPayload;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("net.minecraft.src.EntityLiving")
public class EntityLivingTransformer extends UpsilonMiniTransformer {

	@Patch.Method("attackEntityFrom(Lnet/minecraft/src/DamageSource;I)Z")
	public void patchAttackEntityFrom(PatchContext ctx) {
		ctx.jumpToLastReturn();
		ctx.jumpBackward(1); // go behind the ICONST_1

		ctx.add(
			ALOAD(0),
			INVOKESTATIC(hooks(), "sendPacket", "(Lnet/minecraft/src/EntityLiving;)V")
		);
	}
	
	public static class Hooks {
		
		public static void sendPacket(EntityLiving entity) {
			if (entity instanceof EntityPlayerMP) {
				EntityPlayerMP p = (EntityPlayerMP)entity;
				byte[] bys = new byte[4];
				ByteBuffer.wrap(bys).putFloat(entity.attackedAtYaw);
				p.playerNetServerHandler.sendPacket(new Packet250CustomPayload("υatkyaw", bys));
			}
		}
		
	}

}
