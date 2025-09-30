/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package sistempakar1;

import Tampilan.Login;
public class Sistempakar1 {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Login login = new Login();
                login.setSize(592, 527); // Sesuaikan dengan ukuran aslinya di GUI Builder
                login.setLocationRelativeTo(null);
                login.setVisible(true);

            }
        });
    }
}


