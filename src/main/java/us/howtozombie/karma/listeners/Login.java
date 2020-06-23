package us.howtozombie.karma.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import us.howtozombie.Main;
import us.howtozombie.karma.user.User;
import us.howtozombie.tickets.util.Request;

public class Login implements Listener {

    @EventHandler
    public void onJoin(PlayerLoginEvent e) {

        Player p = e.getPlayer();

        User u = new User(p);

        int openReqs = 0;

        if (u.getArtRank() > 1) {
            for (Request r : Request.artRequests.values()) {
                if (r.getStage().equals(Request.RequestStage.UNCLAIMED) || r.isGroup() && r.getStage().equals(Request.RequestStage.REVIEW)) {
                    openReqs++;
                }
            }
        }
        if (u.getBuildRank() > 1) {
            for (Request r : Request.buildRequests.values()) {
                if (r.getStage().equals(Request.RequestStage.UNCLAIMED)) {
                    openReqs++;
                }
            }
        }
        if (u.getCmdRank() > 1) {
            for (Request r : Request.cmdRequests.values()) {
                if (r.getStage().equals(Request.RequestStage.UNCLAIMED)) {
                    openReqs++;
                }
            }
        }
        if (u.getDevRank() > 1) {
            for (Request r : Request.devRequests.values()) {
                if (r.getStage().equals(Request.RequestStage.UNCLAIMED)) {
                    openReqs++;
                }
            }
        }
        if (u.getGMRank() > 1) {
            for (Request r : Request.gmRequests.values()) {
                if (r.getStage().equals(Request.RequestStage.UNCLAIMED)) {
                    openReqs++;
                }
            }
        }
        if (u.getAdminRank() > 0) {
            for (Request r : Request.managerRequests.values()) {
                if (r.getStage().equals(Request.RequestStage.UNCLAIMED)) {
                    openReqs++;
                }
            }
        }

        if (openReqs > 0) {

            final int reqs = openReqs;

            int id = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                @Override
                public void run() {

                    e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&2There are &a" + reqs + " &2new requests!"));

                }
            }, 5L);
        }

        if (u.getAdminRank() > 0) {
            int review = Request.reviewRequests.size();
            if (review > 0) {
                int id = Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), new Runnable() {
                    @Override
                    public void run() {

                        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&',
                                "&6There are &e" + review + " &6requests up for review!"));

                    }
                }, 5L);
            }
        }

    }

}
