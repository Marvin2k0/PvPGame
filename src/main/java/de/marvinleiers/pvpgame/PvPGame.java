package de.marvinleiers.pvpgame;

import de.marvinleiers.customconfig.CustomConfig;
import de.marvinleiers.gameapi.GameAPI;
import de.marvinleiers.menuapi.MenuAPI;
import de.marvinleiers.pvpgame.commands.ChallengeCommand;
import de.marvinleiers.pvpgame.commands.PvPCommand;
import de.marvinleiers.pvpgame.listeners.GameListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class PvPGame extends JavaPlugin
{
    public GameAPI api;
    public CustomConfig config;

    @Override
    public void onEnable()
    {
        MenuAPI.setUp(this);
        api = GameAPI.getInstance(this);
        config = new CustomConfig(getDataFolder().getPath() + "/internal/arenas.yml");

        api.config.set("min-players", 2);
        api.config.set("max-players", 2);

        getServer().getPluginManager().registerEvents(new GameListener(), this);

        getCommand("pvp").setExecutor(new PvPCommand());
        getCommand("challenge").setExecutor(new ChallengeCommand());
    }

    public static PvPGame getPlugin()
    {
        return getPlugin(PvPGame.class);
    }

    public GameAPI getGameApi()
    {
        return api;
    }
}
