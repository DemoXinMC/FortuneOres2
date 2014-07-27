package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;

public class Ore
{
    public String name;
    public int meta;
    public boolean enabled;
    public int dropCount;

    public float xpSmelt;
    public int xpDropMin;
    public int xpDropMax;

    public boolean oreDicted;
    
    public ArrayList<String> oreNames;
    public ArrayList<String> ingotNames;

    public Ore(String oreName, int oreMeta)
    {
        name = oreName;
        meta = oreMeta;

        xpSmelt = 0;
        xpDropMin = 0;
        xpDropMax = 0;

        oreNames = new ArrayList<String>();
        ingotNames = new ArrayList<String>();
    }
    
    public Ore(String oreName, int oreMeta, int droppedXPMin, int droppedXPMax, float xpSmelt)
    {
        this(oreName, oreMeta);
        this.xpSmelt = xpSmelt;
        xpDropMin = droppedXPMin;
        xpDropMax = droppedXPMax;
    }

    public void addOreName(String name)
    {
        oreNames.add("ore" + name);
        oreNames.add("oreNether" + name);
    }

    public void addIngotName(String name)
    {
        ingotNames.add("ingot" + name);
    }
}
