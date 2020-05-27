package com.fimet.exe;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import com.fimet.socket.PSocket;
import com.fimet.utils.StringUtils;

public class InjectorResult {
	PSocket socket;
	String name;
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
	AtomicLong initialApprovalsSimulator = new AtomicLong(0L);
	AtomicLong numOfCycle = new AtomicLong(0L);
	AtomicLong numOfApprovals = new AtomicLong(0L);
	AtomicLong numOfApprovalsCycle = new AtomicLong(0L);
	AtomicLong numOfRead = new AtomicLong(0L);
	AtomicLong numOfReadCycle = new AtomicLong(0L);
	AtomicLong numOfWrite = new AtomicLong(0L);
	AtomicLong numOfWriteCycle = new AtomicLong(0L);
	long cycleTime;
	long startTime;
	long finishTime;
	InjectorResult(PSocket c) {
		socket = c;
	}
	public String getName() {
		return name;
	}
	public PSocket getSocket() {
		return socket;
	}
	public long getCycleTime() {
		return cycleTime;
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
	public long getInjectorDurationTimeCycle() {
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
	public long getNumOfApprovals() {
		return numOfApprovals.get();
	}
	public long getNumOfApprovalsCycle() {
		return numOfApprovalsCycle.get();
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
	public Double getRatioResponseCycle() {
		if (numOfWrite.get() == 0) {
			return 0D;
		}
		double ratio = (numOfReadCycle.get())/(1D*numOfWriteCycle.get());
		return ratio;
	}
	public String getRatioResponseCycleString() {
		if (numOfWrite.get() == 0) {
			return "Infinity";
		}
		double ratio = (numOfReadCycle.get())/(1D*numOfWriteCycle.get());
		return StringUtils.FORMAT_2DIGITS.format(ratio);
	}
	public Double getRatioResponse() {
		if (numOfWrite.get() == 0) {
			return 0D;
		}
		double ratio = (numOfRead.get())/(1D*numOfWrite.get());
		return ratio;
	}
	public String getRatioResponseString() {
		if (numOfWrite.get() == 0) {
			return "Infinity";
		}
		double ratio = (numOfRead.get())/(1D*numOfWrite.get());
		return StringUtils.FORMAT_2DIGITS.format(ratio);
	}
	public static String getHeadersCycleStats() {
		return   "CycleNumber"
				+",Port"
				+",NumOfWriteSocket"
				+",NumOfReadSocket"
				+",NumOfApprovals"
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
		+","+getNumOfApprovalsCycle()
		+","+getRatioResponseCycleString()
		+","+getInjectorMessagesInjectedCycle()
		+","+getInjectorDurationTimeCycle()
		+","+socket.getAddress()
		+","+new Timestamp(System.currentTimeMillis());
	}
	public static String getHeadersGlobalStats() {
		return   "CycleNumber"
				+",Port"
				+",NumOfWriteSocket"
				+",NumOfReadSocket"
				+",NumOfApprovals"
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
		+","+getNumOfApprovals()
		+","+getRatioResponseString()
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
