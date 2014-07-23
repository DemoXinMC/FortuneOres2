package com.demoxin.minecraft.fortuneores;

import java.util.ArrayList;

public class Ore
{
    public String name;
    public int meta;
    public boolean enabled;
    public int dropCount;

    public float xpSmelt;
    public float xpDrop;

    public ArrayList<String> oreNames;
    public ArrayList<String> ingotNames;

    public Ore(String oreName, int oreMeta)
    {
        name = oreName;
        meta = oreMeta;

        xpSmelt = 0;
        xpDrop = 0;

        oreNames = new ArrayList<String>();
        ingotNames = new ArrayList<String>();
    }

    public void addOreName(String name)
    {
        oreNames.add("ore" + name);
        oreNames.add("oreNether" + name);
        oreNames.add("denseore" + name);
    }

    public void addIngotName(String name)
    {
        ingotNames.add("ingot" + name);
    }
}
