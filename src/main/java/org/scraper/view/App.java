package org.scraper.view;

import org.scraper.controller.ApplicationProperties;
import org.scraper.controller.CrawlerController;
import org.scraper.controller.TimeClockController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

public class App {
	private JTextField login;
	private JPanel panelMain;
	private JPasswordField senha;
	private JButton btnIniciar;
	private JLabel clock;
	private JButton iniciarJornada;
	private JButton finalizarJornada;
	private JButton iniciarPausa;
	private JButton finalizarPausa;
	private JLabel lLogin;

	static CrawlerController crawlerController = new CrawlerController();
	static TimeClockController timeClockController = new TimeClockController();

	public App() {
		ApplicationProperties properties = new ApplicationProperties();
		timeClockController.initialize(clock);

		login.setText(properties.readProperty("cortinaweb.login"));
		senha.setText(properties.readProperty("cortinaweb.senha"));

		btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Map<String, String> paramsFront = new HashMap<>();
				paramsFront.put("LOGIN", login.getText());
				paramsFront.put("SENHA", senha.getText());
				try{
					crawlerController.iniciar(paramsFront);
					JOptionPane.showMessageDialog(null, "Sucesso!");
				}
				catch (Exception ee){
					JOptionPane.showMessageDialog(null, ee.getMessage());
				}
			}
		});

		iniciarJornada.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, String> paramsFront = new HashMap<>();
				paramsFront.put("LOGIN", login.getText());
				paramsFront.put("SENHA", senha.getText());
				try{
					crawlerController.iniciarJornada(paramsFront);
					JOptionPane.showMessageDialog(null, "Sucesso!");
				}
				catch (Exception ee){
					JOptionPane.showMessageDialog(null, ee.getMessage());
				}
			}
		});
		finalizarJornada.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, String> paramsFront = new HashMap<>();
				paramsFront.put("LOGIN", login.getText());
				paramsFront.put("SENHA", senha.getText());
				try{
					crawlerController.finalizarJornada(paramsFront);
					JOptionPane.showMessageDialog(null, "Sucesso!");
				}
				catch (Exception ee){
					JOptionPane.showMessageDialog(null, ee.getMessage());
				}
			}
		});
		iniciarPausa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, String> paramsFront = new HashMap<>();
				paramsFront.put("LOGIN", login.getText());
				paramsFront.put("SENHA", senha.getText());
				try{
					crawlerController.iniciarPausa(paramsFront);
					JOptionPane.showMessageDialog(null, "Sucesso!");
				}
				catch (Exception ee){
					JOptionPane.showMessageDialog(null, ee.getMessage());
				}
			}
		});
		finalizarPausa.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Map<String, String> paramsFront = new HashMap<>();
				paramsFront.put("LOGIN", login.getText());
				paramsFront.put("SENHA", senha.getText());
				try{
					crawlerController.finalizarPausa(paramsFront);
					JOptionPane.showMessageDialog(null, "Sucesso!");
				}
				catch (Exception ee){
					JOptionPane.showMessageDialog(null, ee.getMessage());
				}
			}
		});
	}

	public static void main(String[] args) {

		JFrame frame = new JFrame("AutoPunchClock");
		frame.setContentPane(new App().panelMain);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.pack();

		// here's the part where i center the jframe on screen
		frame.setLocationRelativeTo(null);

		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				crawlerController.logoff();
				e.getWindow().dispose();
				System.exit(0);
			}
		});
	}
}
