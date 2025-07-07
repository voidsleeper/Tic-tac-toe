import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;

public class GameBoard extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private Player player;
    private JLabel statusLabel, scoreLabel, picLabel;
    private int moves = 0;
    private char currentMark = 'X';
    private String mode;
    private Random rand = new Random();

    public GameBoard(Player player, String mode) {
        this.player = player;
        this.mode = mode;

        setTitle("Tic Tac Toe");
        setSize(500, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 50);

        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setFont(font);
                final int x = i, y = j;
                buttons[i][j].addActionListener(e -> handleClick(x, y));
                grid.add(buttons[i][j]);
            }

        JPanel topPanel = new JPanel(new BorderLayout());
        statusLabel = new JLabel("Your Turn (X)", JLabel.CENTER);
        scoreLabel = new JLabel("Wins: 0", JLabel.CENTER);
        picLabel = new JLabel(player.getProfilePicture(), JLabel.CENTER);

        topPanel.add(statusLabel, BorderLayout.NORTH);
        topPanel.add(scoreLabel, BorderLayout.CENTER);
        topPanel.add(picLabel, BorderLayout.SOUTH);

        JButton reset = new JButton("Reset");
        reset.addActionListener(e -> resetBoard());

        JButton save = new JButton("Save History");
        save.addActionListener(e -> saveHistory());

        JPanel bottom = new JPanel();
        bottom.add(reset);
        bottom.add(save);

        add(topPanel, BorderLayout.NORTH);
        add(grid, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void handleClick(int row, int col) {
        if (!buttons[row][col].getText().isEmpty()) return;

        buttons[row][col].setText(String.valueOf(currentMark));
        buttons[row][col].setEnabled(false);
        moves++;

        if (checkWin()) {
            JOptionPane.showMessageDialog(this, currentMark + " Wins!");
            player.incrementWins();
            scoreLabel.setText("Wins: " + player.getWins());
            if (player.getWins() >= 5) {
                JOptionPane.showMessageDialog(this, "🎉 Game Over. 5 Wins Achieved!");
                System.exit(0);
            }
            resetBoard();
        } else if (moves == 9) {
            JOptionPane.showMessageDialog(this, "Draw!");
            resetBoard();
        } else {
            currentMark = currentMark == 'X' ? 'O' : 'X';
            statusLabel.setText("Your Turn (" + currentMark + ")");

            if (mode.contains("Single") && currentMark == 'O') {
                makeAIMove();
            }
        }
    }

    private void makeAIMove() {
        if (mode.contains("Easy")) {
            makeRandomMove();
        } else {
            if (!makeWinningMove('O'))
                if (!makeWinningMove('X'))
                    makeStrategicMove();
        }
    }

    private void makeRandomMove() {
        ArrayList<JButton> empty = new ArrayList<>();
        for (JButton[] row : buttons)
            for (JButton btn : row)
                if (btn.getText().isEmpty()) empty.add(btn);

        if (!empty.isEmpty()) {
            JButton btn = empty.get(rand.nextInt(empty.size()));
            btn.doClick();
        }
    }

    private boolean makeWinningMove(char symbol) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (buttons[i][j].getText().isEmpty()) {
                    buttons[i][j].setText(String.valueOf(symbol));
                    if (checkWin()) {
                        if (symbol == 'O') {
                            buttons[i][j].setEnabled(false);
                        } else {
                            buttons[i][j].setText("O");
                            buttons[i][j].setEnabled(false);
                        }
                        return true;
                    }
                    buttons[i][j].setText("");
                }
            }
        }
        return false;
    }

    private void makeStrategicMove() {
        if (buttons[1][1].getText().isEmpty()) {
            buttons[1][1].doClick();
            return;
        }
        int[][] corners = {{0,0},{0,2},{2,0},{2,2}};
        for (int[] pos : corners)
            if (buttons[pos[0]][pos[1]].getText().isEmpty()) {
                buttons[pos[0]][pos[1]].doClick();
                return;
            }
        makeRandomMove();
    }

    private boolean checkWin() {
        String mark = String.valueOf(currentMark);
        for (int i = 0; i < 3; i++)
            if (mark.equals(buttons[i][0].getText()) && mark.equals(buttons[i][1].getText()) && mark.equals(buttons[i][2].getText())) return true;
        for (int j = 0; j < 3; j++)
            if (mark.equals(buttons[0][j].getText()) && mark.equals(buttons[1][j].getText()) && mark.equals(buttons[2][j].getText())) return true;
        if (mark.equals(buttons[0][0].getText()) && mark.equals(buttons[1][1].getText()) && mark.equals(buttons[2][2].getText())) return true;
        if (mark.equals(buttons[0][2].getText()) && mark.equals(buttons[1][1].getText()) && mark.equals(buttons[2][0].getText())) return true;
        return false;
    }

    private void resetBoard() {
        moves = 0;
        currentMark = 'X';
        statusLabel.setText("Your Turn (X)");
        for (JButton[] row : buttons)
            for (JButton btn : row) {
                btn.setText("");
                btn.setEnabled(true);
            }
    }

    private void saveHistory() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("game_history.txt", true))) {
            writer.write(player.getName() + " - Wins: " + player.getWins() + " - " + LocalDateTime.now());
            writer.newLine();
            JOptionPane.showMessageDialog(this, "Game history saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}