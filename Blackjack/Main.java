/* Niels Weisbeek and Aidan Lieberman
 * nweisbee@u.rochester.edu
 * alieber6@u.rochester.edu
 * 12/3/23
 */

package Blackjack;

import Blackjack.GUI.GameWindow;
import Blackjack.*;

class Main {
  public static void main(String[] args) {

    GameWindow myGameWindow = new GameWindow();
    myGameWindow.setVisible(true);
    GameManager myGameManager = new GameManager(myGameWindow, myGameWindow.userInfoPanel, myGameWindow.gameInfoPanel);

    while (true) {
      myGameManager.payout(myGameManager.executeRound());
      myGameManager.resetVariables();
    }
  }
}