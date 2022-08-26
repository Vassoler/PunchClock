package org.scraper.controller;

import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeClockController {
	public void initialize(JLabel clock){
		runningTime(clock);
	}

	private void runningTime(JLabel clock){
		final Thread thread = new Thread(()->{
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
			while (true){
				try {
					Thread.sleep(1000);
				}
				catch (InterruptedException e){
					e.printStackTrace();
				}

				final String time = simpleDateFormat.format(new Date());
				clock.setText(time);
			}
		});
		thread.start();
	}
}
