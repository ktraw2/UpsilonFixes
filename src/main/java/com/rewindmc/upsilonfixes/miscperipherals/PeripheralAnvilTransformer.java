package com.rewindmc.upsilonfixes.miscperipherals;

import com.rewindmc.upsilonfixes.ConfigOptions;
import com.rewindmc.upsilonfixes.UpsilonMiniTransformer;
import miscperipherals.peripheral.PeripheralAnvil;
import miscperipherals.peripheral.PeripheralXP;
import nilloader.api.lib.mini.PatchContext;
import nilloader.api.lib.mini.annotation.Patch;

@Patch.Class("miscperipherals.peripheral.PeripheralAnvil")
@ConfigOptions("fixAnvilPeripheral")
public class PeripheralAnvilTransformer extends UpsilonMiniTransformer {

    @Patch.Method("callMethod(Ldan200/computer/api/IComputerAccess;I[Ljava/lang/Object;)[Ljava/lang/Object; throws java/lang/Exception")
    public void patchCallMethod(PatchContext ctx) {
        ctx.search(
                ALOAD(6),
                ALOAD(7),
                GETFIELD("miscperipherals/peripheral/PeripheralAnvil$AnvilData", "cost", "I"),
                INEG(),
                ICONST_1(),
                INVOKEVIRTUAL("miscperipherals/peripheral/PeripheralXP", "addLevels", "(IZ)V")
        ).jumpBefore();
        ctx.add(
                ALOAD(0),
                ALOAD(6),
                ALOAD(7),
                ILOAD(4),
                ILOAD(5),
                INVOKESTATIC(hooks(), "repairItem", "(Lmiscperipherals/peripheral/PeripheralAnvil;Lmiscperipherals/peripheral/PeripheralXP;Lmiscperipherals/peripheral/PeripheralAnvil$AnvilData;II)[Ljava/lang/Object;"),
                ARETURN()
        );
    }

    public static class Hooks {
        public static Object[] repairItem(PeripheralAnvil perAnvil, PeripheralXP perXP, PeripheralAnvil.AnvilData data, int slotA, int slotB) {
            if (perXP.experienceLevel < data.cost || data.stack == null)
                return new Object[]{false};

            perXP.addLevels(-data.cost, true);
            perAnvil.turtle.setSlotContents(slotA, data.stack);
            perAnvil.turtle.setSlotContents(slotB, null);
            perAnvil.damage(false);
            return new Object[]{true};
        }
    }
}
