package Blackjack;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

import java.io.File;




//Takes in a game window and info panels, will orchestrate flow of game
class GameManager {
    static GUI.GameWindow gameWindowForGameManager;
    static GUI.GamePanel gamePanelForGameManager;
    static GUI.UserInfoPanel userInfoPanelForGameManager;
    static GUI.GameInfoPanel gameInfoPanelForGameManager;

    GameManager(GUI.GameWindow x, GUI.UserInfoPanel y, GUI.GameInfoPanel z) {
        gameWindowForGameManager = x;
        userInfoPanelForGameManager = y;
        gameInfoPanelForGameManager = z;
    }

    
    // Starts and controls how a round goes.
    public static boolean executeRound() {
        // shuffle cards and deals to start a round.
        Reference.my_deck.shuffle();
        System.out.println("Deck Shuffled.");
        System.out.println("Dealing Cards");
        Reference.user_hand.hit(Reference.my_deck);
        Reference.dealer_hand.hit(Reference.my_deck);
        Reference.user_hand.hit(Reference.my_deck);
        Reference.dealer_hand.hit(Reference.my_deck);
        System.out.println("Cards Dealt");
        userInfoPanelForGameManager.updateHandDisplay();
        System.out.println("User info panel updated");

        // Delay for 2 seconds (2000 milliseconds)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // player turn
        System.out.println("Player Turn");
        gameInfoPanelForGameManager.updateInfoBox("Player Turn.");
        gameWindowForGameManager.changeControlPanelVisibility(true);

        // Stay button ends round
        while (!Reference.userClickedStay) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check if over 21
            if (Reference.user_hand.countTotal() > 21) {
                // User has lost round
                System.out.println("You busted");
                gameInfoPanelForGameManager.updateInfoBox("You busted.");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return false;
            }
        }

        gameWindowForGameManager.changeControlPanelVisibility(false);

        // Delay for 2 seconds (2000 milliseconds)
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // dealer turn
        gameInfoPanelForGameManager.updateInfoBox("Dealer's Turn.");
        System.out.println("Dealer's turn");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (Reference.dealer_hand.countTotal() < 16) {
            Reference.dealer_hand.hit(Reference.my_deck);
            userInfoPanelForGameManager.updateHandDisplay();
            // Delay for 2 seconds (2000 milliseconds)
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (Reference.dealer_hand.countTotal() > 21) {
            System.out.println("Dealer busted- You win!");
            gameInfoPanelForGameManager.updateInfoBox("Dealer busted- You win!");
            return true;
        }

        // compare values
        if (Reference.user_hand.countTotal() > Reference.dealer_hand.countTotal()) {
            System.out.println("You Win!");
            gameInfoPanelForGameManager.updateInfoBox("You won!");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            gameInfoPanelForGameManager.updateInfoBox("You lost.");
            System.out.println("You Lose! Loser.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    // method to handle user's money depending on who won the round
    public static void payout(boolean win) {
        // If user won the round, win is true. If user lost, win is false.
        if (win) {
            Reference.user_money += 100;
        } else {
            Reference.user_money -= 50;
        }
    }

    // Sound Player
    public static void playSound(String filePath) {
        try {
            // Create a File object from the provided file path
            File audioFile = new File(filePath);
            
            // Get an AudioInputStream from the file
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            
            // Get a Clip object
            Clip clip = AudioSystem.getClip();
            
            // Open the clip with the audio input stream
            clip.open(audioInputStream);
            
            // Start playing the clip
            clip.start();
            
            // Wait until the clip is done playing
            while (!clip.isRunning()) {
                Thread.sleep(1000);  // Sleep for a second if the clip is not running yet
            }
            
            // Close the clip after playing
            clip.close();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException | InterruptedException e) {
            e.printStackTrace();  // Print the stack trace if any exceptions occur
        }
    }

    // Checks if win conditions are met and resets all Reference variables
    public static void resetVariables() {
        // Checks if the user has lost all their money, if they have the game will end.
        if (Reference.user_money <= 0) {
            gameInfoPanelForGameManager.updateInfoBox("Game over, you ran out of money.");
            String losesoundfilepath = "./lose-sound.wav";
            playSound(losesoundfilepath);
            gameWindowForGameManager.setVisible(false);
            System.exit(0);
        }
        // Checks if the user has earned more than 500, which is the win condition
        else if (Reference.user_money >= 500) {
            gameInfoPanelForGameManager.updateInfoBox("You won! You reached the target amount of money.");
            System.out.println("You won.");
            String winsoundfilepath = "./win-sound.wav";
            playSound(winsoundfilepath);
            System.out.println("Sound Played");
            gameWindowForGameManager.setVisible(false);
            System.exit(0);
        }
        // If user has neither won nor lost the game, reset variables and continue on
        else {
            // Reset deck reset
            Reference.my_deck.resetDeck();
            // empty user hand
            Reference.user_hand.clearHand();
            // empty dealer hand
            Reference.dealer_hand.clearHand();
            // User clicked stay to false
            Reference.userClickedStay = false;
        }
    }
}