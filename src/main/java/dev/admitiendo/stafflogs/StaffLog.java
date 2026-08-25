package dev.admitiendo.stafflogs;

import dev.admitiendo.stafflogs.listener.JoinAndQuitListener;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.event.log.LogBroadcastEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public final class StaffLog extends JavaPlugin implements Listener {
    public LuckPerms lpApi;
    public String hora() {
        return "`" +LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) + "`";
    }

    public String servidorInfo() throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream("server.properties"));

        return "`" + props.getProperty("server-name") + "`";
    }


    @Override
    public void onEnable() {
        DiscordWebhook webhookLPLogs =
                new DiscordWebhook("https://discord.com/api/webhooks/1274360172579393566/XBoDOIxwqcj_FkluYIrg9ecEFi8vjzHWUwztpzT_Oi45eu-HQPLHI2yWv8-m7O01n2mN");

        DiscordWebhook webhook =
                new DiscordWebhook("https://discord.com/api/webhooks/1273624899684139009/EUUZRyAcZJYhqctnemT_Gork-A0081xRm4y3FqEWUIePU9HqNWSoIHZIuupB9Mi5Yq_6");

        try {
            webhookLPLogs.setContent(hora() + " **|** LuckPerms Logs | Empezando a monitorear acciones en " + servidorInfo());
            webhook.setContent(hora() + " **|** Plugin de registros de Staff encendido en el servidor " + servidorInfo());

            webhookLPLogs.execute();
            webhook.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) {
            lpApi = provider.getProvider();
        }

        subscribeToEventBus();
        Bukkit.getPluginManager().registerEvents(new JoinAndQuitListener(), this);
    }

    public void subscribeToEventBus() {
        lpApi.getEventBus().subscribe(LogBroadcastEvent.class, this::onCommand);
    }

    public void onCommand(LogBroadcastEvent event){
        String performer = event.getEntry().getSource().getName();
        String target = event.getEntry().getTarget().getName();
        String description = event.getEntry().getDescription();
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();

        try {
            embedObject.setTitle("MagicMC @ `" + servidorInfo() + "` | LuckPerms Logs");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        embedObject.addField("**Hora**", hora(), false);
        embedObject.addField("**Comando**", description, false);
        embedObject.addField("**Afectado (Usuario / Rango)**", target, false);
        embedObject.addField("**Usuario**", performer, false);
        embedObject.setColor(Color.RED);

        DiscordWebhook webhookLPLogs =
                new DiscordWebhook("https://discord.com/api/webhooks/1274360172579393566/XBoDOIxwqcj_FkluYIrg9ecEFi8vjzHWUwztpzT_Oi45eu-HQPLHI2yWv8-m7O01n2mN");

        webhookLPLogs.addEmbed(embedObject);
        try {
            webhookLPLogs.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static StaffLog get() {
        return getPlugin(StaffLog.class);
    }

    public LuckPerms getLpApi() {
        return lpApi;
    }

    @Override
    public void onDisable() {
        DiscordWebhook webhook =
                new DiscordWebhook("https://discord.com/api/webhooks/1273624899684139009/EUUZRyAcZJYhqctnemT_Gork-A0081xRm4y3FqEWUIePU9HqNWSoIHZIuupB9Mi5Yq_6");

        DiscordWebhook webhookLPLogs =
                new DiscordWebhook("https://discord.com/api/webhooks/1274360172579393566/XBoDOIxwqcj_FkluYIrg9ecEFi8vjzHWUwztpzT_Oi45eu-HQPLHI2yWv8-m7O01n2mN");

        try {
            webhookLPLogs.setContent(hora() + " **|** LuckPerms Logs | Dejando de monitorear acciones en " + servidorInfo());
            webhook.setContent(hora() + " **|** Plugin de registros de Staff apagado en el servidor " + servidorInfo());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {

            webhookLPLogs.execute();
            webhook.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
