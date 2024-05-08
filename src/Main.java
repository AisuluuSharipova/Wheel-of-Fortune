import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static final String RESET = "\u001B[0m";
    public static final String BRIGHT_CYAN = "\u001B[1;36m";
    public static final String BOLD_BRIGHT_RED = "\u001B[1;91m";
    public static final String GREEN = "\u001B[38;2;102;255;0m";

    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        ArrayList<String> playersNames = new ArrayList<>();
        ArrayList<Integer> playerScores = new ArrayList<>();
        ArrayList<String> guessedLetter = new ArrayList<>();

        StringBuilder hiddenWord = new StringBuilder();

        Random random = new Random();

        List<String> answers = new ArrayList<>();
        Collections.addAll(answers, "NAURU", "SCORPION", "OCTOPUS", "PAN", "LONELINESS", "CONFIDENCE", "CARIES", "EXAMPLE", "CARROTS", "FLOWERS");

        List<String> questions = new ArrayList<>();
        Collections.addAll(questions, "Which country does not have a capital?", "What creature can hold its breath for 6 days?", "What living creature has blue blood?", "What was used in China for ironing clothes instead of an iron?", "Jewelers often say that diamonds need this.", "The English writer Kipling said: 'Women’s intuition is much more accurate than men’s...'.", "What is considered the most common non-communicable disease in the world?", "Best way to demonstrate.", "A minute of laughter is as beneficial as one kilogram of... what?", "What is it not customary to bring with you when visiting China, so as not to offend the hosts?");


        System.out.println(BRIGHT_CYAN + "Welcome to the game 'Wheel of Fortune!'" + RESET);

        System.out.print(BRIGHT_CYAN + "How many players will be playing? " + RESET);
        int playersNumber = scanner.nextInt();
        System.out.println(BRIGHT_CYAN + "Great! Please enter the names of the players:" + RESET);

        String name = "";
        for (int i = 0; i < playersNumber; i++) {
            System.out.print(BRIGHT_CYAN + "Player " + (i + 1) + ": " + RESET);
            name = scanner.next();
            playersNames.add(name);
            playerScores.add(0);
        }
        cleanScreen();

        Collections.shuffle(playersNames);
        System.out.println(BRIGHT_CYAN + "Here is the order of turns:" + RESET);

        for (int i = 0; i < playersNumber; i++) {
            System.out.println((i + 1) + ". " + playersNames.get(i));
        }
        Thread.sleep(4000);
        cleanScreen();


        System.out.println(BRIGHT_CYAN + "Are you ready to dive into the Wheel of Fortune game?" + RESET);

        String readyAnswer = scanner.next().toLowerCase();
        if (readyAnswer.equals("no")) {
            System.out.println(BRIGHT_CYAN + "Maybe next time!" + RESET);
        } else {
            System.out.println(BOLD_BRIGHT_RED + "Invalid input. Please type 'yes' or 'no'." + RESET);
        }

        cleanScreen();

        int randomNumber = random.nextInt(10);

        String hiddenToString = "";
        for (int i = 0; i < answers.get(randomNumber).length(); i++) {
            hiddenWord.append("⬜ ");
            hiddenToString = hiddenWord.toString();
        }

        String winnerFromWholeWord = "";
        while (hiddenToString.contains("⬜") && playersNumber > 0) {
            for (int i = 0; i < playersNumber; i++) {

                displayQuestion(questions.get(randomNumber), hiddenToString, playersNames, playerScores, guessedLetter);

                System.out.println(BRIGHT_CYAN + playersNames.get(i) + ", it's your turn.Guess a letter or the whole word: " + RESET);
                String guess = scanner.next().toUpperCase();
                guessedLetter.add(guess);

                if (guess.length() == 1) {
                    if (answers.get(randomNumber).contains(guess)) {
                        if (!hiddenWord.toString().contains(guess)) {
                            int index = answers.get(randomNumber).indexOf(guess);
                            while (index != -1) {
                                hiddenWord.setCharAt(index * 2, guess.charAt(0));
                                index = answers.get(randomNumber).indexOf(guess, index + 1);
                                int currentScore = playerScores.get(i);
                                playerScores.set(i, currentScore + 100);
                            }
                            hiddenToString = hiddenWord.toString();

                            displayQuestion(questions.get(randomNumber), hiddenToString, playersNames, playerScores, guessedLetter);
                            System.out.println(BRIGHT_CYAN + "Great job, " + playersNames.get(i) + "! You found the letter '" + guess + "'. Your score is now " + playerScores.get(i) + RESET);

                            Thread.sleep(2000);
                            cleanScreen();

                            if (!hiddenToString.contains("⬜")) {
                                break;
                            }
                            i--;
                        } else if(guessedLetter.contains(guess)) {
                            displayQuestion(questions.get(randomNumber), hiddenToString, playersNames, playerScores, guessedLetter);
                            System.out.println(BOLD_BRIGHT_RED + "Sorry, this letter was entered. Let's move to the next player." + RESET);

                            Thread.sleep(3000);
                            cleanScreen();
                        }
                    } else {
                        displayQuestion(questions.get(randomNumber), hiddenToString, playersNames, playerScores, guessedLetter);
                        System.out.println(BOLD_BRIGHT_RED + "Sorry, the word does not contain this letter. Let's move to the next player." + RESET);
                        Thread.sleep(3000);
                        cleanScreen();
                    }
                } else {
                    if (!guess.equals(answers.get(randomNumber))) {
                        displayQuestion(questions.get(randomNumber), hiddenToString, playersNames, playerScores, guessedLetter);
                        System.out.println(BOLD_BRIGHT_RED + "Sorry, your guess was incorrect.You have to leave game." + RESET);

                        Thread.sleep(3000);
                        cleanScreen();

                        boolean moreThanHalf = true;
                        if (playerScores.get(i) <= answers.get(randomNumber).length() * 100 / 2) {
                            moreThanHalf = false;
                        }

                        if (moreThanHalf) {
                            playersNames.remove(i);
                            playerScores.remove(i);
                            playersNumber--;

                            for (int j = 0; j < playersNumber; j++) {
                                displayQuestion(questions.get(randomNumber), hiddenToString, playersNames, playerScores, guessedLetter);

                                System.out.println(BRIGHT_CYAN + playersNames.get(i) + ", it's your turn to guess the word. Please enter the whole word:" + RESET);

                                guess = scanner.next().toUpperCase();
                                if (guess.equals(answers.get(randomNumber))) {
                                    int currentScore = playerScores.get(i);
                                    int additionalPoints = 0;

                                    for (int in = 0; in < hiddenToString.length(); in++) {
                                        if (hiddenToString.charAt(in) == '_') {
                                            additionalPoints += 100;
                                        }
                                    }
                                    hiddenWord.setLength(0);
                                    hiddenWord.append(answers.get(randomNumber));
                                    hiddenToString = hiddenWord.toString();
                                    displayQuestion(questions.get(randomNumber), hiddenToString, playersNames, playerScores, guessedLetter);

                                    playerScores.set(i, currentScore + additionalPoints);
                                    System.out.println(BRIGHT_CYAN + "Congratulations, " + playersNames.get(i) + "! You guessed the whole word correctly." + RESET);

                                    winnerFromWholeWord = playersNames.get(i);

                                    Thread.sleep(3000);
                                    cleanScreen();
                                    i = playersNumber;
                                    break;
                                } else {
                                    displayQuestion(questions.get(randomNumber), hiddenToString, playersNames, playerScores, guessedLetter);
                                    System.out.println(BOLD_BRIGHT_RED + "Sorry, your guess was incorrect. Let's move to the next player." + RESET);

                                    i++;
                                    Thread.sleep(3000);
                                    cleanScreen();
                                }
                            }
                            playersNumber = 0;
                            break;
                        } else {
                            playersNames.remove(i);
                            playerScores.remove(i);
                            playersNumber--;

                        }
                    } else {
                        int currentScore = playerScores.get(i);
                        int additionalPoints = 0;

                        for (int inj = 0; inj < hiddenToString.length(); inj++) {
                            if (hiddenToString.charAt(inj) == '_') {
                                additionalPoints += 100;
                            }
                        }
                        hiddenWord.setLength(0);
                        hiddenWord.append(answers.get(randomNumber));
                        hiddenToString = hiddenWord.toString();
                        displayQuestion(questions.get(randomNumber), hiddenToString, playersNames, playerScores, guessedLetter);

                        playerScores.set(i, currentScore + additionalPoints);

                        System.out.println(BRIGHT_CYAN + "Congratulations, " + playersNames.get(i) + "! You guessed the whole word correctly." + RESET);

                        winnerFromWholeWord = playersNames.get(i);
                        Thread.sleep(3000);
                        cleanScreen();
                        i = playersNumber;
                        break;
                    }
                }
            }
            displayScores(playersNames, playerScores, winnerFromWholeWord, guessedLetter);
            if (!hiddenToString.contains("⬜")) {
                break;
            }
        }

        System.out.println(BRIGHT_CYAN + "The game is over! " + RESET);
        if (!winnerFromWholeWord.isEmpty()) {
            System.out.println(BRIGHT_CYAN + "The player " + winnerFromWholeWord + " guessed the whole word correctly and won the game." + RESET);
        } else {
            int maxScore = Collections.max(playerScores);
            List<String> winners = new ArrayList<>();
            for (int i = 0; i < playerScores.size(); i++) {
                if (playerScores.get(i) == maxScore) {
                    winners.add(playersNames.get(i));
                }
            }
            if (winners.size() == 1) {
                System.out.println(BRIGHT_CYAN + "The player " + winners.get(0) + " has the highest score and wins the game!" + RESET);
            } else {
                System.out.print(BRIGHT_CYAN + "It's a tie between ");
                for (int i = 0; i < winners.size(); i++) {
                    System.out.print(winners.get(i));
                    if (i < winners.size() - 1) {
                        System.out.print(" and ");
                    }
                }
                System.out.println("!");
            }
        }
    }

    public static void displayQuestion(String question, String hiddenToString, ArrayList<String> playersNames, ArrayList<Integer> playerScores, ArrayList<String> guessedLetter) {
        cleanScreen();

        int consoleWidth = 130;
        int padding = Math.max(0, (consoleWidth - question.length()) / 2);
        System.out.print(" ".repeat(padding));
        System.out.println(BRIGHT_CYAN + question + RESET);

        int consoleWidth2 = 120;
        int padding2 = Math.max(0, (consoleWidth - hiddenToString.length()) / 2);
        System.out.print(" ".repeat(padding2));

        System.out.println(hiddenToString);

        System.out.println(BRIGHT_CYAN + "Current Scores:" + RESET);
        for (int i = 0; i < playersNames.size(); i++) {
            System.out.println(BRIGHT_CYAN + playersNames.get(i) + ": " + RESET + GREEN + playerScores.get(i) + " points" + RESET);
        }
        System.out.println("____________________");

        System.out.println(BRIGHT_CYAN + "Used Letters:" + RESET);

        for (int i = 0; i < guessedLetter.size(); i++) {
            System.out.print(guessedLetter.get(i));
        }

        System.out.println();
        System.out.println(BRIGHT_CYAN + "_____________________" + RESET);

    }

    public static void displayScores(ArrayList<String> playersNames, ArrayList<Integer> playerScores, String winnerFromWholeWord, ArrayList<String> guessedLetter) {
        System.out.println(BRIGHT_CYAN + "Current Scores:" + RESET);
        for (int i = 0; i < playersNames.size(); i++) {
            System.out.println(BRIGHT_CYAN + playersNames.get(i) + ": " + RESET + GREEN + playerScores.get(i) + " points" + RESET);
        }

    }

    public static void cleanScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}

