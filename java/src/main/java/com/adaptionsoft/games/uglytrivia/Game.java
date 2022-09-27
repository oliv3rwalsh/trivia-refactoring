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

	public  Game(){
		for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(createRockQuestion(i));
		}
	}

	public String createRockQuestion(int index){
		return "Rock Question " + index;
	}
	
	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {
		
		
		players.add(playerName);
		places[howManyPlayers()] = 0;
		purses[howManyPlayers()] = 0;
		inPenaltyBox[howManyPlayers()] = false;
		
		WriteToFile(playerName + " was added");
		WriteToFile("They are player number " + players.size());
		return true;
	}
	
	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		WriteToFile(players.get(currentPlayer) + " is the current player");
		WriteToFile("They have rolled a " + roll);
		
		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;
				
				WriteToFile(players.get(currentPlayer) + " is getting out of the penalty box");
				places[currentPlayer] = places[currentPlayer] + roll;
				if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
				
				WriteToFile(players.get(currentPlayer) 
						+ "'s new location is " 
						+ places[currentPlayer]);
				WriteToFile("The category is " + currentCategory());
				askQuestion();
			} else {
				WriteToFile(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				}
			
		} else {
		
			places[currentPlayer] = places[currentPlayer] + roll;
			if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;
			
			WriteToFile(players.get(currentPlayer) 
					+ "'s new location is " 
					+ places[currentPlayer]);
			WriteToFile("The category is " + currentCategory());
			askQuestion();
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
		if (places[currentPlayer] == 0) return "Pop";
		if (places[currentPlayer] == 4) return "Pop";
		if (places[currentPlayer] == 8) return "Pop";
		if (places[currentPlayer] == 1) return "Science";
		if (places[currentPlayer] == 5) return "Science";
		if (places[currentPlayer] == 9) return "Science";
		if (places[currentPlayer] == 2) return "Sports";
		if (places[currentPlayer] == 6) return "Sports";
		if (places[currentPlayer] == 10) return "Sports";
		return "Rock";
	}

	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				WriteToFile("Answer was correct!!!!");
				purses[currentPlayer]++;
				WriteToFile(players.get(currentPlayer) 
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");
				
				boolean winner = didPlayerWin();
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				
				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {
		
			WriteToFile("Answer was corrent!!!!");
			purses[currentPlayer]++;
			WriteToFile(players.get(currentPlayer) 
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");
			
			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			
			return winner;
		}
	}
	
	public boolean wrongAnswer(){
		WriteToFile("Question was incorrectly answered");
		WriteToFile(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}

}
