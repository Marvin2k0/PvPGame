package de.marvinleiers.pvpgame.listeners;

import de.marvinleiers.gameapi.GameAPI;
import de.marvinleiers.gameapi.events.GameResetEvent;
import de.marvinleiers.gameapi.events.GameStartEvent;
import de.marvinleiers.gameapi.events.PlayerGameJoinEvent;
import de.marvinleiers.gameapi.game.Game;
import de.marvinleiers.gameapi.game.GamePlayer;
import de.marvinleiers.gameapi.utils.CountdownTimer;
import de.marvinleiers.pvpgame.Challenge;
import de.marvinleiers.pvpgame.PvPGame;
import de.marvinleiers.pvpgame.commands.ChallengeCommand;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class GameListener implements Listener
{
    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();

        if (!GameAPI.inGame(player))
            return;

        for (ItemStack item : player.getInventory().getContents())
        {
            if (item == null)
                continue;

            player.getLocation().getWorld().dropItem(player.getLocation(), item);
        }

        Game game = PvPGame.getPlugin().getGameApi().gameplayers.get(player).getGame();

        game.leave(player);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);

        new CountdownTimer(PvPGame.getPlugin(), 10,
                () -> {
                },
                () -> game.stop(),
                (t) -> {
                    if (t.getSecondsLeft() % 5 == 0)
                    {
                        game.sendMessage("§7Spiel startet in §6" + t.getSecondsLeft() + " Sekunden §7neu!");
                    }
                }).scheduleTimer();
    }
    @EventHandler
    public void onRestart(GameResetEvent event)
    {
        if (event.getPlugin() != PvPGame.getPlugin())
            return;

        Game game = event.getGame();

        for (Player player : game.players)
        {
            GamePlayer gp = PvPGame.getPlugin().getGameApi().gameplayers.get(player);

            gp.setInventoryContents(player.getInventory().getContents());
            gp.setArmorContents(player.getInventory().getArmorContents());
        }
    }

    @EventHandler
    public void onStart(GameStartEvent event)
    {
        if (event.getPlugin() != PvPGame.getPlugin())
            return;

        Game game = event.getGame();

        Challenge challenge = null;

        for (Player player : game.players)
        {
            if (ChallengeCommand.challenges.containsKey(player))
            {
                challenge = ChallengeCommand.challenges.get(player);
                break;
            }
        }

        if (challenge == null)
            return;

        if (challenge.ownItems())
        {
            for (Player p : game.players)
            {
                GamePlayer gp = GameAPI.gameplayers.get(p);

                p.getInventory().clear();
                p.getInventory().setArmorContents(gp.getArmorContents());
                p.getInventory().setContents(gp.getInventoryContents());
                p.updateInventory();
            }
        }

        ChallengeCommand.challenges.remove(challenge.getChallenged());
    }

    @EventHandler
    public void onJoin(PlayerGameJoinEvent event)
    {
        if (event.getPlugin() != PvPGame.getPlugin())
            return;

        Player player = event.getPlayer();

        if (!GameAPI.inGame(player))
            return;

        Challenge challenge = null;

        for (Challenge c : ChallengeCommand.challenges.values())
        {
            if (c.getChallenger() == player || c.getChallenged() == player)
            {
                challenge = c;
                break;
            }
        }

        if (challenge == null)
            return;

        if (challenge.ownItems())
        {
            GamePlayer gp = GameAPI.gameplayers.get(player);

            player.getInventory().clear();
            player.getInventory().setArmorContents(gp.getArmorContents());
            player.getInventory().setContents(gp.getInventoryContents());
            player.updateInventory();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event)
    {
        Player player = event.getEntity();

        if (!GameAPI.inGame(player))
            return;

        Game game = PvPGame.getPlugin().getGameApi().getGame(player);
        GamePlayer gp = PvPGame.getPlugin().getGameApi().gameplayers.get(player);
        gp.setArmorContents(null);
        gp.setInventoryContents(null);

        event.setDroppedExp(0);

        Bukkit.getScheduler().scheduleSyncDelayedTask(PvPGame.getPlugin(), () ->
        {
            player.spigot().respawn();
            player.setHealth(player.getHealthScale());
            game.leave(player);
        }, 2);

        new CountdownTimer(PvPGame.getPlugin(), 10,
                () -> {
                },
                () -> game.stop(),
                (t) -> {
                    if (t.getSecondsLeft() % 5 == 0)
                    {
                        game.sendMessage("§7Spiel startet in §6" + t.getSecondsLeft() + " Sekunden §7neu!");
                    }
                }).scheduleTimer();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event)
    {
        if (!(event.getEntity() instanceof Player))
        {
            return;
        }

        Player player = (Player) event.getEntity();

        if (PvPGame.getPlugin().getGameApi().inGame(player) && PvPGame.getPlugin().getGameApi().gameplayers.get(player).getGame().inLobby())
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();

        if (PvPGame.getPlugin().getGameApi().inGame(player) && PvPGame.getPlugin().getGameApi().gameplayers.get(player).getGame().inLobby())
            event.setCancelled(true);
    }

    @EventHandler
    public void onGameStart(GameStartEvent event)
    {
        if (event.getPlugin() != PvPGame.getPlugin())
            return;

        Game game = event.getGame();

        game.players.get(0).teleport(PvPGame.getPlugin().getGameApi().gamesConfig.getLocation("games." + game.getName() + ".spawn-1"));
        game.players.get(1).teleport(PvPGame.getPlugin().getGameApi().gamesConfig.getLocation("games." + game.getName() + ".spawn-2"));
    }
}
