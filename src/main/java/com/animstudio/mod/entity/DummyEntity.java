package com.animstudio.mod.entity;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class DummyEntity extends EntityLiving {

    public String skinOwner = "";

    public DummyEntity(World world) {
        super(world);
        this.setSize(0.6F, 1.8F);
        this.isImmuneToFire = true;
    }

    public DummyEntity(World world, EntityPlayer source) {
        this(world);
        this.setPosition(source.posX + 1.5, source.posY, source.posZ);
        this.rotationYaw = source.rotationYaw;
        this.skinOwner = source.getGameProfile().getName();
    }

    public DummyEntity(World world, String skinName) {
        this(world);
        this.skinOwner = skinName;
    }

    @Override
    protected void entityInit() {
        super.entityInit();
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tag) {
        super.writeEntityToNBT(tag);
        tag.setString("skinOwner", skinOwner);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tag) {
        super.readEntityFromNBT(tag);
        skinOwner = tag.getString("skinOwner");
    }

    @Override protected String getLivingSound() { return null; }
    @Override protected String getHurtSound() { return null; }
    @Override protected String getDeathSound() { return null; }
    @Override protected float getSoundVolume() { return 0F; }
}
