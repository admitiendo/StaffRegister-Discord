package dev.admitiendo.stafflogs.listener;

import dev.admitiendo.stafflogs.DiscordWebhook;
import dev.admitiendo.stafflogs.StaffLog;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class JoinAndQuitListener implements Listener {



    @EventHandler
    public void join(PlayerJoinEvent event) throws IOException {
        Player p = event.getPlayer();
        if (!p.hasPermission("stafflogs.register")) return;
        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1273624899684139009/EUUZRyAcZJYhqctnemT_Gork-A0081xRm4y3FqEWUIePU9HqNWSoIHZIuupB9Mi5Yq_6");

        webhook.setContent(
                StaffLog.get().hora() + " **|** `" +
                        p.getDisplayName() +
                        "` se ha conectado al servidor " + StaffLog.get().servidorInfo());
        webhook.execute();
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) throws IOException {
        Player p = event.getPlayer();
        if (!p.hasPermission("stafflogs.register")) return;

        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1273624899684139009/EUUZRyAcZJYhqctnemT_Gork-A0081xRm4y3FqEWUIePU9HqNWSoIHZIuupB9Mi5Yq_6");

        webhook.setContent(
                StaffLog.get().hora() + " **|** `" +
                        p.getDisplayName() +
                        "` se ha desconectado del servidor " + StaffLog.get().servidorInfo());
        webhook.execute();
    }
}
