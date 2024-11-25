package bank;


import bank.views.BankFrame;

public class Main {
    public static void main(String[] args) {
        // Configuración del Look and Feel de Nimbus
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Inicia la aplicación mostrando el BankFrame
        java.awt.EventQueue.invokeLater(() -> {
            new BankFrame().setVisible(true);
        });
    }
}
