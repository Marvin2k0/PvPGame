package de.marvinleiers.pvpgame.menus;

import de.marvinleiers.gameapi.game.Game;
import de.marvinleiers.menuapi.Menu;
import de.marvinleiers.menuapi.MenuUserInformation;
import de.marvinleiers.pvpgame.Challenge;
import de.marvinleiers.pvpgame.PvPGame;
import de.marvinleiers.pvpgame.commands.ChallengeCommand;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ChallengeSettingsMenu extends Menu
{
    private Player target;

    public ChallengeSettingsMenu(Player target, MenuUserInformation menuUserInformation)
    {
        super(menuUserInformation);

        this.target = target;
    }

    @Override
    public String getTitle()
    {
        return "Mit eigenen Items kämpfen";
    }

    @Override
    public int getSlots()
    {
        return 9;
    }

    @Override
    public void setItems()
    {
        inventory.setItem(3, makeItem(Material.EMERALD_BLOCK, "§aJa"));
        inventory.setItem(5, makeItem(Material.REDSTONE_BLOCK, "§cNein"));
    }

    @Override
    public void handleClickActions(InventoryClickEvent inventoryClickEvent)
    {
        ItemStack item = inventoryClickEvent.getCurrentItem();

        if (item == null)
            return;

        boolean keepItems = (item.getType() == Material.EMERALD_BLOCK);

        Challenge challenge = new Challenge(player, target, keepItems);

        ChallengeCommand.challenges.put(target, challenge);
        player.closeInventory();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (challenge.isAccepted())
                {
                    this.cancel();
                    return;
                }

                player.sendMessage("§cDeine Challenge wurde nicht angenommen.");
                ChallengeCommand.challenges.remove(target);
            }
        }.runTaskLater(PvPGame.getPlugin(), 30 * 20);
    }
}
