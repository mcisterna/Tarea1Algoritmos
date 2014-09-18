public class StopWatch {
	
	private long startTime;
	private long finalTime;
	
	public StopWatch(){
		startTime = 0;
		finalTime = 0;
	}


	public void start(){
		startTime = System.currentTimeMillis();
	}
	
	public void pause(){
		finalTime += System.currentTimeMillis() - startTime;
	}


	public void reset(){
		startTime = finalTime = 0;
	}

	public long getElapsedTime(){
		return finalTime;
	}
}
