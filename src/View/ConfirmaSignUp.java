package src.View;

import com.amazonaws.services.cognitoidp.model.ConfirmSignUpResult;
import src.Session.Session;
import src.View.User.LoginGUI;
import src.config.CognitoService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConfirmaSignUp extends JFrame {

    private JTextField usernameField;
    private JTextField confirmationCodeField;
    private JButton confirmButton;
    private CognitoService cognitoService;

    public ConfirmaSignUp(CognitoService cognitoService) {
        this.cognitoService = cognitoService;
        setTitle("Confirme seu Email");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 150);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel usernameLabel = new JLabel("Email:");
        usernameField = new JTextField();
        JLabel confirmationCodeLabel = new JLabel("Codigo:");
        confirmationCodeField = new JTextField();

        confirmButton = new JButton("Confirmar");
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean check = confirmSignUp();
                if (check) {
                    LoginGUI loginGUI = new LoginGUI(cognitoService);
                    loginGUI.setVisible(true);
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(confirmationCodeLabel);
        panel.add(confirmationCodeField);
        panel.add(confirmButton);

        add(panel);
        setVisible(true);
    }

    private boolean confirmSignUp() {
        String email = usernameField.getText().trim();
        String confirmationCode = confirmationCodeField.getText().trim();
        ConfirmSignUpResult result = cognitoService.confirmUser(email, confirmationCode);
        if (result != null) {
            JOptionPane.showMessageDialog(this, "Confirmado");
            return true;
        } else {
            JOptionPane.showMessageDialog(this, "Não foi");
            return false;
        }
    }

    public static void main(String[] args) {
//        ConfirmaSignUp c = new ConfirmaSignUp();
//        c.setVisible(true);
    }
}

