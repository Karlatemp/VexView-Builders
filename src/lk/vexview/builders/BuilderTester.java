/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: BuilderTester.java@author: karlatemp@vip.qq.com: 2020/1/27 下午9:48@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.api.VexViewAPI;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public /* Test only */ class BuilderTester extends JavaPlugin {
    @Override
    public void onEnable() {
        ChannelBuilder.builder().name("Channel Builder Tester")
                .onPlayerChat(event -> {
                    Bukkit.getConsoleSender().sendMessage(event.getChatMessage());
                }).register();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        switch (args[0]) {
            case "1":
                VexViewAPI.openGui(p, GuiBuilder.builder().text(text -> text.addLine("FAQ").addLine("HUM?"))
                        .calculateSize().build(
                                "[local]login.png", -1, -1
                        ));
                break;
            case "2": {
                VexViewAPI.sendHUD(p, ImageBuilder.builder()
                        .background("[local]bq.png")
                        .size(250, 50)
                        .imageSize(250, 50)
                        .toHUD("VV-builder-test/2333", 60, 0));
                break;
            }
            case "3": {
                VexViewAPI.openGui(p,
                        GuiBuilder.builder().button(
                                button -> button.text("Hello Button").click(a -> a.sendMessage("HUM?")).id("b1")
                                        .background("[local]button.png", "[local]button_.png")
                                        .calculateSize(20, 20, 10, 10)
                        ).newContext(builder -> {
                            builder.offset(77, 20)
                                    .image(image -> image.background("[local]button.png")
                                            .imageSize(68, 16).size(34, 8))
                                    .text(text -> text.addLine("HUM>").offset(0, 20));
                        }).input(field -> field.offset(0, 40).size(30, 20).value("Default Value"))
                                .calculateSize()
                                .build("[local]login.png", -1, -1));
                break;
            }
            case "4": {
                VexViewAPI.openGui(p, GuiBuilder.builder()
                        .draw(draw -> draw.of(p))
                        .calculateSize().build(
                                "[local]login.png", -1, -1
                        ));
                break;
            }
            case "5": {
                VexViewAPI.openGui(p, GuiBuilder.builder()
                        .input(f -> f.size(30, 10).fieldColor(
                                Color.create(0xFF_66ccFF), Color.create(0xFF_66FFcc)
                        ))
                        .calculateSize().build(
                                "[local]login.png", -1, -1
                        )
                );
                break;
            }
            case "6": {
                Slice9 info = Slice9.slice9()
                        .imageSize(7, 7, 1, 1)
                        .left(3).right(3).top(3).bottom(3)
                        .address("[local]sudoka.png", "[local]sudoka.center.png");
                VexViewAPI.openGui(p, GuiBuilder.builder().size(
                        Integer.parseInt(args[1]), Integer.parseInt(args[2])
                ).buildWith(info, -1, -1));
            }
        }
        return true;
    }
}
