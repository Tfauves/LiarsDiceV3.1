package com.company;

import java.util.*;

public class Game {
    public Scanner scanner = new Scanner(System.in);
    List<Player> playerList = new ArrayList<>();
    public Map<Integer, Integer> diceOnTable = new HashMap<>();
    public final byte MAX_PLAYERS = 6;
    public final byte MIN_PLAYERS = 1;
    public boolean isRoundStartingPlayer = false;
    public int roundCount = 0;
    public int openingRoundBidDieQty;
    public int openingRoundBidDieFaceValue;
    public int playerGuessDieQty;
    public int playerGuessDieFaceValue;


    public Game() {
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
        round();
    }

    public void round() {
        rollAll();
        turn();

    }

    public void rollAll() {
        for (Player activePlayer : playerList) {
            activePlayer.cup.roll();
            setDiceOnTable(activePlayer.cup.dice);
        }
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
            System.out.println(activePlayer.playName + "'s turn.");
            System.out.println(activePlayer.cup.displayHand());

        }

    }

    public void roundOpenBid() {
        System.out.println("Make your bid.");
        System.out.println("Enter die qty: ");
        openingRoundBidDieQty = scanner.nextInt();
        System.out.println("Enter die face value: ");
        openingRoundBidDieFaceValue = scanner.nextInt();
        scanner.nextLine();
    }

    public void playerBid() {
        System.out.println("Type (b) to bid or (l) to call Liar");
        String bidOrCall = scanner.nextLine();
        if (bidOrCall.equals("b")) {
            System.out.println("Enter die qty: ");

        }
    }

}
