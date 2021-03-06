package com.companyname.hearts.model;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

/*
    The Overlord is responsible for maintaining rules and calculations throughout the game.
 */

public class Overlord implements Serializable {

    private boolean playing;
    private boolean heartsBroken;
    private boolean passing;
    private int roundsPlayed;
    private int handsPlayed;
    private Player leadingPlayer;
    private Player previousWinner = null;

    private static Overlord instance = null;

    private Overlord() {
        // Do no allow instantiation
        playing = true;
        heartsBroken = false;
        passing = true;
        roundsPlayed = 1;
        handsPlayed = 0;
    }

    public static Overlord getInstance() {
        if (instance == null) {
            instance = new Overlord();
        }
        return instance;
    }

    // Method returns true if the Player given to it is the leading player, false if not
    public boolean amITheLeadingPlayer(Player player) {
        if (player == leadingPlayer) {
            return true;
        }
        else {
            return false;
        }
    }

    // Method returns the leading Player as an integer, 1 to 4
    public int getLeadingPLayerAsInt() {
        if (leadingPlayer.getName().equals(Table.getInstance().getPlayer1().getName())) {
            return 1;
        }
        else if (leadingPlayer.getName().equals(Table.getInstance().getPlayer2().getName())) {
            return 2;
        }
        else if (leadingPlayer.getName().equals(Table.getInstance().getPlayer3().getName())) {
            return 3;
        }
        else {
            return 4;
        }
    }

    // Method returns true if the Player given to it won the previous trick, false if not
    public boolean amIThePreviousWinner(Player player) {
        if (player == previousWinner) {
            return true;
        }
        else {
            return false;
        }
    }

    public static void putInstance(Overlord newInstance) {
        instance = newInstance;
    }

    public static void saveOverlord(Context context) {
        try {
            FileOutputStream fos = context.openFileOutput("overlord_data", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(Overlord.getInstance());
            os.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadOverlord(Context context) {
        try {
            FileInputStream fis = context.openFileInput("overlord_data");
            ObjectInputStream is = new ObjectInputStream(fis);
            Overlord overlord = (Overlord) is.readObject();
            Overlord.putInstance(overlord);
            is.close();
            fis.close();
        } catch (IOException e) {
            System.out.println("File not found");
        } catch (ClassNotFoundException e) {
            System.out.println("Issue with class");
        }
    }

    // Method sets the leadingPlayer to be equal to the Player who has the two of clubs.
    // This should only be done at the start of a hand.
    public void setPlayerWithTheTwoOfClubs() {
        for (int i = 0; i < 13; i++) {
            if (Table.getInstance().getPlayer1().getHand().get(i).getRank().getValue() == 2
                    && Table.getInstance().getPlayer1().getHand().get(i).getSuit() == Suit.Clubs)
                leadingPlayer = Table.getInstance().getPlayer1();
            if (Table.getInstance().getPlayer2().getHand().get(i).getRank().getValue() == 2
                    && Table.getInstance().getPlayer2().getHand().get(i).getSuit() == Suit.Clubs)
                leadingPlayer = Table.getInstance().getPlayer2();
            if (Table.getInstance().getPlayer3().getHand().get(i).getRank().getValue() == 2
                    && Table.getInstance().getPlayer3().getHand().get(i).getSuit() == Suit.Clubs)
                leadingPlayer = Table.getInstance().getPlayer3();
            if (Table.getInstance().getPlayer4().getHand().get(i).getRank().getValue() == 2
                    && Table.getInstance().getPlayer4().getHand().get(i).getSuit() == Suit.Clubs)
                leadingPlayer = Table.getInstance().getPlayer4();
        }
    }

    // Method determines the winner of the current cards on the board. The leadingPlayer is updated
    // to become the winner of the trick when this method is finished. Obviously this method should
    // only be called when there are four cards on the board, but it will work regardless of that fact
    public void determineTrickWinner() {
        // Keeps the previousWinner one hand behind as desired:
        previousWinner = leadingPlayer;

        Card winner = Table.getInstance().getBoard().get(0);
        Suit lead = winner.getSuit();
        int leadValue = winner.getRank().getValue();
        int winnerPosition = 0;
        for (int i = 1; i < Table.getInstance().getBoard().size(); i++) {
            if (Table.getInstance().getBoard().get(i).getSuit() == lead) {
                if (Table.getInstance().getBoard().get(i).getRank().getValue() > leadValue) {
                    winner = Table.getInstance().getBoard().get(i);
                    winnerPosition = i;
                    leadValue = winner.getRank().getValue();
                }
            }
        }

        // Leading player wins trick:
        if (getLeadingPlayer().getName().equals(Table.getInstance().getPlayer1().getName())) {
            if (winnerPosition == 0) {

            }
            else if (winnerPosition == 1) {
                setLeadingPlayer(Table.getInstance().getPlayer2());
            }
            else if (winnerPosition == 2) {
                setLeadingPlayer(Table.getInstance().getPlayer3());
            }
            else {
                setLeadingPlayer(Table.getInstance().getPlayer4());
            }
        }
        // HAL9000 wins trick:
        else if (getLeadingPlayer().getName().equals(Table.getInstance().getPlayer2().getName())) {
            if (winnerPosition == 0) {

            }
            else if (winnerPosition == 1) {
                setLeadingPlayer(Table.getInstance().getPlayer3());
            }
            else if (winnerPosition == 2) {
                setLeadingPlayer(Table.getInstance().getPlayer4());
            }
            else {
                setLeadingPlayer(Table.getInstance().getPlayer1());
            }
        }
        // Terminator wins trick:
        else if (getLeadingPlayer().getName().equals(Table.getInstance().getPlayer3().getName())) {
            if (winnerPosition == 0) {

            }
            else if (winnerPosition == 1) {
                setLeadingPlayer(Table.getInstance().getPlayer4());
            }
            else if (winnerPosition == 2) {
                setLeadingPlayer(Table.getInstance().getPlayer1());
            }
            else {
                setLeadingPlayer(Table.getInstance().getPlayer2());
            }
        }
        // Zombo wins trick:
        else {
            if (winnerPosition == 0) {

            }
            else if (winnerPosition == 1) {
                setLeadingPlayer(Table.getInstance().getPlayer1());
            }
            else if (winnerPosition == 2) {
                setLeadingPlayer(Table.getInstance().getPlayer2());
            }
            else {
                setLeadingPlayer(Table.getInstance().getPlayer3());
            }
        }

        for (int i = 0; i < Table.getInstance().getBoard().size(); i++) {
            getLeadingPlayer().getOldCards().add(Table.getInstance().getBoard().get(i));
        }

    }

    // This method removes any remaining cards from all Players oldCards and hands. It should be
    // called at the end of 13 rounds. It also resets heartsBroken, roundsPlayed, the previousWinner,
    // and updates pass direction.
    public void reset() {
        Table.getInstance().getPlayer1().getHand().clear();
        Table.getInstance().getPlayer2().getHand().clear();
        Table.getInstance().getPlayer3().getHand().clear();
        Table.getInstance().getPlayer4().getHand().clear();
        Table.getInstance().getPlayer1().getOldCards().clear();
        Table.getInstance().getPlayer2().getOldCards().clear();
        Table.getInstance().getPlayer3().getOldCards().clear();
        Table.getInstance().getPlayer4().getOldCards().clear();
        heartsBroken = false;
        roundsPlayed = 1;
        handsPlayed ++;
        previousWinner = null;
        getPassingDirection();
    }

    // Method that resets scores and other variables needed for an entirely new game of Hearts.
    public void prepareForNextGame() {
        playing = true;
        heartsBroken = false;
        passing = true;
        roundsPlayed = 1;
        handsPlayed = 0;
        previousWinner = null;
        Table.getInstance().getPlayer1().setPoints(0);
        Table.getInstance().getPlayer2().setPoints(0);
        Table.getInstance().getPlayer3().setPoints(0);
        Table.getInstance().getPlayer4().setPoints(0);
    }

    // Method that determines if the game is over or not
    public void updatePlaying() {
        if (Table.getInstance().getPlayer1().getPoints() >= 100 || Table.getInstance().getPlayer2().getPoints() >= 100
                || Table.getInstance().getPlayer3().getPoints() >= 100 || Table.getInstance().getPlayer4().getPoints() >= 100) {
            playing = false;
        }
    }

    // Method that calculates and updates the points for all the Players
    public void calculatePoints() {
        int player1Points = 0;
        int player2Points = 0;
        int player3Points = 0;
        int player4Points = 0;

        for (int i = 0; i < Table.getInstance().getPlayer1().getOldCards().size(); i++) {
            if (Table.getInstance().getPlayer1().getOldCards().get(i).getSuit() == Suit.Hearts)
                player1Points++;
            if (Table.getInstance().getPlayer1().getOldCards().get(i).getSuit() == Suit.Spades
                    && Table.getInstance().getPlayer1().getOldCards().get(i).getRank() == Rank.Queen)
                player1Points = player1Points + 13;
        }
        for (int i = 0; i < Table.getInstance().getPlayer2().getOldCards().size(); i++) {
            if (Table.getInstance().getPlayer2().getOldCards().get(i).getSuit() == Suit.Hearts)
                player2Points++;
            if (Table.getInstance().getPlayer2().getOldCards().get(i).getSuit() == Suit.Spades
                    && Table.getInstance().getPlayer2().getOldCards().get(i).getRank() == Rank.Queen)
                player2Points = player2Points + 13;
        }
        for (int i = 0; i < Table.getInstance().getPlayer3().getOldCards().size(); i++) {
            if (Table.getInstance().getPlayer3().getOldCards().get(i).getSuit() == Suit.Hearts)
                player3Points++;
            if (Table.getInstance().getPlayer3().getOldCards().get(i).getSuit() == Suit.Spades
                    && Table.getInstance().getPlayer3().getOldCards().get(i).getRank() == Rank.Queen)
                player3Points = player3Points + 13;
        }
        for (int i = 0; i < Table.getInstance().getPlayer4().getOldCards().size(); i++) {
            if (Table.getInstance().getPlayer4().getOldCards().get(i).getSuit() == Suit.Hearts)
                player4Points++;
            if (Table.getInstance().getPlayer4().getOldCards().get(i).getSuit() == Suit.Spades
                    && Table.getInstance().getPlayer4().getOldCards().get(i).getRank() == Rank.Queen)
                player4Points = player4Points + 13;
        }
        // Calculate points, moon shoot:
        if (player1Points == 26) {
            Table.getInstance().getPlayer2().setPoints(Table.getInstance().getPlayer2().getPoints() + 26);
            Table.getInstance().getPlayer3().setPoints(Table.getInstance().getPlayer3().getPoints() + 26);
            Table.getInstance().getPlayer4().setPoints(Table.getInstance().getPlayer4().getPoints() + 26);
        } else if (player2Points == 26) {
            Table.getInstance().getPlayer1().setPoints(Table.getInstance().getPlayer1().getPoints() + 26);
            Table.getInstance().getPlayer3().setPoints(Table.getInstance().getPlayer3().getPoints() + 26);
            Table.getInstance().getPlayer4().setPoints(Table.getInstance().getPlayer4().getPoints() + 26);
        } else if (player3Points == 26) {
            Table.getInstance().getPlayer1().setPoints(Table.getInstance().getPlayer1().getPoints() + 26);
            Table.getInstance().getPlayer2().setPoints(Table.getInstance().getPlayer2().getPoints() + 26);
            Table.getInstance().getPlayer4().setPoints(Table.getInstance().getPlayer4().getPoints() + 26);
        } else if (player4Points == 26) {
            Table.getInstance().getPlayer1().setPoints(Table.getInstance().getPlayer1().getPoints() + 26);
            Table.getInstance().getPlayer2().setPoints(Table.getInstance().getPlayer2().getPoints() + 26);
            Table.getInstance().getPlayer3().setPoints(Table.getInstance().getPlayer3().getPoints() + 26);
        } else {
            // Calculate points, no moon shoot:
            for (int i = 0; i < Table.getInstance().getPlayer1().getOldCards().size(); i++) {
                if (Table.getInstance().getPlayer1().getOldCards().get(i).getSuit() == Suit.Hearts)
                    Table.getInstance().getPlayer1().setPoints(Table.getInstance().getPlayer1().getPoints() + 1);
                if (Table.getInstance().getPlayer1().getOldCards().get(i).getSuit() == Suit.Spades
                        && Table.getInstance().getPlayer1().getOldCards().get(i).getRank() == Rank.Queen)
                    Table.getInstance().getPlayer1().setPoints(Table.getInstance().getPlayer1().getPoints() + 13);
            }
            for (int i = 0; i < Table.getInstance().getPlayer2().getOldCards().size(); i++) {
                if (Table.getInstance().getPlayer2().getOldCards().get(i).getSuit() == Suit.Hearts)
                    Table.getInstance().getPlayer2().setPoints(Table.getInstance().getPlayer2().getPoints() + 1);
                if (Table.getInstance().getPlayer2().getOldCards().get(i).getSuit() == Suit.Spades
                        && Table.getInstance().getPlayer2().getOldCards().get(i).getRank() == Rank.Queen)
                    Table.getInstance().getPlayer2().setPoints(Table.getInstance().getPlayer2().getPoints() + 13);
            }
            for (int i = 0; i < Table.getInstance().getPlayer3().getOldCards().size(); i++) {
                if (Table.getInstance().getPlayer3().getOldCards().get(i).getSuit() == Suit.Hearts)
                    Table.getInstance().getPlayer3().setPoints(Table.getInstance().getPlayer3().getPoints() + 1);
                if (Table.getInstance().getPlayer3().getOldCards().get(i).getSuit() == Suit.Spades
                        && Table.getInstance().getPlayer3().getOldCards().get(i).getRank() == Rank.Queen)
                    Table.getInstance().getPlayer3().setPoints(Table.getInstance().getPlayer3().getPoints() + 13);
            }
            for (int i = 0; i < Table.getInstance().getPlayer4().getOldCards().size(); i++) {
                if (Table.getInstance().getPlayer4().getOldCards().get(i).getSuit() == Suit.Hearts)
                    Table.getInstance().getPlayer4().setPoints(Table.getInstance().getPlayer4().getPoints() + 1);
                if (Table.getInstance().getPlayer4().getOldCards().get(i).getSuit() == Suit.Spades
                        && Table.getInstance().getPlayer4().getOldCards().get(i).getRank() == Rank.Queen)
                    Table.getInstance().getPlayer4().setPoints(Table.getInstance().getPlayer4().getPoints() + 13);
            }
        }
    }

    // Method that will return the Player who won the game.
    public Player getWinningPlayer() {
        Player winningPlayer;
        int oneMin = Math.min(Table.getInstance().getPlayer1().getPoints(), Table.getInstance().getPlayer2().getPoints());
        int twoMin = Math.min(Table.getInstance().getPlayer3().getPoints(), Table.getInstance().getPlayer4().getPoints());
        int overallMin = Math.min(oneMin, twoMin);
        if (overallMin == Table.getInstance().getPlayer1().getPoints()) {
            winningPlayer = Table.getInstance().getPlayer1();
        }
        else if (overallMin == Table.getInstance().getPlayer2().getPoints()) {
            winningPlayer = Table.getInstance().getPlayer2();
        }
        else if (overallMin == Table.getInstance().getPlayer3().getPoints()) {
            winningPlayer = Table.getInstance().getPlayer3();
        }
        else {
            winningPlayer = Table.getInstance().getPlayer4();
        }
        return winningPlayer;
    }

    // Returns a String version representing the current direction of passing
    public String getPassingDirection() {
        // Pass Cards Direction:
        if (handsPlayed % 4 == 0) {
            passing = true;
            return "Pass three cards left";
        }
        if (handsPlayed % 4 == 1) {
            passing = true;
            return "Pass three cards right";
        }
        if (handsPlayed % 4 == 2) {
            passing = true;
            return  "Pass three cards across";
        }
        passing = false;
        return "No passing";
    }

    // Returns an Enum that represents the current direction of passing
    public Direction passingDirection() {
        // Pass Cards Direction:
        if (handsPlayed % 4 == 0) {
            passing = true;
            return Direction.LEFT;
        }
        if (handsPlayed % 4 == 1) {
            passing = true;
            return Direction.RIGHT;
        }
        if (handsPlayed % 4 == 2) {
            passing = true;
            return  Direction.ACROSS;
        }
        passing = false;
        return Direction.NO_PASSING;
    }

    // Method that determines if the Card and the Player given to it is allowed to be played or not
    public boolean canPlayCard(Card userCard, Player whosPlaying) {
        if (getRoundsPlayed() == 1) {
            if (getLeadingPlayer() == whosPlaying) {
                if (!(userCard.getSuit() == Suit.Clubs && userCard.getRank()
                        .getValue() == 2)) {
                    return false;
                }
            }
            // If you aren't the leading player, you have to follow suit, unless
            // you don't have clubs, then you can play anything except the queen
            // of spades or hearts, unless its all you have:
            else {
                int numberOfClubs = 0;
                for (int i = 0; i < whosPlaying.getHand().size(); i++) {
                    if (whosPlaying.getHand().get(i).getSuit() == Suit.Clubs) {
                        numberOfClubs++;
                    }
                }
                // Have to play a club here:
                if (numberOfClubs > 0) {
                    if (userCard.getSuit() != Suit.Clubs)
                        return false;
                }
                // Don't have any clubs, can't play a trick unless there is no
                // other choice:
                else {
                    int notHearts = 0;
                    for (int i = 0; i < whosPlaying.getHand().size(); i++) {
                        if (whosPlaying.getHand().get(i).getSuit() != Suit.Hearts
                                || (whosPlaying.getHand().get(i).getSuit() != Suit.Spades && whosPlaying.getHand()
                                .get(i).getRank() != Rank.Queen)) {
                            notHearts++;
                        }
                    }
                    // Have stuff that isn't Hearts, must play a non-heart here:
                    if (notHearts > 0) {
                        if (userCard.getSuit() == Suit.Hearts || (userCard.getSuit() == Suit.Spades && userCard.getRank() == Rank.Queen)) {
                            return false;
                        }
                    }
                    // All cards are hearts
                    else {
                        return true;
                    }

                }

            }

        }

        ////////////////////////  joker crap: ///////////////////////////////////

        // Make a temporary hand that is duplicate of original hand:
        ArrayList<Card> copy = new ArrayList<>();
        for (int i = 0; i < whosPlaying.getHand().size(); i++) {
            copy.add(whosPlaying.getHand().get(i));
        }
        // Remove all jokers from this hand:
        for (int i = 0; i < copy.size(); i++) {
            if (copy.get(i).toString().equals("Joker of Joker")) {
                copy.remove(i);
                i--;
            }
        }
        // Use this hand from now on...

        ////////////////////////////////////////////////////////////////////////

        // If all you have is hearts or all hearts and the queen of spades, you
        // can play right away no matter what:
        int numHearts = 0;
        for (int i = 0; i < copy.size(); i++) {
            if (copy.get(i).getSuit() == Suit.Hearts
                    || (copy.get(i).getSuit() == Suit.Spades && copy.get(i).getRank() == Rank.Queen))
                numHearts++;
        }
        if (numHearts == copy.size()) {
            // All you have is hearts, return true and break hearts:
            heartsBroken = true;
            return true;
        }

        if (Table.getInstance().getBoard().isEmpty()) {
            // Nobody has played any cards yet, make sure they don't play a
            // heart, or the queen, if hearts have not been broken:
            if (!heartsBroken) {
                if (userCard.getSuit() == Suit.Hearts
                        || (userCard.getSuit() == Suit.Spades && userCard.getRank() == Rank.Queen)) {
                    // System.out.println("Cant play that, Hearts have not been broken yet, ");
                    return false;
                }
            }
            // Hearts have been broken, the user can play any card
            leadingPlayer = whosPlaying;
            return true;
        } else {
            // board is not empty so there is at least one card in board:
            Suit leadingSuit = Table.getInstance().getBoard().get(0).getSuit();
            // Check to see if the current player can follow suit:
            int times = 0;
            for (int i = 0; i < copy.size(); i++) {
                if (copy.get(i).getSuit() == leadingSuit)
                    times++;
            }
            // I have to follow suit here
            if (times > 0) {
                // Didn't follow suit, cant do this because they have the leading
                // suit
                if (userCard.getSuit() != leadingSuit) {
                    // System.out.println("Cant play that, you must follow Suit, ");
                    return false;
                }
            }
            // Don't have to follow suit, can play whatever card you want
            // Check to see if this causes hearts to be broken:
            if (userCard.getSuit() == Suit.Hearts
                    || (userCard.getSuit() == Suit.Spades && userCard.getRank() == Rank.Queen)) {
                heartsBroken = true;
            }
            return true;
        }

    }

    // GETTERS, SETTERS:

    public boolean getPlaying() {
        return playing;
    }

    public boolean getHeartsBroken() {
        return heartsBroken;
    }

    public boolean getPassing() {
        return passing;
    }

    public void setPassing(boolean newValue) {
        passing = newValue;
    }

    public Player getLeadingPlayer() {
        return leadingPlayer;
    }

    public void setLeadingPlayer(Player newPlayer) {
        leadingPlayer = newPlayer;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public void setRoundsPlayed(int newValue) {
        roundsPlayed = newValue;
    }

}
