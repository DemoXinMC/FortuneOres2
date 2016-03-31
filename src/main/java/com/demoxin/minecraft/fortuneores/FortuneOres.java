package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = FortuneOres.MODID, name = FortuneOres.NAME, version = FortuneOres.VERSION)
public class FortuneOres {
    public static final String MODID = "FortuneOres";
    public static final String NAME = "FortuneOres";
    public static final String VERSION = "3.0.0";

    // Singleton
    @Instance("FortuneOres")
    public static FortuneOres instance;
    public static Config config;
    
    @SidedProxy(clientSide="com.demoxin.minecraft.fortuneores.ProxyClient", serverSide="com.demoxin.minecraft.fortuneores.ProxyCommon")
    public static ProxyCommon proxy;
    
    public static ArrayList<Ore> ores;

    public static CreativeTabs creativeTab;
    public static Item itemChunk;

    private int nextMeta;

    public FortuneOres()
    {
        setupOres();
    }
    
    static
    {
        if(instance == null)
            instance = new FortuneOres();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        config = new Config(new Configuration(event.getSuggestedConfigurationFile()));
        creativeTab = new CTabChunks(CreativeTabs.getNextID(), "OreChunks");
        itemChunk = new ItemChunk();
        GameRegistry.registerItem(itemChunk, "oreChunk");
        proxy.preInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        OreDictionaryMagic.addOreDicting();
        MinecraftForge.EVENT_BUS.register(new OreSwapper());
        MinecraftForge.EVENT_BUS.register(new OreDictionaryMagic());
        proxy.init();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
        OreDictionaryMagic.addSmelting();
        proxy.postInit();
    }

    public Ore addOre(String oreName)
    {
        return this.addOre(oreName, 0, 0, 0);
    }
    
    public Ore addOre(String oreName, int xpMin, int xpMax, float xpSmelt)
    {
        Ore newOre = new Ore(oreName, nextMeta, xpMin, xpMax, xpSmelt);
        newOre.addOreName(oreName);
        newOre.addIngotName(oreName);
        ores.add(newOre);
        nextMeta++;
        return newOre;
    }

    private void setupOres()
    {
        if(ores != null)
            return;
        
        nextMeta = 0;
        ores = new ArrayList<Ore>();
        
        // Vanilla
        addOre("Iron", 1, 5, 0.4f);
        addOre("Gold", 2, 6, 1.0f);

        // Common Ores
        addOre("Copper", 1, 5, 0.4f);
        addOre("Tin", 1, 5, 0.4f);
        addOre("Lead", 1, 5, 0.4f);
        addOre("Silver", 2, 6, 1.0f);

        // Less Common Ores
        addOre("Nickel", 1, 5, 0.4f);
        addOre("Platinum", 2, 6, 1.0f);
        addOre("Aluminum", 1, 5, 0.4f).addNames("Aluminium").addNames("NaturalAluminum");

        // TiCon Ores
        addOre("Cobalt", 4, 7, 0.8f);
        addOre("Ardite", 3, 6, 0.8f);

        // Metallurgy Base
        addOre("Manganese", 1, 5, 0.4f);
        
        // Metallurgy Precious
        addOre("Zinc", 2, 6, 1.0f);
        
        // Metallurgy Nether
        addOre("Ignatius", 3, 7, 0.8f);
        addOre("ShadowIron", 3, 7, 0.8f);
        addOre("Lemurite", 3, 7, 0.8f);
        addOre("Midasium", 3, 7, 0.8f);
        addOre("Vyroxeres", 3, 7, 0.8f);
        addOre("Ceruclase", 3, 7, 0.8f);
        addOre("Alduorite", 3, 7, 0.8f);
        addOre("Kalendrite", 3, 7, 0.8f);
        addOre("Vulcanite", 3, 7, 0.8f);
        addOre("Sanguinite", 3, 7, 0.8f);
        
        // Metallurgy Fantasy
        addOre("Prometheum", 3, 7, 0.9f);
        addOre("DeepIron", 3, 7, 0.9f);
        addOre("Infuscolium", 3, 7, 0.9f);
        addOre("Oureclase", 3, 7, 0.9f);
        addOre("AstralSilver", 3, 7, 0.9f);
        addOre("Carmot", 3, 7, 0.9f);
        addOre("Mithril", 3, 7, 0.9f).addNames("Mythril");
        addOre("Rubracium", 3, 7, 0.9f);
        addOre("Orichalcum", 3, 7, 0.9f);
        addOre("Adamantine", 3, 7, 0.9f);
        addOre("Atlarus", 3, 7, 0.9f);

        // Metallurgy End
        addOre("Eximite", 3, 7, 0.8f);
        addOre("Meutoite", 3, 7, 0.8f);

        // Factorization
        addOre("DarkIron", 1, 5, 0.4f);
        
        // Radioactive
        addOre("Uranium", 1, 5, 0.4f);
        addOre("Yellorium", 1, 5, 0.4f).addNames("Yellorite");
        addOre("Osmium", 1, 5, 0.4f);
    }
}
