import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HkgCipherAlgo extends JFrame {
    private JTextArea inputText, outputText;
    private JButton encryptButton, decryptButton;
    private JTextField decryptionKeyField;

    public HkgCipherAlgo() {
        super("Vigenere Cipher");

        JPanel panel = new JPanel(new BorderLayout(5, 5));

        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));

        JLabel inputLabel = new JLabel("Enter Password:");
        inputText = new JTextArea(5, 20);
        JScrollPane inputScrollPane = new JScrollPane(inputText);

        encryptButton = new JButton("Encrypt");

        JLabel outputLabel = new JLabel("Encrypted Text:");
        outputText = new JTextArea(5, 20);
        outputText.setEditable(true);
        JScrollPane outputScrollPane = new JScrollPane(outputText);

        JLabel keyLabel = new JLabel("Enter Key:");
        decryptionKeyField = new JTextField(20);      
        decryptButton = new JButton("Decrypt");
        encryptButton.addActionListener(e -> {
            String password = inputText.getText();
            String key = generateRandomKey(password.length());
            String encrypted = vigenereCipher(password, key);
            outputText.setText(encrypted);
            saveToFile("encrypted.txt", key, encrypted);
        });

        decryptButton.addActionListener(e -> {
            String encrypted = outputText.getText();
            String key = decryptionKeyField.getText();
            String decrypted = vigenereDecipher(encrypted, key);
            inputText.setText(decrypted);
        });
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(inputLabel, BorderLayout.NORTH);
        topPanel.add(inputScrollPane, BorderLayout.CENTER);
        topPanel.add(encryptButton, BorderLayout.SOUTH);       
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.add(outputLabel, BorderLayout.NORTH);
        centerPanel.add(outputScrollPane, BorderLayout.CENTER); 
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.add(keyLabel, BorderLayout.NORTH);
        bottomPanel.add(decryptionKeyField, BorderLayout.CENTER);
        bottomPanel.add(decryptButton, BorderLayout.SOUTH);      
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        add(panel);
        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    private String generateRandomKey(int length) {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < length; i++) {
            key.append((char) (random.nextInt(94) + 33));
        }
        return key.toString();
    }
    private String vigenereCipher(String text, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char k = key.charAt(i % key.length());

            int encryptedChar = ((c - 33 + k - 33) % 94) + 33;
            result.append((char) encryptedChar);
        }
        return result.toString();
    }
    private String vigenereDecipher(String text, String key) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            char k = key.charAt(i % key.length());

            int decryptedChar = ((c - 33 - (k - 33) + 94) % 94) + 33;
            result.append((char) decryptedChar);
        }
        return result.toString();
    }

    private void saveToFile(String filename, String key, String encrypted) {
        try {
            String directoryPath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "password";
            File directory = new File(directoryPath);  
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directoryPath + File.separator + filename);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            writer.write("Key: " + key + ", Encrypted Password: " + encrypted + ", Time Created: " + dtf.format(now));
            writer.newLine();

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new HkgCipherAlgo());
    }
}

