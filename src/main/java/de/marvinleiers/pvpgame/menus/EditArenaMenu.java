package de.marvinleiers.pvpgame.menus;

import de.marvinleiers.gameapi.game.Game;
import de.marvinleiers.menuapi.Menu;
import de.marvinleiers.menuapi.MenuUserInformation;
import de.marvinleiers.pvpgame.PvPGame;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class EditArenaMenu extends Menu
{
    private Game game;

    public EditArenaMenu(MenuUserInformation menuUserInformation, Game game)
    {
        super(menuUserInformation);
        this.game = game;
    }

    @Override
    public String getTitle()
    {
        return "Edit";
    }

    @Override
    public int getSlots()
    {
        return 9;
    }

    @Override
    public void setItems()
    {
        inventory.setItem(0, makeItem(Material.GOLD_BLOCK, "§e§lLobby setzen", "§fHier werden die Spieler vor dem Spiel hinteleportiert"));
        inventory.setItem(1, makeItem(Material.GRASS_BLOCK, "§a§lSpawn 1", "§fSpawn für ersten Spieler setzen"));
        inventory.setItem(2, makeItem(Material.OAK_WOOD, "§a§lSpawn 2", "§fSpawn für zweiten Spieler setzen"));
    }

    @Override
    public void handleClickActions(InventoryClickEvent event)
    {
        ItemStack item = event.getCurrentItem();

        switch (item.getType())
        {
            case GOLD_BLOCK:
                game.setEntryPoint(player.getLocation());
                break;
            case GRASS_BLOCK:
                PvPGame.getPlugin().getGameApi().gamesConfig.setLocation("games." + game.getName() + ".spawn-1", player.getLocation());
                break;
            case OAK_WOOD:
                PvPGame.getPlugin().getGameApi().gamesConfig.setLocation("games." + game.getName() + ".spawn-2", player.getLocation());
                break;
        }

        player.closeInventory();
        player.sendMessage("§aErfolgreich gesetzt!");
    }
}
