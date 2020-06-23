package us.howtozombie.misc;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

public class NightVisionCommand extends Command {

    public HashMap<Player, Boolean> nv = new HashMap<>();

    public NightVisionCommand() {
        super("nv");
    }

    @Override
    public boolean execute(CommandSender sender, String ss, String[] args) {
        if (sender instanceof Player) {
            if (!nv.containsKey(sender)) {
                nv.put((Player) sender, true);
                PotionEffect p = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false);
                ((Player) sender).addPotionEffect(p);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6Night vision enabled."));
            } else if (nv.get(sender)) {
                nv.put((Player) sender, false);
                ((Player) sender).removePotionEffect(PotionEffectType.NIGHT_VISION);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6Night vision disabled."));
            } else if (!nv.get(sender)) {
                nv.put((Player) sender, true);
                PotionEffect p = new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, true, false);
                ((Player) sender).addPotionEffect(p);
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        "&6Night vision enabled."));
            }
        }

        return true;
    }
}
