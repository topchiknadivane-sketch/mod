package com.animstudio.mod.client.cinema;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;

public class CinematicMode {

    public static final CinematicMode INSTANCE = new CinematicMode();

    private boolean active = false;
    private float smoothYaw = 0;
    private float smoothPitch = 0;
    private static final float SMOOTH = 0.08f;

    private CinematicMode() {}

    public void toggle() {
        active = !active;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        if (active) {
            mc.thePlayer.capabilities.allowFlying = true;
            mc.thePlayer.capabilities.isFlying = true;
            mc.thePlayer.sendPlayerAbilities();
            smoothYaw = mc.thePlayer.rotationYaw;
            smoothPitch = mc.thePlayer.rotationPitch;
        } else {
            if (!mc.thePlayer.capabilities.isCreativeMode) {
                mc.thePlayer.capabilities.allowFlying = false;
                mc.thePlayer.capabilities.isFlying = false;
                mc.thePlayer.sendPlayerAbilities();
            }
        }
    }

    public boolean isActive() { return active; }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (!active || event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;

        EntityClientPlayerMP p = mc.thePlayer;
        smoothYaw   += (p.rotationYaw   - smoothYaw)   * SMOOTH;
        smoothPitch += (p.rotationPitch - smoothPitch) * SMOOTH;
        p.rotationYaw   = smoothYaw;
        p.rotationPitch = smoothPitch;
        p.capabilities.flySpeed = 0.15f;
        p.sendPlayerAbilities();
    }
}
