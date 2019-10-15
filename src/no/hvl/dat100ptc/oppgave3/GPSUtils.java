package no.hvl.dat100ptc.oppgave3;

import static java.lang.Math.*;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;

public class GPSUtils
{

	public static double findMax(double[] da) {

		double max; 
		
		max = da[0];
		
		for (double d : da)
		{
			if (d > max)
			{
				max = d;
			}
		}
		
		return max;
	}

	public static double findMin(double[] da)
	{

		double min;

		min = da[0];

		for (double d : da)
		{
			if (d < min)
			{
				min = d;
			}
		}

		return min;
	}

	public static double[] getLatitudes(GPSPoint[] gpspoints)
	{
		double[] points = new double[gpspoints.length];

		for (int i = 0; i < gpspoints.length; i++)
		{
			points[i] = gpspoints[i] != null ? gpspoints[i].getLatitude() : 0;
		}

		return points;
	}

	public static double[] getLongitudes(GPSPoint[] gpspoints)
	{
		double[] points = new double[gpspoints.length];

		for (int i = 0; i < gpspoints.length; i++)
		{
			points[i] = gpspoints[i] != null ? gpspoints[i].getLongitude() : 0;
		}

		return points;
	}

	private static int R = 6371000; // jordens radius

	public static double distance(GPSPoint gpspoint1, GPSPoint gpspoint2)
	{
		double phi1 = toRadians(gpspoint1.getLatitude());
		double phi2 = toRadians(gpspoint2.getLatitude());
		double deltaPhi = toRadians(gpspoint2.getLatitude()) - toRadians(gpspoint1.getLatitude());
		double deltaLambda = toRadians(gpspoint2.getLongitude()) - toRadians(gpspoint1.getLongitude());

		double a = pow((sin(deltaPhi / 2)), 2) + cos(phi1) * cos(phi2) * pow((sin(deltaLambda / 2)), 2);
		double c = 2 * atan2(sqrt(a), sqrt(1 - a));

		return c * R;
	}

	public static double speed(GPSPoint gpspoint1, GPSPoint gpspoint2)
	{
		int secs = gpspoint2.getTime() - gpspoint1.getTime();
		return distance(gpspoint1, gpspoint2) / (secs / 3.6);
	}

	public static String formatTime(int secs)
	{
		String hh = String.format("%02d", (secs / 3600));
		String mm = String.format("%02d", ((secs % 3600) / 60));
		String ss = String.format("%02d", (secs % 60));

		return String.format("  %s:%s:%s", hh, mm, ss);
	}

	private static int TEXTWIDTH = 10;

	public static String formatDouble(double d)
	{
		String dStr = String.format("%1$.2f", d);
		return String.format("%" + TEXTWIDTH + "s", dStr);
	}
}
