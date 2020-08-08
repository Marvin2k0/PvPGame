package de.marvinleiers.pvpgame.commands;

import de.marvinleiers.menuapi.MenuAPI;
import de.marvinleiers.pvpgame.Challenge;
import de.marvinleiers.pvpgame.PvPGame;
import de.marvinleiers.pvpgame.menus.ChallengeSettingsMenu;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class ChallengeCommand implements CommandExecutor
{
    public static HashMap<Player, Challenge> challenges = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("§cNur für Spieler!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length < 1)
        {
            player.sendMessage("§cUsage: /challenge <player>");
            return true;
        }

        if (args[0].equalsIgnoreCase("accept"))
        {
            if (!challenges.containsKey(player))
            {
                player.sendMessage("§eDir liegt keine Challenge vor!");
                return true;
            }

            Challenge challenge = challenges.get(player);
            challenge.accept();
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null)
        {
            player.sendMessage("§e" + args[0] + " ist nicht online!");
            return true;
        }

        if (PvPGame.getPlugin().getGameApi().inGame(player))
        {
            player.sendMessage("§eDu bist bereits in einem Spiel!");
            return true;
        }

        new ChallengeSettingsMenu(target, MenuAPI.getMenuUserInformation(player)).open();

        return true;
    }
}
