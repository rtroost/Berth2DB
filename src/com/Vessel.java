package com;

public class Vessel {
	
	private int vesselId;
	private int shipNumber;
	private String shipType;
	private String shipName;
	private float length;
	private float beam;
	private int maxDraught;
	private String category;
	
	public Vessel() {}
	
	/** ----------------------------
	 *  Getters & Setters
	 *  ----------------------------
	 */
	
	public int getVesselId() {
		return vesselId;
	}

	public void setVesselId(int vesselId) {
		this.vesselId = vesselId;
	}

	public int getShipNumber() {
		return shipNumber;
	}

	public void setShipNumber(int shipNumber) {
		this.shipNumber = shipNumber;
	}

	public String getShipType() {
		return shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public String getShipName() {
		return shipName;
	}

	public void setShipName(String shipName) {
		this.shipName = shipName;
	}

	public float getLength() {
		return length;
	}

	public void setLength(float length) {
		this.length = length;
	}

	public float getBeam() {
		return beam;
	}

	public void setBeam(float beam) {
		this.beam = beam;
	}

	public int getMaxDraught() {
		return maxDraught;
	}

	public void setMaxDraught(int maxDraught) {
		this.maxDraught = maxDraught;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		final String NEW_LINE = "\r\n";
		
		sb.append("vesselId: " + vesselId + NEW_LINE);
		sb.append("shipNumber: " + shipNumber + NEW_LINE);
		sb.append("shipType: " + shipType + NEW_LINE);
		sb.append("shipName: " + shipName + NEW_LINE);
		sb.append("length: " + length + NEW_LINE);
		sb.append("beam: " + beam + NEW_LINE);
		sb.append("maxDraught: " + maxDraught + NEW_LINE);
		sb.append("category: " + category + NEW_LINE);
		
		return sb.toString();
	}
}
