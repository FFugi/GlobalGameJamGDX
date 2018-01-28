package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	private static SoundManager instance;
	private Sound music;
	private Sound emit;
	private Sound collect;
	
	
	private SoundManager() {
		music = Gdx.audio.newSound(Gdx.files.internal("audio/Bastardized_Sinewaves_Remastered.mp3"));
		emit = Gdx.audio.newSound(Gdx.files.internal("audio/emitsound.mp3"));
		collect = Gdx.audio.newSound(Gdx.files.internal("audio/collectsound.mp3"));
	}
	
	public static SoundManager GetInstance() {
		if(instance==null) {
			instance=new SoundManager();
		}
		return instance;
	}
	
	public void playBgMusic() {

		//music.play(0.1f, 4, 1);
		music.loop(0.25f, 0.8f, 1);
	}
	
	public void playEmitSound() {
		emit.play(1.0f);
	}
	public void playCollectSound() {
		collect.play(1.0f);
	}
	public void dispose() {
		music.dispose();
		emit.dispose();
		collect.dispose();
	}
}
