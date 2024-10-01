package Blackjack;

import javax.swing.*;

import Blackjack.GUI.ControlPanel;
import Blackjack.GUI.GameInfoPanel;
import Blackjack.GUI.UserInfoPanel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

import java.io.File;


public class GUI {

  // GameWindow class for the main application frame
  public static class GameWindow extends JFrame {
    // Instance variables for the different panels of GameWindow
    UserInfoPanel userInfoPanel; // Top panel (hands, money, card totals)
    ControlPanel controlPanel; // Bottom panel for controls (hit and stay)
    GameInfoPanel gameInfoPanel; // East panel, tells users whos turn it is and who won round

    public GameWindow() {
      setTitle("Blackjack Game");
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setSize(800, 600);
      setLocationRelativeTo(null); // Center the window

      // Add the game panel and control panel
      setLayout(new BorderLayout());
      userInfoPanel = new UserInfoPanel();
      controlPanel = new ControlPanel(userInfoPanel);
      gameInfoPanel = new GameInfoPanel();

      add(userInfoPanel, BorderLayout.NORTH);
      add(new GamePanel(), BorderLayout.CENTER);
      add(gameInfoPanel, BorderLayout.EAST);
      add(controlPanel, BorderLayout.SOUTH);

      controlPanel.setVisible(Reference.isControlPanelVisible); // Attached to a static variable so we can change the
                                                                // visibility.

    }

    // "setter" method for control panel visibility
    public void changeControlPanelVisibility(boolean boolean_input) {
      controlPanel.setVisible(boolean_input);
    }
  }

  // GamePanel class for the game area
  public static class GamePanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      // Paint Background
      g.setColor(new Color(209, 216, 224));
      g.fillRect(0, 0, getWidth(), getHeight());
      // Paint table
      g.setColor(new Color(71, 138, 51));
      g.fillOval(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
      // Paint player
      int d = (getHeight() / 10 + getWidth() / 10) / 2; // d is the diameter of the "player" circles
      g.setColor(new Color(241, 194, 125));

      g.fillOval(getWidth() / 2 - d / 2, 3 * getHeight() / 4, d, d);
      g.setColor(Color.BLACK);
      g.drawOval(getWidth() / 2 - d / 2, 3 * getHeight() / 4, d, d);

      // Paint dealer
      g.setColor(Color.BLUE);
      g.fillOval(getWidth() / 2 - d / 2, getHeight() / 4 - d, d, d);

      // Paint cards
      for (int i = 0; i < Reference.dealer_hand.getHand().size(); i++) {
        g.setColor(Color.RED);
        g.fillRect(getWidth() / 2 + (int) (1.2 * (d / 4) * i), getHeight() / 4, d / 4, d / 2);
        g.setColor(Color.BLACK);
        g.drawRect(getWidth() / 2 + (int) (1.2 * (d / 4) * i), getHeight() / 4, d / 4, d / 2);
      }
      for (int i = 0; i < Reference.user_hand.getHand().size(); i++) {
        g.setColor(Color.RED);
        g.fillRect(getWidth() / 2 + (int) (1.2 * (d / 4) * i), 3 * getHeight() / 4 - d / 2, d / 4, d / 2);
        g.setColor(Color.BLACK);
        g.drawRect(getWidth() / 2 + (int) (1.2 * (d / 4) * i), 3 * getHeight() / 4 - d / 2, d / 4, d / 2);
      }
      // Game lost screen
      if (Reference.user_money <= 0) {
        BufferedImage image = null;
        try {
          String lose_image_path = "./lose-image.png";
          File lose_image_file = new File(lose_image_path);
          image = ImageIO.read(lose_image_file);
        } catch (IOException e) {
          e.printStackTrace();
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
      }
      // Game won screen
      else if (Reference.user_money >= 500) {
        BufferedImage image = null;
        try {
          String win_image_path = "./win-image.png";
          File win_image_file = new File(win_image_path);
          image = ImageIO.read(win_image_file);
        } catch (IOException e) {
          e.printStackTrace();
        }
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
      }
    }
  }

  // ControlPanel class for controls
  public static class ControlPanel extends JPanel {
    JButton stayButton;
    JButton hitButton;

    public ControlPanel(UserInfoPanel uip) {
      hitButton = new JButton("Hit");
      stayButton = new JButton("Stay");

      // Add action listeners for buttons
      hitButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          // Handle hit action
          System.out.println("Hit");
          Reference.user_hand.hit(Reference.my_deck);
          uip.updateHandDisplay();
        }
      });

      stayButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          // Handle stay action
          System.out.println("Stay");
          Reference.userClickedStay = true;
        }
      });

      add(hitButton);
      add(stayButton);
    }

  }

  // Display for dealer and user cards and card totals, and the amount of money
  // the player has.
  public static class UserInfoPanel extends JPanel {
    JLabel moneyTotal;
    JLabel cardTotal;
    JLabel userHand;
    JTextArea userHandDisplay;

    JLabel dealerHand;
    JTextArea dealerHandDisplay;
    JLabel dealerCardTotal;

    public UserInfoPanel() {
      userHand = new JLabel("       Your Hand: ");
      userHandDisplay = new JTextArea();
      cardTotal = new JLabel("Your Card Total: " + Reference.user_hand.countTotal());
      moneyTotal = new JLabel("       Your Money Total: " + Reference.user_money);

      dealerHand = new JLabel("Dealer Hand: ");
      dealerHandDisplay = new JTextArea();
      dealerCardTotal = new JLabel("Dealer Card Total: " + Reference.dealer_hand.countTotal());

      userHandDisplay.setEditable(false);
      dealerHandDisplay.setEditable(false);

      add(dealerHand);
      add(dealerHandDisplay);
      add(dealerCardTotal);

      add(userHand);
      add(userHandDisplay);
      add(cardTotal);
      add(moneyTotal);

    }

    // Method to update the display when new cards are added.
    public void updateHandDisplay() {
      dealerHandDisplay.setText(Reference.dealer_hand.toString());
      dealerCardTotal.setText("Dealer Card Total: " + Reference.dealer_hand.countTotal());

      userHandDisplay.setText(Reference.user_hand.toString());
      cardTotal.setText("Your Card Total: " + Reference.user_hand.countTotal());
      moneyTotal.setText("       Your Money Total: " + Reference.user_money);
    }
  }

  // Info panel for for turn and who won/
  public static class GameInfoPanel extends JPanel {
    JTextArea infoBox;

    public GameInfoPanel() {
      infoBox = new JTextArea(2, 10);
      infoBox.setEditable(false);
      add(infoBox);
    }

    // Method to update text box.
    public void updateInfoBox(String a) {
      infoBox.setText(a);
    }
  }
}