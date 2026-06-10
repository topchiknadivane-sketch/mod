package com.animstudio.mod.client.gui;

import com.animstudio.mod.animation.AnimationManager;
import com.animstudio.mod.entity.DummyEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AnimStudioScreen extends GuiScreen {

    private static final int TAB_MOBS = 0;
    private static final int TAB_SKINS = 1;
    private static final int TAB_DUPLICATE = 2;
    private static final int TAB_ANIMATION = 3;

    private int tab = TAB_MOBS;
    private UUID selectedId = null;
    private float animSpeed = 1.0f;

    private static final String[] MOB_NAMES = {
        "Creeper", "Zombie", "Skeleton", "Spider", "Enderman",
        "Cow", "Pig", "Sheep", "Chicken", "Wolf"
    };

    private static final List<String> SKINS = Arrays.asList(
        "Notch", "Jeb_", "Dream", "Technoblade", "Ph1LzA",
        "Skeppy", "BadBoyHalo", "Tubbo", "TommyInnit", "Wilbur"
    );

    @Override
    public void initGui() {
        buildButtons();
    }

    private void buildButtons() {
        buttonList.clear();
        int cx = width / 2;
        buttonList.add(new GuiButton(10, cx - 210, 10, 100, 20, "Mobs"));
        buttonList.add(new GuiButton(11, cx - 105, 10, 100, 20, "Skins"));
        buttonList.add(new GuiButton(12, cx,       10, 100, 20, "Duplicate"));
        buttonList.add(new GuiButton(13, cx + 105, 10, 100, 20, "Animation"));

        int sy = 45;
        if (tab == TAB_MOBS) {
            for (int i = 0; i < MOB_NAMES.length; i++) {
                buttonList.add(new GuiButton(100 + i,
                    cx - 110 + (i % 2) * 115, sy + (i / 2) * 24, 110, 20,
                    MOB_NAMES[i]));
            }
        }
        if (tab == TAB_SKINS) {
            for (int i = 0; i < SKINS.size(); i++) {
                buttonList.add(new GuiButton(200 + i,
                    cx - 110 + (i % 2) * 115, sy + (i / 2) * 24, 110, 20,
                    SKINS.get(i)));
            }
        }
        if (tab == TAB_DUPLICATE) {
            buttonList.add(new GuiButton(300, cx - 100, sy,      200, 22, "Duplicate yourself"));
            buttonList.add(new GuiButton(301, cx - 100, sy + 30, 200, 22, "Remove all duplicates"));
        }
        if (tab == TAB_ANIMATION) {
            String rec = AnimationManager.isRecording ? "Stop Recording" : "Start Recording";
            buttonList.add(new GuiButton(400, cx - 105, sy,      100, 22, rec));
            buttonList.add(new GuiButton(401, cx,       sy,      100, 22, "Play"));
            buttonList.add(new GuiButton(402, cx - 105, sy + 28, 100, 22, "Speed -"));
            buttonList.add(new GuiButton(403, cx,       sy + 28, 100, 22, "Speed +"));
        }
    }

    @Override
    protected void actionPerformed(GuiButton btn) {
        Minecraft mc = Minecraft.getMinecraft();
        switch (btn.id) {
            case 10: tab = TAB_MOBS;      buildButtons(); return;
            case 11: tab = TAB_SKINS;     buildButtons(); return;
            case 12: tab = TAB_DUPLICATE; buildButtons(); return;
            case 13: tab = TAB_ANIMATION; buildButtons(); return;
        }
        if (btn.id >= 100 && btn.id < 200) { spawnMob(btn.id - 100, mc); }
        if (btn.id >= 200 && btn.id < 300) { spawnSkin(SKINS.get(btn.id - 200), mc); }
        if (btn.id == 300) { duplicateSelf(mc); }
        if (btn.id == 301) { removeAllDummies(mc); }
        if (btn.id == 400) {
            if (AnimationManager.isRecording) AnimationManager.stopRecording();
            else if (selectedId != null) AnimationManager.startRecording(selectedId);
            buildButtons();
        }
        if (btn.id == 401 && selectedId != null) {
            for (Object obj : mc.theWorld.loadedEntityList) {
                if (obj instanceof EntityLivingBase) {
                    EntityLivingBase e = (EntityLivingBase) obj;
                    if (e.getUniqueID().equals(selectedId)) {
                        AnimationManager.playAnimation(e, animSpeed);
                        break;
                    }
                }
            }
        }
        if (btn.id == 402) animSpeed = Math.max(0.25f, animSpeed - 0.25f);
        if (btn.id == 403) animSpeed = Math.min(4.0f,  animSpeed + 0.25f);
    }

    private void spawnMob(int idx, Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        EntityLivingBase e = null;
        double x = mc.thePlayer.posX + 2, y = mc.thePlayer.posY, z = mc.thePlayer.posZ;
        switch (idx) {
            case 0: e = new EntityCreeper(mc.theWorld);  break;
            case 1: e = new EntityZombie(mc.theWorld);   break;
            case 2: e = new EntitySkeleton(mc.theWorld); break;
            case 3: e = new EntitySpider(mc.theWorld);   break;
            case 4: e = new EntityEnderman(mc.theWorld); break;
            case 5: e = new EntityCow(mc.theWorld);      break;
            case 6: e = new EntityPig(mc.theWorld);      break;
            case 7: e = new EntitySheep(mc.theWorld);    break;
            case 8: e = new EntityChicken(mc.theWorld);  break;
            case 9: e = new EntityWolf(mc.theWorld);     break;
        }
        if (e != null) {
            e.setPosition(x, y, z);
            mc.theWorld.spawnEntityInWorld(e);
            selectedId = e.getUniqueID();
        }
    }

    private void spawnSkin(String skin, Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        DummyEntity d = new DummyEntity(mc.theWorld, skin);
        d.setPosition(mc.thePlayer.posX + 2, mc.thePlayer.posY, mc.thePlayer.posZ);
        mc.theWorld.spawnEntityInWorld(d);
        selectedId = d.getUniqueID();
    }

    private void duplicateSelf(Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null) return;
        DummyEntity d = new DummyEntity(mc.theWorld, mc.thePlayer);
        mc.theWorld.spawnEntityInWorld(d);
        selectedId = d.getUniqueID();
    }

    private void removeAllDummies(Minecraft mc) {
        if (mc.theWorld == null) return;
        List<DummyEntity> list = new ArrayList<DummyEntity>();
        for (Object obj : mc.theWorld.loadedEntityList) {
            if (obj instanceof DummyEntity) list.add((DummyEntity) obj);
        }
        for (DummyEntity d : list) d.setDead();
        selectedId = null;
    }

    @Override
    public void drawScreen(int mx, int my, float pt) {
        drawDefaultBackground();
        int px = width / 2 - 130, py = 35;
        drawRect(px, py, px + 260, py + 220, 0xCC111122);
        fontRendererObj.drawStringWithShadow("§b§lAnim Studio", px + 85, py + 5, 0xFFFFFF);

        if (tab == TAB_ANIMATION) {
            int iy = py + 180;
            fontRendererObj.drawStringWithShadow("Speed: " + String.format("%.2f", animSpeed) + "x", px + 8, iy, 0xFFFFFF);
            if (selectedId != null)
                fontRendererObj.drawStringWithShadow("Target: " + selectedId.toString().substring(0, 8), px + 8, iy + 11, 0xAAFFAA);
            else
                fontRendererObj.drawStringWithShadow("Spawn a mob first!", px + 8, iy + 11, 0xFF5555);
            if (AnimationManager.isRecording)
                fontRendererObj.drawStringWithShadow("§c* RECORDING", px + 8, iy + 22, 0xFF0000);
        }
        super.drawScreen(mx, my, pt);
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
