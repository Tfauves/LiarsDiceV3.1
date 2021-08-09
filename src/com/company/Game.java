package com.company;

import java.util.*;

public class Game {
    public Scanner scanner = new Scanner(System.in);
    List<Player> playerList = new ArrayList<>();
    public Map<Integer, Integer> diceOnTable = new HashMap<>();
    public final byte MAX_PLAYERS = 6;
    public final byte MIN_PLAYERS = 1;
    public int previousBidDieQty;
    public int previousBidDieFaceValue;
    public int currentGuessDieQty;
    public int currentGuessDieFaceValue;
    public boolean isRoundStartingPlayer = true;
    public boolean isValidBid = false;
    public boolean isActiveRound = false;
    public boolean callLie = false;
    public boolean isALie = false;
    public boolean isActiveGame = false;



    public Game() {
        if (!isActiveGame) {
            System.out.println("Welcome to Liars Dice.");
        }
        System.out.println("Enter number of players: ");
        int numberOfPlayers;
        do {
            numberOfPlayers = scanner.nextByte();
            scanner.nextLine();
        } while (numberOfPlayers < MIN_PLAYERS || numberOfPlayers > MAX_PLAYERS);

        while (playerList.size() < numberOfPlayers) {
            System.out.println("Enter Player Name: ");
            playerList.add(new Player((scanner.nextLine()).trim()));
        }
    }
    
    public void play() {
        isActiveGame = true;
        round();
        declareWinner();
    }

    public void round() {
        diceOnTable.clear();
        System.out.println("New Round");
        rollAll();
        isActiveRound = true;
        while (isActiveRound) {
        turn();
        }
    }

    public void rollAll() {
        for (Player activePlayer : playerList) {
            activePlayer.cup.roll();
            setDiceOnTable(activePlayer.cup.dice);
        }
        // TODO: 8/8/2021 comment out when testing is complete. 
        System.out.println(diceOnTable);
    }

    public void setDiceOnTable(List<Die> dice) {
        for (Die die : dice) {
            if (diceOnTable.containsKey(die.faceValue)) {
                diceOnTable.put(die.faceValue, diceOnTable.get(die.faceValue) + 1);
            } else {
                diceOnTable.put(die.faceValue, 1);
            }
        }
    }

    public void turn() {
        for (Player activePlayer : playerList) {
            System.out.println(activePlayer.playerName + "'s turn.");
            System.out.println("Your Hand is " + activePlayer.cup.displayHand());
            if (isRoundStartingPlayer) {
                roundOpenBid();
                isRoundStartingPlayer = false;
            } else {
                playerBid(activePlayer);
                if (!callLie) {
                validateBid(currentGuessDieQty, currentGuessDieFaceValue);
                } else  {
                    showHands();
                    checkLie(activePlayer);
                    play();
                }
            }
        }
        
    }

    public void roundOpenBid() {
        System.out.println("Make your bid.");
        System.out.println("Enter die qty: ");
        currentGuessDieQty = scanner.nextByte();
        System.out.println("Enter die face value: ");
        currentGuessDieFaceValue = scanner.nextByte();
        scanner.nextLine();
//        System.out.println("The current bid is " + currentGuessDieQty + "x " + currentGuessDieFaceValue);
    }

    public void playerBid(Player activePlayer) {
        previousBidDieQty = currentGuessDieQty;
        previousBidDieFaceValue = currentGuessDieFaceValue;
        System.out.println("the previous bid " + previousBidDieQty + "x " + previousBidDieFaceValue);
        System.out.println("Type (b) to bid or (l) to call Liar");
        String bidOrCall = scanner.nextLine();
        if (bidOrCall.equals("b")) {
            System.out.println("Enter die qty: ");
            currentGuessDieQty = scanner.nextByte();
            System.out.println("Enter die face value: ");
            currentGuessDieFaceValue = scanner.nextByte();
            scanner.nextLine();
            //System.out.println("The current bid is " + currentGuessDieQty + "x " + currentGuessDieFaceValue);
        } else if (bidOrCall.equals("l")) {
            callLie = true;
            isActiveRound = false;
        }
    }
//    Bid must have a qty and a face value.
//    Bid can not equal the previous bid.
//    Qty of the bid can equal, be less than, or greater than previous as long as the faceValue is greater.
//    If the faceValue of the bid is equal to the faceValue of the previous bid. The qty must be greater.

    public void validateBid(int qty, int faceValue) {
        if (faceValue > previousBidDieFaceValue) {
            System.out.println("Valid Bid");
            isValidBid = true;
        } else if (faceValue == previousBidDieFaceValue && qty > previousBidDieQty) {
            System.out.println("Valid Bid");
            isValidBid = true;
        } else {
            System.out.println("Invalid Bid");
        }
    }

    // TODO: 8/8/2021 lie crashes program if called on ply2 after first round of bets.
    public void checkLie(Player activePlayer) {
        isALie = !diceOnTable.containsKey(previousBidDieFaceValue) || diceOnTable.get(previousBidDieFaceValue) < previousBidDieQty;
        if (isALie) {
            System.out.println("bid was a lie");
            System.out.println(playerList.get(playerList.indexOf(activePlayer) - 1).playerName + " loses a die.");
            playerList.get(playerList.indexOf(activePlayer) - 1).cup.dice.remove(0);
            isActiveRound = false;
//            System.out.println(playerList.get(playerList.indexOf(activePlayer) - 1).cup.dice);
        }
        else if (!isALie){
            System.out.println("Bid was not a lie you lose a die");
            playerList.get(playerList.indexOf(activePlayer)).cup.dice.remove(0);
            isActiveRound = false;
        }
        if (playerList.get(playerList.indexOf(activePlayer) - 1).cup.dice.size() == 0) {
            System.out.println(playerList.get(playerList.indexOf(activePlayer) - 1).playerName + " is out of dice. You are out of the game");
            playerList.remove(playerList.get(playerList.indexOf(activePlayer) - 1));
            isActiveRound = false;
            declareWinner();
        }
    }
    
    public void declareWinner() {
        for (Player players : playerList) {
            if (playerList.size() == 1) {
                System.out.println(players.playerName + " is the winner, Game Over.");
                isActiveGame = false;
                System.exit(0);
            }
        }
    }

    public void showHands() {
        for (Player players : playerList) {
            System.out.println(players.playerName + "'s Hand " + players.cup.displayHand());
            isRoundStartingPlayer = true;
        }
    }

}

