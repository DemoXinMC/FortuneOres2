package com.demoxin.minecraft.fortuneores;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CTabChunks extends CreativeTabs
{
    private ItemStack tabLabel;
    
	public CTabChunks(int fID, String fName) { super(fID, fName); }
	
	@Override
	public ItemStack getIconItemStack()
	{
	    if(tabLabel == null)
	    {
	        tabLabel = new ItemStack(FortuneOres.itemChunk, 0);
	    }
		return tabLabel;
	}

    @Override
    public Item getTabIconItem()
    {
        return FortuneOres.itemChunk;
    }
}
