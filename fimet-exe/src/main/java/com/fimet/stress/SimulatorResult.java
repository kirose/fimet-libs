package com.fimet.stress;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.fimet.utils.StringUtils;
import com.fimet.utils.concurrent.AtomicDouble;

public class SimulatorResult {
	String name;
	AtomicBoolean hasFinished = new AtomicBoolean(false);
	AtomicLong initialReadSocket = new AtomicLong(0L);
	AtomicLong initialWriteSocket = new AtomicLong(0L);
	AtomicDouble sumOfRatioResponse = new AtomicDouble(0D);
	AtomicDouble ratioResponseCycle = new AtomicDouble(0L);
	AtomicLong numOfCycle = new AtomicLong(0L);
	AtomicLong numOfRead = new AtomicLong(0L);
	AtomicLong numOfWrite = new AtomicLong(0L);
	AtomicLong numOfReadCycle = new AtomicLong(0L);
	AtomicLong numOfWriteCycle = new AtomicLong(0L);
	long startTime;
	long finishTime;
	public SimulatorResult(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public long getDurationTime() {
		if (finishTime>startTime) {
			return finishTime-startTime;
		} else {
			return System.currentTimeMillis() - startTime;
		}
	}
	public long getStartTime() {
		return startTime;
	}
	public long getFinishTime() {
		return finishTime;
	}
	public long getNumOfRead() {
		return numOfRead.get();
	}
	public long getNumOfWrite() {
		return numOfWrite.get();
	}
	public long getNumOfReadCycle() {
		return numOfReadCycle.get();
	}
	public long getNumOfWriteCycle() {
		return numOfWriteCycle.get();
	}
	public long getNumOfCycle() {
		return numOfCycle.get();
	}
	private String getRatioCycleResponse() {
		if (numOfWrite.get() == 0) {
			return "Infinity";
		}
		double ratio = (numOfReadCycle.get())/(1D*numOfWriteCycle.get());
		return StringUtils.FORMAT_2DIGITS.format(ratio);
	}
	private String getRatioResponse() {
		if (numOfWrite.get() == 0) {
			return "Infinity";
		}
		double ratio = (numOfRead.get())/(1D*numOfWrite.get());
		return StringUtils.FORMAT_2DIGITS.format(ratio);
	}
	private String getMeanRatioResponse() {
		if (numOfCycle.get() == 0) {
			return "Infinity";
		}
		double ratio = sumOfRatioResponse.get()/numOfCycle.get();
		return StringUtils.FORMAT_2DIGITS.format(ratio);
	}
	public String getStatsCycle() {
		return 	"SW:"+getNumOfWriteCycle()+",SR:"+getNumOfReadCycle()
		+",R:"+getRatioCycleResponse()
		+",M:"+getMeanRatioResponse();
	}
	public String getStatsGlobal() {
		return 	"SocketWrite:"+getNumOfWrite()+",SocketRead:"+getNumOfRead()
				+",RatioResponse:"+getRatioResponse()
				+",MeanRatioResponse:"+getMeanRatioResponse(); 
	}
	public String toString() {
		return getStatsGlobal();
	}
}
