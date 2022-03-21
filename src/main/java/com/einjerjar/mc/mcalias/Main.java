package com.einjerjar.mc.mcalias;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main implements ModInitializer {
    public static final String MOD_ID = "mc-alias";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static MCAliasConfig cfg;
    public static ConfigHolder<MCAliasConfig> cfgHolder;
    public static MinecraftServer server;
    public static long startTime;

    @Override
    public void onInitialize() {
        LOGGER.info("Hello Fabric world!");
        AutoConfig.register(MCAliasConfig.class, Toml4jConfigSerializer::new);
        cfgHolder = AutoConfig.getConfigHolder(MCAliasConfig.class);
        cfg = cfgHolder.getConfig();

        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            Main.server = server;
            startTime = System.currentTimeMillis();
        });

        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            cfg.mappings.forEach((s, s2) -> {
                dispatcher.getRoot().addChild(
                        CommandManager.literal(s)
                                .then(CommandManager.argument("args", MessageArgumentType.message())
                                        .executes(context -> {
                                            try {
                                                String args = MessageArgumentType.getMessage(context, "args").getString();
                                                return server.getCommandManager().execute(context.getSource(), s2 + " " + args);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            return 0;
                                        }))
                                .executes(context -> {
                                            try {
                                                return server.getCommandManager().execute(context.getSource(), s2);
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }
                                            return 0;
                                        }
                                ).build()
                );
            });
        });
    }
}
