package me.lightdream.royalsecurity;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.awt.*;


public class DiscordListener extends ListenerAdapter {

    public static JDA instance;

    @Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        instance = event.getJDA();

        String[] message = event.getMessage().getContentRaw().split(" ");

        if(!event.getAuthor().isBot())
        {
            //commands
            if(String.valueOf(event.getMessage().getChannel().getIdLong()).equalsIgnoreCase(Royalsecurity.CommandsChannel))
            {
                if(message[0].equalsIgnoreCase("/help"))
                {
                    event.getChannel().sendMessage(createEmbed("Comenzi:\n/help - Afiseaza acest embed de ajutor\n" +
                            "/link [Nume Minecraft] - Iti legi contul de minecraft de cel de discord\n" +
                            "/unlink [Nume Minecraft/ @Mention] - Iti dezlegi contul de minecraft de cel de discord", 255, 255, 0).build()).queue();
                }
                if(message[0].equalsIgnoreCase("/link"))
                {
                    if(message.length == 1)
                    {
                        event.getChannel().sendMessage(createEmbed("Folosire: /link [Nume Minecraft]", 0, 255, 0).build()).queue();
                    }
                    else
                    {
                        if(!event.getAuthor().isBot())
                        {
                            if(!Royalsecurity.secured(message[1]))
                            {
                                event.getChannel().sendMessage(createEmbed("V-am trimis o comanda pe care trebuie sa o executati in joc pe privat", 0, 255, 0).build()).queue();
                                event.getAuthor().openPrivateChannel().queue((channel) ->
                                {
                                    channel.sendMessage(createEmbed("Foloseste commanda /gw link " + Royalsecurity.generateCode(message[1], event.getAuthor()), 0, 255, 0).build()).queue();
                                });
                            }
                            else
                            {
                                event.getChannel().sendMessage(createEmbed("Utilizatorul dorit este deja legat cu un cont de discord. Daca cosiderati ca este " +
                                        "o greseala va rugam sa contactati un administrator pentru a il dezlega", 255, 0, 0).build()).queue();
                            }
                        }
                    }
                }
                if(message[0].equalsIgnoreCase("/unlink"))
                {
                    if(message.length == 1)
                    {
                        event.getChannel().sendMessage(createEmbed("Folosire: /unlink @Mention", 0, 255, 0).build()).queue();
                    }
                    else
                    {
                        if(!event.getAuthor().isBot())
                        {
                            if(event.getMessage().getMentionedMembers().size() == 1)
                            {
                                if(Royalsecurity.discordAdmin.contains(event.getMessage().getAuthor().getIdLong()) || event.getMessage().getMentionedMembers().get(0).getIdLong() == event.getMessage().getAuthor().getIdLong())
                                {
                                    if(Royalsecurity.secured(event.getMessage().getMentionedMembers().get(0).getIdLong()))
                                    {
                                        Royalsecurity.removeSecurity(event.getMessage().getMentionedMembers().get(0).getIdLong());
                                        event.getChannel().sendMessage(createEmbed("Contul utilizatorului mentionat nu mai este unit", 0, 255, 0).build()).queue();
                                    }
                                    else
                                    {
                                        event.getChannel().sendMessage(createEmbed("Utilizatorul respectiv nu are contul unit", 255, 0, 0).build()).queue();
                                    }
                                }
                                else
                                {
                                    event.getChannel().sendMessage(createEmbed("Nu ai permisiunea de a folosi aceasta comanda", 255, 0, 0).build()).queue();
                                }
                            }
                            else if(event.getMessage().getMentionedMembers().size() < 1)
                            {
                                event.getChannel().sendMessage(createEmbed("Folosire: /unlink @Mention", 0, 255, 0).build()).queue();
                            }
                            else
                            {
                                event.getChannel().sendMessage(createEmbed("Ai mentiontat prea multe perosane", 255, 0, 0).build()).queue();
                            }
                        }
                    }
                }

            }
            if(message[0].equalsIgnoreCase("/admin"))
            {
                System.out.println("Test");
                if(event.getAuthor().getIdLong() == 710479968949501973L || Royalsecurity.discordAdmin.contains(event.getAuthor().getIdLong()))
                {
                    if(message.length>=2)
                    {
                        if(message[1].equalsIgnoreCase("add"))
                        {
                            if(message.length>=3)
                            {
                                if(message[2].equalsIgnoreCase("minecraft"))
                                {
                                    if(message.length>=4)
                                    {
                                        if(!Royalsecurity.perms.contains(message[3]))
                                        {
                                            Royalsecurity.perms.add(message[3]);
                                            //Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + message[3] + " parrent add administrator");
                                            event.getChannel().sendMessage(createEmbed("Utilizatorul " + message[3] + "  a fost adaugata la lisata administratorilor a serverului de minecraft",0, 255, 0).build()).queue();
                                        }
                                        else
                                        {
                                            event.getChannel().sendMessage(createEmbed("Utilizatorul " + message[3] + " era deja in lista de administratori a serverului de minecraft",255,  0, 0).build()).queue();
                                        }
                                    }
                                    else
                                    {
                                        event.getChannel().sendMessage(createEmbed("Usage: /admin [add/remove] [discord/minecrat] [nume/id/mention]", 255, 255, 0).build()).queue();
                                    }
                                }
                                else if(message[2].equalsIgnoreCase("discord"))
                                {
                                    if(message.length>=4)
                                    {
                                        if(!Royalsecurity.discordAdmin.contains(message[3]))
                                        {
                                            Royalsecurity.discordAdmin.add(Long.parseLong(message[3]));
                                            event.getChannel().sendMessage(createEmbed("Utilizatorul " + message[3] + "  a fost adaugata la lisata administratorilor a serverului de discord",0, 255, 0).build()).queue();
                                        }
                                        else
                                        {
                                            event.getChannel().sendMessage(createEmbed("Utilizatorul " + message[3] + " era deja in lista de administratori a serverului de discord",255,  0, 0).build()).queue();
                                        }
                                    }
                                    else
                                    {
                                        event.getChannel().sendMessage(createEmbed("Usage: /admin [add/remove] [discord/minecrat] [nume/id/mention]", 255, 255, 0).build()).queue();
                                    }
                                }
                                else
                                {
                                    event.getChannel().sendMessage(createEmbed("Usage: /admin [add/remove] [discord/minecrat] [nume/id/mention]", 255, 255, 0).build()).queue();
                                }
                            }
                        }
                        else if(message[1].equalsIgnoreCase("remove"))
                        {
                            if(message.length>=3)
                            {
                                if(message[2].equalsIgnoreCase("minecraft"))
                                {
                                    if(message.length>=4)
                                    {
                                        if(Royalsecurity.perms.contains(message[3]))
                                        {
                                            Royalsecurity.perms.remove(message[3]);
                                            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + message[3] + " parrent remove administrator");
                                            event.getChannel().sendMessage(createEmbed("Utilizatorul " + message[3] + "  a fost scos din lisata administratorilor a serverului de minecraft",0, 255, 0).build()).queue();
                                        }
                                        else
                                        {
                                            event.getChannel().sendMessage(createEmbed("Utilizatorul " + message[3] + "  nu era in lista de administratori a serverului de minecraft",255,  0, 0).build()).queue();
                                        }
                                    }
                                    else
                                    {
                                        event.getChannel().sendMessage(createEmbed("Usage: /admin [add/remove] [discord/minecrat] [nume/id/mention]", 255, 255, 0).build()).queue();
                                    }
                                }
                                else if(message[2].equalsIgnoreCase("discord"))
                                {
                                    if(message.length>=4)
                                    {
                                        if(!Royalsecurity.discordAdmin.contains(message[3]))
                                        {
                                            Royalsecurity.discordAdmin.remove(Long.parseLong(message[3]));
                                            event.getChannel().sendMessage(createEmbed("Utilizatorul " + message[3] + "  a fost scos de pe lisata administratorilor a serverului de discord",0, 255, 0).build()).queue();
                                        }
                                        else
                                        {
                                            event.getChannel().sendMessage(createEmbed("Utilizatorul " + message[3] + " nu era in lista de administratori a serverului de discord",255,  0, 0).build()).queue();
                                        }
                                    }
                                    else
                                    {
                                        event.getChannel().sendMessage(createEmbed("Usage: /admin [add/remove] [discord/minecrat] [nume/id/mention]", 255, 255, 0).build()).queue();
                                    }
                                }
                                else
                                {
                                    event.getChannel().sendMessage(createEmbed("Usage: /admin [add/remove] [discord/minecrat] [nume/id/mention]", 255, 255, 0).build()).queue();
                                }
                            }
                        }
                        else
                        {
                            event.getChannel().sendMessage(createEmbed("Usage: /admin [add/remove] [discord/minecrat] [nume/id/mention]", 255, 255, 0).build()).queue();
                        }
                    }
                    else
                    {
                        event.getChannel().sendMessage(createEmbed("Usage: /admin [add/remove] [discord/minecrat] [nume/id/mention]", 255, 255, 0).build()).queue();
                    }
                }
                else
                {
                    event.getChannel().sendMessage(wrongSenderError().build()).queue();
                }
            }
        }



    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e)
    {
        if(Royalsecurity.secured(e.getUserIdLong()))
        {
            if(e.getReactionEmote().getName().equals("✔"))
            {
                if(!e.getUser().isBot())
                {
                    String name = Royalsecurity.getName(e.getUserIdLong());
                    Royalsecurity.logedInPlayers.add(name);
                    //Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + name + " permission set dm.selector true");
                    if(Royalsecurity.perms.contains(name))
                        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + name + " permission set * true");
                    Royalsecurity.setIp(name, Bukkit.getPlayer(name).getAddress().getHostName());
                }
            }
            else if(e.getReactionEmote().getName().equals("❌"))
            {
                if(!e.getUser().isBot())
                {
                    String name = Royalsecurity.getName(e.getUserIdLong());
                    //Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "ban " + name + " 24h conecsiune refuzata");
                    instance.getTextChannelById(Royalsecurity.AlertsChannel).sendMessage(createEmbed(e.getUser().getName() + " a refuzat conectarea. " +
                            "(IGN: " + name + ")", 255, 0, 0).build()).queue();
                }
            }
        }
    }

    private EmbedBuilder wrongChanelError()
    {
        return createEmbed("Nu poti folosi comenzile acesti bot decat pe canelele destinate acestui scop", 255, 0, 0);
    }

    public static EmbedBuilder createEmbed(String str, int r, int g, int b)
    {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("RoyalSecurity", null);
        eb.setColor(new Color(r, g, b));
        eb.setDescription(str);
        eb.setFooter("Contact RS LightDream#4379");

        return eb;
    }

    private EmbedBuilder wrongSenderError()
    {
        return createEmbed("Nu poti folosi aceasta comanda! Doar RS LightDream#4379 poate.", 255, 0, 0);
    }
}
