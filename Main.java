package com.coderdojoginowan.myspigotplugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;

public class Main extends JavaPlugin implements Listener{

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equals("hello")) {

            Player player = (Player)sender;
            player.sendTitle("Hello World!", "", 10, 70, 20);
        }
        return false;
    }

    /***
     * プレイヤーがアイテムを振った時（イベント発生時）に
     * 持つアイテムに応じて、稲妻を落としたり、ウィザースケルトンの頭を投げつけたりするメソッドです
     * @param event
     */
    @EventHandler
    public void onPlayerEvent(PlayerInteractEvent event) {

        // イベントを発生させたプレイヤーを取得します
        Player player = event.getPlayer();

        // カーソルがあるブロックを取得します
        Block focusBlock = getCursorFocusBlock(player);

        // 金の剣（GOLDEN_SWORD）なら稲妻を落とす
        if (player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_SWORD) {

            if (focusBlock != null) {

                focusBlock.getWorld().strikeLightning(focusBlock.getLocation());
            }

        // 金の斧（GOLDEN_AXE）ならブロックを爆発させる
        } else if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_AXE) {

            if(focusBlock != null) {

              focusBlock.getWorld().createExplosion(focusBlock.getLocation(), 10);
            }

        // 金のピッケル（GOLDEN_PICKAXE）ならウィザースケルトンの頭を投げつける
        } else if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_PICKAXE) {

            if(focusBlock != null) {

                player.launchProjectile(WitherSkull.class);
            }

        // 金のスコップ（GOLDEN_SHOVEL）ならファイヤーボールを投げつける
        } else if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_SHOVEL) {

            if(focusBlock != null) {

                player.launchProjectile(Fireball.class);
            }

        // 金りんご（GOLDEN_APPLE）なら村人をスポーンさせる
        } else if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_APPLE) {

            if(focusBlock != null) {

                focusBlock.getWorld().spawnEntity(player.getLocation(),EntityType.VILLAGER);
            }

        // 金のにんじん（GOLDEN_CARROT）ならアイアンゴーレムをスポーンさせる
        } else if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_CARROT) {

            if(focusBlock != null) {

                focusBlock.getWorld().spawnEntity(player.getLocation(),EntityType.IRON_GOLEM);
            }

        // 金のクワ（GOLDEN_HOE）なら謎の建造物を出現させる
        } else if(player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_HOE) {

            if (focusBlock != null) {

                // Ｘ軸の開始座標
                int startX = focusBlock.getX();

                // Y軸の開始座標
                int startY = focusBlock.getY();

                // Z軸の開始座標
                int startZ = focusBlock.getZ();

                // X、Y、Zそれぞれに置くブロックサイズ
                int sizeX = 20;
                int sizeY = 20;
                int sizeZ = 20;

                for(int countY=0; countY<=sizeY; countY++) {

                    for(int countX=0; countX<=sizeX; countX++) {

                        // 最初は床を創る
                        if(countX == 0) {

                            for(int i=0; i<=sizeX; i++) {

                                Block frontRightBlock = focusBlock.getWorld().getBlockAt(startX+i, startY+countY, startZ+i);
                                frontRightBlock.setType(Material.STONE);

                                Block frontLeftBlock = focusBlock.getWorld().getBlockAt(startX-i, startY+countY, startZ+i);
                                frontLeftBlock.setType(Material.STONE);
                            }

                        } else {

                            // 手前横ブロック（X軸）
                            Block frontRightBlock = focusBlock.getWorld().getBlockAt(startX+countX, startY+countY, startZ);
                            frontRightBlock.setType(Material.STONE);

                            Block frontLeftBlock = focusBlock.getWorld().getBlockAt(startX-countX, startY+countY, startZ);
                            frontLeftBlock.setType(Material.STONE);

                            // 後ろ横ブロック（X軸）
                            Block backRightBlock = focusBlock.getWorld().getBlockAt(startX+countX, startY+countY, startZ+sizeZ);
                            backRightBlock.setType(Material.STONE);

                            Block backLeftBlock = focusBlock.getWorld().getBlockAt(startX-countX, startY+countY, startZ+sizeZ);
                            backLeftBlock.setType(Material.STONE);

                            // 縦ブロック（Z軸）
                            if(countX == sizeX) {

                                for(int countZ=0;countZ<sizeZ;countZ++) {

                                    Block rightBlock = focusBlock.getWorld().getBlockAt(startX+countX, startY+countY, startZ+countZ);
                                    rightBlock.setType(Material.STONE);

                                    Block leftBlock = focusBlock.getWorld().getBlockAt(startX-countX, startY+countY, startZ+countZ);
                                    leftBlock.setType(Material.STONE);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    /*
     * マウスのカーソルがフォーカスしているブロックを取得する
     */
    private Block getCursorFocusBlock(Player player) {

        // 100ブロック先までを対象にします
        BlockIterator blocks = new BlockIterator(player, 100);

        // プレイヤーの目の前から、
        // カーソルのあるブロックまでチェックして、
        // AIRじゃないブロックが見つかったら、
        // そのブロックをかえします

        while (blocks.hasNext()) {

            // ブロックをひとつひとつ取得します
            Block block = blocks.next();

            // ブロックが空気じゃないかチェックします
            if ( block.getType() != Material.AIR ) {

                // もしブロックが空気じゃないものだったらチェックを終わります
                return block;
            }
        }
        // 100ブロック先まで空気しかなかったら何も返さない
        return null;
    }

    /*
     * プログラムが読み込まれたときに実行されるメソッド
     */
    @Override
    public void onEnable() {

        // サーバー上で発生したイベントをプログラムに通知するための処理です
        getServer().getPluginManager().registerEvents(this, this);
    }
}
