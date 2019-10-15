package no.hvl.dat100ptc.oppgave5;

import javax.swing.JOptionPane;

import easygraphics.EasyGraphics;
import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave3.GPSUtils;
import no.hvl.dat100ptc.oppgave4.GPSComputer;

import java.util.concurrent.ThreadLocalRandom;

public class ShowRoute extends EasyGraphics {

	private static int MARGIN = 50;
	private static int MAPXSIZE = 600;
	private static int MAPYSIZE = 600;

	private GPSPoint[] gpspoints;
	private GPSComputer gpscomputer;
	
	public ShowRoute()
	{

		String filename = JOptionPane.showInputDialog("GPS data filnavn: ");
		gpscomputer = new GPSComputer(filename);

		gpspoints = gpscomputer.getGPSPoints();

	}

	public static void main(String[] args) {
		launch(args);
	}

	public void run()
	{
		makeWindow("Route", MAPXSIZE + 2 * MARGIN, MAPYSIZE + 2 * MARGIN);

		showRouteMap(MARGIN + MAPYSIZE);

		playRoute(MARGIN + MAPYSIZE);

		showStatistics();
	}

	// antall x-pixels per lengdegrad
	public double xstep()
	{

		double maxlon = GPSUtils.findMax(GPSUtils.getLongitudes(gpspoints));
		double minlon = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));

		return MAPXSIZE / (Math.abs(maxlon - minlon));
	}

	// antall y-pixels per breddegrad
	public double ystep()
	{
	
		double maxlat = GPSUtils.findMax(GPSUtils.getLatitudes(gpspoints));
		double minlat = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		return MAPYSIZE / (Math.abs(maxlat - minlat));
	}

	public void showRouteMap(int ybase)
	{
		double minX = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		double minY = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		GPSPoint lastPoint = null;

		setColor(0, 255, 0);

		for (GPSPoint point : gpspoints)
		{
			if (lastPoint != null)
			{
				drawLine(MARGIN + (int)((lastPoint.getLongitude() - minX) * xstep()),
						ybase - (int)((lastPoint.getLatitude() - minY) * ystep()),
						MARGIN + (int)((point.getLongitude() - minX) * xstep()),
						ybase - (int)((point.getLatitude() - minY) * ystep()));
			}

			fillCircle(MARGIN + (int)((point.getLongitude() - minX) * xstep()),
					  ybase - (int)((point.getLatitude() - minY) * ystep()), 2);

			lastPoint = point;
		}
	}

	public void showStatistics()
	{
		int TEXTDISTANCE = 20;

		setColor(0,0,0);
		setFont("Courier",12);

		drawString(String.format("Total Time     :    %s", GPSUtils.formatTime(gpscomputer.totalTime())), TEXTDISTANCE, TEXTDISTANCE);
		drawString(String.format("Total distance :    %s km", GPSUtils.formatDouble((gpscomputer.totalDistance() / 1000))), TEXTDISTANCE, TEXTDISTANCE + 10);
		drawString(String.format("Total elevation:    %s m", GPSUtils.formatDouble(gpscomputer.totalElevation())), TEXTDISTANCE, TEXTDISTANCE + 20);
		drawString(String.format("Max speed      :    %s km/t", GPSUtils.formatDouble(gpscomputer.maxSpeed())), TEXTDISTANCE, TEXTDISTANCE + 30);
		drawString(String.format("Average speed  :    %s km/t", GPSUtils.formatDouble(gpscomputer.averageSpeed())), TEXTDISTANCE, TEXTDISTANCE + 40);
		drawString(String.format("Energy (80kg)  :    %s kcal", GPSUtils.formatDouble(gpscomputer.totalKcal(80))), TEXTDISTANCE, TEXTDISTANCE + 50);
	}

	public void playRoute(int ybase)
	{

		double minX = GPSUtils.findMin(GPSUtils.getLongitudes(gpspoints));
		double minY = GPSUtils.findMin(GPSUtils.getLatitudes(gpspoints));

		setColor(0,0,255);
		int marker = -1;

		int i = 0;

		for (GPSPoint point : gpspoints)
		{
			if (marker == -1)
			{
				marker = fillCircle(MARGIN + (int)((point.getLongitude() - minX) * xstep()),
						ybase - (int)((point.getLatitude() - minY) * ystep()), 4);
			}
			else
			{
				setSpeed((int)(Math.max(gpscomputer.speeds()[i] / gpscomputer.maxSpeed(), 0.1) * 10));
				moveCircle(marker,MARGIN + (int)((point.getLongitude() - minX) * xstep()),
						ybase - (int)((point.getLatitude() - minY) * ystep()));

				i++;
			}
		}
	}

}
