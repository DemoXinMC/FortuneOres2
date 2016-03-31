package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;

public class Ore
{
    public String name;
    public int meta;
    public boolean enabled;
    public int dropCount;

    public float xpSmelt;
    public int xpMin;
    public int xpMax;

    public boolean oreDicted;
    
    public ArrayList<String> oreNames;
    public ArrayList<String> ingotNames;

    public Ore(String oreName, int oreMeta)
    {
        name = oreName;
        meta = oreMeta;
        dropCount = 1;

        xpSmelt = 0;
        xpMin = 0;
        xpMax = 0;

        oreNames = new ArrayList<String>();
        ingotNames = new ArrayList<String>();
    }
    
    public Ore(String oreName, int oreMeta, int droppedXPMin, int droppedXPMax, float xpSmelt)
    {
        this(oreName, oreMeta);
        this.xpSmelt = xpSmelt;
        xpMin = droppedXPMin;
        xpMax = droppedXPMax;
        dropCount = 1;
    }
    
    public Ore addNames(String name)
    {
        this.addOreName(name);
        this.addIngotName(name);
        return this;
    }

    public Ore addOreName(String name)
    {
        oreNames.add("ore" + name);
        return this;
    }

    public Ore addIngotName(String name)
    {
        ingotNames.add("ingot" + name);
        return this;
    }
}
