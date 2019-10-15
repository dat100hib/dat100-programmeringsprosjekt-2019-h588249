package no.hvl.dat100ptc.oppgave2;

import no.hvl.dat100ptc.TODO;
import no.hvl.dat100ptc.oppgave1.GPSPoint;

public class GPSData
// curlybrackets on next line, we are not monsters...
{

	private GPSPoint[] gpspoints;
	protected int antall = 0;

	public GPSData(int antall)
	{
		gpspoints = new GPSPoint[antall];
		this.antall = 0;
	}

	public GPSPoint[] getGPSPoints() {
		return this.gpspoints;
	}
	
	protected boolean insertGPS(GPSPoint gpspoint)
	{
		if (antall == gpspoints.length)
		{
			return false;
		}

		gpspoints[antall++] = gpspoint;

		return true;
	}

	public boolean insert(String time, String latitude, String longitude, String elevation)
	{

		return insertGPS(GPSDataConverter.convert(time, latitude, longitude, elevation));
	}

	public void print()
	{
		System.out.println("====== Konvertert GPS Data - START ======");
		for (GPSPoint point : gpspoints)
		{
			if (point != null)
			{
				System.out.print(point);
			}
		}
		System.out.println("====== Konvertert GPS Data - SLUTT ======");
	}
}
