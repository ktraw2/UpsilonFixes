package com.rewindmc.upsilonfixes.redesign;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPool;
import net.minecraft.client.audio.SoundPoolEntry;
import paulscode.sound.SoundSystem;

import java.io.File;

public class MenuMusicManager {
    private static final SoundPool menuMusic = new SoundPool();

    private static boolean firstTime = true;
    private static int musicTime = 0;

    private MenuMusicManager() {}

    static {
        final File dir = new File("resources/menumusic");
        if (dir.isDirectory()) {
            for (final File f : dir.listFiles()) {
                menuMusic.addSound(f.getName(), f);
            }
        }
    }

    public static void updateMusic(
            final Minecraft mc
    ) {
        musicTime++;
        final SoundSystem sys = SoundManager.sndSystem;
        if (sys != null && musicTime > 20 && mc.options.musicVolume > 0 && SoundSystem.initialized && !menuMusic.allSoundPoolEntries.isEmpty() && !sys.playing("BgMusic")) {
            final SoundPoolEntry en = menuMusic.getRandomSound();
            if (en != null) {
                sys.setVolume("BgMusic", mc.options.musicVolume);
                sys.backgroundMusic("BgMusic", en.soundUrl, en.soundName, false);

                if (firstTime) {
                    sys.fadeOutIn("BgMusic", en.soundUrl, en.soundName, 0, 5000);
                    firstTime = false;
                } else {
                    sys.play("BgMusic");
                }

                sys.setVolume("BgMusic", mc.options.musicVolume);
                musicTime = 0;
            }
        }
    }
}
