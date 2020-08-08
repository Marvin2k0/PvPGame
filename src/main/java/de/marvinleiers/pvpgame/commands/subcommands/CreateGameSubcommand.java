package de.marvinleiers.pvpgame.commands.subcommands;

import de.marvinleiers.gameapi.game.Game;
import de.marvinleiers.pvpgame.PvPGame;
import de.marvinleiers.pvpgame.commands.Subcommand;
import org.bukkit.entity.Player;

public class CreateGameSubcommand extends Subcommand
{
    @Override
    public String getName()
    {
        return "create";
    }

    @Override
    public String getDescription()
    {
        return "Erstellt eine neue PvP-Arena";
    }

    @Override
    public String getSyntax()
    {
        return "/pvp create <name>";
    }

    @Override
    public void execute(Player player, String[] args)
    {
        if (args.length < 2)
        {
            player.sendMessage("§cUsage: " + getSyntax());
            return;
        }

        String name = args[1];
        PvPGame.getPlugin().getGameApi().getGame(name);

        player.sendMessage("§7Spiel §3" + name + " §7wurde erstellt!");
    }
}
