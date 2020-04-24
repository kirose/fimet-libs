package com.fimet.core.stress.exe;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.fimet.commons.concurrent.AtomicDouble;
import com.fimet.commons.utils.StringUtils;
import com.fimet.core.net.ISocket;

public class InjectorResult {
	ISocket socket;
	AtomicBoolean hasFinished = new AtomicBoolean(false);
	AtomicLong injectorMessagesInjected = new AtomicLong(0L);
	AtomicLong injectorMessagesInjectedCycle = new AtomicLong(0L);
	AtomicLong injectorStartCycleTime = new AtomicLong(0L);
	AtomicLong injectorFinishCycleTime = new AtomicLong(0L);
	long injectorStartTime;
	long injectorFinishTime;
	long readerNumberOfWaits;
	long readerMessagesRead;
	long readerStartTime;
	long readerFinishTime;
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
	public InjectorResult(ISocket c) {
		socket = c;
	}
	public ISocket getConnection() {
		return socket;
	}
	public long getInjectorMessagesInjected() {
		return injectorMessagesInjected.get();
	}
	public long getInjectorStartTime() {
		return injectorStartTime;
	}
	public long getInjectorFinishTime() {
		return injectorFinishTime;
	}
	public long getReaderNumberOfWaits() {
		return readerNumberOfWaits;
	}
	public long getReaderMessagesRead() {
		return readerMessagesRead;
	}
	public long getReaderStartTime() {
		return readerStartTime;
	}
	public long getReaderFinishTime() {
		return readerFinishTime;
	}
	public long getInjectorDurationTime() {
		if (injectorFinishTime > injectorStartTime) {
			return injectorFinishTime-injectorStartTime;
		} else {
			return System.currentTimeMillis()-injectorStartTime;
		}
	}
	public long getReaderDurationTime() {
		if (readerFinishTime>readerStartTime) {
			return readerFinishTime-readerStartTime;
		} else {
			return System.currentTimeMillis()-readerStartTime;
		}
	}
	public long getInjectorStartCycleTime() {
		return injectorStartCycleTime.get();
	}
	public long getInjectorFinishCycleTime() {
		return injectorFinishCycleTime.get();
	}
	public long getInjectorDurationCycleTime() {
		return injectorFinishCycleTime.get()-injectorStartCycleTime.get();
	}
	public long getInjectorMessagesInjectedCycle() {
		return injectorMessagesInjectedCycle.get();
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
	public static String getHeadersCycleStats() {
		return   "CycleNumber"
				+",Port"
				+",NumOfWriteSocket"
				+",NumOfReadSocket"
				+",RatioResponse"
				+",MeanRatioResponse"
				+",NumOfInject"
				+",InjectedTime"
				+",IP"
				+",Timestamp";
	}
	public String getCycleStats() {
		return 
		String.format("% 4d",getNumOfCycle())
		+","+socket.getPort()
		+","+getNumOfWriteCycle()
		+","+getNumOfReadCycle()
		+","+getRatioCycleResponse()
		+","+getMeanRatioResponse()
		+","+getInjectorMessagesInjectedCycle()
		+","+getInjectorDurationCycleTime()
		+","+socket.getAddress()
		+","+new Timestamp(System.currentTimeMillis());
	}
	public static String getHeadersGlobalStats() {
		return   "CycleNumber"
				+",Port"
				+",NumOfWriteSocket"
				+",NumOfReadSocket"
				+",RatioResponse"
				+",MeanRatioResponse"
				+",NumOfInject"
				+",InjectedTime"
				+",IP"
				+",TimestampStart"
				+",TimestampFinish";
	}
	public String getGlobalStats() {
		return ""+getNumOfCycle()
		+","+socket.getPort()
		+","+getNumOfWrite()
		+","+getNumOfRead()
		+","+getRatioResponse()
		+","+getMeanRatioResponse()
		+","+getInjectorMessagesInjected()
		+","+getInjectorDurationTime()
		+","+socket.getAddress()
		+","+new Timestamp(injectorStartTime)
		+","+new Timestamp(injectorFinishTime); 
	}
	public String toString() {
		return 	getGlobalStats();
	}
}
