package com;

public class Berth {
	
	private char berthType;
	private int berthNumber;
	private String name;
	private String companyName;
	private int berthNumberNormalized;
	private boolean visibleOnMap;
	
	/** Coordinates */
	private float rds_x, rds_y;
	private float lat, lng;
	
	public Berth() {}
	
	public static Float[] rd2wgs(float x, float y) {
		float dX = (x - 155000f) * (float) Math.pow(10, -5);
		float dY = (y - 463000f) * (float) Math.pow(10, -5);

		float SomN = ((3235.65389f * dY) + (-32.58297f * (float) Math.pow(dX, 2))
				+ (-0.2475f * (float) Math.pow(dY, 2))
				+ (-0.84978f * (float) Math.pow(dX, 2) * dY)
				+ (-0.0655f * (float) Math.pow(dY, 3))
				+ (-0.01709f * (float) Math.pow(dX, 2) * (float) Math.pow(dY, 2))
				+ (-0.00738f * dX) + (0.0053f * (float) Math.pow(dX, 4))
				+ (-0.00039f * (float) Math.pow(dX, 2) * (float) Math.pow(dY, 3))
				+ (0.00033f * (float) Math.pow(dX, 4) * dY) + (-0.00012f * dX * dY));
		float SomE = ((5260.52916f * dX) + (105.94684f * dX * dY)
				+ (2.45656f * dX * (float) Math.pow(dY, 2))
				+ (-0.81885f * (float) Math.pow(dX, 3))
				+ (0.05594f * dX * (float) Math.pow(dY, 3))
				+ (-0.05607f * (float) Math.pow(dX, 3) * dY) + (0.01199f * dY)
				+ (-0.00256f * (float) Math.pow(dX, 3) * (float) Math.pow(dY, 2))
				+ (0.00128f * dX * (float) Math.pow(dY, 4))
				+ (0.00022f * (float) Math.pow(dY, 2)) + (-0.00022f * (float) Math.pow(dX, 2)) + (0.00026f * (float) Math
				.pow(dX, 5)));
		float lat = 52.15517f + (SomN / 3600f);
		float lng = 5.387206f + (SomE / 3600f);
		return new Float[] { lat, lng };
	}
	
	public float getDistanceFromRds(float rds_x, float rds_y) {
		return (float) Math.sqrt( Math.pow((rds_x - getRds_x()), 2) + Math.pow((rds_x - getRds_x()), 2) );
	}
	
	
	/** ----------------------------
	 *  Getters & Setters
	 *  ----------------------------
	 */
	
	public char getBerthType() {
		return berthType;
	}

	public void setBerthType(char berthType) {
		this.berthType = berthType;
	}

	public int getBerthNumber() {
		return berthNumber;
	}

	public void setBerthNumber(int berthNumber) {
		this.berthNumber = berthNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public float getRds_x() {
		return rds_x;
	}

	public float getRds_y() {
		return rds_y;
	}
	
	public void setRds(float x, float y) {
		this.rds_x = x;
		this.rds_y = y;
		Float[] wgsCoordinates = rd2wgs(rds_x, rds_y);
		this.lat = wgsCoordinates[0];
		this.lng = wgsCoordinates[1];
	}

	public float getLat() {
		return lat;
	}

	public float getLng() {
		return lng;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		final String NEW_LINE = "\r\n";
		
		sb.append("berthNumber: " + berthNumber + NEW_LINE);
		sb.append("berthType: " + berthType + NEW_LINE);
		sb.append("name: " + name + NEW_LINE);
		sb.append("companyName: " + companyName + NEW_LINE);
		sb.append("rds: " + rds_x + ", " + rds_y + NEW_LINE);
		sb.append("latlng: " + lat + ", " + lng + NEW_LINE);	
		
		return sb.toString();		
	}

	public int getBerthNumberNormalized() {
		return berthNumberNormalized;
	}

	public void setBerthNumberNormalized(int berthNumberNormalized) {
		this.berthNumberNormalized = berthNumberNormalized;
	}

	public boolean isVisibleOnMap() {
		return visibleOnMap;
	}

	public void setVisibleOnMap(boolean visibleOnMap) {
		this.visibleOnMap = visibleOnMap;
	}
}
