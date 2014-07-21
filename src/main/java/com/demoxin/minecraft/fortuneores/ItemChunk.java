package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ItemChunk extends Item
{
    private ArrayList<RenderStorage> renderStorage;
    private IIcon mysterious;

    public ItemChunk()
    {
        super();
        setMaxStackSize(64);
        setCreativeTab(FortuneOres.creativeTab);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public IIcon getIconFromDamage(int meta)
    {
        if(meta > renderStorage.size()
                || renderStorage.get(meta) == null
                || renderStorage.get(meta).unlocalizedName == null
                || renderStorage.get(meta).unlocalizedName.isEmpty())
        {
            return mysterious;
        }
        return renderStorage.get(meta).icon;

    }

    @Override
    public void registerIcons(IIconRegister iconRegister)
    {
        mysterious = iconRegister.registerIcon(FortuneOres.MODID + ":mysteriouschunk");
        renderStorage = new ArrayList<RenderStorage>();
        for (int i = 0; i < FortuneOres.nextMeta; i++)
        {
            renderStorage.add(i, new RenderStorage());
            renderStorage.get(i).icon = iconRegister.registerIcon(FortuneOres.MODID + ":" + FortuneOres.oreStorage.get(i).name.toLowerCase() + "chunk");
            renderStorage.get(i).unlocalizedName = "item.orechunks." + FortuneOres.oreStorage.get(i).name.toLowerCase();

            if(!FortuneOres.oreStorage.get(i).enabled)
                renderStorage.get(i).unlocalizedName = "item.orechunks.mysterious";
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemStack)
    {
        int meta = itemStack.getItemDamage();

        if(meta > renderStorage.size()
                || renderStorage.get(meta) == null
                || renderStorage.get(meta).unlocalizedName == null
                || renderStorage.get(meta).unlocalizedName.isEmpty())
        {
            return "item.orechunks.mysterious";
        }

        return renderStorage.get(meta).unlocalizedName;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
        for(int i = 0; i < renderStorage.size(); ++i)
        {
            ItemStack oreChunk = new ItemStack(this, 1, i);
            list.add(oreChunk);
        }
    }

    protected class RenderStorage
    {
        IIcon icon;
        String unlocalizedName;
    }
}
