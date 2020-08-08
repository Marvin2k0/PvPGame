package de.marvinleiers.pvpgame.commands;

import org.bukkit.entity.Player;

public abstract class Subcommand
{
    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void execute(Player player, String[] args);
}
