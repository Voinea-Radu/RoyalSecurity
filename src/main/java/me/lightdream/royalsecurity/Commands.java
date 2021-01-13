package me.lightdream.royalsecurity;

import net.dv8tion.jda.api.requests.restaction.RoleAction;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.List;

public class Commands implements CommandExecutor {

    Royalsecurity plugin;

    public Commands(Royalsecurity plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        FileConfiguration config = Royalsecurity.getPlugin().getConfig();
        if(label.equalsIgnoreCase("gw")) {
            if(args.length == 0) {
                List<String> help = (List<String>) config.getList("help");
                for(String str : help)
                    sender.sendMessage(Royalsecurity.color(str));
            } else {
                if(args[0].equalsIgnoreCase("link")) {
                    if(sender instanceof Player) {
                        if(args.length == 2) {
                            Player p = (Player) sender;
                            if(Royalsecurity.pendingCodes.containsKey((args[1]))) {
                                if(Royalsecurity.pendingCodes.get(args[1]).containsKey(p.getName())) {
                                    Royalsecurity.addSecurity(p.getName(), Royalsecurity.pendingCodes.get(args[1]).get(p.getName()));
                                    sender.sendMessage(Royalsecurity.color(MessageFormat.format(config.getString("link-complete"), Royalsecurity.pendingCodes.get(args[1]).get(p.getName()).getName())));
                                    Royalsecurity.logedInPlayers.add(p.getName());
                                    Royalsecurity.setIp(p.getName(), p.getAddress().getHostName());
                                } else {
                                    sender.sendMessage(Royalsecurity.color(config.getString("invalid-code")));
                                }
                            } else {
                                sender.sendMessage(Royalsecurity.color(config.getString("invalid-code")));
                            }
                        } else {
                            sender.sendMessage(Royalsecurity.color(config.getString("invalid-code")));
                        }
                    } else {
                        sender.sendMessage(Royalsecurity.color(config.getString("not-player")));
                    }
                }
                else if(args[0].equalsIgnoreCase("help")) {
                    List<String> help = (List<String>) config.getList("help");
                    for(String str : help) {
                        sender.sendMessage(Royalsecurity.color(str));
                    }
                }
                else if(args[0].equalsIgnoreCase("add")) {
                }
            }
        }
        return true;
    }
}
