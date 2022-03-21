package com.einjerjar.mc.mcalias;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import java.util.Hashtable;

@Config(name = Main.MOD_ID)
public class MCAliasConfig implements ConfigData {
    public Hashtable<String, String> mappings = new Hashtable<>(){{
        put("wasd", "tp");
    }};
}
