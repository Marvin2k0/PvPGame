package de.marvinleiers.pvpgame.commands.subcommands;

import de.marvinleiers.gameapi.game.Game;
import de.marvinleiers.menuapi.MenuAPI;
import de.marvinleiers.pvpgame.PvPGame;
import de.marvinleiers.pvpgame.commands.Subcommand;
import de.marvinleiers.pvpgame.menus.EditArenaMenu;
import org.bukkit.entity.Player;

public class EditSubcommand extends Subcommand
{
    @Override
    public String getName()
    {
        return "edit";
    }

    @Override
    public String getDescription()
    {
        return "Befehl um existierende Arenen zu bearbeiten.";
    }

    @Override
    public String getSyntax()
    {
        return "/pvp edit <name>";
    }

    @Override
    public void execute(Player player, String[] args)
    {
        if (args.length < 2)
        {
            player.sendMessage("§cUsage: " + getSyntax());
            return;
        }

        if (!PvPGame.getPlugin().getGameApi().exists(args[1]))
        {
            player.sendMessage("§eDieses Spiel existiert nicht!");
            return;
        }

        Game game = PvPGame.getPlugin().getGameApi().getGame(args[1]);

        new EditArenaMenu(MenuAPI.getMenuUserInformation(player), game).open();

        //TODO: Open GUI with options
        /* == OPTIONS ==
        set spawns
         */
    }
}
