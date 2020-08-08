package de.marvinleiers.pvpgame.commands;

import de.marvinleiers.pvpgame.commands.subcommands.CreateGameSubcommand;
import de.marvinleiers.pvpgame.commands.subcommands.EditSubcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class PvPCommand implements CommandExecutor
{
    private ArrayList<Subcommand> subcommands = new ArrayList<>();

    public PvPCommand()
    {
        subcommands.add(new CreateGameSubcommand());
        subcommands.add(new EditSubcommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            sender.sendMessage("§cNur für Spieler!");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0)
        {
            for (Subcommand subcommand : subcommands)
                player.sendMessage("§c" + subcommand.getSyntax() + " §7- §e" + subcommand.getDescription());

            return true;
        }

        for (Subcommand subcommand : subcommands)
        {
            if (args[0].equalsIgnoreCase(subcommand.getName()))
            {
                subcommand.execute(player, args);
                return true;
            }
        }

        for (Subcommand subcommand : subcommands)
            player.sendMessage("§c" + subcommand.getSyntax() + " §7- §e" + subcommand.getDescription());

        return true;
    }
}
