package com.camelot.lrprsawebapp.utils;

import java.util.Random;

/**
 *  分布式ID获取
 * 
 * @Description -
 * @createDate - 
 */
public class IdWorker {

	private long workerId;
	private long datacenterId;
	private final long idepoch = 1288834974657L;

	public void setWorkerId(long workerId) {
		this.workerId = workerId;
	}

	public void setDatacenterId(long datacenterId) {
		this.datacenterId = datacenterId;
	}

	private static final long workerIdBits = 5L;
	private static final long datacenterIdBits = 5L;
	private static final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	private static final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

	private static final long sequenceBits = 12L;
	private static final long workerIdShift = sequenceBits;
	private static final long datacenterIdShift = sequenceBits + workerIdBits;
	private static final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	private static final long sequenceMask = -1L ^ (-1L << sequenceBits);

	private volatile long lastTimestamp = -1L;
	private volatile long sequence = 0L;
	private static final Random r = new Random();

	public IdWorker() {
		this.workerId = r.nextInt((int) maxWorkerId);
		this.datacenterId = r.nextInt((int) maxDatacenterId);
	}

	public synchronized long nextId() {
		long timestamp = System.currentTimeMillis();
		if (timestamp < lastTimestamp) {
			throw new IllegalStateException("Clock moved backwards.");
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0;
		}
		lastTimestamp = timestamp;
		//System.err.println("workerId:" + workerId);
		//System.err.println("datacenterId:" + datacenterId);
		long id = ((timestamp - idepoch) << timestampLeftShift)//
				| (datacenterId << datacenterIdShift)//
				| (workerId << workerIdShift)//
				| sequence;
		return id;
	}

	private long tilNextMillis(long lastTimestamp) {
		long timestamp = System.currentTimeMillis();
		while (timestamp <= lastTimestamp) {
			timestamp = System.currentTimeMillis();
		}
		return timestamp;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("IdWorker{");
		sb.append("workerId=").append(workerId);
		sb.append(", datacenterId=").append(datacenterId);
		sb.append(", idepoch=").append(idepoch);
		sb.append(", lastTimestamp=").append(lastTimestamp);
		sb.append(", sequence=").append(sequence);
		sb.append('}');
		return sb.toString();
	}
}