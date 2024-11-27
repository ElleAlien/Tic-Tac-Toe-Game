import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToe extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[9];
    private String currentPlayer = "X"; // Default to X
    private String playerSymbol = "";
    private String computerSymbol = "";
    private boolean gameOver = false;

    public TicTacToe() {
        setTitle("Tic Tac Toe Game");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 3));
        initializeButtons();

        // Ask user for symbol each time the game starts
        askForSymbol();
    }

    private void askForSymbol() {
        String[] options = {"X", "O"};
        int choice = JOptionPane.showOptionDialog(this, "Choose your symbol", "Pick Symbol", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice == 0) {
            playerSymbol = "X";
            computerSymbol = "O"; // If player chooses X, computer plays O
        } else {
            playerSymbol = "O";
            computerSymbol = "X"; // If player chooses O, computer plays X
        }
        currentPlayer = playerSymbol; // Player goes first
        resetGame();
    }

    private void initializeButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 60));
            buttons[i].setBackground(Color.ORANGE);
            buttons[i].setFocusPainted(false);
            buttons[i].setActionCommand(String.valueOf(i));
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        JButton clickedButton = (JButton) e.getSource();
        int buttonIndex = Integer.parseInt(clickedButton.getActionCommand());

        // Allow move only if the button is empty
        if (clickedButton.getText().equals("") && !gameOver) {
            clickedButton.setText(currentPlayer);
            clickedButton.setForeground(currentPlayer.equals("X") ? Color.BLUE : Color.YELLOW);

            // Check if the player wins after this move
            if (checkWinner()) {
                JOptionPane.showMessageDialog(this, (currentPlayer.equals(playerSymbol) ? "Congrats! You win!" : "Sorry, you lost!") + " Do you want to play again?");
                int option = JOptionPane.showOptionDialog(this, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Yes", "No"}, null);
                if (option == JOptionPane.YES_OPTION) {
                    askForSymbol();
                } else {
                    System.exit(0);
                }
            } else if (isBoardFull()) {
                JOptionPane.showMessageDialog(this, "It's a draw! Do you want to try again?");
                int option = JOptionPane.showOptionDialog(this, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Yes", "No"}, null);
                if (option == JOptionPane.YES_OPTION) {
                    askForSymbol();
                } else {
                    System.exit(0);
                }
            } else {
                // Change turn to computer after player move
                currentPlayer = computerSymbol;
                computerMove();
            }
        }
    }

    private void computerMove() {
        // Simple AI for computer (random move)
        if (gameOver) return;
        int move = (int) (Math.random() * 9);
        while (!buttons[move].getText().equals("")) {
            move = (int) (Math.random() * 9);
        }
        buttons[move].setText(computerSymbol);
        buttons[move].setForeground(computerSymbol.equals("X") ? Color.BLUE : Color.YELLOW);

        // Check if computer wins after its move
        if (checkWinner()) {
            JOptionPane.showMessageDialog(this, "Sorry, you lost! Try again?");
            int option = JOptionPane.showOptionDialog(this, "Play again?", "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Yes", "No"}, null);
            if (option == JOptionPane.YES_OPTION) {
                askForSymbol();
            } else {
                System.exit(0);
            }
        } else {
            currentPlayer = playerSymbol; // Change back to player's turn
        }
    }

    private boolean checkWinner() {
        int[][] winPatterns = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
                {0, 4, 8}, {2, 4, 6} // diagonals
        };

        for (int[] pattern : winPatterns) {
            String b1 = buttons[pattern[0]].getText();
            String b2 = buttons[pattern[1]].getText();
            String b3 = buttons[pattern[2]].getText();
            if (b1.equals(b2) && b2.equals(b3) && !b1.equals("")) {
                gameOver = true;
                highlightWinner(pattern);
                return true;
            }
        }
        return false;
    }

    private void highlightWinner(int[] pattern) {
        for (int i : pattern) {
            buttons[i].setBackground(Color.GREEN);
        }
    }

    private boolean isBoardFull() {
        for (JButton button : buttons) {
            if (button.getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        for (JButton button : buttons) {
            button.setText("");
            button.setBackground(Color.ORANGE);
        }
        currentPlayer = playerSymbol; // Player starts first
        gameOver = false;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TicTacToe().setVisible(true);
        });
    }
}


