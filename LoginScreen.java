// === LoginScreen.java ===
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LoginScreen extends JFrame {
    private JTextField nameField;
    private JLabel imageLabel;
    private ImageIcon profilePic;
    private JComboBox<String> modeSelect;

    public LoginScreen() {
        setTitle("Login");
        setLayout(new BorderLayout());
        setSize(400, 350);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel centerPanel = new JPanel(new GridLayout(4, 1));

        nameField = new JTextField();
        modeSelect = new JComboBox<>(new String[]{"Single Player - Easy", "Single Player - Hard", "2 Player"});

        centerPanel.add(new JLabel("Enter your name:"));
        centerPanel.add(nameField);
        centerPanel.add(modeSelect);

        JButton uploadButton = new JButton("Upload Profile Picture");
        uploadButton.addActionListener(e -> choosePicture());
        centerPanel.add(uploadButton);

        imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(JLabel.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> startGame());

        add(centerPanel, BorderLayout.CENTER);
        add(imageLabel, BorderLayout.SOUTH);
        add(startButton, BorderLayout.NORTH);

        setVisible(true);
    }

    private void choosePicture() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            profilePic = new ImageIcon(file.getAbsolutePath());
            Image img = profilePic.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            profilePic = new ImageIcon(img);
            imageLabel.setIcon(profilePic);
        }
    }

    private void startGame() {
        String name = nameField.getText().trim();
        if (name.isEmpty() || profilePic == null) {
            JOptionPane.showMessageDialog(this, "Please enter a name and upload a picture.");
            return;
        }

        Player player = new Player(name, profilePic);
        String mode = (String) modeSelect.getSelectedItem();
        new GameBoard(player, mode);
        dispose();
    }
}
