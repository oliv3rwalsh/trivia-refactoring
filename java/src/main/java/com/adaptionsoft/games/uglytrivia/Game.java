package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Game {
	ArrayList players = new ArrayList();
	int[] places = new int[6];
	int[] purses  = new int[6];
	boolean[] inPenaltyBox  = new boolean[6];
	
	LinkedList popQuestions = new LinkedList();
	LinkedList scienceQuestions = new LinkedList();
	LinkedList sportsQuestions = new LinkedList();
	LinkedList rockQuestions = new LinkedList();
	
	int currentPlayer = 0;
	boolean isGettingOutOfPenaltyBox;

	boolean firstRun = true;

	public void WriteToFile(String line){
		try {
			File outputFile = new File("/Users/oliverwalsh/git/trivia-refactoring/java/src/main/java/com/adaptionsoft/games/uglytrivia/testoutput.txt");
			if(firstRun){
				FileWriter outWriter = new FileWriter(outputFile, false);
				outWriter.write("");
				outWriter.close();
				firstRun = false;
			}
			FileWriter outWriter = new FileWriter(outputFile, true);
			outWriter.write(line + "\n");
			outWriter.close();
		} catch (IOException e){
			System.out.println(e);
		}
	}
// actual code below this line
	public  Game(){
		for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(("Rock Question " + i));
		}
	}

	public boolean add(String playerName) {
		players.add(playerName);
		int playerNumber = players.size();
		places[playerNumber] = 0;
		purses[playerNumber] = 0;
		inPenaltyBox[playerNumber] = false;
		WriteToFile(playerName + " was added\nThey are player number " + playerNumber);
		return true;
	}
	public void isValidRoll(int roll){
		places[currentPlayer] = places[currentPlayer] + roll;
		if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
	}
	public void showLocationAndCategory(){
		WriteToFile(players.get(currentPlayer) 
						+ "'s new location is " 
						+ places[currentPlayer]);
		WriteToFile("The category is " + currentCategory());
		askQuestion();
	}
	public void leavePenaltyBox(int roll){
		if (roll % 2 != 0) {
			isGettingOutOfPenaltyBox = true;
			WriteToFile(players.get(currentPlayer) + " is getting out of the penalty box");
			isValidRoll(roll);
			showLocationAndCategory();
		} else {
			WriteToFile(players.get(currentPlayer) + " is not getting out of the penalty box");
			isGettingOutOfPenaltyBox = false;
		}
	}
	public void roll(int roll) {
		WriteToFile(players.get(currentPlayer) + " is the current player");
		WriteToFile("They have rolled a " + roll);
		if (inPenaltyBox[currentPlayer]) {
			leavePenaltyBox(roll);
		} else {
			isValidRoll(roll);
			showLocationAndCategory();
		}
		
	}
	private void askQuestion() {
		if (currentCategory() == "Pop")
			WriteToFile(popQuestions.removeFirst().toString());
		if (currentCategory() == "Science")
			WriteToFile(scienceQuestions.removeFirst().toString());
		if (currentCategory() == "Sports")
			WriteToFile(sportsQuestions.removeFirst().toString());
		if (currentCategory() == "Rock")
			WriteToFile(rockQuestions.removeFirst().toString());		
	}
	private String currentCategory() {
		if((places[currentPlayer] + 4) % 4 == 0){ return "Pop"; }
		if((places[currentPlayer] + 4) % 4 == 1){ return "Science"; }
		if((places[currentPlayer] + 4) % 4 == 2){ return "Sports"; }
		return "Rock";
	}
	public void incrementGoldCoins(){
		purses[currentPlayer]++;
		WriteToFile(players.get(currentPlayer) 
				+ " now has "
				+ purses[currentPlayer]
				+ " Gold Coins.");
	}
	public void nextPlayer(){
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
	}
	public boolean winnerAfterCorrectlyAnswering(){
		WriteToFile("Answer was correct!!!!");
		incrementGoldCoins();
		boolean winner = !(purses[currentPlayer] == 6);
		nextPlayer();
		return winner;
	}
	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				return winnerAfterCorrectlyAnswering();
			} else {
				nextPlayer();
				return true;
			}
		} else {
			return winnerAfterCorrectlyAnswering();
		}
	}
	public boolean wrongAnswer(){
		WriteToFile("Question was incorrectly answered");
		WriteToFile(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		nextPlayer();
		return true;
	}
}
