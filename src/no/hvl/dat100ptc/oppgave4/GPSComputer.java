package no.hvl.dat100ptc.oppgave4;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;
import no.hvl.dat100ptc.oppgave2.GPSData;
import no.hvl.dat100ptc.oppgave2.GPSDataConverter;
import no.hvl.dat100ptc.oppgave2.GPSDataFileReader;
import no.hvl.dat100ptc.oppgave3.GPSUtils;

public class GPSComputer
{
	private GPSPoint[] gpspoints;
	
	public GPSComputer(String filename)
	{

		GPSData gpsdata = GPSDataFileReader.readGPSFile(filename);
		gpspoints = gpsdata.getGPSPoints();

	}

	public GPSComputer(GPSPoint[] gpspoints) {
		this.gpspoints = gpspoints;
	}
	
	public GPSPoint[] getGPSPoints() {
		return this.gpspoints;
	}
	
	// beregn total distances (i meter)
	public double totalDistance()
	{
		double distance = 0;
		for (int i = 0; i < gpspoints.length - 1; i++)
		{
			distance += GPSUtils.distance(gpspoints[i], gpspoints[i + 1]);
		}

		return distance;
	}

	// beregn totale høydemeter (i meter)
	public double totalElevation()
	{
		double elevation = 0;

		for (int i = 0; i < gpspoints.length - 1; i++)
		{
			if (gpspoints[i].getElevation() < gpspoints[i + 1].getElevation())
			{
				elevation += gpspoints[i + 1].getElevation() - gpspoints[i].getElevation();
			}
		}

		return elevation;
	}

	// beregn total tiden for hele turen (i sekunder)
	public int totalTime()
	{
		return gpspoints[gpspoints.length - 1].getTime() - gpspoints[0].getTime();
	}
		
	// beregn gjennomsnitshastighets mellom hver av gps punktene

	public double[] speeds()
	{
		double[] speeds = new double[gpspoints.length - 1];

		for (int i = 0; i < gpspoints.length - 1; i++)
		{
			speeds[i] = GPSUtils.speed(gpspoints[i], gpspoints[i + 1]);
		}

		return speeds;
	}
	
	public double maxSpeed()
	{
		double max = speeds()[0];

		for (double speed : speeds())
		{
			if (speed > max)
			{
				max = speed;
			}
		}

		return max;
	}

	public double averageSpeed()
	{
		return totalDistance() / totalTime() * 3.6;
	}

	/*
	 * bicycling, <10 mph, leisure, to work or for pleasure 4.0 bicycling,
	 * general 8.0 bicycling, 10-11.9 mph, leisure, slow, light effort 6.0
	 * bicycling, 12-13.9 mph, leisure, moderate effort 8.0 bicycling, 14-15.9
	 * mph, racing or leisure, fast, vigorous effort 10.0 bicycling, 16-19 mph,
	 * racing/not drafting or >19 mph drafting, very fast, racing general 12.0
	 * bicycling, >20 mph, racing, not drafting 16.0
	 */

	// conversion factor m/s to miles per hour
	public static double MS = 2.236936;

	// beregn kcal gitt weight og tid der kjøres med en gitt hastighet
	public double kcal(double weight, int secs, double speed)
	{
		// MET: Metabolic equivalent of task angir (kcal x kg-1 x h-1)
		double met = 4;
		double speedmph = speed * MS;

		if (speedmph >= 10){ met = 6; }
		if (speedmph >= 12){ met = 8; }
		if (speedmph >= 14){ met = 10; }
		if (speedmph >= 16){ met = 12; }
		if (speedmph >= 20){ met = 16; }

		return met * weight * (secs / 3600.0);
	}

	public double totalKcal(double weight)
	{
		return kcal(weight, totalTime(), averageSpeed());
	}
	
	private static double WEIGHT = 80.0;

	public double[] climbs()
	{
		double[] climbs = new double[gpspoints.length - 1];

		for (int i = 0; i < gpspoints.length - 1; i++)
		{
			climbs[i] = 100 * (gpspoints[i + 1].getElevation() - gpspoints[i].getElevation()) / GPSUtils.distance(gpspoints[i],gpspoints[i + 1]);
		}

		return climbs;
	}

	public double minClimb()
	{
		return GPSUtils.findMin(climbs());
	}

	public double maxClimb()
	{
		return GPSUtils.findMax(climbs());
	}
	
	public void displayStatistics()
	{

		System.out.println("==============================================");
		System.out.println("Total time     :   " + GPSUtils.formatTime(totalTime()));
		System.out.println("Total distance :" + GPSUtils.formatDouble((totalDistance() / 1000)) + " km");
		System.out.println("Total elevation:" + GPSUtils.formatDouble(totalElevation()) + " m");
		System.out.println("Max speed      :" + GPSUtils.formatDouble(maxSpeed()) + " km/t");
		System.out.println("Average speed  :" + GPSUtils.formatDouble(averageSpeed()) + " km/t");
		System.out.println("Energy         :" + GPSUtils.formatDouble(totalKcal(WEIGHT)) + " kcal");
		System.out.println("==============================================");
	}
}
