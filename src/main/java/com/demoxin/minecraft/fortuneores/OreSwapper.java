package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.oredict.OreDictionary;

public class OreSwapper
{
    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void SwapOres(HarvestDropsEvent event)
    {
        if(event.isSilkTouching())
            return;
        
        if(event.getDrops().isEmpty())
            return;

        if(event.getState().getBlock().hasTileEntity(event.getState()))
        {
            TileEntity te = event.getWorld().getTileEntity(event.getPos());
            if(te instanceof ISidedInventory) return;
            if(te instanceof IInventory) return;
            IItemHandler handler = null;
            handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            for(EnumFacing side : EnumFacing.VALUES)
            {
                if(handler != null)
                    return;
                handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
            }
            if(handler != null) return;
        }

        ArrayList<ItemStack> newDrops = new ArrayList<ItemStack>();
        int xp = 0;
        boolean modifiedDrops = false;
        
        for(int i = 0; i < event.getDrops().size(); ++i)
        {
            if(event.getDrops().get(i) == null)
                continue;
            
            ItemStack currentDrop = event.getDrops().get(i);
            
            if(currentDrop.getItem() == null)
                continue;
            
            if(event.getDropChance() < 1.0f)
            {
                if(event.getWorld().rand.nextFloat() > event.getDropChance())
                    continue;
            }
            
            boolean dropHandled = false;
            for(ItemStack stack : OreDictionaryMagic.preservedOreDicting.keySet())
            {
                if(OreDictionary.itemMatches(stack, currentDrop, true))
                {
                    Ore ore = OreDictionaryMagic.preservedOreDicting.get(stack);
                    int randomCount = this.randomCount(ore.dropCount * currentDrop.stackSize, event.getFortuneLevel(), event.getWorld());
                    for(int j = 0; j < randomCount; ++j)
                    {
                        newDrops.add(new ItemStack(FortuneOres.itemChunk, 1, ore.meta));
                    }
                    xp += event.getWorld().rand.nextInt(ore.xpMax - ore.xpMin) + ore.xpMin;
                    dropHandled = true;
                    modifiedDrops = true;
                    break;
                }
            }
            
            if(!dropHandled)
                newDrops.add(currentDrop);
        }

        if(!modifiedDrops)
            return;
        
        event.getDrops().clear();
        event.getDrops().addAll(newDrops);
        event.setDropChance(1.0f);

        if(xp <= 0)
            return;
        
        while(xp > 0)
        {
            int i = EntityXPOrb.getXPSplit(xp);
            xp -= i;
            event.getWorld().spawnEntityInWorld(new EntityXPOrb(event.getWorld(), (double)event.getPos().getX() + 0.5D, (double)event.getPos().getY() + 0.5D, (double)event.getPos().getZ() + 0.5D, i));
        }
    }

    private int randomCount(int baseCount, int fortuneLevel, World world)
    {
        if(fortuneLevel <= 0)
            return baseCount;
        
        int j = world.rand.nextInt(fortuneLevel + 2) - 1;

        if (j < 0)
            j = 0;

        return baseCount * (j + 1);
    }
}
