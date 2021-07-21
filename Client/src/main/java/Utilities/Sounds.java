package Utilities;

import fxmlController.App;
import javafx.scene.media.AudioClip;

public class Sounds {
    public static AudioClip buttonClick = new AudioClip(App.class.getResource("/Assets/Sounds/buttonClick.wav").toString());
    public static AudioClip eatBomb = new AudioClip(App.class.getResource("/Assets/Sounds/eatBomb.wav").toString());
    public static AudioClip letsduel = new AudioClip(App.class.getResource("/Assets/Sounds/letsduel.wav").toString());
    public static AudioClip monsterDestroyed = new AudioClip(App.class.getResource("/Assets/Sounds/Monsterdestroyed.wav").toString());
    public static AudioClip setActive = new AudioClip(App.class.getResource("/Assets/Sounds/setActivemp3.wav").toString());
    public static AudioClip setCard = new AudioClip(App.class.getResource("/Assets/Sounds/setcard .wav").toString());

}
