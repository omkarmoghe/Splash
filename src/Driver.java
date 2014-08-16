public class Driver {
	
	public static void main(String[] args){
		Game splash = new Game();
		
		//Threads
		Thread t1 = new Thread(splash);
		t1.start();
	}


}
