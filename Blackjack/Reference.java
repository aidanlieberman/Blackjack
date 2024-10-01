package Blackjack;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

// Class that defines globally used variables. 
public class Reference {
    public static Deck my_deck;
    public static Hand user_hand;
    public static Hand dealer_hand;
    public static int user_money;
    public static boolean isControlPanelVisible;
    public static boolean userClickedStay;

    static {
        my_deck = new Deck();
        user_hand = new Hand();
        dealer_hand = new Hand();
        user_money = 100;
        isControlPanelVisible = false;
        userClickedStay = false;
    }
}